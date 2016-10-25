package com.devmobile.ofait.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.ui.adapters.ArrayAdapterContent;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.interfaces.MenuAction;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements MenuAction {

    private static HistoryFragment historyFragment;
    private ArrayAdapterContent arrayAdapter;
    private List<Content> listContents;
    private ListView listView;
    private TextView textViewNotFavorite;


    public HistoryFragment() {
        listContents = new ArrayList<>();
    }

    public static HistoryFragment getInstance() {
        if (historyFragment == null)
            historyFragment = new HistoryFragment();
        return historyFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listContents.clear();
        listView = (ListView) view.findViewById(R.id.history_listView);
        textViewNotFavorite = (TextView) view.findViewById(R.id.history_not_favorites);
        arrayAdapter = new ArrayAdapterContent(getContext(), R.layout.item_card_content, listContents);
        arrayAdapter.current_fragment_calling = ArrayAdapterContent.FRAGMENT_CALLING.FRAGMENT_HISTORY;
        arrayAdapter.fragment = HistoryFragment.this;
        listView.setAdapter(arrayAdapter);
        loadDatas();
    }

    public void loadDatas() {
        Account account = Preference.getAccount(getContext());
        APIHelper.getHistoryContents(getContext(), account, new TaskComplete<Content>() {
            @Override
            public void run() {

                Answer<Content> answer = this.result;
                if (answer.status < 300) {
                    List<Content> listFavoriteContents = answer.datas;
                    if (listFavoriteContents.size() > 0) {
                        listContents.clear();
                        textViewNotFavorite.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        listContents.addAll(listFavoriteContents);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                else
                    answer.message.displayMessage(getContext());
            }
        });
    }


    @Override
    public void refresh() {
        loadDatas();
    }
}
