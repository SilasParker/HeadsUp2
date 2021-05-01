package com.silas.headsup;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class SpinnerImageAdapter extends ArrayAdapter<Integer> {

    int groupId;
    Activity context;
    ArrayList<Integer> list;
    LayoutInflater inflater;

    public SpinnerImageAdapter(Activity context, int groupId, int id, ArrayList<Integer> list) {
        super(context,id,list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupId = groupId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupId,parent,false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.selectorIcon);
        imageView.setImageResource(list.get(position));
        return  itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position,convertView,parent);
    }
}
