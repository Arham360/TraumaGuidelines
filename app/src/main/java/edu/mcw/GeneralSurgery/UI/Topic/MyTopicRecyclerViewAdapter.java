package edu.mcw.GeneralSurgery.UI.Topic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.UI.Topic.TopicFragment.OnListFragmentInteractionListener;
import edu.mcw.GeneralSurgery.models.Topic;

import java.util.List;


public class MyTopicRecyclerViewAdapter extends RecyclerView.Adapter<MyTopicRecyclerViewAdapter.ViewHolder> {
    //for more info about adapters, check out SearchAdapter

    private final List<Topic> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();//this is for swipe to send feedback


    public MyTopicRecyclerViewAdapter(List<Topic> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        viewBinderHelper.setOpenOnlyOne(true);//only one swipe is enabled at a time
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getSummary());
        holder.feedbackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    mListener.onListFeedbackInteraction(holder.mItem);
                }
            }
        });
        //this is a necessary redundancy. If we put the listener on the card itself,
        // it has problems with the swipe layout as it registers most taps as swipes
        holder.mIdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Topic mItem;
        public SwipeRevealLayout swipeRevealLayout;
        public ImageView feedbackImageView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView =  view.findViewById(R.id.content);
            swipeRevealLayout =  view.findViewById(R.id.swipeTopicReveal);
            feedbackImageView =  view.findViewById(R.id.feedbackImageView);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
