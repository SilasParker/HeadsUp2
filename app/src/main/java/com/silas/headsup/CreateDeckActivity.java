package com.silas.headsup;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CreateDeckActivity extends AppCompatActivity {
    private EditText deckNameEntry, authorNameEntry, descriptionEntry, easyCardEntry, mediumCardEntry, hardCardEntry;
    private TextView easyCardCountView, mediumCardCountView, hardCardCountView, totalCardsView;
    private Spinner iconSpinner;
    private ImageView exitButton;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private Button submit;
    private int easyCardCount = 0, mediumCardCount = 0, hardCardCount = 0;

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
        this.easyCardCountView = findViewById(R.id.customCreateEasyTotal);
        this.mediumCardCountView = findViewById(R.id.customCreateMediumTotal);
        this.hardCardCountView = findViewById(R.id.customCreateHardTotal);
        this.totalCardsView = findViewById(R.id.customCreateTotalCards);

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

        this.easyCardEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String entered = easyCardEntry.getText().toString();
                String[] lines = new String[0];
                if(!entered.equals("")) {
                    lines = entered.split("\n");
                }
                easyCardCount = lines.length;
                easyCardCountView.setText("Total: " + String.valueOf(lines.length));
                totalCardsView.setText("Total Cards: " + String.valueOf(easyCardCount + mediumCardCount + hardCardCount));
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        this.mediumCardEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String entered = mediumCardEntry.getText().toString();
                String[] lines = new String[0];
                if(!entered.equals("")) {
                    lines = entered.split("\n");

                }
                mediumCardCount = lines.length;
                mediumCardCountView.setText("Total: " + String.valueOf(lines.length));
                totalCardsView.setText("Total Cards: " + String.valueOf(easyCardCount + mediumCardCount + hardCardCount));
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        this.hardCardEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String entered = hardCardEntry.getText().toString();
                String[] lines = new String[0];
                if(!entered.equals("")) {
                    lines = entered.split("\n");

                }
                hardCardCount = lines.length;
                hardCardCountView.setText("Total: " + String.valueOf(lines.length));
                totalCardsView.setText("Total Cards: " + String.valueOf(easyCardCount + mediumCardCount + hardCardCount));
            }

            @Override
            public void afterTextChanged(Editable s) { }
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
        System.out.println("CARDS: "+easyCards.length);
        if(easyCards.length == 0 || (easyCards.length == 1 && easyCards[0].equals(""))) {
            validDeck = false;
            error += "Decks require at least 1 easy card";
        }

        if(validDeck) {
            Deck deck = new Deck(deckName,description,authorName,easyCards,mediumCards,hardCards,iconSpinner.getSelectedItemPosition(),true,0,false);
            deck.saveJsonToFile(this,true);
            DatabaseReference newDeckRef = database.child("decks").push();
            newDeckRef.setValue(deck);
            newDeckRef.child("downloads").setValue(0);
            newDeckRef.child("id").setValue(newDeckRef.getKey());
            newDeckRef.child("easyCount").setValue(easyCards.length);
            newDeckRef.child("mediumCount").setValue(mediumCards.length);
            newDeckRef.child("hardCount").setValue(hardCards.length);
            newDeckRef.child("deckSize").setValue(easyCards.length+mediumCards.length+hardCards.length);
            Date date = new Date();
            ZoneId timeZone = ZoneId.systemDefault();
            LocalDate localDate = date.toInstant().atZone(timeZone).toLocalDate();
            newDeckRef.child("timeYear").setValue(localDate.getYear());
            newDeckRef.child("timeDay").setValue(localDate.getDayOfYear());
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