package com.example.headsup;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FavouritesFragment extends DeckLayoutFragment {

    private GridView grid;
    private GridAdapter adapter;
    private DeckList deckList;
    private SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        updateGrid(R.id.favouritesGrid);
        this.searchView = getActivity().findViewById(R.id.favouritesSearch);
        searchView.setOnQueryTextListener(new DeckSearchOnQueryTextListener(deckList,this));
    }






}
