package com.example.headsup;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int score, timer, difficulty;
    private Deck deck;
    private ArrayList<String> correct, incorrect, deckUsed;
    private ArrayList<Boolean> scoreOrder;
    private String currentCard;

    public Game(Deck deck, int timer, int difficulty) {
        this.deck = deck;
        this.score = 0;
        this.timer = timer;
        this.correct = new ArrayList<>();
        this.incorrect = new ArrayList<>();
        this.difficulty = difficulty;
        this.scoreOrder = new ArrayList<>();
        this.deckUsed = getUsedDeck();
    }

    private void setNextCard() {
        Random r = new Random();
        int randomNum = r.nextInt(deckUsed.size());
        //continue
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

    public void addToCorrect(String answer) {
        correct.add(answer);
        scoreOrder.add(true);
    }

    public void addToIncorrect(String answer) {
        incorrect.add(answer);
        scoreOrder.add(false);
    }

    public ArrayList<String> getUsedDeck() {
        return deck.getDeckByDifficulty(difficulty);
    }




}
