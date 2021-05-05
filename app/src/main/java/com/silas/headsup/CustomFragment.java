package com.silas.headsup;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CustomFragment extends Fragment {

    private ArrayList<ResultDeck> allResultDecks;
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
        allResultDecks = new ArrayList<>();
        addDatabaseListener();
    }



    private void addDatabaseListener() {
        DatabaseReference decksRef = database.getReference().child("decks");
        decksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                generateTable(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Could not connect to database, try again later",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateTable(DataSnapshot dataSnapshot) {
        Map<String,Object> results = (Map<String, Object>) dataSnapshot.getValue();
        if(results != null) {
            for (Map.Entry<String, Object> entry : results.entrySet()) {
                Map deck = (Map) entry.getValue();
                String id = (String) deck.get("id");
                String name = (String) deck.get("name");
                String author = (String) deck.get("author");
                int downloads = (int) (long) deck.get("downloads");
                int size = (int) (long) deck.get("deckSize");
                ResultDeck resultDeck = new ResultDeck(id, name, author, downloads, size);
                allResultDecks.add(resultDeck);
            }
            TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.customTableLayout);
            for (ResultDeck resultDeck : allResultDecks) {
                TableRow tableRow = new TableRow(getContext());
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView nameView = new TextView(getContext());
                TextView authorView = new TextView(getContext());
                TextView sizeView = new TextView(getContext());
                TextView downloadsView = new TextView(getContext());
                nameView.setText(resultDeck.getName());
                authorView.setText(resultDeck.getAuthor());
                sizeView.setText(String.valueOf(resultDeck.getSize()));
                downloadsView.setText(String.valueOf(resultDeck.getDownloads()));
                tableRow.addView(nameView);
                tableRow.addView(authorView);
                tableRow.addView(sizeView);
                tableRow.addView(downloadsView);
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeckSelectedToDownload(resultDeck);
                    }
                });
                tableLayout.addView(tableRow);
            }
        }
    }

    private void onDeckSelectedToDownload(ResultDeck resultDeck) {
        DatabaseReference deckRef = database.getReference("decks/"+resultDeck.getId());
        //final DataSnapshot[] deckSnapshot = new DataSnapshot[1];
        deckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("set datasnapshot");
                Map<String,Object> deckMap = (Map<String, Object>) snapshot.getValue();

                View view = getView();
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.deck_download_overlay,null);
                int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                TextView deckName = (TextView) popUpView.findViewById(R.id.customDownloadName);
                deckName.setText((String) deckMap.get("name"));
                TextView deckDescription = (TextView) popUpView.findViewById(R.id.customDownloadDescription);
                deckDescription.setText((String) deckMap.get("description"));
                TextView easyCount = (TextView) popUpView.findViewById(R.id.customDownloadEasyTotal);
                TextView mediumCount = (TextView) popUpView.findViewById(R.id.customDownloadMediumTotal);
                TextView hardCount = (TextView) popUpView.findViewById(R.id.customDownloadHardTotal);
                easyCount.setText(String.valueOf((int) (long) deckMap.get("easyCount")));
                mediumCount.setText(String.valueOf((int) (long) deckMap.get("mediumCount")));
                hardCount.setText(String.valueOf((int) (long) deckMap.get("hardCount")));

                Button downloadButton = (Button) popUpView.findViewById(R.id.customDownloadButton);
                TextView exitButton = (TextView) popUpView.findViewById(R.id.customDownloadExitButton);





                final PopupWindow popUp = new PopupWindow(popUpView,width,height,true);

                downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = (String) deckMap.get("name");
                        String description = (String) deckMap.get("description");
                        String author = (String) deckMap.get("author");
                        String[] easyCards = new String[(int) deckMap.get("easyCount")];
                        String[] mediumCards = new String[(int) deckMap.get("mediumCount")];
                        String[] hardCards = new String[(int) deckMap.get("hardCount")];
                        deckRef.child("easyCards").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(int i = 0; i < easyCards.length;i++) {
                                    easyCards[i] = snapshot.child(String.valueOf(i)).getValue().toString();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        deckRef.child("mediumCards").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(int i = 0; i < mediumCards.length;i++) {
                                    mediumCards[i] = snapshot.child(String.valueOf(i)).getValue().toString();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        deckRef.child("hardCards").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(int i = 0; i < hardCards.length;i++) {
                                    hardCards[i] = snapshot.child(String.valueOf(i)).getValue().toString();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        int iconId = (int) deckMap.get("iconId");
                        Deck newDeck = new Deck(name,description,author,easyCards,mediumCards,hardCards,iconId,true,0,false);
                        try {
                            newDeck.saveJsonToFile(getContext(),true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        deckRef.child("downloads").setValue(String.valueOf((int) deckMap.get("downloadss") + 1));
                        popUp.dismiss();
                    }
                });

                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUp.dismiss();
                    }
                });

                popUp.showAtLocation(getView(),Gravity.CENTER,0,0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("didnt set");
                Toast.makeText(getContext(),"Could not connect to database, try again later",Toast.LENGTH_LONG).show();
            }
        });

    }





}
