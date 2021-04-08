package com.example.headsup;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

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

    public void updateGrid() {
        if(this.deckList != null) {
            adapter = new GridAdapter(getContext(),deckList.getAllDecks(),this);
            grid = (GridView) getView().findViewById(R.id.all_decks_grid);
            grid.setAdapter(adapter);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        updateGrid();
    }

    public void onDeckSelectedToPlay(int deckSelected) {
        View view = getView();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.game_setup_overlay,null);
        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        TextView deckName = (TextView) popupView.findViewById(R.id.startGameDeckName);
        deckName.setText(deckList.getDeckAt(deckSelected).getName());
        TextView deckDescription = (TextView) popupView.findViewById(R.id.startGameDeckDescription);
        deckDescription.setText(deckList.getDeckAt(deckSelected).getDescription());

        final PopupWindow popUp = new PopupWindow(popupView,width,height, true);



        popUp.showAtLocation(view, Gravity.CENTER,0,0);

    }


}
