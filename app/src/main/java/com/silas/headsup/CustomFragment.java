package com.silas.headsup;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomFragment extends Fragment {

    private FirebaseDatabase database;
    private FloatingActionButton createDeckBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_custom,container,false);


    }

    @Override
    public void onResume() {
        super.onResume();
        this.database = FirebaseDatabase.getInstance();
        createDeckBtn = getView().findViewById(R.id.customFloatingAdd);
        createDeckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CreateDeckActivity.class);
                startActivity(intent);
            }
        });

    }

    private void generateTable() {
        DatabaseReference decksRef = database.getReference().child("decks");

    }




}
