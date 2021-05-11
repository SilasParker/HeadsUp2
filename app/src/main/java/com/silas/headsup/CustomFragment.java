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
import android.widget.SearchView;
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
    private FloatingActionButton createDeckBtn, sortDownloadBtn, sortDateBtn;
    private TableLayout tableLayout;
    private SearchView searchView;
    private DataSnapshot tempSnapShot;

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
        this.sortDownloadBtn = getView().findViewById(R.id.customFloatingDownload);
        sortDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateTable(tempSnapShot,"",true,false);
            }
        });
        this.sortDateBtn = getView().findViewById(R.id.customFloatingDate);
        sortDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateTable(tempSnapShot,"",false,true);
            }
        });
        allResultDecks = new ArrayList<>();
        addDatabaseListener();
        this.tableLayout = getView().findViewById(R.id.customTableLayout);
        this.searchView = getView().findViewById(R.id.customSearch);
        this.searchView.setOnQueryTextListener(new DatabaseSearchOnQueryTextListener(this));
    }

    private void setTempSnapShot(DataSnapshot snapshot) {
        this.tempSnapShot = snapshot;
    }

    public DataSnapshot getTempSnapShot() {
        return this.tempSnapShot;
    }



    private void addDatabaseListener() {
        DatabaseReference decksRef = database.getReference().child("decks");
        decksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setTempSnapShot(snapshot);
                generateTable(snapshot,"",false,false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Could not connect to database, try again later",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean matchTerm(String search, String name) {
        if(search.length() == 0) {
            return true;
        } if(search.length() > name.length()) {
            return false;
        } else if(name.substring(0,search.length()).toLowerCase().equals(search.toLowerCase())) {
            return true;
        }
        return  false;
    }

    public void generateTable(DataSnapshot dataSnapshot, String search, boolean sortByDownload, boolean sortByDate) {
        Map<String,Object> results = (Map<String, Object>) dataSnapshot.getValue();
        TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.customTableLayout);
        allResultDecks.clear();
        for(int i = 1;i < tableLayout.getChildCount();i++) {
            View child = tableLayout.getChildAt(i);
            if(child instanceof  TableRow) {
                ((TableRow) child).removeAllViews();
            }
        }
        if(results != null) {
            for (Map.Entry<String, Object> entry : results.entrySet()) {
                Map deck = (Map) entry.getValue();
                String id = (String) deck.get("id");
                String name = (String) deck.get("name");
                if(matchTerm(search,name)) {
                    if (name.length() > 20) {
                        String beginning = name.substring(0, 20);
                        String end = name.substring(20);
                        name = beginning + "\n" + end;
                    }
                    String author = (String) deck.get("author");
                    if (author.length() > 10) {
                        String beginning = author.substring(0, 10);
                        String end = author.substring(10);
                        author = beginning + "\n" + end;
                    }
                    int downloads = (int) (long) deck.get("downloads");
                    int size = (int) (long) deck.get("deckSize");
                    int year = (int) (long) deck.get("timeYear");
                    int day = (int) (long) deck.get("timeDay");
                    ResultDeck resultDeck = new ResultDeck(id, name, author, downloads, size,year,day);
                    allResultDecks.add(resultDeck);
                }
            }
            if(sortByDownload) {
                allResultDecks = this.sortByDownloads(allResultDecks);
            } else if(sortByDate) {
                allResultDecks = this.sortByDate(allResultDecks);
            }
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

    private ArrayList<ResultDeck> sortByDownloads(ArrayList<ResultDeck> resultDecks) {
        ArrayList<ResultDeck> sortedResultDecks = new ArrayList<>();
        while(resultDecks.size() > 0) {
            int high = -1;
            ResultDeck deckToAdd = null;
            for(ResultDeck deck : resultDecks) {
                if(deck.getDownloads() > high) {
                    deckToAdd = deck;
                    high = deck.getDownloads();
                }
            }
            sortedResultDecks.add(deckToAdd);
            resultDecks.remove(deckToAdd);
        }
        return sortedResultDecks;
    }

    private ArrayList<ResultDeck> sortByDate(ArrayList<ResultDeck> resultDecks) {
        ArrayList<ResultDeck> sortedResultDecks = new ArrayList<>();
        while(resultDecks.size() > 0) {
            int highYear = 0;
            int highDay = 0;
            ResultDeck deckToAdd = null;
            for(ResultDeck deck : resultDecks) {
                if(deck.getYear() > highYear || (deck.getYear() == highYear && deck.getDay() > highDay)) {
                    deckToAdd = deck;
                    highYear = deck.getYear();
                    highDay = deck.getDay();
                }
            }
            sortedResultDecks.add(deckToAdd);
            resultDecks.remove(deckToAdd);
        }
        return sortedResultDecks;
    }

    private void onDeckSelectedToDownload(ResultDeck resultDeck) {
        DatabaseReference deckRef = database.getReference("decks/"+resultDeck.getId());
        deckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                TextView author = (TextView) popUpView.findViewById(R.id.customDownloadAuthor);
                author.setText("By: "+(String) deckMap.get("author"));
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
                        String[] easyCards = new String[(int) (long) deckMap.get("easyCount")];
                        String[] mediumCards = new String[(int) (long) deckMap.get("mediumCount")];
                        String[] hardCards = new String[(int) (long) deckMap.get("hardCount")];
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
                        int iconId = (int) (long) deckMap.get("iconId");
                        Deck newDeck = new Deck(name,description,author,easyCards,mediumCards,hardCards,iconId,true,0,false);
                        try {
                            newDeck.saveJsonToFile(getContext(),false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        deckRef.child("downloads").setValue((int) (long) deckMap.get("downloads")+1);
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
                Toast.makeText(getContext(),"Could not connect to database, try again later",Toast.LENGTH_LONG).show();
            }
        });

    }





}
