package edu.mcw.GeneralSurgery.UI.SearchActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.UI.Topic.TopicActivity;
import edu.mcw.GeneralSurgery.dagger.MainApplication;
import edu.mcw.GeneralSurgery.models.DBhelper;
import edu.mcw.GeneralSurgery.models.SharedPreferencesHelper;
import edu.mcw.GeneralSurgery.models.Topic;

public class SearchActivity extends AppCompatActivity {

    MaterialSearchView searchView;
    RecyclerView recyclerView;
    List<Topic> topics;
    DBhelper dBhelper;
    boolean searchFilter;
    int currentID;
    Intent intent;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ((MainApplication)getApplicationContext()).getAppComponent().inject(this);

        intent = getIntent();
        currentID = intent.getIntExtra(getResources().getString(R.string.topicID), -1);

        searchView = findViewById(R.id.search_view);

        searchFilter = sharedPreferencesHelper.getIsComprehensiveSearch();

        topics = new ArrayList<>();

        dBhelper = new DBhelper(this);

        recyclerView = findViewById(R.id.searchRecyclerView);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        searchView.setQuery(sharedPreferencesHelper.getString(getResources().getString(R.string.lastSearch)),true);

        final SearchAdapter adapter = new SearchAdapter(this, topics);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                int topicID = topics.get(position).getId();
                Intent intent = new Intent(SearchActivity.this, TopicActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(getResources().getString(R.string.searchID), topicID);
                startActivity(intent);
            }
        });



        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                topics.clear();
                String searchType;
                if (searchFilter && currentID != -1) {
                    searchType = "SubTopic Search" + dBhelper.getTopic(currentID).getTitle();
                    topics.addAll(findWithin(currentID,query));

                }else  {
                    searchType = "Comprehensive Search";
                    topics.addAll(dBhelper.getTopicsBasedOnQuerry(query));
                }
                sharedPreferencesHelper.putString(getResources().getString(R.string.lastSearch),query);
                adapter.notifyDataSetChanged();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, searchType);
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, query);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Results Size: "+ adapter.getItemCount());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                keepOpen();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchView.setSuggestions(dBhelper.getSuggestionsBasedOnText(newText));
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setQuery(sharedPreferencesHelper.getString(getResources().getString(R.string.lastSearch)),false);

            }

            @Override
            public void onSearchViewClosed() {
                    keepOpen();
            }
        });

    }

    private void keepOpen() {
        if (!searchView.isSearchOpen()){
            searchView.showSearch();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)searchView.showSearch();

    }


    private List<Topic> findWithin(int currentID, String query) {

        List<Topic> topics = new ArrayList<>();

        List<Topic> children = dBhelper.getTopicsBasedOnID(currentID);

        for (Topic topic: children){

            if(topic.getTitle().toLowerCase().contains(query.toLowerCase())
                    ||topic.getTags().toLowerCase().contains(query.toLowerCase())
                    ||topic.getSummary().toLowerCase().contains(query.toLowerCase())){
                topics.add(topic);
            }
            topics.addAll(findWithin(topic.getId(),query));

        }
            return topics;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

}
