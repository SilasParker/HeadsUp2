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
    private long timeLastHeldProperly;
    private boolean initialGameStart;

    //9 < X < 11 || -9 < X < -11 (this sees what side it's on)
    //-5 < Y < 5 //phone is on it's side

    //-7.5 < Z < 7.5 //phone's tilt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        initialGameStart = true;
        game.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        long timeNow = System.currentTimeMillis();
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if(initialGameStart) {
                this.cardName.setVisibility(View.VISIBLE);
                this.timerView.setVisibility(View.VISIBLE);
                this.scoreView.setVisibility(View.VISIBLE);
                this.countdownView.setVisibility(View.GONE);
                this.gamePauseTextView.setVisibility(View.INVISIBLE);
                this.
                initialGameStart = false;
            }
            if(game.hasGameStarted()) {
                countdownView.setText("GOT EEEEM");
            } else {
                if(heldProperly(x,y,z)) {
                    if(timeLastHeldProperly != 0L) {
                        if(timeNow-timeLastHeldProperly >= 1000) {
                            game.countdown();
                            countdownView.setText(String.valueOf(game.getCountdown()));
                            if(game.getCountdown() == 0) {
                                readyToStart();
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

    private boolean heldProperly(float x, float y, float z) {
        if(((x > 9 && x < 11) || (x < -9 && x > -11)) && (y > -5 && y < 5) && (z > -7.5 && z < 7.5)) {
            System.out.println("HELD CORRECT");
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