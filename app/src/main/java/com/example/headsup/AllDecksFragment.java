package com.example.headsup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AllDecksFragment extends DeckLayoutFragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_decks,container,false);
    }

    @Override
    public void updateGrid(int gridId) {
        if(this.getDeckList() != null) {
            this.setGridAdapter(new GridAdapter(getContext(),getDeckList().getAllDecks(),this));
            setGridView((GridView) getView().findViewById(gridId));
            getGridView().setAdapter(getGridAdapter());
        }
    }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        updateGrid(R.id.allDecksGrid);
        this.setSearchView(getActivity().findViewById(R.id.allDecksSearch));
        this.getSearchView().setOnQueryTextListener(new DeckSearchOnQueryTextListener(this.getDeckList(),this));
    }






}
