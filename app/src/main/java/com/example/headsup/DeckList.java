package com.example.headsup;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class DeckList {
    private ArrayList<Deck> decks;
    private Context context;

    public DeckList(Context context) {
        decks = new ArrayList<Deck>();
        this.context = context;
    }

    public ArrayList<Deck> getAllDecks() {
        return this.decks;
    }

    public void setAllDecks(ArrayList<Deck> deckArray) {
        this.decks = deckArray;
    }

    public String toString() {
        String toReturn = "";
        for(Deck deck : decks) {
            toReturn += deck.toString()+"\n\n";

        }
        return toReturn;
    }

    public void addDeck(String name, String description, String author, String[] easyCards, String[] mediumCards, String[] hardCards, int iconId, boolean custom, int highScore, boolean favourite) throws IOException, JSONException {
        Deck newDeck = new Deck(name, description, author, easyCards, mediumCards, hardCards, iconId, custom, highScore, favourite);
        this.decks.add(newDeck);
        newDeck.saveJsonToFile(context);
    }

    public void addLiteralDeck(Deck deck) throws IOException, JSONException {
        this.decks.add(deck);
        deck.saveJsonToFile(context);
    }

    public Deck getDeckAt(int position) {
        return decks.get(position);
    }

    public void remove(int position) {
        decks.remove(position);
        decks.get(position).removeJsonFromDir(context);
    }

    public void toggleDeckFavourite(int position, Context context) throws IOException, JSONException {
        decks.get(position).toggleFavourite(context);
    }
}
