package com.example.headsup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AllDecksFragment extends Fragment {

    private GridView grid;
    private GridAdapter adapter;
    private DeckList deckList;
    private float scale;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: 1");
        scale = getContext().getResources().getDisplayMetrics().density;

        return inflater.inflate(R.layout.fragment_all_decks,container,false);


    }

    public void setAllDecks(DeckList deckList) {
        System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: 2");
        this.deckList = deckList;
        System.out.println(deckList);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(this.deckList != null) {
            adapter = new GridAdapter(getContext(),deckList.getAllDecks());
            grid = (GridView) getView().findViewById(R.id.all_decks_grid);
            grid.setAdapter(adapter);
        }
    }


}
