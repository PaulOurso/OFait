package com.devmobile.ofait.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.ui.adapters.ArrayAdapterContent;
import com.devmobile.ofait.ui.mainmenu.MainActivity;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private static ContentFragment contentInstance;
    public SwipeFlingAdapterView flingContainer;
    public List<String> listContents;
    public ArrayAdapterContent arrayAdapter;

    public ContentFragment() {
        // Required empty public constructor
        listContents = new ArrayList<>();
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.fling_cards_contents);
        listContents.clear();
        listContents.add("Test 1");
        listContents.add("Test 2");
        listContents.add("Test 3");
        listContents.add("Test 4");
        arrayAdapter = new ArrayAdapterContent(getContext(), R.layout.item_card_content, listContents);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                listContents.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(getContext(), "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(getContext(), "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                listContents.add("Test Suivant");
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(float v) {

            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void voteLike(MainActivity activity) {
        Log.d("ContentFrag", "VOTE LIKE");
        flingContainer.getTopCardListener().selectRight();
    }

    public void voteDislike(MainActivity activity) {
        Log.d("ContentFrag", "VOTE DISLIKE");
        flingContainer.getTopCardListener().selectLeft();
    }

    public void setFavorite(MainActivity activity) {
        //Content content = listContents.get(0);
        Log.d("ContentFrag", "SET FAVORITE");
    }
}
