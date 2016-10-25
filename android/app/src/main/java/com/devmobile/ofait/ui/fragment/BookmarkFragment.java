package com.devmobile.ofait.ui.fragment;


import android.content.DialogInterface;
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
import com.devmobile.ofait.utils.FastDialog;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.interfaces.MenuAction;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment implements MenuAction {

    private static BookmarkFragment bookmarkFragment;
    private ArrayAdapterContent arrayAdapter;
    private List<Content> listContents;
    private ListView listView;
    private TextView textViewNotFavorite;


    public BookmarkFragment() {
        listContents = new ArrayList<>();
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
        textViewNotFavorite = (TextView) view.findViewById(R.id.bookmark_not_favorites);
        arrayAdapter = new ArrayAdapterContent(getContext(), R.layout.item_card_content, listContents);
        arrayAdapter.current_fragment_calling = ArrayAdapterContent.FRAGMENT_CALLING.FRAGMENT_BOOKMARK;
        arrayAdapter.fragment = BookmarkFragment.this;
        listView.setAdapter(arrayAdapter);
        loadContents(false);
    }

    public void loadContents() {
        loadContents(true);
    }
    public void loadContents(boolean displayLoading) {
        Account account = Preference.getAccount(getContext());
        APIHelper.getFavoriteContents(getContext(), displayLoading, account, new TaskComplete<Content>() {
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
        loadContents();
    }

    public void removeBookmark(final Content content) {
        FastDialog.showDialog(
                getContext(),
                FastDialog.SIMPLE_DIALOG,
                R.string.ask_remove_favorite,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Account account = Preference.getAccount(getContext());
                        APIHelper.putOrDeleteFavorite(getContext(), content, account, new TaskComplete<Content>() {
                            @Override
                            public void run() {
                                Answer<Content> answer= this.result;
                                if(answer.status<300){
                                    loadContents();
                                }
                                else{
                                    answer.message.displayMessage(AddContentFragment.getInstance().getContext());
                                }
                            }
                        });
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing
                    }
                });

    }
}
