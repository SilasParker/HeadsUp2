package com.silas.headsup;

import android.widget.SearchView;

public class DatabaseSearchOnQueryTextListener implements SearchView.OnQueryTextListener{
    private CustomFragment customFragment;

    public DatabaseSearchOnQueryTextListener(CustomFragment fragment) {
        this.customFragment = fragment;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        customFragment.generateTable(customFragment.getTempSnapShot(),query,false,false);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        customFragment.generateTable(customFragment.getTempSnapShot(),newText,false,false);
        return true;
    }
}
