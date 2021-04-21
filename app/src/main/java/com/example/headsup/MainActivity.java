package com.example.headsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    /*
                        TODO:
     -see if there's anything left to do or branch for favourites
    */
    public static DeckList deckList;
    public static SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = getSharedPreferences("headsUpPrefs",Context.MODE_PRIVATE);
        if(!sharedPrefs.contains("firstAccess")) {
            setUpAppForFirstRun();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AllDecksFragment()).commit();
        deckList = new DeckList(getApplicationContext());


/*
        try {
            test();
            deckList.getDeckAt(0).saveJsonToFile(this);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        try {
            this.deckList.setAllDecks(getAllStoredDecks());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

        }


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
                System.out.println("No Length to Search, Returning All");
                return decksSearched;
            }
            for (int i = 0; i < search.length(); i++) {
                System.out.println("Searching character #" + i);
                char searchedChar = search.charAt(i);
                System.out.println(decksSearched.size() + " decks left");
                System.out.println(tempDecksSearched.size() + " temp decks left");
                for (Deck deck : decksSearched) {
                    System.out.println(deck.getName().length()+" "+i);
                    if(deck.getName().length() == i) {
                        tempDecksSearched.remove(deck);
                    } else if (deck.getName().toLowerCase().charAt(i) != searchedChar) {
                        System.out.println("Removing " + deck.getName() + " as " + deck.getName().toLowerCase().charAt(i) + " != " + searchedChar);
                        tempDecksSearched.remove(deck);
                    }
                }

                if (tempDecksSearched.size() == 0 && !noMatchesFirstChar) {
                    System.out.println("No matches left, returning what was left of last round");
                    return decksSearched;
                } else if (tempDecksSearched.size() == 0 && noMatchesFirstChar) {
                    System.out.println("No matches on first character, returning all");
                    return tempDecksSearched;
                } else {
                    System.out.println("Ready to continue searching");
                    decksSearched = tempDecksSearched;
                }
            }
            return decksSearched;

        }
        System.out.println("Somehow got here, returning all");
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
                    String name = deckJSON.getString("name");
                    String description = deckJSON.getString("description");
                    String author = deckJSON.getString("author");
                    JSONArray easyJson = deckJSON.getJSONArray("easy");
                    JSONArray mediumJson = deckJSON.getJSONArray("medium");
                    JSONArray hardJson = deckJSON.getJSONArray("hard");
                    int icon = deckJSON.getInt("icon");
                    boolean custom = deckJSON.getBoolean("custom");
                    int highscore = deckJSON.getInt("highscore");
                    boolean favourite = deckJSON.getBoolean("favourite");
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
                            allDecks.add(newDeck);
                            if(newDeck.isFavourite()) {
                                favDecks.add(newDeck);
                            }
                        } else {
                            Toast.makeText(this,"Invalid Deck Found",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this,"Invalid Deck Found",Toast.LENGTH_LONG).show();
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
        Deck deck2 = new Deck("Harry Potter","Abra kadabra","Alex",new String[]{"Harry Potter","Ronald Weasley","Hermione Granger"},new String[]{"Luna Lovegood","Neville Longbottom"},new String[]{"Albus Dumbledore"},2,false,0,false);
        deckList.addLiteralDeck(deck2);
    }


}