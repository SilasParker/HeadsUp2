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

public class AllDecksFragment extends DeckLayoutFragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_decks,container,false);
    }

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
            this.setGridAdapter(new GridAdapter(getContext(),getDeckList().getAllDecks(),this));
            setGridView((GridView) getView().findViewById(gridId));
            getGridView().setAdapter(getGridAdapter());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateGrid(R.id.allDecksGrid);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        updateGrid(R.id.allDecksGrid);
        this.setSearchView(getActivity().findViewById(R.id.allDecksSearch));
        this.getSearchView().setOnQueryTextListener(new DeckSearchOnQueryTextListener(this.getDeckList(),this));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateGrid(R.id.allDecksGrid);
    }
}
