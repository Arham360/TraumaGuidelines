package edu.mcw.GeneralSurgery.UI.Content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.UI.Feedback.FeedbackActivity;
import edu.mcw.GeneralSurgery.models.DBhelper;
import edu.mcw.GeneralSurgery.models.Option;
import edu.mcw.GeneralSurgery.models.Prompt;

import java.util.ArrayList;


public class PromptFragment extends Fragment {

    //for more info about fragments, refer to loginFragment.java
    private static final String ARG_PARAM1 = "param1";//contentID
    private static final String ARG_PARAM2 = "param2";//ifInitial

    private int mParam1;
    private int mParam2;

    private OnListInteractionListener mListener;

    Button prevButton;
    Button restartButton;
    TextView promptTV;
    RecyclerView recyclerView;
    ImageButton feedbackImageButton;

    DBhelper dBhelper;
    Prompt prompt;


    public PromptFragment() {
    }

    public static PromptFragment newInstance( int param1, int initial) {
        PromptFragment fragment = new PromptFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, initial);
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree, container, false);

        dBhelper = new DBhelper(getContext());

        prevButton = view.findViewById(R.id.prevButton);
        restartButton = view.findViewById(R.id.restartButton);

        if(mParam2==1){
            prompt = dBhelper.getPromptFromContentID(mParam1);
            prevButton.setVisibility(View.INVISIBLE);
            restartButton.setVisibility(View.INVISIBLE);
        }else{
            prompt = dBhelper.getPromptFromOptionID(mParam1);
        }

        ArrayList<Option> options = dBhelper.getOptionsFromPrompt(prompt.getId());

        promptTV = view.findViewById(R.id.prompt_label);
        feedbackImageButton = view.findViewById(R.id.promptFeedbackButton);
        recyclerView = view.findViewById(R.id.option_recycler);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRestartButtonPressed();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPrevButtonPressed();
            }
        });

        promptTV.setText(prompt.getPrompt());

        feedbackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                intent.putExtra(getResources().getString(R.string.topicID), prompt.getId());
                intent.putExtra(getResources().getString(R.string.type), getResources().getString(R.string.prompt));
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ContentActivity.OptionRecylcerAdapter(options, mListener));

        return view;
    }

    public void onRestartButtonPressed() {
        if (mListener != null) {
            mListener.onRestartFragmentInteraction();
        }
    }
    public void onPrevButtonPressed() {
        if (mListener != null) {
            mListener.onPrevFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment fragment = getParentFragment();//this is crucial! fragments attach onto activities via this method
        //but since we have a fragment within a fragment, this needs to latch onto the parent fragment.

        if (fragment instanceof OnListInteractionListener) {
            mListener = (OnListInteractionListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnListInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
