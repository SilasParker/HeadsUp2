package com.silas.headsup;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class CreateDeckActivity extends AppCompatActivity {
    private EditText deckNameEntry, authorNameEntry, descriptionEntry, easyCardEntry, mediumCardEntry, hardCardEntry;
    private Spinner iconSpinner;
    private ImageView exitButton;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);
        this.deckNameEntry = findViewById(R.id.customCreateDeckName);
        this.authorNameEntry = findViewById(R.id.customCreateAuthorName);
        this.easyCardEntry = findViewById(R.id.customCreateEasyInput);
        this.mediumCardEntry = findViewById(R.id.customCreateMediumInput);
        this.hardCardEntry = findViewById(R.id.customCreateHardInput);
        this.descriptionEntry = findViewById(R.id.customCreateDescription);
        this.exitButton = findViewById(R.id.customCreateExit);
        this.iconSpinner = findViewById(R.id.customCreateSpinner);
        this.submit = findViewById(R.id.customCreateSaveUpload);

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

        this.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createDeck();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private void createDeck() throws IOException, JSONException {
        boolean validDeck = true;
        String deckName = "",authorName = "",description = "",error = "";
        String[] easyCards, mediumCards, hardCards;
        if(this.deckNameEntry.getText().toString().length() > 0 && this.deckNameEntry.getText().toString().length() <= 35) {
            deckName = deckNameEntry.getText().toString();
        } else {
            validDeck = false;
            error += "Deck Name is either empty or too long (max 35 chars)\n";
        }
        if(this.authorNameEntry.getText().toString().length() > 0 && this.authorNameEntry.getText().toString().length() <= 20) {
            authorName = authorNameEntry.getText().toString();
        } else {
            validDeck = false;
            error += "Author Name is either empty or too long (max 20 chars)\n";
        }
        if(this.descriptionEntry.getText().toString().length() > 0 && this.descriptionEntry.getText().toString().length() <= 200) {
            description = descriptionEntry.getText().toString();
        } else {
            validDeck = false;
            error += "Description is either empty or too long (max 200 chars)\n";
        }
        easyCards = this.easyCardEntry.getText().toString().split("\n");
        mediumCards = this.mediumCardEntry.getText().toString().split("\n");
        hardCards = this.hardCardEntry.getText().toString().split("\n");
        if(easyCards.length == 0) {
            validDeck = false;
            error += "Decks require at least 1 easy card";
        }

        if(validDeck) {
            Deck deck = new Deck(deckName,description,authorName,easyCards,mediumCards,hardCards,iconSpinner.getSelectedItemPosition(),true,0,false);
            deck.saveJsonToFile(this,true);
            DatabaseReference newDeckRef = database.child("decks").push();
            newDeckRef.setValue(deck);
            Toast.makeText(this,"Deck successfully uploaded!",Toast.LENGTH_LONG).show();
            finish();
        } else {
            AlertDialog.Builder alertDiaBuilder = new AlertDialog.Builder(CreateDeckActivity.this);
            alertDiaBuilder
                    .setTitle("ERROR CREATING DECK")
                    .setMessage(error)
                    .setIcon(R.drawable.ic_baseline_warning_amber_24).show();
        }
    }


}