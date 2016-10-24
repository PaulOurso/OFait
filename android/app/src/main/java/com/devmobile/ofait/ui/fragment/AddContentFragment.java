package com.devmobile.ofait.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.ui.mainmenu.MainActivity;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContentFragment extends Fragment {


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

        Switch notifForMyContentSwitch = (Switch) view.findViewById(R.id.new_content_switch_notif);
        Account account = Preference.getAccount(this.getContext());
        notifForMyContentSwitch.setChecked(account.notif);

        APIHelper.getAccountStats(this.getContext(), account, new TaskComplete<Account>() {
            @Override
            public void run() {
                Answer<Account> answer = this.result;
                TextView contentsToMake = (TextView) view.findViewById(R.id.content_number);

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

    public void createNewContent(MainActivity activity) {
        final EditText newContentText = (EditText) activity.findViewById(R.id.new_content_text);
        Switch notifForMyContentSwitch = (Switch) activity.findViewById(R.id.new_content_switch_notif);

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
                    if (answer.status < 300) {
                        newContentText.setText("");
                        Toast.makeText(AddContentFragment.getInstance().getContext(), R.string.create_content_done, Toast.LENGTH_LONG).show();
                    }
                    else{
                        newContentText.setText("");
                        answer.message.displayMessage(AddContentFragment.getInstance().getContext());
                    }
                }
            });
        }
    }

}
