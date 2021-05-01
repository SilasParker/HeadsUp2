package com.silas.headsup;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CreateDeckActivity extends AppCompatActivity {
    private EditText deckNameEntry, authorNameEntry, decriptionEntry, easyCardEntry, mediumCardEntry, hardCardEntry;
    private Spinner iconSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);
        this.deckNameEntry = findViewById(R.id.customCreateDeckName);
        this.authorNameEntry = findViewById(R.id.customCreateAuthorName);
        this.easyCardEntry = findViewById(R.id.customCreateEasyInput);
        this.mediumCardEntry = findViewById(R.id.customCreateMediumInput);
        this.hardCardEntry = findViewById(R.id.customCreateHardInput);
        this.decriptionEntry = findViewById(R.id.customCreateDescription);

        ArrayList<Integer> iconsList = new ArrayList<>();
        iconsList.add(R.drawable.ic_baseline_computer_24);
        iconsList.add(R.drawable.ic_baseline_map_24);
        iconsList.add(R.drawable.ic_baseline_menu_book_24);
        iconsList.add(R.drawable.ic_baseline_music_note_24);
        iconsList.add(R.drawable.ic_baseline_people_outline_24);
        iconsList.add(R.drawable.ic_baseline_pets_24);
        iconsList.add(R.drawable.ic_baseline_science_24);
        iconsList.add(R.drawable.ic_baseline_sports_esports_24);
        iconsList.add(R.drawable.ic_baseline_sports_soccer_24);
        iconsList.add(R.drawable.ic_baseline_tv_24);

        this.iconSpinner = findViewById(R.id.customCreateSpinner);
        SpinnerImageAdapter adapter = new SpinnerImageAdapter(this,R.layout.icon_selector_row,0,iconsList);
        iconSpinner.setAdapter(adapter);
    }

    private void createDeck() {
        boolean validDeck = true;
        String deckName,authorName,description,error = "";
        String[] easyCards, mediumCards, hardCards;
        if(this.deckNameEntry.getText().toString().length() > 0 && this.deckNameEntry.getText().toString().length() <= 35) {
            deckName = deckNameEntry.getText().toString();
        } else {
            validDeck = false;
            error += "Deck Name is either empty or too long (max 35 chars)";
        }
        if(this.authorNameEntry.getText().toString().length() > 0 && this.authorNameEntry.getText().toString().length() <= 20) {
            authorName = authorNameEntry.getText().toString();
        } else {
            validDeck = false;
            error += "Author Name is either empty or too long (max 20 chars)";
        }
        if(this.decriptionEntry.getText().toString().length() > 0 && this.decriptionEntry.getText().toString().length() <= 200) {
            description = decriptionEntry.getText().toString();
        } else {
            validDeck = false;
            error += "Description is either empty or too long (max 200 chars)";
        }
        easyCards = this.easyCardEntry.getText().toString().split("\n");
        mediumCards = this.mediumCardEntry.getText().toString().split("\n");
        hardCards = this.hardCardEntry.getText().toString().split("\n");
        if(easyCards.length == 0) {
            validDeck = false;
            error += "Decks require at least 1 easy card";
        }

        if(validDeck) {

        }
    }


}