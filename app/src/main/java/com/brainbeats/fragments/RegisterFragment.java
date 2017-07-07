package com.brainbeats.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.brainbeats.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mRegisterButton;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mUsername = (EditText) view.findViewById(R.id.username_edit_text);
        mPassword = (EditText) view.findViewById(R.id.password_edit_text);
        mConfirmPassword = (EditText) view.findViewById(R.id.confirm_password_edit_text);
        mRegisterButton = (Button) view.findViewById(R.id.create_user_button);

        mRegisterButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }

    public void registerUser(){
    }

    public boolean isValid(){
        return false;
    }
}
