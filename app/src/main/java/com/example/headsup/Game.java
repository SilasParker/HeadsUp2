package com.example.headsup;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int score, timer, difficulty, countdown;
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
        System.out.println("DECK USED: "+getUsedDeck());
        this.paused = true;
        this.gameStarted = false;
        this.countdown = 3;
    }

    public void start() {
        this.gameStarted = true;
        setNextCard();
    }

    private void setNextCard() {
        if(deckUsed.size() > 0) {
            Random r = new Random();
            System.out.println("DECK DIFFICULTY: " + this.difficulty);
            System.out.println("DECK USED SIZE IS: " + deckUsed.size());
            int randomNum = r.nextInt(deckUsed.size());

            currentCard = deckUsed.get(randomNum);
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX REMOVING: "+deckUsed.get(randomNum));
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
