package edu.mcw.GeneralSurgery.UI.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.mcw.GeneralSurgery.R;

public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;
    private Button loginButton;
    private TextView loginToRegister;
    private TextView loginToForgot;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment below
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        //Bind views to variables below.
        email = rootView.findViewById(R.id.loginEmail);
        password = rootView.findViewById(R.id.loginPassword);
        loginButton = rootView.findViewById(R.id.loginAuthBtn);
        loginToRegister = rootView.findViewById(R.id.loginToRegister);
        loginToForgot = rootView.findViewById(R.id.loginToForgot);
        //set touch events to these views below

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailString  = email.getText().toString().trim();
                String passwordString  = password.getText().toString();

                if(emailString.isEmpty()){
                    ((MainActivity)getActivity()).toast(getString(R.string.field_empty));
                    return;
                }

                if(passwordString.isEmpty()){
                    ((MainActivity)getActivity()).toast(getString(R.string.field_empty));
                    return;
                }

                onLoginButtonPressed(emailString, passwordString);
            }
        });

        loginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//these methods call methods on our touch listener which are hooked together by an interface.
                onLoginToRegisterButtonPressed();
            }
        });

        loginToForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginToForgotButtonPressed();
            }
        });
        
        return rootView;
    }

    public void onLoginButtonPressed(String email, String password) {//this method checks that your fragment is indeed hooked up to its parent, which in this case is the activity which hosts it.
        if (mListener != null) {
            mListener.onLoginButtonInteraction(email, password);
        }
    }

    public void onLoginToRegisterButtonPressed() {
        if (mListener != null) {
            mListener.onLoginToRegisterInteraction();
        }
    }

    public void onLoginToForgotButtonPressed() {
        if (mListener != null) {
            mListener.onLoginToForgotInteraction();
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
    public void onDetach() {//this makes sure that our listener interface is disconnected when our fragment is disconnected
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {//this interface helps the fragment communicate back to the activity and call methods there.
        void onLoginButtonInteraction(String email, String password);

        void onLoginToRegisterInteraction();

        void onLoginToForgotInteraction();
    }
}
