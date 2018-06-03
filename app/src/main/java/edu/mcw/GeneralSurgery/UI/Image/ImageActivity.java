package edu.mcw.GeneralSurgery.UI.Image;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.UI.Feedback.FeedbackActivity;
import edu.mcw.GeneralSurgery.models.DBhelper;
import edu.mcw.GeneralSurgery.models.Image;

public class ImageActivity extends AppCompatActivity {

    Intent intent;
    Image image;
    DBhelper dBhelper;
    ImageView imageView;
    FloatingActionButton floatingActionButton;
    int imageID;
    String url;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_expand);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        intent = getIntent();

        dBhelper = new DBhelper(this);
        imageView = findViewById(R.id.imageExpandView);
        floatingActionButton = findViewById(R.id.imageExpandFAB);
        imageID = intent.getIntExtra(getResources().getString(R.string.ID), -1) ;
        image = dBhelper.getImage(imageID);
        url = intent.getStringExtra(getResources().getString(R.string.url));

        try {
            Picasso.with(this).load(url)
                    .error(R.drawable.ic_close_white_24dp)
                    .into(imageView);
        }catch (Exception e){
            Toast.makeText(this, getResources().getString(R.string.network_error),
                    Toast.LENGTH_LONG).show();
        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Image " + imageID);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, image.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageActivity.this, FeedbackActivity.class);
                intent.putExtra(getResources().getString(R.string.ID), image.getId());
                intent.putExtra(getResources().getString(R.string.type), getResources().getString(R.string.image));
                startActivity(intent);
            }
        });
    }

}
