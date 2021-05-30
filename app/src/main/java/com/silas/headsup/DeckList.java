package com.silas.headsup;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

//Class to manage all Decks better than an ArrayList on its own
public class DeckList {

    private ArrayList<Deck> decks;
    private Context context;

    //Constructor for this DeckList
    //context: Context that this DeckList is created under
    public DeckList(Context context) {
        decks = new ArrayList<>();
        this.context = context;
    }

    //Converts this DeckList into a readable String
    //Returns: This DeckList as a readable String
    public String toString() {
        String toReturn = "";
        for(Deck deck : decks) {
            toReturn += deck.toString()+"\n\n";
        }
        return toReturn;
    }

    //Getter for all Decks in this DeckList
    public ArrayList<Deck> getAllDecks() {
        return this.decks;
    }

    //Removes a Deck from this DeckList
    //position: Index of Deck to be removed
    //favouriteFragment: Whether the Deck being deleted is favourited or not
    public void remove(int position, boolean favouriteFragment) {
        if(!favouriteFragment) {
            decks.get(position).removeJsonFromDir(context);
            decks.remove(position);
        } else {
            int favPosition = -1;
            while(position >= 0) {
                favPosition++;
                if(getDeckAt(favPosition).isFavourite()) {
                    position--;
                }
            }
            decks.get(favPosition).removeJsonFromDir(context);
            decks.remove(favPosition);
        }
    }

    //Retrieves the Deck at a particular index
    //position: Index of Deck to retrieve
    //Returns: Deck at the index specified
    public Deck getDeckAt(int position) {
        return decks.get(position);
    }

    //Toggles the favourite status of a specific Deck
    //position: Index of Deck being toggled
    //context: Context to toggle the Deck under
    //favouriteFragment: Whether this Deck is a favourite or not to begin with
    public void toggleDeckFavourite(int position, Context context, boolean favouriteFragment) throws IOException, JSONException {
        if(!favouriteFragment) {
            decks.get(position).toggleFavourite(context);
        } else {
            int favPosition = -1;
            while(position >= 0) {
                favPosition++;
                if(getDeckAt(favPosition).isFavourite()) {
                    position--;
                }
            }
            decks.get(favPosition).toggleFavourite(context);
        }
    }

    //Retrieves all Decks marked as a favourite
    //Returns: An ArrayList containing all favourite Decks
    public ArrayList<Deck> getAllFavouriteDecks() {
        ArrayList<Deck> deckArrToReturn = new ArrayList<>();
        for(Deck deck : this.decks) {
            if(deck.isFavourite()) {
                deckArrToReturn.add(deck);
            }
        }
        return deckArrToReturn;
    }

    //Retrieves all Decks marked as a favourite in the form of this class
    //Returns: DeckList only containing this DeckList's favourites
    public DeckList getAllFavouritesAsDeckList() {
        DeckList deckListToReturn = new DeckList(context);
        deckListToReturn.setAllDecks(getAllFavouriteDecks());
        return deckListToReturn;
    }

    //Adds a Deck to this DeckList
    //deck: Deck to add
    public void addLiteralDeck(Deck deck) throws IOException, JSONException {
        this.decks.add(deck);
        deck.saveJsonToFile(context,false);
    }

    //Overwrites this DeckLists Decks
    //deckArray: ArrayList of Decks to overwrite the Decks within this DeckList with
    public void setAllDecks(ArrayList<Deck> deckArray) {
        this.decks = deckArray;
    }

}


