package com.example.headsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
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

    private ArrayList<Deck> deckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AllDecksFragment()).commit();

        try {
            test();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            this.deckList = getAllStoredDecks();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFrag = null;
            switch(item.getItemId()) {
                case R.id.nav_all:
                    selectFrag = new AllDecksFragment();
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
                            Deck newDeck = new Deck(name,description,author,easy,medium,hard,icon,custom,highscore);
                            allDecks.add(newDeck);
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

    private void test() throws IOException, JSONException {
        String[] easy = new String[]{"Pikachu","Pichu","Jigglypuff"};
        ArrayList<String> easyA = new ArrayList<>();
        easyA.addAll(Arrays.asList(easy));
        String[] medium = new String[]{"Mewtwo","Game and Watch"};
        ArrayList<String> mediumA = new ArrayList<>();
        mediumA.addAll(Arrays.asList(medium));
        String[] hard = new String[]{"Meta Knight"};
        ArrayList<String> hardA = new ArrayList<>();
        hardA.addAll(Arrays.asList(hard));
        Deck deck = new Deck("Super Smash Bros. Project","A video game that is OK at best","Silas da Boi",easy,medium,hard,1,true,0);
        deck.saveJsonToFile(this.getApplicationContext());
        ArrayList<Deck> ignoreMe = getAllStoredDecks();
        for(Deck decks : ignoreMe) {
            System.out.println("XXXXXXXXXXX:"+decks.getName());
        }
    }


}