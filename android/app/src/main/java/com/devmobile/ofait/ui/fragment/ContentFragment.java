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
import android.widget.ImageButton;
import android.widget.Toast;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.models.Vote;
import com.devmobile.ofait.ui.adapters.ArrayAdapterContent;
import com.devmobile.ofait.ui.mainmenu.MainActivity;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.interfaces.MenuAction;
import com.devmobile.ofait.utils.notifs.NotifInfo;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements MenuAction {

    private static ContentFragment contentInstance;
    public SwipeFlingAdapterView flingContainer;
    public List<Content> listContents;
    public ArrayAdapterContent arrayAdapter;
    private boolean isRefreshing = false;
    private Account account;
    private MainActivity mainActivity;
    private ImageButton buttonFavorite;

    public ContentFragment() {
        // Required empty public constructor
        listContents = new ArrayList<>();
    }

    public static ContentFragment getInstance(MainActivity activity) {
        if (contentInstance == null) {
            contentInstance = new ContentFragment();
            contentInstance.mainActivity = activity;
        }
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
        account = Preference.getAccount(getContext());
        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.fling_cards_contents);
        listContents.clear();
        arrayAdapter = new ArrayAdapterContent(getContext(), R.layout.item_card_content, listContents);
        arrayAdapter.current_fragment_calling = ArrayAdapterContent.FRAGMENT_CALLING.FRAGMENT_CONTENT;
        flingContainer.setAdapter(arrayAdapter);
         buttonFavorite = (ImageButton) view.findViewById(R.id.button_favorite);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                listContents.remove(0);
                arrayAdapter.notifyDataSetChanged();
                refreshImageFavorite();
            }

            @Override
            public void onLeftCardExit(Object o) {
                //Log.d("ContentFrag", "VOTE DISLIKE");
                voteDislike((Content) o);
            }

            @Override
            public void onRightCardExit(Object o) {
                //Log.d("ContentFrag", "VOTE LIKE");
                voteLike((Content) o);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                //Log.d("SWIPER", "Empty");
                refreshData();
            }

            @Override
            public void onScroll(float v) {

            }
        });

        /*flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                Vote vote = new Vote();
                int min = 0;
                int max = 1;

                Random r = new Random();
                vote.value = r.nextInt(max - min + 1) + min;
                mainActivity.displayVoteForMe(vote);
            }
        });*/
    }

    public void btnVoteLike(MainActivity activity) {
        if (listContents.size() > 0)
            flingContainer.getTopCardListener().selectRight();
    }

    public void btnVoteDislike(MainActivity activity) {
        if (listContents.size() > 0)
            flingContainer.getTopCardListener().selectLeft();
    }

    public void btnSetFavorite(MainActivity activity) {
        //Content content = listContents.get(0);
        Log.d("ContentFrag", "SET FAVORITE");
        if (listContents.size() > 0) {

            APIHelper.putOrDeleteFavorite(getContext(), listContents.get(0), account, new TaskComplete<Content>() {
                @Override
                public void run() {
                    Answer<Content> answer= this.result;
                    if(answer.status<300){
                        for (Content c : listContents) {
                            if (c._id.equals(answer.data._id)) {
                                c.isFavorite = answer.data.isFavorite;
                                arrayAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        refreshImageFavorite();
                    }
                    else{
                        answer.message.displayMessage(AddContentFragment.getInstance().getContext());
                    }
                }
            });
        }
    }

    public void voteLike(Content content) {
        Vote vote = new Vote();
        vote.account = account;
        vote.content = content;
        vote.value = 1;
        mainActivity.socketManager.actionVote(vote);
    }

    public void voteDislike(Content content) {
        Vote vote = new Vote();
        vote.account = account;
        vote.content = content;
        vote.value = 0;
        mainActivity.socketManager.actionVote(vote);
    }

    public void refreshData() {
        if (!isRefreshing) {
            isRefreshing = true;
            APIHelper.getContentsToVote(getContext(), account, new TaskComplete<Content>() {
                @Override
                public void run() {
                    isRefreshing = false;
                    Answer<Content> answer = this.result;
                    if (answer.status < 300) {
                        List<Content> listContentsD = answer.datas;
                        List<Content> listNewContents = clearDuplicateData(listContentsD);
                        if (listNewContents.size() > 0) {
                            Collections.shuffle(listNewContents);
                            listContents.addAll(listNewContents);
                            arrayAdapter.notifyDataSetChanged();
                            refreshImageFavorite();
                        }
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

    @Override
    public void refresh() {
        if (listContents.size() <= 2) {
            refreshData();
        }
    }
    public void refreshImageFavorite() {
        if (listContents.size() > 0) {
            Content content = listContents.get(0);
            if (content.isFavorite) {
                buttonFavorite.setImageResource(R.drawable.btn_favorite);
            } else {
                buttonFavorite.setImageResource(R.drawable.btn_not_favorite);
            }
        }
        else {
            buttonFavorite.setImageResource(R.drawable.btn_not_favorite);
        }
    }
}
