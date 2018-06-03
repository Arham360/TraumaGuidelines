package edu.mcw.GeneralSurgery.UI.Topic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.UI.Topic.TopicFragment.OnListFragmentInteractionListener;
import edu.mcw.GeneralSurgery.models.Topic;

/**
 * Created by arham on 2/16/18.
 */

public class NavRecyclerViewAdapter extends RecyclerView.Adapter<NavRecyclerViewAdapter.ViewHolder> {
        //for more info about adapters, check out SearchAdapter
    private List<Topic> mTopics;
     final OnListFragmentInteractionListener mListener;

    public NavRecyclerViewAdapter(OnListFragmentInteractionListener listener, List<Topic> topics) {
        mTopics = topics;
        mListener = listener;
    }


    @Override
    public NavRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.horizontal_recyclerview, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mTopics.get(position);
        holder.title.setText(mTopics.get(position).getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onNavListFragmentInteraction(holder.mItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public TextView title;
        public Topic mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            title = itemView.findViewById(R.id.navTitle);

        }
    }

}
