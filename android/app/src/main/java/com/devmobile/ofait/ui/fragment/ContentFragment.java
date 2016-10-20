package com.devmobile.ofait.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmobile.ofait.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private static ContentFragment contentInstance;

    public ContentFragment() {
        // Required empty public constructor
    }

    public static ContentFragment getInstance() {
        if (contentInstance == null)
            contentInstance = new ContentFragment();
        return contentInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

}
