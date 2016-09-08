package org.iflab.ibistubydreamfactory.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.iflab.ibistubydreamfactory.R;

public class PostLostFoundFragment extends Fragment {
    private Button postButton;
    private View rootView;


    public PostLostFoundFragment() {
    }

    public static PostLostFoundFragment newInstance() {
        return new PostLostFoundFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_post_lost_found, container, false);
        initView();

        return rootView;
    }

    private void initView() {
        postButton = (Button) rootView.findViewById(R.id.button_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
