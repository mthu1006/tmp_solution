package com.example.pk.tpmresolution.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.model.CommonClass;

import java.util.ArrayList;

/**
 * Created by Julian on 5/11/2017.
 */

public class CommonAdapter extends BaseAdapter{

    Context context;
    int resource;
    ViewHolder viewHolder;
    ArrayList<CommonClass> list;


    private static class ViewHolder {
        private CustomFontTextView itemView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CommonClass getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public CommonAdapter(ArrayList<CommonClass> objects, Context context) {
        this.context = context;
        this.list = objects;
    }

    public CustomFontTextView getTextView() {
        return viewHolder.itemView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.common_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (CustomFontTextView) convertView.findViewById(R.id.textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommonClass item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(item.getName());
        }

        return convertView;
    }

}

