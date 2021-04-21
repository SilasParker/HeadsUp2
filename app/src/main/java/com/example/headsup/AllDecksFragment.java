package com.example.headsup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AllDecksFragment extends Fragment {

    private GridView grid;
    private GridAdapter adapter;
    private DeckList deckList;
    private SearchView searchView;
    private float scale;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        scale = getContext().getResources().getDisplayMetrics().density;



        return inflater.inflate(R.layout.fragment_all_decks,container,false);


    }

    public void setAllDecks(DeckList deckList) {
        this.deckList = deckList;
        System.out.println("Deck List Size: "+deckList.getAllDecks().size());

    }

    public void displaySearchResults(ArrayList<Deck> tempDeckList) {
        GridAdapter tempAdapter = new GridAdapter(getContext(),tempDeckList,this);
        if(this.grid != null) {
            this.grid.setAdapter(tempAdapter);
        }
    }


    public void updateGrid() {
        if(this.deckList != null) {
            adapter = new GridAdapter(getContext(),deckList.getAllDecks(),this);
            grid = (GridView) getView().findViewById(R.id.allDecksGrid);

            grid.setAdapter(adapter);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        updateGrid();
        this.searchView = getActivity().findViewById(R.id.allDecksSearch);
        searchView.setOnQueryTextListener(new DeckSearchOnQueryTextListener(deckList,this));
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
