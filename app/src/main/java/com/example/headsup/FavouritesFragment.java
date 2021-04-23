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




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites,container,false);
    }

    @Override
    public void updateGrid(int gridId) {
        if(this.getDeckList() != null) {
            this.setGridAdapter(new GridAdapter(getContext(),getDeckList().getAllFavouriteDecks(),this));
            setGridView((GridView) getView().findViewById(gridId));
            getGridView().setAdapter(getGridAdapter());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        updateGrid(R.id.favouritesGrid);
        setSearchView(getActivity().findViewById(R.id.favouritesSearch));
        getSearchView().setOnQueryTextListener(new DeckSearchOnQueryTextListener(getDeckList(),this));
    }






}
