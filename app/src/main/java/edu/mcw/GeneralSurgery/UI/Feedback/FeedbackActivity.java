package edu.mcw.GeneralSurgery.UI.Feedback;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import javax.inject.Inject;

import edu.mcw.GeneralSurgery.Network.APIHelper;
import edu.mcw.GeneralSurgery.Network.GenericNetworkCallback;
import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.dagger.MainApplication;
import edu.mcw.GeneralSurgery.models.DBhelper;

public class FeedbackActivity extends AppCompatActivity {


    EditText editTextOthers;
    Button submit;
    RadioGroup radioGroup;

    Intent intent;
    Context context;
    DBhelper dBhelper;

    int topicID;
    String type;

    @Inject
    APIHelper apiHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        context = this;
        dBhelper = new DBhelper(context);

        setTitle(getResources().getString(R.string.sendFeeback));

        ((MainApplication)getApplicationContext()).getAppComponent().inject(this);

        intent = getIntent();
        topicID = intent.getIntExtra(getResources().getString(R.string.ID), 0);
        type = intent.getStringExtra(getResources().getString(R.string.type));

        radioGroup = findViewById(R.id.feedbackRadioGroup);

        editTextOthers = findViewById(R.id.feedbackOthersEditText);
        submit = findViewById(R.id.feedbackSubmitButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                if (radioButton == null){
                    Toast.makeText(context, getResources().getString(R.string.selectOptionPrompt),
                            Toast.LENGTH_LONG).show();
                    return;//this is because you do not want to run the api call without verification
                }else{

                String reasonString = "";
                String descriptionString = "";

                switch (radioButton.getTag().toString()){
                    case "0":
                        reasonString = getResources().getString(R.string.Typo);
                        if(checkForDescription()){
                        descriptionString = editTextOthers.getText().toString();}
                        else{
                            return;
                        }
                        break;
                        
                    case "1":
                        reasonString = getResources().getString(R.string.Error);
                        if(checkForDescription()){
                            descriptionString = editTextOthers.getText().toString();}
                        else{
                            return;
                        }
                        break;
                        
                    case "2":
                        reasonString = getResources().getString(R.string.Suggestion);
                        if(checkForDescription()){
                            descriptionString = editTextOthers.getText().toString();}
                        else{
                            return;
                        }
                        break;

                    case "3":
                        reasonString = getResources().getString(R.string.Other);
                        if(checkForDescription()){
                            descriptionString = editTextOthers.getText().toString();}
                        else{
                            return;
                        }
                        break;
                }


               apiHelper.sendFeedback( topicID,type, reasonString,descriptionString, new GenericNetworkCallback(){

                   @Override
                   public void onSuccess(String title, String message) {
                        alert(title, message, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                   }

                   @Override
                   public void onError(String title, String message) {
                       Toast.makeText(getApplicationContext(),getResources().getString(R.string.ok) ,
                               Toast.LENGTH_LONG).show();
                   }
               });

            }
            }
        });

    }

    private boolean checkForDescription() {
        Boolean check;
        if(editTextOthers.getText().toString().isEmpty()){
            check = false;
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.addDescription) ,
                    Toast.LENGTH_LONG).show();
        }else{
            check = true;
        }
        return check;
    }


    public void alert(String title, String message, String buttonText, @Nullable DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title).setMessage(message);
        builder.setNegativeButton(buttonText, onClickListener);
        builder.create().show();
    }

    }

