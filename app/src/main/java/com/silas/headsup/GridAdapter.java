package com.silas.headsup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

//Custom Adapter for the Deck Grid used in the All and Favourite Decks Screens
public class GridAdapter extends BaseAdapter {
    private ArrayList<Deck> decks;
    private Fragment parentFragment;
    private Context context;
    private LayoutInflater inflater;

    //Constructor for this GridAdapter
    //c: Context to create the GridAdapter under
    //decks: Decks to inflate the Grid with
    //fragment: Fragment that this GridAdapter
    GridAdapter(Context c, ArrayList decks, Fragment fragment) {
        this.context = c;
        this.decks = decks;
        this.parentFragment = fragment;
    }

    //Generates and retrieves a View representing a Deck to inflate the Grid with
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.row_item,null);
        }
        if(!decks.get(position).isCustom()) {
            convertView.findViewById(R.id.delete).setVisibility(View.GONE);
        }
        TextView deckNameView = convertView.findViewById(R.id.deck_name);
        ImageView iconView = convertView.findViewById(R.id.icon);
        String deckName = decks.get(position).getName();
        if(deckName.length() > 25) {
            deckName = deckName.substring(0,21)+"...";
        }
        deckNameView.setText(deckName);
        iconView.setImageResource(decks.get(position).getIconId());
        ImageView favouriteView = convertView.findViewById(R.id.favourite_star);
        if(decks.get(position).isFavourite()) {
            favouriteView.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            favouriteView.setImageResource(R.drawable.ic_baseline_star_outline_24);
        }
        favouriteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if(parentFragment instanceof AllDecksFragment) {
                        toggleFavouriteOnDeck(position,false);
                        AllDecksFragment allDecksFragment = (AllDecksFragment) parentFragment;
                        allDecksFragment.updateGrid(R.id.allDecksGrid);
                    } else if(parentFragment instanceof FavouritesFragment) {
                        toggleFavouriteOnDeck(position,true);
                        FavouritesFragment favouritesFragment = (FavouritesFragment) parentFragment;
                        favouritesFragment.updateGrid(R.id.favouritesGrid);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        TextView highScoreView = convertView.findViewById(R.id.highscore);
        highScoreView.setText(String.valueOf(decks.get(position).getHighScore()));
        TextView deleteView = convertView.findViewById(R.id.delete);
        deleteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDiaBuilder = new AlertDialog.Builder(v.getContext());
                alertDiaBuilder
                        .setTitle("WARNING")
                        .setCancelable(true)
                        .setMessage("Are you sure you want to delete this deck? Cannot be undone")
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(parentFragment instanceof AllDecksFragment) {
                                    removeDeck(position,false);
                                    AllDecksFragment allDecksFragment = (AllDecksFragment) parentFragment;
                                    allDecksFragment.updateGrid(R.id.allDecksGrid);
                                } else if(parentFragment instanceof FavouritesFragment) {
                                    removeDeck(position,true);
                                    FavouritesFragment favouritesFragment = (FavouritesFragment) parentFragment;
                                    favouritesFragment.updateGrid(R.id.favouritesGrid);
                                }
                            }
                        }).show();
            }
        });
        convertView.setBackgroundResource(R.drawable.background_border);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(parentFragment instanceof AllDecksFragment) {
                    AllDecksFragment allDecksFragment = (AllDecksFragment) parentFragment;
                    allDecksFragment.onDeckSelectedToPlay(position);
                } else if(parentFragment instanceof FavouritesFragment) {
                    FavouritesFragment favouritesFragment = (FavouritesFragment) parentFragment;
                    favouritesFragment.onDeckSelectedToPlay(position);
                }
            }

        });
        return convertView;
    }

    //Retrieves the size of the specified Deck
    @Override
    public int getCount() {
        return decks.size();
    }

    //Retrieves the Deck of the index specified
    @Override
    public Object getItem(int position) {
        return decks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //Toggles the favourite status of the indexed Deck
    //position: Index of the Deck to toggle
    //favFragment: Whether the fragment that this took place in was a FavouritesFragment instance or not
    private void toggleFavouriteOnDeck(int position, boolean favFragment) throws IOException, JSONException {
        MainActivity.deckList.toggleDeckFavourite(position,context,favFragment);
    }

    //Removes the specified Deck
    //position: Index of Deck to remove
    //favFragment: Whether the fragment that this took place in was a FavouritesFragment instance or not
    private void removeDeck(int position, boolean favFragment) {
        MainActivity.deckList.remove(position,favFragment);
    }

}
