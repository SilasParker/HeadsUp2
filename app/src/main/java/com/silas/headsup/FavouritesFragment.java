package com.silas.headsup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.IOException;

public class FavouritesFragment extends DeckLayoutFragment {

    //During onCreateView in the Fragment lifecycle, inflates this fragment's layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites,container,false);
    }

    //During onResume in the Fragment lifecycle, the grid is updated
    @Override
    public void onResume() {
        super.onResume();
        updateGrid(R.id.favouritesGrid);
    }

    //During onViewCreated in the Fragment lifecycle, the grid is updated and the search bar is initialised
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        updateGrid(R.id.favouritesGrid);
        setSearchView(getActivity().findViewById(R.id.favouritesSearch));
        getSearchView().setOnQueryTextListener(new DeckSearchOnQueryTextListener(getDeckList(),this));
    }

    //Updates the Deck grid
    //gridId: GridView id to update
    @Override
    public void updateGrid(int gridId) {
        try {
            MainActivity activity = (MainActivity) getActivity();
            MainActivity.deckList.setAllDecks(activity.getAllStoredDecks());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(this.getDeckList() != null) {
            this.setGridAdapter(new GridAdapter(getContext(),getDeckList().getAllFavouriteDecks(),this));
            setGridView((GridView) getView().findViewById(gridId));
            getGridView().setAdapter(getGridAdapter());
        }
    }

}
