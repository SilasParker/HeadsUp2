package com.silas.headsup;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;

//Class representing a Game in progress
public class Game {

    private int score, timer, difficulty, countdown, maxTimer;
    private Deck deck;
    private ArrayList<String> correct, incorrect, deckUsed;
    private ArrayList<Boolean> scoreOrder;
    private String currentCard;
    private boolean gameStarted, bonusTime, soundEffects;

    //Constructor for a Game instance
    //deck: Deck that is being played with
    //timer: The length of the Game's timer
    //difficulty: The difficulty of the Game
    public Game(Deck deck, int timer, int difficulty) {
        this.deck = deck;
        this.score = 0;
        this.timer = timer;
        this.correct = new ArrayList<>();
        this.incorrect = new ArrayList<>();
        this.difficulty = difficulty;
        this.scoreOrder = new ArrayList<>();
        this.deckUsed = getUsedDeck();
        this.gameStarted = false;
        this.countdown = 3;
        this.maxTimer = timer;
        this.soundEffects = MainActivity.sharedPrefs.getBoolean("soundEffects",true);
        this.bonusTime = MainActivity.sharedPrefs.getBoolean("bonusTime",false);
    }

    //Getter for this Game's Deck
    public Deck getDeck() {
        return this.deck;
    }

    //Getter for the Game's current score
    public int getScore() {
        return this.score;
    }

    //Getter for the Game's max timer
    public int getMaxTimer() {
        return this.maxTimer;
    }

    //Getter for all this Game's correct answers so far
    public ArrayList<String> getCorrectStringArray() {
        return this.correct;
    }

    //Getter for all this Game's skipped answers so far
    public ArrayList<String> getIncorrectStringArray() {
        return this.incorrect;
    }

    //Getter for this Game's difficulty
    public int getDifficulty() {
        return this.difficulty;
    }

    //Getter for the order in which the user has answered correctly or skipped
    public ArrayList<Boolean> getScoreOrder() {
        return this.scoreOrder;
    }

    //Getter for whether the Game has started or not
    public boolean hasGameStarted() {
        return this.gameStarted;
    }

    //Getter for the current state of the countdown
    public int getCountdown() {
        return this.countdown;
    }

    //Getter for the current state of the timer
    public int getTimer() {
        return this.timer;
    }

    //Getter for the current card being guessed
    public String getCurrentCard() {
        return this.currentCard;
    }

    //Starts this Game
    public void start() {
        this.gameStarted = true;
        setNextCard();
    }

    //Adds a card to the list of correctly answered cards
    //answer: The card being added
    public void addToCorrect(String answer) {
        correct.add(answer);
        scoreOrder.add(true);
        setNextCard();
        if(this.bonusTime) {
            this.timer += 3;
        }
    }

    //Adds a card to the list of skipped card
    //answer: The card being added
    public void addToIncorrect(String answer) {
        incorrect.add(answer);
        scoreOrder.add(false);
        setNextCard();
    }

    //Sets the next card
    private void setNextCard() {
        if(deckUsed.size() > 0) {
            Random r = new Random();
            int randomNum = r.nextInt(deckUsed.size());
            currentCard = deckUsed.get(randomNum);
            deckUsed.remove(randomNum);
        } else {
            currentCard = null;
        }
    }

    //Counts down the Game's countdown
    //context: Context in which this countdown takes place
    public void countdown(Context context) {
        if(soundEffects) {
            if (this.countdown == 3) {
                MediaPlayer mp = MediaPlayer.create(context, R.raw.start_tone_1);
                mp.start();
            } else if (countdown == 2) {
                MediaPlayer mp = MediaPlayer.create(context, R.raw.start_tone_2);
                mp.start();
            } else {
                MediaPlayer mp = MediaPlayer.create(context, R.raw.start_tone_3);
                mp.start();
            }
        }
        this.countdown--;
    }

    //Retrieves a String representing the difficulty of this Game
    //Returns: The difficulty of this Game as a string
    public String getDifficultyAsString() {
        if(this.difficulty == 0) {
            return "Easy";
        } else if(this.difficulty == 1) {
            return "Medium";
        } else {
            return "Hard";
        }
    }

    //Retrieves the Deck being used for this Game
    public ArrayList<String> getUsedDeck() {
        return deck.getDeckByDifficulty(difficulty);
    }

    //Increments this Game's score
    public void incrementScore() {
        this.score++;
    }

    //Decrements this Game's timer
    public void decrementTimer() {
        this.timer--;
    }

}
