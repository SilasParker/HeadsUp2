package com.example.headsup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private ArrayList<Deck> decks;

    private Context context;
    private LayoutInflater inflater;


    GridAdapter(Context c, ArrayList decks) {
        this.context = c;
        this.decks = decks;
    }

    @Override
    public int getCount() {
        return decks.size();
    }

    @Override
    public Object getItem(int position) {
        return decks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

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
        deckNameView.setText(decks.get(position).getName());
        iconView.setImageResource(decks.get(position).getIconId());
        ImageView favouriteView = convertView.findViewById(R.id.favourite_star);
        if(decks.get(position).isFavourite()) {
            favouriteView.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            favouriteView.setImageResource(R.drawable.ic_baseline_star_outline_24);
        }
        TextView highScoreView = convertView.findViewById(R.id.highscore);
        highScoreView.setText(String.valueOf(decks.get(position).getHighScore()));


        convertView.setBackgroundResource(R.color.teal_200);
        return convertView;
    }

    //Write functions to remove current deck etc. need reference to mainactivity




}
