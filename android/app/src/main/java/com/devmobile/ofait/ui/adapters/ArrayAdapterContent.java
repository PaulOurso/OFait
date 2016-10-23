package com.devmobile.ofait.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.utils.FormatHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Tony on 20/10/2016.
 */

public class ArrayAdapterContent extends ArrayAdapter<Content> {

    public LayoutInflater layoutInflater;
    public int resId;

    public ArrayAdapterContent(Context context, int resource, List<Content> objects) {
        super(context, resource, objects);
        resId = resource;
        layoutInflater = LayoutInflater.from(context);
    }

    class ContentViewHolder {
        public TextView tvContentValue;
        public TextView tvContentCreatedBy;
        public TextView tvContentCreatedDate;
        public TextView tvContentPoints;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContentViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_card_content, parent, false);
            viewHolder = new ContentViewHolder();
            viewHolder.tvContentValue = (TextView) convertView.findViewById(R.id.card_content_value);
            viewHolder.tvContentCreatedBy = (TextView) convertView.findViewById(R.id.card_content_created_by);
            viewHolder.tvContentCreatedDate = (TextView) convertView.findViewById(R.id.card_content_created_date);
            viewHolder.tvContentPoints = (TextView) convertView.findViewById(R.id.card_content_points);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ContentViewHolder) convertView.getTag();

        Content content = getItem(position);
        viewHolder.tvContentValue.setText(content.content_value);
        viewHolder.tvContentCreatedBy.setText(getContext().getString(R.string.content_created_by, content.created_by.pseudo));
        viewHolder.tvContentPoints.setText(getContext().getString(R.string.content_points, content.points));

        Calendar calendar = FormatHelper.formatStringToCal(content.created_date);
        String date = getContext().getString(
                R.string.content_date,
                calendar.get(Calendar.DAY_OF_MONTH),
                getContext().getResources().getStringArray(R.array.months)[calendar.get(Calendar.MONTH)],
                calendar.get(Calendar.YEAR));
        viewHolder.tvContentCreatedDate.setText(date);

        return convertView;
    }
}
