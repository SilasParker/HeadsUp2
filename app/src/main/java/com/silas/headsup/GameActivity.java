package com.silas.headsup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private com.silas.headsup.Game game;
    private ArrayList<String> deck;
    private TextView cardName, timerView, scoreView, countdownView, gamePauseTextView, gameResultActualScore, gameResultHighScore, gameResultDifficulty;
    private String currentCard;
    private SensorManager sensorManager;
    private Sensor accSensor;
    private long timeLastHeldProperly, timeLastAnswered;
    private boolean initialGameStart, heldIncorrectly, gameInProgress, testCheck, postAnswer, soundEffects;
    private LinearLayout correctLinearLayout, incorrectLinearLayout;
    private Button gameResultBackBtn, gameResultReplayBtn;
    private int cardColour, textColour;

    private int testTempScore;
    private ArrayList<String> correctList, skipList;

    //9 < X < 11 || -9 < X < -11 (this sees what side it's on)
    //-5 < Y < 5 //phone is on it's side

    //-7.5 < Z < 7.5 //phone's tilt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
        this.soundEffects = MainActivity.sharedPrefs.getBoolean("soundEffects",true);
        this.testTempScore = 0;
        applyColourPreferences();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,accSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }



    private void applyColourPreferences() {
        int card = MainActivity.sharedPrefs.getInt("cardColour",0);
        int text = MainActivity.sharedPrefs.getInt("textColour",4);
        System.out.println("Card Colour: "+card+" Text Colour: "+text);
        ConstraintLayout layout = findViewById(R.id.gameLayout);
        switch (card) {
            case 1:
                this.cardColour = R.color.red;
                break;
            case 2:
                this.cardColour = R.color.green;
                break;
            case 3:
                this.cardColour = R.color.blue;
                break;
            case 4:
                this.cardColour = R.color.black;
                break;
            default:
                this.cardColour = R.color.white;
        }
        layout.setBackgroundColor(getColor(this.cardColour));


        switch(text) {
            case 0:
                this.textColour = R.color.white;
                break;
            case 1:
                this.textColour = R.color.red;
                break;
            case 2:
                this.textColour = R.color.green;
                break;
            case 3:
                this.textColour = R.color.blue;
                break;
            default:
                this.textColour = R.color.black;
        }
        System.out.println(this.cardColour+" "+this.textColour);

        cardName.setTextColor(getColor(this.textColour));
        timerView.setTextColor(getColor(this.textColour));
        scoreView.setTextColor(getColor(this.textColour));
        countdownView.setTextColor(getColor(this.textColour));
        gamePauseTextView.setTextColor(getColor(this.textColour));


    }


    private void setGame(Deck deck, int timer, int difficulty) {
        this.game = new Game(deck, timer, difficulty);
        this.deck = game.getUsedDeck();
    }

    private void readyToStart() {
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
                this.currentCard = game.getCurrentCard();
                this.timerView.setVisibility(View.VISIBLE);
                this.scoreView.setVisibility(View.VISIBLE);
                this.countdownView.setVisibility(View.GONE);
                this.gamePauseTextView.setVisibility(View.INVISIBLE);
                gamePauseTextView.setText("Hold phone correctly to continue playing");
                initialGameStart = false;
            }
            if(game.hasGameStarted()) {
                if(game.getCurrentCard() == null) {
                    gameInProgress = false;
                    setContentView(R.layout.activity_result);

                    try {
                        generateResultsScreen();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                            setContentView(R.layout.activity_result);
                            try {
                                generateResultsScreen();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //finish game, move on to score
                        } else {
                            timeLastHeldProperly = timeNow;
                        }
                    }



                } else if(turnedToCorrect(x,y,z) && (timeNow-timeLastAnswered >= 1000) && !postAnswer) {
                    if(soundEffects) {
                        MediaPlayer mp = MediaPlayer.create(this, R.raw.correct_tone);
                        mp.start();
                    }
                    gamePauseTextView.setText("ANSWER CORRECT");
                    game.addToCorrect(currentCard);
                    game.incrementScore();
                    currentCard = game.getCurrentCard();
                    cardName.setText(currentCard);
                    timeLastAnswered = timeNow;
                    postAnswer = true;
                } else if(turnedToSkip(x,y,z) && (timeNow-timeLastAnswered >= 1000) && !postAnswer) {
                    if(soundEffects) {
                        MediaPlayer mp = MediaPlayer.create(this, R.raw.incorrect_tone);
                        mp.start();
                    }
                    gamePauseTextView.setText("ANSWER SKIPPED");
                    game.addToIncorrect(currentCard);
                    currentCard = game.getCurrentCard();
                    cardName.setText(currentCard);
                    timeLastAnswered = timeNow;
                    postAnswer = true;


                } else {
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
                            game.countdown(this);
                            countdownView.setText(String.valueOf(game.getCountdown()));
                            if(game.getCountdown() == 0 && !game.hasGameStarted()) {
                                if(soundEffects) {
                                    MediaPlayer mp = MediaPlayer.create(this, R.raw.start_tone_0);
                                    mp.start();
                                }
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

    private boolean resultsScreenVisible() {
        if(this.gameResultBackBtn.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }



    private void generateResultsScreen() throws InterruptedException, IOException, JSONException {
        if(soundEffects) {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.times_up);
            mp.start();
        }
        this.gameResultBackBtn = findViewById(R.id.gameResultBackButton);
        this.correctLinearLayout = findViewById(R.id.gameResultCorrectLinearLayout);
        this.incorrectLinearLayout = findViewById(R.id.gameResultIncorrectLinearLayout);
        this.gameResultDifficulty = findViewById(R.id.gameResultDifficulty);

        this.gameResultReplayBtn = findViewById(R.id.gameResultReplayButton);
        this.gameResultHighScore = findViewById(R.id.gameResultHighScore);
        this.gameResultActualScore = findViewById(R.id.gameResultActualScore);
        this.gameResultHighScore.setVisibility(View.INVISIBLE);

        getWindow().getDecorView().setBackgroundColor(getColor(this.cardColour));
        this.gameResultDifficulty.setTextColor(getColor(this.textColour));
        this.gameResultHighScore.setTextColor(getColor(this.textColour));
        this.gameResultActualScore.setTextColor(getColor(this.textColour));


        this.gameResultBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.gameResultReplayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.silas.headsup.GameActivity.class);
                intent.putExtra("deck", game.getDeck());
                intent.putExtra("timer",game.getMaxTimer());
                intent.putExtra("difficulty",game.getDifficulty());
                startActivity(intent);
                finish();
            }
        });

        this.correctList = game.getCorrectStringArray();
        this.skipList = game.getIncorrectStringArray();
        int counter = 0;
        this.gameResultDifficulty.setText("Difficulty: " + game.getDifficultyAsString());
        for(boolean win : game.getScoreOrder()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    incrementCorrectSkippedLists(win);
                }
            },counter += 1000);
        }
        if(game.getScore() > game.getDeck().getHighScore()) {
            gameResultActualScore.setVisibility(View.VISIBLE);
            game.getDeck().setHighScore(game.getScore());
            game.getDeck().saveJsonToFile(this,true);
        }


    }

    private void incrementCorrectSkippedLists(boolean win) {
        TextView textView = new TextView(this);
        textView.setTextColor(getColor(this.textColour));
        if(win) {
            testTempScore++;
            this.gameResultActualScore.setText(String.valueOf(testTempScore));
            textView.setText(correctList.remove(0));
            if(soundEffects) {
                MediaPlayer mp = MediaPlayer.create(this, R.raw.correct_tone);
                mp.start();
            }
            correctLinearLayout.addView(textView);


        } else {
            textView.setText(skipList.remove(0));
            if(soundEffects) {
                MediaPlayer mp = MediaPlayer.create(this, R.raw.incorrect_tone);
                mp.start();
            }
            incorrectLinearLayout.addView(textView);
        }
    }

    private boolean turnedToCorrect(float x, float y, float z) {
        if(z < -7.5) {
            //(x > 5 && x < 7) && (y > -1 && y < 1) && (z < -7.5)
            return true;
        }
        return false;
    }

    private boolean turnedToSkip(float x, float y, float z) {
        if(z > 7.5) {
            //(x > -1 && x < 6) && (y > -1 && y < 1) && (z > 7.5)

            return true;
        }
        return false;
    }

    private boolean heldProperly(float x, float y, float z) {
        if(z > -7.5 && z < 7.5) {
            //(x > 4.5 && x < 11) && (y > -5 && y < 5) && (z > -7.5 && z < 7.5)
            return true;
        }
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