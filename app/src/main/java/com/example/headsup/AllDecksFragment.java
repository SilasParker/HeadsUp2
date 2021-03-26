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

    private ArrayList<Deck> allDecks, favourites;
    private float scale;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: 1");
        scale = getContext().getResources().getDisplayMetrics().density;

        return inflater.inflate(R.layout.fragment_all_decks,container,false);

    }

    public void setAllDecks(ArrayList<Deck> deckArrayList, ArrayList<Deck> favourites) {
        System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: 2");
        this.allDecks = deckArrayList;
        this.favourites = favourites;
        System.out.println(allDecks);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(this.allDecks != null) {
            generateDeckGrid();
        }
    }

    private void generateDeckGrid() {

        GridView grid = (GridView) getView().findViewById(R.id.all_decks_grid);
        for(Deck deck : allDecks) {
            RelativeLayout deckViewContainer = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(100,(int) (100*scale+0.5f));
            deckViewContainer.setLayoutParams(containerParams);
            //deckViewContainer.getLayoutParams().height = (int) (100 * scale + 0.5f);
            grid.getParent().addView(deckViewContainer);

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

            ImageView favouriteStar = new ImageView(getContext());
            if(this.favourites.contains(deck)) {
                favouriteStar.setImageResource(R.drawable.ic_baseline_star_24);
            } else {
                favouriteStar.setImageResource(R.drawable.ic_baseline_star_outline_24);
            }
            favouriteStar.setMaxHeight(Math.round(5*scale));
            favouriteStar.setMaxWidth(Math.round(5*scale));
            params = (RelativeLayout.LayoutParams) favouriteStar.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            favouriteStar.setLayoutParams(params);
            deckViewContainer.addView(favouriteStar);

            ImageView icon = new ImageView(getContext());
            icon.setImageResource(deck.getIconId());
            icon.setMaxWidth(Math.round(15*scale));
            icon.setMaxHeight(Math.round(15*scale));
            params = (RelativeLayout.LayoutParams) icon.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            icon.setLayoutParams(params);
            deckViewContainer.addView(icon);

            TextView highScoreTextView = new TextView(getContext());
            highScoreTextView.setText(deck.getHighScore());
            params = (RelativeLayout.LayoutParams) highScoreTextView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_RIGHT);
            highScoreTextView.setLayoutParams(params);
            deckViewContainer.addView(highScoreTextView);
        }
    }
}
