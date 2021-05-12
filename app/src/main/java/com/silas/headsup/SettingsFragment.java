package com.silas.headsup;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;

public class SettingsFragment extends Fragment {
    private int selectedCardColour, selectedCardTextColour;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initialiseSettings() {
        float timer = MainActivity.sharedPrefs.getFloat("timer",120f);
        ((Slider) getActivity().findViewById(R.id.settingsTimerSlider)).setValue(timer);

        RadioGroup radGroup = (RadioGroup) getActivity().findViewById(R.id.settingsDifficultyRadioGroup);
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

        ImageButton imageBut;
        switch (MainActivity.sharedPrefs.getInt("cardColour",0)) {
            case 1:
                imageBut = (ImageButton) getActivity().findViewById(R.id.settingsCardColourRed);
                break;
            case 2:
                imageBut = (ImageButton) getActivity().findViewById(R.id.settingsCardColourGreen);
                break;
            case 3:
                imageBut = (ImageButton) getActivity().findViewById(R.id.settingsCardColourBlue);
                break;
            case 4:
                imageBut = (ImageButton) getActivity().findViewById(R.id.settingsCardColourBlack);
                break;
            default:
                imageBut = (ImageButton) getActivity().findViewById(R.id.settingsCardColourWhite);
                break;
        }
        //implement highlighted button
    }
}
