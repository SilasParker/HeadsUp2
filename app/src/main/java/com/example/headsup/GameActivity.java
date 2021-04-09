package com.example.headsup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private ArrayList<String> deck;
    private TextView cardName, timerView, scoreView;
    private String currentCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.cardName = findViewById(R.id.gameCardName);
        this.timerView = findViewById(R.id.gameTimer);
        this.scoreView = findViewById(R.id.gameScore);
    }

    public void setGame(Deck deck, int timer, int difficulty) {
        this.game = new Game(deck, timer, difficulty);
        this.deck = game.getUsedDeck();
    }

    private void readyToStart() {

    }
}