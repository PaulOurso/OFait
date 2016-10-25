package com.devmobile.ofait.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.ui.mainmenu.MainActivity;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.interfaces.MenuAction;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContentFragment extends Fragment implements MenuAction {


    private static AddContentFragment addContentFragment;

    public AddContentFragment() {
        // Required empty public constructor
    }

    public static AddContentFragment getInstance() {
        if (addContentFragment == null)
            addContentFragment = new AddContentFragment();
        return addContentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_content, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshData(false);
    }

    public void createNewContent(MainActivity activity) {
        closeKeyboard();
        final EditText newContentText = (EditText) activity.findViewById(R.id.new_content_text);
        final SwitchCompat notifForMyContentSwitch = (SwitchCompat) activity.findViewById(R.id.new_content_switch_notif);

        if(!newContentText.getText().toString().isEmpty()){
            Content newContent = new Content();
            Account account = Preference.getAccount(this.getContext());
            newContent.content_value = newContentText.getText().toString();
            newContent.created_by = account;
            newContent.notif = notifForMyContentSwitch.isChecked();

            APIHelper.createNewContent(this.getContext(),newContent,new TaskComplete<Content>() {
                @Override
                public void run() {
                    Answer<Content> answer = this.result;
                    newContentText.setText("");
                    Account account = Preference.getAccount(AddContentFragment.this.getContext());
                    notifForMyContentSwitch.setChecked(account.notif);
                    refreshData();
                    if (answer.status < 300) {
                        Toast.makeText(AddContentFragment.getInstance().getContext(), R.string.create_content_done, Toast.LENGTH_LONG).show();
                    }
                    else{
                        answer.message.displayMessage(AddContentFragment.getInstance().getContext());
                    }
                }
            });
        }
    }

    public void refreshData() {
        refreshData(true);
    }

    public void refreshData(boolean displayLoading) {
        Account account = Preference.getAccount(this.getContext());
        APIHelper.getAccountStats(this.getContext(), displayLoading, account, new TaskComplete<Account>() {
            @Override
            public void run() {
                Answer<Account> answer = this.result;
                TextView contentsToMake = (TextView) AddContentFragment.this.getActivity().findViewById(R.id.content_number);
                SwitchCompat notifForMyContentSwitch = (SwitchCompat) AddContentFragment.this.getActivity().findViewById(R.id.new_content_switch_notif);
                Account account = Preference.getAccount(AddContentFragment.this.getContext());
                notifForMyContentSwitch.setChecked(account.notif);

                if(answer.status < 300 ) {
                    String text = String.format(getString(R.string.content_count), answer.data.remaining_contents);
                    contentsToMake.setText(text);
                }
                else{
                    String text = "";
                    contentsToMake.setText(text);
                    answer.message.displayMessage(AddContentFragment.getInstance().getContext());
                }
            }
        });
    }

    @Override
    public void refresh() {
        refreshData();
    }

    public void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
