package com.example.headsup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GridAdapter extends BaseAdapter {
    private Deck deck;

    private Context context;
    private LayoutInflater inflater;

    private GridAdapter(Context c, Deck deck) {
        this.context = c;
        this.deck = deck;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
        return null;
    }
    //TODO you need to create the row_item.xml to accurately represent a deck in the grid and then complete this adapter
    //https://www.youtube.com/watch?v=2gbPOEH7cQ8
    //you were having trouble aligning the linear layout correctly, probs change the view
}
