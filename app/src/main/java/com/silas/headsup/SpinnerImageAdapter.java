package com.silas.headsup;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

//Custom Adapter for the Spinner used to select an icon in the Create Deck activity
public class SpinnerImageAdapter extends ArrayAdapter<Integer> {

    int groupId;
    ArrayList<Integer> list;
    LayoutInflater inflater;

    //Constructor for this SpinnerImageAdapter
    //context: Context to create the SpinnerImageAdapter under
    //groupId: The view Id that is inflated
    //id: The Spinner Id that this adapter is attached to
    //list: All icon Ids to inflate with
    public SpinnerImageAdapter(Activity context, int groupId, int id, ArrayList<Integer> list) {
        super(context,id,list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupId = groupId;
    }

    //Generates a View for each item in the Spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupId,parent,false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.selectorIcon);
        imageView.setImageResource(list.get(position));
        return  itemView;
    }

    //Retrieves the View selected in the Spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position,convertView,parent);
    }

}
