package com.example.headsup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private Game game;
    private ArrayList<String> deck;
    private TextView cardName, timerView, scoreView, countdownView, gamePauseTextView;
    private String currentCard;
    private SensorManager sensorManager;
    private Sensor accSensor;
    private long timeLastHeldProperly, timeLastAnswered;
    private boolean initialGameStart, heldIncorrectly, gameInProgress, testCheck, postAnswer;

    //9 < X < 11 || -9 < X < -11 (this sees what side it's on)
    //-5 < Y < 5 //phone is on it's side

    //-7.5 < Z < 7.5 //phone's tilt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("ACTIVITY STARTED: "+this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.cardName = findViewById(R.id.gameCardName);
        this.timerView = findViewById(R.id.gameTimer);
        this.scoreView = findViewById(R.id.gameScore);
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.countdownView = findViewById(R.id.gameStartCountdown);
        this.timeLastHeldProperly = 0L;
        setGame(getIntent().getParcelableExtra("deck"),getIntent().getIntExtra("timer",120),getIntent().getIntExtra("difficulty",1));
        this.initialGameStart = false;
        this.gamePauseTextView = findViewById(R.id.gamePauseText);
        this.gameInProgress = true;
        this.testCheck = true;
        this.timeLastAnswered = 0L;
        this.postAnswer = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,accSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setGame(Deck deck, int timer, int difficulty) {
        this.game = new Game(deck, timer, difficulty);
        this.deck = game.getUsedDeck();
    }

    private void readyToStart() {
        System.out.println("ACTIVITY ID: "+this);
        if(testCheck) {
            initialGameStart = true;
            game.start();
            testCheck = false;
        }
    }

    private void setCurrentCard() {
        this.currentCard = game.getCurrentCard();
    }

    private void displayNextCard() {
        this.cardName.setText(currentCard);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        long timeNow = System.currentTimeMillis();
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && gameInProgress) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];


            if(initialGameStart) {
                this.cardName.setVisibility(View.VISIBLE);
                cardName.setText(game.getCurrentCard());
                this.timerView.setVisibility(View.VISIBLE);
                this.scoreView.setVisibility(View.VISIBLE);
                this.countdownView.setVisibility(View.GONE);
                this.gamePauseTextView.setVisibility(View.INVISIBLE);
                gamePauseTextView.setText("Hold phone correctly to continue playing");
                initialGameStart = false;
                System.out.println("YYYYYYYYYYYYYYYYYY: "+game.getCurrentCard());
            }
            if(game.hasGameStarted()) {
                if(game.getCurrentCard() == null) {
                    gameInProgress = false;
                    System.out.println("FINAL SCORE: "+game.getScore());
                    //finish game (out of cards), move onto score
                }
                if(heldProperly(x,y,z)) {
                    postAnswer = false;
                    if(heldIncorrectly) {
                        this.cardName.setVisibility(View.VISIBLE);
                        this.timerView.setVisibility(View.VISIBLE);
                        this.scoreView.setVisibility(View.VISIBLE);
                        this.gamePauseTextView.setVisibility(View.INVISIBLE);
                        heldIncorrectly = false;
                    }
                    if(timeNow-timeLastHeldProperly >= 1000) {
                        game.decrementTimer();
                        this.timerView.setText(String.valueOf(game.getTimer()));
                        if(game.getTimer() < 0) {
                            gameInProgress = false;
                            //finish game, move on to score
                        } else {
                            timeLastHeldProperly = timeNow;
                        }
                    }



                } else if(turnedToCorrect(x,y,z) && (timeNow-timeLastAnswered >= 1000) && !postAnswer) {
                    System.out.println("TURNED FOR CORRECT");
                    gamePauseTextView.setText("ANSWER CORRECT");
                    game.addToCorrect(currentCard);
                    game.incrementScore();
                    currentCard = game.getCurrentCard();
                    cardName.setText(currentCard);
                    timeLastAnswered = timeNow;
                    postAnswer = true;
                } else if(turnedToSkip(x,y,z) && (timeNow-timeLastAnswered >= 1000) && !postAnswer) {
                    System.out.println("TURNED FOR SKIP, SKIPPING: "+currentCard);
                    gamePauseTextView.setText("ANSWER SKIPPED");
                    game.addToIncorrect(currentCard);
                    currentCard = game.getCurrentCard();
                    System.out.println("CARD IS NOW "+currentCard);
                    cardName.setText(currentCard);
                    timeLastAnswered = timeNow;
                    postAnswer = true;


                } else {
                    System.out.println(x+" "+y+" "+z);
                    heldIncorrectly = true;
                    gamePauseTextView.setText("Hold phone correctly to continue playing");
                    this.cardName.setVisibility(View.INVISIBLE);
                    this.timerView.setVisibility(View.INVISIBLE);
                    this.scoreView.setVisibility(View.INVISIBLE);
                    this.gamePauseTextView.setVisibility(View.VISIBLE);

                }
            } else {
                if(heldProperly(x,y,z)) {
                    if(timeLastHeldProperly != 0L) {
                        if(timeNow-timeLastHeldProperly >= 1000) {
                            game.countdown();
                            countdownView.setText(String.valueOf(game.getCountdown()));
                            if(game.getCountdown() == 0 && !game.hasGameStarted()) {
                                readyToStart();
                                this.heldIncorrectly = false;
                            } else {
                                timeLastHeldProperly = timeNow;
                            }
                        }
                    } else {
                        timeLastHeldProperly = timeNow;
                    }
                }
            }
        }
    }

    private boolean turnedToCorrect(float x, float y, float z) {
        if(z < -7.5) {
            //(x > 5 && x < 7) && (y > -1 && y < 1) && (z < -7.5)
            System.out.println("ANSWER CORRECT");
            return true;
        }
        return false;
    }

    private boolean turnedToSkip(float x, float y, float z) {
        if(z > 7.5) {
            //(x > -1 && x < 6) && (y > -1 && y < 1) && (z > 7.5)
            System.out.println("ANSWER SKIPPED");

            return true;
        }
        return false;
    }

    private boolean heldProperly(float x, float y, float z) {
        if(z > -7.5 && z < 7.5) {
            //(x > 4.5 && x < 11) && (y > -5 && y < 5) && (z > -7.5 && z < 7.5)
            //System.out.println("HELD CORRECT");
            return true;
        }
        System.out.println("HELD INCORRECT");
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float lowPass(float current, float gravity) {
        float mAlpha = 0.8f;
        return gravity * mAlpha + current * (1-mAlpha);
    }

    private float highPass(float current, float gravity) {
        return current-gravity;
    }
}