package edu.mcw.GeneralSurgery.UI.Topic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;

import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.models.DBhelper;
import edu.mcw.GeneralSurgery.models.Topic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopicFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";//topicId

    private int mID;

    private OnListFragmentInteractionListener mListener;

    DBhelper dBhelper;

    private FirebaseAnalytics mFirebaseAnalytics;

    public TopicFragment() {
    }

    public static TopicFragment newInstance( int id) {
        TopicFragment fragment = new TopicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dBhelper = new DBhelper(getActivity());

        if (getArguments() != null) {
           mID = getArguments().getInt(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        ArrayList<Topic> topics;

        RecyclerView rView = view.findViewById(R.id.list);
        RecyclerView navRV = view.findViewById(R.id.recyclerView);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        if (mID == -1){
            topics = dBhelper.getInitialTopics();
            ((TopicActivity) getActivity()).initMenu();//hide home/feedback buttons
        }else{
            topics = dBhelper.getTopicsBasedOnID(mID);
            ((TopicActivity) getActivity()).expandMenu();//show buttons
        }

        Collections.sort(topics, new Comparator<Topic>() {
            @Override
            public int compare(Topic topic, Topic t1) {
                return topic.getPriority() < t1.getPriority() ? -1 : topic.getPriority() == t1.getPriority() ? 0 : 1;
            }
        });


            final Context context = view.getContext();

                rView.setLayoutManager(new LinearLayoutManager(context));
                //this is a plain recyclerview so we use a linear layout.
                navRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
                //setting horizontal linear layout and reversing the list so the recyclerview scrolls to the end

            rView.setAdapter(new MyTopicRecyclerViewAdapter(topics, mListener));

            List<Topic> navTopics = new ArrayList<>();
            Topic parent ;
            while(mID > 0){//adding parent topics for the current topic recursively
                        parent = dBhelper.getTopic(dBhelper.getTopic(mID).getParentID());
                        if(parent!= null){
                            navTopics.add(parent);
                            mID = parent.getId();
                        }else{
                            mID = 0;
                        }
            }
            if(mID>-1) {
                navTopics.add(new Topic(-1, -1, getResources().getString(R.string.Home), "", "", 0));//add a constant home button in nav button aside from the root page
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Topic ID " + mID);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
            if(navTopics.size()==0) {
                navRV.setVisibility(View.GONE);//make nav bar invisible if no content
            }
            navRV.setAdapter(new NavRecyclerViewAdapter(mListener, navTopics));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        void onNavListFragmentInteraction(Topic item);

        void onListFragmentInteraction(Topic item);

        void onListFeedbackInteraction(Topic mItem);
    }
}
