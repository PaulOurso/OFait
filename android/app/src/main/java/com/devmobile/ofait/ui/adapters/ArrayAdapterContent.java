package com.devmobile.ofait.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.ui.fragment.BookmarkFragment;
import com.devmobile.ofait.utils.FormatHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Tony on 20/10/2016.
 */

public class ArrayAdapterContent extends ArrayAdapter<Content> {

    public LayoutInflater layoutInflater;
    public int resId;
    public enum FRAGMENT_CALLING{FRAGMENT_CONTENT, FRAGMENT_BOOKMARK, FRAGMENT_HISTORY};
    public FRAGMENT_CALLING current_fragment_calling;
    public Fragment fragment;

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
        public LinearLayout rlRemoveButton;
        public LinearLayout tvHot;
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
            viewHolder.rlRemoveButton = (LinearLayout) convertView.findViewById(R.id.remove_button);
            viewHolder.tvHot = (LinearLayout) convertView.findViewById(R.id.content_is_hot);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ContentViewHolder) convertView.getTag();

        final Content content = getItem(position);
        viewHolder.tvContentValue.setText(content.content_value);
        viewHolder.tvContentCreatedBy.setText(getContext().getString(R.string.content_created_by, content.created_by.pseudo));
        viewHolder.tvContentPoints.setText(getContext().getString(R.string.content_points, content.nb_points));

        Calendar calendar = FormatHelper.formatStringToCal(content.created_date);
        String date = getContext().getString(
                R.string.content_date,
                calendar.get(Calendar.DAY_OF_MONTH),
                getContext().getResources().getStringArray(R.array.months)[calendar.get(Calendar.MONTH)],
                calendar.get(Calendar.YEAR));
        viewHolder.tvContentCreatedDate.setText(date);

        if (current_fragment_calling == FRAGMENT_CALLING.FRAGMENT_HISTORY) {
            if (content.isHot)
                viewHolder.tvHot.setVisibility(View.VISIBLE);
            else
                viewHolder.tvHot.setVisibility(View.GONE);
        }
        else
            viewHolder.tvHot.setVisibility(View.GONE);

        if (current_fragment_calling == FRAGMENT_CALLING.FRAGMENT_BOOKMARK) {
            viewHolder.rlRemoveButton.setVisibility(View.VISIBLE);
            viewHolder.rlRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment != null) {
                        ((BookmarkFragment)fragment).removeBookmark(content);
                    }
                }
            });
        }
        else {
            viewHolder.rlRemoveButton.setVisibility(View.GONE);
        }

        return convertView;
    }
}
