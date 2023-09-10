package com.example.etlizmordechai.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etlizmordechai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends Fragment {

    private EditText userEmail, userPassword;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_page, container, false);
        TextView tvRegister = view.findViewById(R.id.registerNowBtn);
        Button btnLogin = view.findViewById(R.id.loginPage_loginButton);
        userEmail = view.findViewById(R.id.email_login);
        userPassword = view.findViewById(R.id.password_login);


        //After login succeed you move to products
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty())
                {
                    userEmail.setError("Please provide valid email");
                    userEmail.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    userPassword.setError("Password is required!");
                    userPassword.requestFocus();
                    return;
                }
                if(password.length() < 6)
                {
                    userPassword.setError("Min password length should be 6 chars");
                    userPassword.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //to products
                            Navigation.findNavController(view).navigate(R.id.action_loginPage_to_products);
                        }else{
                            Toast.makeText(getActivity(), "Failed to login, please check your credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        //if you pressed by any mistake to login page and you dont have account
        //you move to register page with the textview of "Register Now!"
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginPage_to_registerPage);
            }
        });
        return view;
    }
}