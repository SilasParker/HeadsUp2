package com.example.headsup;

import java.util.ArrayList;

public class DeckList {
    private ArrayList<Deck> decks;

    public DeckList() {
        decks = new ArrayList<>();
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

    public void addDeck(String name, String description, String author, String[] easyCards, String[] mediumCards, String[] hardCards, int iconId, boolean custom, int highScore, boolean favourite) {
        Deck newDeck = new Deck(name, description, author, easyCards, mediumCards, hardCards, iconId, custom, highScore, favourite);
        this.decks.add(newDeck);
    }

    public void addLiteralDeck(Deck deck) {
        this.decks.add(deck);
    }

    public Deck getDeckAt(int position) {
        return decks.get(position);
    }

    public void remove(int position) {
        decks.remove(position);
    }
}
