package com.example.headsup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AllDecksFragment extends Fragment {

    private ArrayList<Deck> allDecks;
    private final float scale = getContext().getResources().getDisplayMetrics().density;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_decks,container,false);

    }

    public void setAllDecks(ArrayList<Deck> deckArrayList) {
        this.allDecks = deckArrayList;
    }

    private void generateDeckGrid() {
        GridView grid = (GridView) getActivity().findViewById(R.id.all_decks_grid);
        for(Deck deck : allDecks) {
            RelativeLayout deckViewContainer = new RelativeLayout(getContext());
            deckViewContainer.getLayoutParams().height = (int) (100 * scale + 0.5f);
            grid.addView(deckViewContainer);

            if(deck.isCustom()) {
                Button deleteButton = new Button(getContext());
                deleteButton.setText("X");
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
                deckViewContainer.addView(deleteButton,params);
            }

            TextView deckTitleTextView = new TextView(getContext());
            deckTitleTextView.setText(deck.getName());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) deckTitleTextView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            deckTitleTextView.setLayoutParams(params);
            deckViewContainer.addView(deckTitleTextView);


        }
    }
}
