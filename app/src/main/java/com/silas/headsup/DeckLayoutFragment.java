package com.silas.headsup;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;

import java.util.ArrayList;

public class DeckLayoutFragment extends Fragment {
    private GridView grid;
    private GridAdapter adapter;
    private DeckList deckList;
    private SearchView searchView;

    public void setAllDecks(DeckList deckList) {
        this.deckList = deckList;
    }

    public DeckList getDeckList() {
        return this.deckList;
    }

    public GridView getGridView() {
        return this.grid;
    }

    public GridAdapter getGridAdapter() {
        return this.adapter;
    }

    public SearchView getSearchView() {
        return this.searchView;
    }

    public void setGridView(GridView grid) {
        this.grid = grid;
    }

    public void setGridAdapter(GridAdapter adapter) {
        this.adapter = adapter;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public void displaySearchResults(ArrayList<Deck> tempDeckList) {
        GridAdapter tempAdapter = new GridAdapter(getContext(),tempDeckList,this);
        if(this.grid != null) {
            this.grid.setAdapter(tempAdapter);
        }
    }

    public void updateGrid(int gridId) {
        System.out.println("OVERRIDE ME");
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
        Slider timer = (Slider) popupView.findViewById(R.id.startGameTimerSlider);
        float prefFloat = MainActivity.sharedPrefs.getFloat("timer",120.0f);
        timer.setValue(prefFloat);
        RadioGroup radGroup = (RadioGroup) popupView.findViewById(R.id.startGameDifficultyRadioGroup);
        RadioButton radBut;
        switch(MainActivity.sharedPrefs.getInt("difficulty",2)) {
            case 1:
                radBut = (RadioButton) popupView.findViewById(R.id.startGameEasyRadio);
                break;
            case 3:
                radBut = (RadioButton) popupView.findViewById(R.id.startGameHardRadio);
                break;
            default:
                radBut = (RadioButton) popupView.findViewById(R.id.startGameMediumRadio);
                break;
        }
        radBut.toggle();
        Button startBtn = (Button) popupView.findViewById(R.id.startGameStartButton);

        final PopupWindow popUp = new PopupWindow(popupView,width,height, true);

        startBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RadioButton selectedRadBut = popupView.findViewById(radGroup.getCheckedRadioButtonId());
                String difficultyStr = (String) selectedRadBut.getText();

                int difficulty = 0;
                if(difficultyStr.equals("MEDIUM")) {
                    difficulty = 1;
                } else if(difficultyStr.equals("HARD")) {
                    difficulty = 2;
                }
                int timerInt = (int) timer.getValue();
                Intent intent = new Intent(getActivity(),GameActivity.class);
                intent.putExtra("deck", deckList.getDeckAt(deckSelected));
                intent.putExtra("timer",timerInt);
                intent.putExtra("difficulty",difficulty);
                startActivity(intent);
                popUp.dismiss();
            }
        });



        TextView closeButton = (TextView) popupView.findViewById(R.id.startGameExitDeckButton);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });
        popUp.showAtLocation(view, Gravity.CENTER,0,0);

    }
}
