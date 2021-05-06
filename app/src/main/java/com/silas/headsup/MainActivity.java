package com.silas.headsup;
//TODO:
/*
refresh grid when deck created not showing new deck (have to restart app)
center screen when typing new deck
count cards as typing new deck
clear database results instead of appending
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    public static DeckList deckList;
    public static SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = getSharedPreferences("headsUpPrefs", Context.MODE_PRIVATE);
        if(!sharedPrefs.contains("firstAccess")) {
            setUpAppForFirstRun();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AllDecksFragment()).commit();
        deckList = new DeckList(getApplicationContext());



        try {
            this.deckList.setAllDecks(getAllStoredDecks());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

        }
/*
        try {
            test();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/


    }

    public static ArrayList<Deck> searchDeck(String search, DeckList deckListToSearch) {
        search = search.toLowerCase();
        ArrayList<Deck> decksSearched = new ArrayList<>();
        ArrayList<Deck> tempDecksSearched = new ArrayList<>();

        if(deckListToSearch != null) {
            decksSearched.addAll(deckListToSearch.getAllDecks());
            tempDecksSearched.addAll(decksSearched);
            boolean noMatchesFirstChar = true;
            if (search.length() == 0) {
                return decksSearched;
            }
            for (int i = 0; i < search.length(); i++) {
                char searchedChar = search.charAt(i);
                for (Deck deck : decksSearched) {
                    if(deck.getName().length() == i) {
                        tempDecksSearched.remove(deck);
                    } else if (deck.getName().toLowerCase().charAt(i) != searchedChar) {
                        tempDecksSearched.remove(deck);
                    }
                }

                if (tempDecksSearched.size() == 0 && !noMatchesFirstChar) {
                    return decksSearched;
                } else if (tempDecksSearched.size() == 0 && noMatchesFirstChar) {
                    return tempDecksSearched;
                } else {
                    decksSearched = tempDecksSearched;
                }
            }
            return decksSearched;

        }
        return new ArrayList<Deck>();
    }





    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFrag = null;
            switch(item.getItemId()) {
                case R.id.nav_all:
                    selectFrag = new AllDecksFragment();
                    ((AllDecksFragment) selectFrag).setAllDecks(deckList);
                    break;
                case R.id.nav_favourites:
                    selectFrag = new FavouritesFragment();
                    ((FavouritesFragment) selectFrag).setAllDecks(deckList);
                    break;
                case R.id.nav_custom:
                    selectFrag = new CustomFragment();
                    break;
                case R.id.nav_settings:
                    selectFrag = new SettingsFragment();
                    break;
                case R.id.nav_help:
                    selectFrag = new HelpFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFrag).commit();
            return true;
        }
    };

    private ArrayList<Deck> getAllStoredDecks() throws IOException, JSONException {
        ArrayList<Deck> allDecks = new ArrayList<>();
        ArrayList<Deck> favDecks = new ArrayList<>();
        File directory = new File(String.valueOf(this.getApplicationContext().getFilesDir()));
        for(File file : directory.listFiles()) {
            if(file.isFile() && file.getPath().endsWith(".json")) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while(line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
                String response = stringBuilder.toString();
                JSONObject deckJSON = new JSONObject(response);
                if(deckJSON != null) {
                    boolean validJson = true;
                    String name = null, description = null, author = null;
                    JSONArray easyJson = null, mediumJson = null, hardJson = null;
                    int icon = -1, highscore = -1;
                    boolean custom = true, favourite = false;
                    try {
                        name = deckJSON.getString("name");
                        description = deckJSON.getString("description");
                        author = deckJSON.getString("author");
                        easyJson = deckJSON.getJSONArray("easy");
                        mediumJson = deckJSON.getJSONArray("medium");
                        hardJson = deckJSON.getJSONArray("hard");
                        icon = deckJSON.getInt("icon");
                        custom = deckJSON.getBoolean("custom");
                        highscore = deckJSON.getInt("highscore");
                        favourite = deckJSON.getBoolean("favourite");
                    } catch(JSONException e) {
                    }
                    if(name == null || description == null || author == null || easyJson == null || mediumJson == null || hardJson == null) {
                        validJson = false;
                    }
                    if(validJson) {
                        String[] easy = jsonStringArrayToRegularStringArray(easyJson);
                        String[] medium = jsonStringArrayToRegularStringArray(mediumJson);
                        String[] hard = jsonStringArrayToRegularStringArray(hardJson);
                        if(name.length() > 35 || description.length() > 200 || author.length() > 20 || easy.length == 0 || icon > 9 || highscore < 0 || highscore > 999) {
                            validJson = false;
                        }
                        if(validJson) {
                            Deck newDeck = new Deck(name,description,author,easy,medium,hard,icon,custom,highscore,favourite);
                            System.out.println(newDeck.toString());
                            allDecks.add(newDeck);
                            if(newDeck.isFavourite()) {
                                favDecks.add(newDeck);
                            }
                        } else {
                            file.delete();
                            Toast.makeText(this,"Invalid Deck Found and Deleted",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        file.delete();
                        Toast.makeText(this,"Invalid Deck Found and Deleted",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        return allDecks;
    }

    private String[] jsonStringArrayToRegularStringArray(JSONArray array) throws JSONException {
        String[] arrayToReturn = new String[array.length()];
        for(int i = 0;i < array.length();i++) {
            arrayToReturn[i] = array.get(i).toString();
        }
        return arrayToReturn;
    }

    private void setUpAppForFirstRun() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putFloat("timer",120f);
        editor.putInt("difficulty",2);
        editor.putBoolean("bonusTime",false);
        editor.putBoolean("soundEffects",true);
        editor.putInt("cardColour",1);
        editor.putInt("textColour",1);
        editor.putBoolean("firstAccess",false);
        editor.commit();
    }

    private void test() throws IOException, JSONException {
        Deck deck = new Deck("Smash","It's a smash game...","Silas",new String[]{"Peach","Bayonetta","Meta Knight"}, new String[]{"Fox","Kirby"},new String[]{"Pikachu"},1,true,0,true);
        deckList.addLiteralDeck(deck);
        Deck deck2 = new Deck("Harry Potter","Abra kadabra","Alex",new String[]{"Harry Potter","Ronald Weasley","Hermione Granger"},new String[]{"Luna Lovegood","Neville Longbottom"},new String[]{"Albus Dumbledore"},2,true,0,false);
        deckList.addLiteralDeck(deck2);
    }

    /*
    FirebaseDatabase root;
    DatabaseReference reference;

    EditText name, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editPhone);
        email = findViewById(R.id.editEmail);
    }


    public void onSubmit(View view)
    {
        Student student = new Student(name.getText().toString(),phone.getText().toString(),email.getText().toString());


        root = FirebaseDatabase.getInstance();
        reference = root.getReference("student");
        System.out.println(reference);
        reference.setValue(student);

    }
*/

}