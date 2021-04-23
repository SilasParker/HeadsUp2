package com.example.headsup;

import android.widget.SearchView;

import androidx.fragment.app.Fragment;

public class DeckSearchOnQueryTextListener implements SearchView.OnQueryTextListener {
    private DeckList deckList;
    private Fragment fragment;

    public DeckSearchOnQueryTextListener(DeckList deckList, Fragment fragment) {
        this.deckList = deckList;
        this.fragment = fragment;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(fragment instanceof AllDecksFragment) {
            ((AllDecksFragment) fragment).displaySearchResults(MainActivity.searchDeck(query,deckList));
        } else if(fragment instanceof FavouritesFragment) {
            ((FavouritesFragment) fragment).displaySearchResults(MainActivity.searchDeck(query,deckList.getAllFavouritesAsDeckList()));
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(fragment instanceof AllDecksFragment) {
            ((AllDecksFragment) fragment).displaySearchResults(MainActivity.searchDeck(newText,deckList));
        } else if(fragment instanceof FavouritesFragment) {
            ((FavouritesFragment) fragment).displaySearchResults(MainActivity.searchDeck(newText,deckList.getAllFavouritesAsDeckList()));
        }
        return true;
    }
}
