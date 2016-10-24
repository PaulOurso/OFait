package com.devmobile.ofait.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.interfaces.MenuAction;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;

import java.text.DecimalFormatSymbols;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements MenuAction {

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
        refreshData();
    }

    public void refreshData() {
        APIHelper.getAccountStats(this.getContext(), Preference.getAccount(this.getContext()), new TaskComplete<Account>() {
            @Override
            public void run() {
                Answer<Account> answer = this.result;
                Account account = answer.data;
                TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);

                String textPseudo = String.format(getString(R.string.dynamical_string), Preference.getAccount(AccountFragment.getInstance().getContext()).pseudo);
                accountName.setText(textPseudo);

                //reputations
                TextView accountReputationValue = (TextView) getActivity().findViewById(R.id.account_reputation_value);
                TextView accountPreviousReputation= (TextView) getActivity().findViewById(R.id.account_previous_reputation);
                TextView accountNextReputation= (TextView) getActivity().findViewById(R.id.account_next_reputation);

                //content available
                TextView accountContentAvailable = (TextView) getActivity().findViewById(R.id.account_content_available);

                //votes
                TextView accountCurrentVotes= (TextView) getActivity().findViewById(R.id.account_current_votes);
                TextView accountVotesLevel= (TextView) getActivity().findViewById(R.id.account_votes_next_content);

                ProgressBar progressReputation = (ProgressBar) getActivity().findViewById(R.id.progressBar_reputation);
                ProgressBar progressVotes= (ProgressBar) getActivity().findViewById(R.id.progressBar_votes);

                if(answer.status < 300 ) {

                    String textReputation= String.format(getString(R.string.dynamical_integer), account.reputation);
                    accountReputationValue.setText(textReputation);
                    String textPreviousReputation= String.format(getString(R.string.dynamical_integer), account.previous_reputation);
                    accountPreviousReputation.setText(textPreviousReputation);
                    String textNextReputation= String.format(getString(R.string.dynamical_integer), account.next_lvl_reputation);
                    accountNextReputation.setText(textNextReputation);

                    String textRemainingContents= String.format(getString(R.string.account_content_available_text), account.remaining_contents);
                    accountContentAvailable.setText(textRemainingContents);

                    String textCurrentVotes= String.format(getString(R.string.dynamical_integer), account.votes_unused%account.votes_by_content);
                    accountCurrentVotes.setText(textCurrentVotes);
                    String textVotesLevel= String.format(getString(R.string.dynamical_integer), account.votes_by_content);
                    accountVotesLevel.setText(textVotesLevel);

                    progressReputation.setMax(account.next_lvl_reputation-account.previous_reputation);
                    if(account.next_lvl_reputation == -1){
                        accountNextReputation.setText(DecimalFormatSymbols.getInstance().getInfinity());
                        progressReputation.setMax(1);
                        progressReputation.setProgress(1);
                    }
                    else{
                        progressReputation.setProgress(account.reputation-account.previous_reputation);
                    }

                    progressVotes.setMax(account.votes_by_content);
                    progressVotes.setProgress(account.votes_unused%account.votes_by_content);

                }
                else{
                    accountReputationValue.setText("");
                    accountContentAvailable.setText(getString(R.string.account_content_votes_left_text));
                    accountPreviousReputation.setText("");
                    accountNextReputation.setText("");
                    accountCurrentVotes.setText("");
                    accountVotesLevel.setText("");
                    answer.message.displayMessage(AccountFragment.getInstance().getContext());
                }
            }
        });
    }

    @Override
    public void refresh() {
        refreshData();
    }
}
