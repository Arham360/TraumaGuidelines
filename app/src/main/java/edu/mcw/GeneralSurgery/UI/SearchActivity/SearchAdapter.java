package edu.mcw.GeneralSurgery.UI.SearchActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.models.Topic;

/**
 * Created by arham on 2/16/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Topic> mTopics;
    private Context mContext;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();//this is for swipe to send feedback

    public SearchAdapter(Context context, List<Topic> topics) {
        mTopics = topics;
        mContext = context;
        viewBinderHelper.setOpenOnlyOne(true);//only one swipe is enabled at a time

    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.fragment_topic, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Topic topic = mTopics.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.title;
        textView.setText(topic.getTitle());
        TextView textView2 = viewHolder.summary;
        textView2.setText(topic.getSummary());

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTopics.size();
    }

    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;
        public TextView summary;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = itemView.findViewById(R.id.id);
            summary =  itemView.findViewById(R.id.content);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(view, position);
                    }
                }
            });
            summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(view, position);
                    }
                }
            });
        }
    }

}
