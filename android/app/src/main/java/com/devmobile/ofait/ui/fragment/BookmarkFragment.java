package com.devmobile.ofait.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.ui.adapters.ArrayAdapterContent;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment {

    private static BookmarkFragment bookmarkFragment;
    private ArrayAdapterContent arrayAdapter;
    private List<Content> listContents;
    private ListView listView;
    private Account account;


    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment getInstance() {
        if (bookmarkFragment == null)
            bookmarkFragment = new BookmarkFragment();
        return bookmarkFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listContents.clear();
        listView = (ListView) view.findViewById(R.id.bookmark_listView);
        arrayAdapter = new ArrayAdapterContent(getContext(), R.layout.item_card_content, listContents);
        arrayAdapter.current_fragment_calling = ArrayAdapterContent.FRAGMENT_CALLING.FRAGMENT_CONTENT;
        listView.setAdapter(arrayAdapter);
        loadContents();
    }

    public void loadContents() {

            APIHelper.getFavoriteContents(getContext(), account, new TaskComplete<Content>() {
                @Override
                public void run() {

                    Answer<Content> answer = this.result;
                    if (answer.status < 300) {
                        List<Content> listFavoriteContents = answer.datas;
                        if (listFavoriteContents.size() > 0) {
                            listContents.addAll(listFavoriteContents);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                        answer.message.displayMessage(getContext());
                }
            });
    }
}
