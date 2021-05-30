package com.silas.headsup;

import android.widget.SearchView;

//Class to handle the SearchView.OnQueryListener listener to automatically update the custom fragment list based on a search query
public class DatabaseSearchOnQueryTextListener implements SearchView.OnQueryTextListener {

    private CustomFragment customFragment;

    //Constructor to initialise this custom Listener
    //fragment: the CustomFragment instance that is being updated
    public DatabaseSearchOnQueryTextListener(CustomFragment fragment) {
        this.customFragment = fragment;
    }

    //When the submit button is pressed on the keyboard, update the list based on the search query
    @Override
    public boolean onQueryTextSubmit(String query) {
        customFragment.generateTable(customFragment.getTempSnapShot(),query,false,false);
        return false;
    }

    //When the text in the search bar is changed, update the list based on the search query
    @Override
    public boolean onQueryTextChange(String newText) {
        customFragment.generateTable(customFragment.getTempSnapShot(),newText,false,false);
        return true;
    }

}
