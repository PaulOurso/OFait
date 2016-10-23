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
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.ui.adapters.ArrayAdapterContent;
import com.devmobile.ofait.ui.mainmenu.MainActivity;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private static ContentFragment contentInstance;
    public SwipeFlingAdapterView flingContainer;
    public List<Content> listContents;
    public ArrayAdapterContent arrayAdapter;
    private boolean isRefreshing = false;

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
                Log.d("SWIPER", "Empty");
                refreshData();
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

    public void refreshData() {
        if (!isRefreshing) {
            isRefreshing = true;
            Account account = Preference.getAccount(getContext());
            APIHelper.getContentsToVote(getContext(), account, new TaskComplete<Content>() {
                @Override
                public void run() {
                    isRefreshing = false;
                    Answer<Content> answer = this.result;
                    if (answer.status < 300) {
                        List<Content> listContentsD = answer.datas;
                        List<Content> listNewContents = clearDuplicateData(listContentsD);
                        Collections.shuffle(listNewContents);
                        listContents.addAll(listNewContents);
                        arrayAdapter.notifyDataSetChanged();
                    }
                    else
                        answer.message.displayMessage(getContext());
                }
            });
        }
    }

    public List<Content> clearDuplicateData(List<Content> newListD) {
        List<Content> newList = new ArrayList<>();
        boolean existing;
        for (Content newC : newListD) {
            existing = false;
            for (Content presentC : listContents) {
                if (presentC._id.equals(newC._id)) {
                    existing = true;
                    break;
                }
            }
            if (!existing)
                newList.add(newC);
        }
        return newList;
    }
}
