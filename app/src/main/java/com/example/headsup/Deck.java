package com.example.headsup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Deck {
    private String name, description, author;
    private ArrayList<String> easyCards, mediumCards, hardCards;
    private int deckSize, highScore, iconId;
    private boolean custom, favourite;

    public Deck (String name, String description, String author, String[] easyCards, String[] mediumCards, String[] hardCards, int iconId, boolean custom, int highScore, boolean favourite) {
        this.easyCards = new ArrayList<String>();
        this.mediumCards = new ArrayList<String>();
        this.hardCards = new ArrayList<String>();
        this.name = name;
        this.description = description;
        this.author = author;
        this.easyCards.addAll(Arrays.asList(easyCards));
        this.mediumCards.addAll(Arrays.asList(mediumCards));
        this.hardCards.addAll(Arrays.asList(hardCards));
        this.deckSize = this.easyCards.size()+this.mediumCards.size()+this.hardCards.size();
        this.highScore = highScore;
        this.custom = custom;
        this.iconId = iconId;
        this.favourite = favourite;
    }

    public boolean isFavourite() {return favourite;}

    public String getName() {
        return this.name;
    }

    public int getHighScore() {
        return this.highScore;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<String> getDeck(int difficulty) {
        switch(difficulty) {
            case 0: return this.easyCards;
            case 1: return this.mediumCards;
            case 2: return this.hardCards;
            default: return null;
        }
    }

    public boolean isCustom() {
        return this.custom;
    }

    public int getDeckSize() {
        return this.deckSize;
    }

    private JSONObject getAsJson() throws JSONException {
        JSONObject deckJSON = new JSONObject();
        deckJSON.put("name",this.name);
        deckJSON.put("description",this.description);
        deckJSON.put("author",this.author);
        JSONArray easy = new JSONArray();
        JSONArray medium = new JSONArray();
        JSONArray hard = new JSONArray();
        for(String card : this.easyCards) {
            easy.put(card);
        }
        for(String card : this.mediumCards) {
            medium.put(card);
        }
        for(String card : this.hardCards) {
            hard.put(card);
        }
        deckJSON.put("easy",easy);
        deckJSON.put("medium",medium);
        deckJSON.put("hard",hard);
        deckJSON.put("highscore",this.highScore);
        deckJSON.put("custom",this.custom);
        deckJSON.put("icon",this.iconId);
        deckJSON.put("favourite",this.favourite);
        return deckJSON;
    }

    public void saveJsonToFile(Context context) throws JSONException, IOException {
        JSONObject deck = this.getAsJson();
        String deckString = deck.toString();
        String deckFileName = this.getDirectorySafeName()+".json";
        File file = new File(context.getFilesDir(),deckFileName);
        if(!file.exists()) {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(deckString);
            bufferedWriter.close();
        } else {
            Toast.makeText(context,"File exists",Toast.LENGTH_LONG).show();
        }
    }



    private String getDirectorySafeName() {
        String string = this.name;
        char[] unsuitableChars = { '#', '%', '&', '{', '}', '\\', '<', '>', '*', '?', '/', ' ', '$', '!', '\'', '"',
                ':', '@', '+', '`', '|', '=', '.' };
        String fileName = "";
        for (int i = 0; i < string.length(); i++) {
            boolean charSafe = true;
            for (int j = 0; j < unsuitableChars.length; j++) {
                if (string.charAt(i) == unsuitableChars[j]) {
                    charSafe = false;
                }
            }
            if (charSafe) {
                fileName += string.charAt(i);
            }
        }
        return fileName.toLowerCase();

    }

    public int getIconId() {
        switch(this.iconId) {
            case 0:
                return R.drawable.ic_baseline_computer_24;
            case 1:
                return R.drawable.ic_baseline_map_24;
            case 2:
                return R.drawable.ic_baseline_menu_book_24;
            case 3:
                return R.drawable.ic_baseline_music_note_24;
            case 4:
                return R.drawable.ic_baseline_people_outline_24;
            case 5:
                return R.drawable.ic_baseline_pets_24;
            case 6:
                return R.drawable.ic_baseline_science_24;
            case 7:
                return R.drawable.ic_baseline_sports_esports_24;
            case 8:
                return R.drawable.ic_baseline_sports_soccer_24;
            case 9:
                return R.drawable.ic_baseline_tv_24;
            default:
                return R.drawable.ic_baseline_device_unknown_24;
        }
    }


}
