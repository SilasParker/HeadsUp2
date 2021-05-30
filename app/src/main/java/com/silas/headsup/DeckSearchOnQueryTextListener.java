package com.silas.headsup;

import android.widget.SearchView;

import androidx.fragment.app.Fragment;

//Class to handle the SearchView.OnQueryListener to automatically update the all/favourite Deck tabs grid based on a search query
public class DeckSearchOnQueryTextListener implements SearchView.OnQueryTextListener {

    private DeckList deckList;
    private Fragment fragment;

    //Constructor to initialise this custom listener
    //deckList: DeckList to search through
    //fragment: the Fragment instance that is being updated
    public DeckSearchOnQueryTextListener(DeckList deckList, Fragment fragment) {
        this.deckList = deckList;
        this.fragment = fragment;
    }

    //When the submit button is pressed on the keyboard, update the grid based on the search query
    @Override
    public boolean onQueryTextSubmit(String query) {
        if(fragment instanceof AllDecksFragment) {
            ((AllDecksFragment) fragment).displaySearchResults(MainActivity.searchDeck(query,deckList));
        } else if(fragment instanceof FavouritesFragment) {
            ((FavouritesFragment) fragment).displaySearchResults(MainActivity.searchDeck(query,deckList.getAllFavouritesAsDeckList()));
        }
        return false;
    }

    //When the text in the searchh bar is changed, update the list based on the search query
    @Override
    public boolean onQueryTextChange(String newText) {
        if(fragment instanceof AllDecksFragment) {
            ((AllDecksFragment) fragment).displaySearchResults(MainActivity.searchDeck(newText,deckList));
        } else if(fragment instanceof FavouritesFragment) {
            if(deckList != null) {
                ((FavouritesFragment) fragment).displaySearchResults(MainActivity.searchDeck(newText, deckList.getAllFavouritesAsDeckList()));
            }
        }
        return true;
    }

}
