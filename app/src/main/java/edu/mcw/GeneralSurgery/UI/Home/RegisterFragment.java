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


public class RegisterFragment extends Fragment {

    //Please refer to LoginFragment.java for detailed workings of fragments.

    private EditText emailEditText;
    private Button registerButton;
    private EditText nameEditText;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        emailEditText= rootView.findViewById(R.id.registerEmail);
        nameEditText = rootView.findViewById(R.id.registerName);
        registerButton = rootView.findViewById(R.id.registerAuthBtn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailString = emailEditText.getText().toString().trim();
                String nameString = nameEditText.getText().toString().trim();
                if(emailString.isEmpty() && nameString.isEmpty()){
                    ((MainActivity)getActivity()).toast(getString(R.string.field_empty));
                    return;
                }

                onRegisterButtonPressed(emailString, nameString);

            }
        });

        return rootView;
    }


    public void onRegisterButtonPressed(String email, String name) {
        if (mListener != null) {
            mListener.onRegisterButtonInteraction(email, name);
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

        void onRegisterButtonInteraction(String email, String name);

    }
}
