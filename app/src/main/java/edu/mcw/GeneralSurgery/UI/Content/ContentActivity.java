package edu.mcw.GeneralSurgery.UI.Content;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.UI.Feedback.FeedbackActivity;
import edu.mcw.GeneralSurgery.UI.Image.ImageActivity;
import edu.mcw.GeneralSurgery.UI.SettingsPage.SettingActivity;
import edu.mcw.GeneralSurgery.UI.Topic.TopicActivity;
import edu.mcw.GeneralSurgery.Utilities.SmartFragmentStatePagerAdapter;
import edu.mcw.GeneralSurgery.models.DBhelper;
import edu.mcw.GeneralSurgery.models.Image;
import edu.mcw.GeneralSurgery.models.Option;
import edu.mcw.GeneralSurgery.models.Prompt;

public class ContentActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFeedbackFab;
    private FloatingActionButton mSearchFab;
    MaterialSearchView searchView;

    private String searchQuery = "";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    DBhelper dBhelper;
    int topicID;
    int contentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dBhelper = new DBhelper(this);

        searchView = findViewById(R.id.searchView);

        Intent intent = getIntent();
        topicID = intent.getIntExtra(getResources().getString(R.string.topicID), 0);
        contentID = dBhelper.getContentFromTopicID(topicID).get(0).getId();
        
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mTabLayout = findViewById(R.id.tabs);

        TabLayout.Tab contentTab = mTabLayout.newTab();
        contentTab.setIcon(R.drawable.ic_subject_white_24dp);
        mTabLayout.addTab(contentTab);

        if(!dBhelper.getImagesFromContentID(contentID).isEmpty()) {
            TabLayout.Tab imagesTab = mTabLayout.newTab();
            imagesTab.setIcon(R.drawable.ic_image_white_24dp);
            mTabLayout.addTab(imagesTab);
        }

        if (dBhelper.getPromptFromContentID(contentID)!=null) {
            TabLayout.Tab treeTab = mTabLayout.newTab();
            treeTab.setIcon(R.drawable.ic_tree_white_24dp);
            mTabLayout.addTab(treeTab);
        }

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mSearchFab = findViewById(R.id.search_fab);
        mFeedbackFab = findViewById(R.id.feedback_fab);

        mSearchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(searchView.isSearchOpen()){
                    searchView.closeSearch();
                }else {
                    searchView.showSearch();
                }

            }
        });
        mFeedbackFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContentActivity.this, FeedbackActivity.class);
                intent.putExtra(getResources().getString(R.string.ID), topicID);
                intent.putExtra(getResources().getString(R.string.type), getResources().getString(R.string.content));
                startActivity(intent);

            }
        });

        final MaterialSearchView searchView = (MaterialSearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    searchQuery = query;

                performSearch();
                searchView.closeSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                mSearchFab.setImageResource(R.drawable.ic_find_in_page_white_24dp);
            }
        });
        
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // If navigating to the first page, show search fab
                // If search fab is shown and not on first page, hide search fab
                if(position == 0) {

                    performSearch();
                    toggleFab(mSearchFab, true);
                } else if(mSearchFab.getVisibility() != View.GONE){
                    toggleFab(mSearchFab, false);
                }
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    public void performSearch() {
        if(mViewPager.getCurrentItem() == 0) {//allow search only if on first tab
            PlaceholderFragment fragment = (PlaceholderFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
            View rootView = fragment.getRootView();
            WebView webView = rootView.findViewById(R.id.contentWebView);
            webView.findAllAsync(searchQuery);//searchQuery might be coming in as intent so having global access is advised
        }
    }

    public void toggleFab(final FloatingActionButton fab, boolean show) {
        Animation fabAnimation;
        if(show) {
            fab.setVisibility(View.VISIBLE);
            fabAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        } else {
            fabAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
            fabAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    fab.setVisibility(View.GONE);
                }
            });
        }
        fab.startAnimation(fabAnimation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent intent = new Intent(ContentActivity.this, SettingActivity.class);
            startActivity(intent);

        }

        if(id == R.id.action_home){
            topicID = dBhelper.getTopic(topicID).getParentID();
            Intent intent = new Intent(ContentActivity.this, TopicActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(getResources().getString(R.string.topicID), -1);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    public static class PlaceholderFragment extends Fragment implements OnListInteractionListener{

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_CONTENT_ID = "contentID";
        private View rootView;

        public PlaceholderFragment() {
        }

        public View getRootView() {
            return this.rootView;
        }

        public static PlaceholderFragment newInstance(int sectionNumber, int contentID) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_CONTENT_ID, contentID);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            DBhelper dBhelper = new DBhelper(getContext());
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (sectionNumber) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_content_web_view, container, false);
                    WebView webView = rootView.findViewById(R.id.contentWebView);
                    webView.loadData(dBhelper.getContent(getArguments().getInt(ARG_CONTENT_ID)).getDescription(),  "text/html; charset=UTF-8", null);
                    break;
                case 2:
                    if(!dBhelper.getImagesFromContentID(getArguments().getInt(ARG_CONTENT_ID)).isEmpty()){
                    loadImagesFragment(dBhelper, container,inflater);
                    } else if (dBhelper.getPromptFromContentID(getArguments().getInt(ARG_CONTENT_ID))!=null){
                     loadTreeFragment(dBhelper, container,inflater);
                    }
                    break;
                case 3:
                    loadTreeFragment(dBhelper, container,inflater);
                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    TextView textView = rootView.findViewById(R.id.section_label);
                    textView.setText(getString(R.string.section_format, sectionNumber));
                    break;
            }
            return rootView;
        }

        private void loadTreeFragment(DBhelper dBhelper, ViewGroup container,LayoutInflater inflater) {
            Prompt prompt = dBhelper.getPromptFromContentID(getArguments().getInt(ARG_CONTENT_ID));
            if(prompt == null){
                return;
            }
            rootView = inflater.inflate(R.layout.fragment_replacement, container, false);
            PromptFragment promptFragment = PromptFragment.newInstance(getArguments().getInt(ARG_CONTENT_ID),1);

            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.home, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(R.id.replacementLayout,promptFragment).commit();


        }

        private void loadImagesFragment( DBhelper dBhelper, ViewGroup container,LayoutInflater inflater) {
            final ArrayList<Image> imageObjects = dBhelper.getImagesFromContentID(getArguments().getInt(ARG_CONTENT_ID));
            Collections.sort(imageObjects, new Comparator<Image>() {
                @Override
                public int compare(Image image, Image t1) {
                    return image.getPriority() < t1.getPriority() ? -1 : image.getPriority() == t1.getPriority() ? 0 : 1;

                }
            });
            rootView = inflater.inflate(R.layout.fragment_images, container, false);
            RecyclerView imageRecycler = rootView.findViewById(R.id.image_recycler);
            imageRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            imageRecycler.setAdapter(new ImageRecyclerAdapter(imageObjects, getContext(), new OnImageInteractionListener() {
                @Override
                public void onImageFragmentInteraction(Image image) {

                    Intent intent = new Intent(getActivity(), ImageActivity.class);
                    intent.putExtra(getResources().getString(R.string.ID), image.getId());
                    intent.putExtra(getResources().getString(R.string.url), image.getUrl());
                    startActivity(intent);

                }

                @Override
                public void onImageFeedbackFragmentInteraction(Image image) {
                    Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                    intent.putExtra(getResources().getString(R.string.ID), image.getId());
                    intent.putExtra(getResources().getString(R.string.type), getResources().getString(R.string.image));
                    startActivity(intent);
                }
            }));
        }

        @Override
        public void onAttachFragment(Fragment childFragment) {
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onRestartFragmentInteraction() {
            if(getChildFragmentManager().getBackStackEntryCount()!=0){
                for (int i = 0 ; i < getChildFragmentManager().getBackStackEntryCount();i++){
                    getChildFragmentManager().popBackStack();
                }
            }

        }

        @Override
        public void onListFragmentInteraction(Option option) {

            FragmentManager fragmentManager = getChildFragmentManager();FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            PromptFragment fragment = PromptFragment.newInstance(option.getTargetID(),2);
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.replace(R.id.replacementLayout,fragment).addToBackStack(null).commit();

        }

        @Override
        public void onPrevFragmentInteraction() {
            if(getChildFragmentManager().getBackStackEntryCount()!=0){
                getChildFragmentManager().popBackStack();
            }

        }

        @Override
        public void onListFeedbackFragmentInteraction(Option option) {
            Intent intent = new Intent(getActivity(), FeedbackActivity.class);
            intent.putExtra(getResources().getString(R.string.ID), option.getId());
            intent.putExtra(getResources().getString(R.string.type), getResources().getString(R.string.option));
            startActivity(intent);
        }


    }

    @Override
    public void onBackPressed() {

        topicID = dBhelper.getTopic(topicID).getParentID();
        Intent intent = new Intent(ContentActivity.this, TopicActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(getResources().getString(R.string.topicID), topicID);
        startActivity(intent);
    }

    public class SectionsPagerAdapter extends SmartFragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1, contentID);
        }

        @Override
        public int getCount() {
            return mTabLayout.getTabCount();
        }

    }

    public static class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageViewHolder> {

        private ArrayList<Image> images;
        private Context context;
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
        private final OnImageInteractionListener mListener;

        ImageRecyclerAdapter(ArrayList<Image> images, Context context, OnImageInteractionListener listener) { // would be image objects
            this.images = images;
            this.context = context;
            viewBinderHelper.setOpenOnlyOne(true);
            this.mListener = listener;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_image_card, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ImageViewHolder holder, int position) {
            final Image image = images.get(position);

            holder.image = image;
            holder.label.setText(image.getTitle());
            viewBinderHelper.bind(holder.swipeRevealLayout, images.get(position).getTitle());

            try {
                Picasso.with(context).load(images.get(position).getUrl())
                        .into(holder.imageView);
            }catch (Exception e){

            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.onImageFragmentInteraction(holder.image);
                    }
                }
            });

            holder.feedbackImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.onImageFeedbackFragmentInteraction(holder.image);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return images.size();
        }
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView label;
        public ImageView imageView;
        public Image image;
        public ImageView feedbackImg;
        public SwipeRevealLayout swipeRevealLayout;


        public ImageViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            label = itemView.findViewById(R.id.image_label);
            imageView = itemView.findViewById(R.id.image_view);
            swipeRevealLayout = itemView.findViewById(R.id.imageSwipeLayout);
            feedbackImg = itemView.findViewById(R.id.feedbackImgView);
        }
    }

    public static class OptionRecylcerAdapter extends RecyclerView.Adapter<OptionViewHolder> {
        private ArrayList<Option> options;
        private final OnListInteractionListener mListener;
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        OptionRecylcerAdapter(ArrayList<Option> options, OnListInteractionListener listener) {
            this.options = options;
            this.mListener = listener;
            viewBinderHelper.setOpenOnlyOne(true);
        }

        @Override
        public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_option_card, parent, false);
            return new OptionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final OptionViewHolder holder, int position) {

            holder.option = options.get(position); // would be option object
            holder.label.setText(options.get(position).getPrompt()); // would be option prompt
            viewBinderHelper.bind(holder.swipeRevealLayout, options.get(position).getPrompt());

            holder.label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(holder.option);
                    }
                }
            });
            holder.feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                      mListener.onListFeedbackFragmentInteraction(holder.option);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return options.size();
        }
    }
    public static class OptionViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView label;
        public Option option;
        public SwipeRevealLayout swipeRevealLayout;
        public TextView feedback;

        public OptionViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            label = itemView.findViewById(R.id.option_label);
            swipeRevealLayout= itemView.findViewById(R.id.swipeReveal);
            feedback = itemView.findViewById(R.id.optionFeedbackTV);
        }
    }
}
