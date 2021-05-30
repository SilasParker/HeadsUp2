package com.silas.headsup;

import android.content.Context;
import android.icu.text.SymbolTable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//Class representing a Deck for the game
public class Deck implements Parcelable {

    private String name, description, author;
    private ArrayList<String> easyCards, mediumCards, hardCards;
    private int deckSize, highScore, iconId;
    private boolean custom, favourite;

    //Constructor for a Deck instance
    //name: Deck name
    //author: Deck author name
    //easyCards: Array containing all easy cards
    //mediumCards: Array containing all medium cards
    //hardCards: Array containing all hard cards
    //iconId: Id of this Deck's icon
    //custom: Whether this Deck is custom or not
    //highScore: Highscore of this Deck
    //favourite: Whether this Deck is a favourite or not
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
        this.deckSize = this.hardCards.size();
        this.highScore = highScore;
        this.custom = custom;
        this.iconId = iconId;
        this.favourite = favourite;
    }

    //The constructor for this Deck that constructs a Deck instance from a Parcel version
    //in: Parcel version of this Deck
    protected Deck(Parcel in) {
        name = in.readString();
        description = in.readString();
        author = in.readString();
        easyCards = in.createStringArrayList();
        mediumCards = in.createStringArrayList();
        hardCards = in.createStringArrayList();
        deckSize = in.readInt();
        highScore = in.readInt();
        iconId = in.readInt();
        custom = in.readByte() != 0;
        favourite = in.readByte() != 0;
    }

    //Default method to describe contents of a Parcel Deck
    @Override
    public int describeContents() {
        return 0;
    }

    //Sets up a Parcel of this Deck to pass along to the Game Activity
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(author);
        dest.writeStringList(easyCards);
        dest.writeStringList(mediumCards);
        dest.writeStringList(hardCards);
        dest.writeInt(deckSize);
        dest.writeInt(highScore);
        dest.writeInt(iconId);
        dest.writeByte((byte) (custom ? 1 : 0));
        dest.writeByte((byte) (favourite ? 1 : 0));
    }

    //Converts this Deck to a formatted string for testing purposes
    //Returns: This Deck's attributes as a readable string
    public String toString() {
        String toReturn = "";
        toReturn += this.name+"\n";
        toReturn += this.description+"\n";
        toReturn += "Easy: ";
        for(int i = 0; i < 3; i++) {
            for (String card : getDeckByDifficulty(i)) {
                toReturn += card + ", ";
            }
            toReturn += "\n";
            if(i == 0) {
                toReturn += "Medium: ";
            } else if(i == 1) {
                toReturn += "Hard: ";
            }
        }
        toReturn += "Author: "+this.author+"\n";
        toReturn += "Favourite: "+this.favourite+"\n";
        toReturn += "Custom: "+this.custom+"\n";
        toReturn += "IconID: "+this.iconId+"\n";
        toReturn += "Highscore: "+this.highScore+"\n";
        return toReturn;
    }

    //Getter for this Deck's name
    public String getName() {
        return this.name;
    }

    //Getter for this Deck's description
    public String getDescription() {
        return this.description;
    }

    //Getter for this Deck's author's name
    public String getAuthor() { return this.author; }

    //Getter for this Deck's icon's resource Id
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

    //Getter for whether this Deck is custom or not
    public boolean isCustom() {
        return this.custom;
    }

    //Getter for this Deck's high score
    public int getHighScore() {
        return this.highScore;
    }

    //Getter for whether this Deck is favourited or not
    public boolean isFavourite() { return favourite; }

    //Getter for this Deck's size
    public int getDeckSize() {
        return this.deckSize;
    }


    //Setter for this Deck's high score
    public void setHighScore(int newHighScore) {
        this.highScore = newHighScore;
    }

    //Retrieves all the cards from this Deck depending on the difficulty
    //difficulty: Difficulty to get Deck by
    //Returns: An ArrayList containing cards
    public ArrayList<String> getDeckByDifficulty(int difficulty) {
        ArrayList<String> deckToReturn = new ArrayList<>();
        switch(difficulty) {
            case 0: deckToReturn.addAll(this.easyCards);
                break;
            case 1:
                deckToReturn.addAll(this.easyCards);
                deckToReturn.addAll(this.mediumCards);
                break;
            case 2:
                deckToReturn.addAll(this.easyCards);
                deckToReturn.addAll(this.mediumCards);
                deckToReturn.addAll(this.hardCards);
                break;
            default:
                break;
        }
        return deckToReturn;
    }

    //Creator for this class (from Parcel)
    public static final Creator<Deck> CREATOR = new Creator<Deck>() {

        //Converts Parcel to Deck
        @Override
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
        }

    };

    //Toggles this Deck's favourite status and updates it locally
    //context: Context to update json from
    public void toggleFavourite(Context context) throws IOException, JSONException {
        this.favourite = !this.favourite;
        saveJsonToFile(context,true);
    }

    //Saves or updates this Deck locally as a json file
    //context: Context to save or update json in
    //overwrite: Whether the file should be overwritten or not
    public void saveJsonToFile(Context context, boolean overwrite) throws JSONException, IOException {
        JSONObject deck = this.getAsJson();
        String deckString = deck.toString();
        String deckFileName = this.getDirectorySafeName()+".json";
        if(context != null) {
            File file = new File(context.getFilesDir(), deckFileName);
            if (!file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(deckString);
                bufferedWriter.close();
            } else if (overwrite) {
                FileWriter fw = new FileWriter(file, false);
                BufferedWriter bufferedWriter = new BufferedWriter(fw);
                bufferedWriter.write(deckString);
                bufferedWriter.close();
            } else if (file.exists()) {
                Toast.makeText(context.getApplicationContext(), "Couldn't download because name is too similar to existing deck", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Converts this Deck into a JsonObject
    //Returns: This Deck as a JsonObject
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

    //Converts this Deck's name into a directory-safe name
    //Returns: This Deck's directory-safe name
    private String getDirectorySafeName() {
        String string = this.name;
        char[] unsuitableChars = { '#', '%', '&', '{', '}', '\\', '<', '>', '*', '?', '/', ' ', '$', '!', '\'', '"',
                ':', '@', '+', '`', '|', '=', '.', '(', ')' };
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

    //Deletes a locally saved Deck (as a json)
    //context: Context to delete Deck in
    public void removeJsonFromDir(Context context) {
        String deckFileName = getDirectorySafeName()+".json";
        File file = new File(context.getFilesDir(),deckFileName);
        if(file.exists()) {
            boolean deleted  = file.delete();
            if(!deleted) {
                Toast.makeText(context,"Not deleted for some reason",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,"File does not exist",Toast.LENGTH_LONG).show();
        }
    }

}
