package com.silas.headsup;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.slider.Slider;

//Fragment Class for the Settings Tab
public class SettingsFragment extends Fragment {

    private int selectedCardColour, selectedCardTextColour;

    //During onCreateView in the Fragment lifecycle, inflates this fragment's layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }

    //During onResume in the Fragment lifecycle, initialise the settings form
    @Override
    public void onResume() {
        super.onResume();
        initialiseSettings();
        setColourOnClick(getActivity().findViewById(R.id.settingsCardColourWhite),true,0);
        setColourOnClick(getActivity().findViewById(R.id.settingsCardColourRed),true,1);
        setColourOnClick(getActivity().findViewById(R.id.settingsCardColourGreen),true,2);
        setColourOnClick(getActivity().findViewById(R.id.settingsCardColourBlue),true,3);
        setColourOnClick(getActivity().findViewById(R.id.settingsCardColourBlack),true,4);
        setColourOnClick(getActivity().findViewById(R.id.settingsCardTextColourWhite),false,0);
        setColourOnClick(getActivity().findViewById(R.id.settingsCardTextColourRed),false,1);
        setColourOnClick(getActivity().findViewById(R.id.settingsCardTextColourGreen),false,2);
        setColourOnClick(getActivity().findViewById(R.id.settingsCardTextColourBlue),false,3);
        setColourOnClick(getActivity().findViewById(R.id.settingsCardTextColourBlack),false,4);
        getActivity().findViewById(R.id.settingsSaveButton).setOnClickListener(new View.OnClickListener() {

            //Save button saves the form's details in the SharedPreferences
            @Override
            public void onClick(View v) {
                savePrefs();
            }

        });
    }

    //Initialises the form
    private void initialiseSettings() {
        float timer = MainActivity.sharedPrefs.getFloat("timer",120f);
        ((Slider) getActivity().findViewById(R.id.settingsTimerSlider)).setValue(timer);
        RadioButton radBut;
        switch(MainActivity.sharedPrefs.getInt("difficulty",2)) {
            case 1:
                radBut = (RadioButton) getActivity().findViewById(R.id.settingsEasyRadio);
                break;
            case 3:
                radBut = (RadioButton) getActivity().findViewById(R.id.settingsHardRadio);
                break;
            default:
                radBut = (RadioButton) getActivity().findViewById(R.id.settingsMediumRadio);
                break;
        }
        radBut.toggle();
        boolean bonusTime = MainActivity.sharedPrefs.getBoolean("bonusTime",false);
        ((Switch) getActivity().findViewById(R.id.settingsBonusSwitch)).setChecked(bonusTime);
        boolean soundEffects = MainActivity.sharedPrefs.getBoolean("soundEffects",true);
        ((Switch) getActivity().findViewById(R.id.settingsSoundSwitch)).setChecked(soundEffects);
        highlightColour(true,MainActivity.sharedPrefs.getInt("cardColour",0));
        highlightColour(false,MainActivity.sharedPrefs.getInt("textColour",0));
    }

    //Highlights a colour selected by the user
    //background: whether the colour saved is for the background or text
    //colour: The colour being highlighted
    private void highlightColour(boolean background,int colour) {
        ImageButton[] buttons = new ImageButton[5];
        if(background) {
            buttons[0] = getActivity().findViewById(R.id.settingsCardColourWhite);
            buttons[1] = getActivity().findViewById(R.id.settingsCardColourRed);
            buttons[2] = getActivity().findViewById(R.id.settingsCardColourGreen);
            buttons[3] = getActivity().findViewById(R.id.settingsCardColourBlue);
            buttons[4] = getActivity().findViewById(R.id.settingsCardColourBlack);
        } else {
            buttons[0] = getActivity().findViewById(R.id.settingsCardTextColourWhite);
            buttons[1] = getActivity().findViewById(R.id.settingsCardTextColourRed);
            buttons[2] = getActivity().findViewById(R.id.settingsCardTextColourGreen);
            buttons[3] = getActivity().findViewById(R.id.settingsCardTextColourBlue);
            buttons[4] = getActivity().findViewById(R.id.settingsCardTextColourBlack);
        }
        for(int i = 0; i < 5; i++) {
            GradientDrawable imageButDrawable = (GradientDrawable) buttons[i].getDrawable();
            if(colour == i) {
                imageButDrawable.setStroke(8, Color.parseColor("#FFD700"));
                if(background) {
                    this.selectedCardColour = colour;
                } else {
                    this.selectedCardTextColour = colour;
                }
            } else {
                imageButDrawable.setStroke(4, Color.parseColor("#000000"));
            }
        }
    }

    //Sets each colours on click function
    private void setColourOnClick(ImageButton imgBut, boolean background, int colour) {
        imgBut.setOnClickListener(new View.OnClickListener() {

            //When a colour is selected, it is highlighted
            @Override
            public void onClick(View v) {
                highlightColour(background,colour);
            }

        });
    }

    //Saves the form to SharedPreferences
    private void savePrefs() {
        Slider slider = getActivity().findViewById(R.id.settingsTimerSlider);
        float timer = slider.getValue();
        RadioGroup radGroup = getActivity().findViewById(R.id.settingsDifficultyRadioGroup);
        RadioButton radBut = getActivity().findViewById(radGroup.getCheckedRadioButtonId());
        String difficultyStr = (String) radBut.getText();
        int difficulty = 1;
        if(difficultyStr.equals("MEDIUM")) {
            difficulty = 2;
        } else if(difficultyStr.equals("HARD")) {
            difficulty = 3;
        }
        boolean bonus = ((Switch) getActivity().findViewById(R.id.settingsBonusSwitch)).isChecked();
        boolean sound = ((Switch) getActivity().findViewById(R.id.settingsSoundSwitch)).isChecked();
        SharedPreferences.Editor editor = MainActivity.sharedPrefs.edit();
        editor.putFloat("timer",timer);
        editor.putInt("difficulty",difficulty);
        editor.putBoolean("bonusTime",bonus);
        editor.putBoolean("soundEffects",sound);
        editor.putInt("textColour",this.selectedCardTextColour);
        editor.putInt("cardColour",this.selectedCardColour);
        editor.commit();
        Toast.makeText(getContext(),"Settings Saved",Toast.LENGTH_SHORT).show();
    }

}
