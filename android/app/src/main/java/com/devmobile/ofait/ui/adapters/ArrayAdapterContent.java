package com.devmobile.ofait.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.devmobile.ofait.R;

import java.util.List;

/**
 * Created by Tony on 20/10/2016.
 */

public class ArrayAdapterContent extends ArrayAdapter<String> {

    public LayoutInflater layoutInflater;
    public int resId;

    public ArrayAdapterContent(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        resId = resource;
        layoutInflater = LayoutInflater.from(context);
    }

    class ContentViewHolder {
        public TextView text;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContentViewHolder viewHolder;
        if (convertView == null) {
            //convertView = layoutInflater.inflate(resId, null);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_card_content, parent, false);
            viewHolder = new ContentViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.card_content_title);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ContentViewHolder) convertView.getTag();
        String item = getItem(position);
        viewHolder.text.setText(item);
        return convertView;
    }
}
