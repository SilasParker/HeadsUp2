package com.silas.headsup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AllDecksFragment extends com.silas.headsup.DeckLayoutFragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_decks,container,false);
    }

    @Override
    public void updateGrid(int gridId) {
        if(this.getDeckList() != null) {
            this.setGridAdapter(new com.silas.headsup.GridAdapter(getContext(),getDeckList().getAllDecks(),this));
            setGridView((GridView) getView().findViewById(gridId));
            getGridView().setAdapter(getGridAdapter());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("yo");
        updateGrid(R.id.allDecksGrid);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        updateGrid(R.id.allDecksGrid);
        this.setSearchView(getActivity().findViewById(R.id.allDecksSearch));
        this.getSearchView().setOnQueryTextListener(new com.silas.headsup.DeckSearchOnQueryTextListener(this.getDeckList(),this));
    }






}
