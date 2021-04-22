package com.example.headsup;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int score, timer, difficulty, countdown, maxTimer;
    private Deck deck;
    private ArrayList<String> correct, incorrect, deckUsed;
    private ArrayList<Boolean> scoreOrder;
    private String currentCard;
    private boolean paused, gameStarted;

    public Game(Deck deck, int timer, int difficulty) {
        this.deck = deck;
        this.score = 0;
        this.timer = timer;
        this.correct = new ArrayList<>();
        this.incorrect = new ArrayList<>();
        this.difficulty = difficulty;
        this.scoreOrder = new ArrayList<>();
        this.deckUsed = getUsedDeck();
        this.paused = true;
        this.gameStarted = false;
        this.countdown = 3;
        this.maxTimer = timer;
    }

    public String getDifficultyAsString() {
        if(this.difficulty == 0) {
            return "Easy";
        } else if(this.difficulty == 1) {
            return "Medium";
        } else {
            return "Hard";
        }
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public int getMaxTimer() {
        return this.maxTimer;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public ArrayList<String> getCorrectStringArray() {
        return this.correct;
    }

    public ArrayList<String> getIncorrectStringArray() {
        return this.incorrect;
    }

    public void start() {
        this.gameStarted = true;
        setNextCard();
    }

    public ArrayList<Boolean> getScoreOrder() {
        return this.scoreOrder;
    }

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

    public String getCurrentCard() {
        return this.currentCard;
    }

    public void countdown() {
        this.countdown--;
    }

    public int getCountdown() {
        return this.countdown;
    }

    public boolean hasGameStarted() {
        return this.gameStarted;
    }

    public void pause() {
        this.paused = true;
    }

    public void unpause() {
        this.paused = false;
    }

    public boolean isPaused() {
        return this.paused;
    }


    public int getScore() {
        return this.score;
    }

    public void incrementScore() {
        this.score++;
    }

    public int getTimer() {
        return this.timer;
    }

    public void decrementTimer() {
        this.timer--;
    }

    public void addToCorrect(String answer) {
        correct.add(answer);
        scoreOrder.add(true);
        setNextCard();
    }

    public void addToIncorrect(String answer) {
        incorrect.add(answer);
        scoreOrder.add(false);
        setNextCard();
    }

    public ArrayList<String> getUsedDeck() {
        return deck.getDeckByDifficulty(difficulty);
    }




}
