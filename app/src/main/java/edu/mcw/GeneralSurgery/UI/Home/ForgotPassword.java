package edu.mcw.GeneralSurgery.UI.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.mcw.GeneralSurgery.R;


public class ForgotPassword extends Fragment {

//Please refer to LoginFragment.java for detailed workings of fragments.
    private Button forgotButton;
    private EditText email;

    private OnFragmentInteractionListener mListener;

    public ForgotPassword() {
        // Required empty public constructor
    }


    public static ForgotPassword newInstance(String param1, String param2) {
        ForgotPassword fragment = new ForgotPassword();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        email = rootView.findViewById(R.id.forgotEmail);
        forgotButton = rootView.findViewById(R.id.forgotAuthBtn);

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString().trim();
                onForgotButtonPressed(emailString);
            }
        });

        return rootView;
    }


    public void onForgotButtonPressed(String email) {
        if (mListener != null) {
            mListener.onForgotButtonInteraction(email);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onForgotButtonInteraction(String email);
    }
}
