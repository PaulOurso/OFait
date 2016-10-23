package com.devmobile.ofait.ui.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

        APIHelper.getNbContentsOfAnAccount(this.getContext(), Preference.getAccount(this.getContext()), new TaskComplete() {
            @Override
            public void run() {
                Log.d("AddContentFragment", "in request run");
                if(result.data == null){
                    Log.d("AddContentFragment", "data is null");
                    Resources res = getResources();
                    String text = String.format(res.getString(R.string.content_count),0);
                }
                else{
                    Log.d("AddContentFragment", "data is not null");
                    Resources res = getResources();
                    String text = String.format(res.getString(R.string.content_count),result.data);
                }
            }
        });


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_content, container, false);
    }

    public void createNewContent(MainActivity activity) {
        final EditText newContentText = (EditText) activity.findViewById(R.id.new_content_text);

        if(!newContentText.getText().toString().isEmpty()){
            Content newContent = new Content();
            Account account = Preference.getAccount(this.getContext());
            newContent.content_value = newContentText.getText().toString();
            newContent.created_by = account;

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
