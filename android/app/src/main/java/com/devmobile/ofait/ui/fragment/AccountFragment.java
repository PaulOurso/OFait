package com.devmobile.ofait.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private static AccountFragment accountFragment;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment getInstance() {
        if (accountFragment == null)
            accountFragment = new AccountFragment();
        return accountFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        APIHelper.getAccountStats(this.getContext(), Preference.getAccount(this.getContext()), new TaskComplete<Account>() {
            @Override
            public void run() {
                Answer<Account> answer = this.result;
                TextView accountName = (TextView) view.findViewById(R.id.account_name);
                TextView accountReputationValue = (TextView) view.findViewById(R.id.account_reputation_value);
                TextView accountContentAvailable = (TextView) view.findViewById(R.id.account_content_available);

                String textPseudo = String.format(getString(R.string.dynamical_string), Preference.getAccount(AccountFragment.getInstance().getContext()).pseudo);
                accountName.setText(textPseudo);

                if(answer.status < 300 ) {

                    String textReputation= String.format(getString(R.string.dynamical_integer), answer.data.reputation);
                    accountReputationValue.setText(textReputation);

                    String textRemainingContents= String.format(getString(R.string.dynamical_integer), answer.data.remaining_contents);
                    accountContentAvailable.setText(textRemainingContents);
                }
                else{
                    String text = "";
                    accountReputationValue.setText(text);
                    accountContentAvailable.setText(text);

                    answer.message.displayMessage(AccountFragment.getInstance().getContext());
                }
            }
        });
    }
}
