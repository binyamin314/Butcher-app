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
import android.widget.Toast;

import com.example.etlizmordechai.R;
import com.example.etlizmordechai.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterPage extends Fragment {

    private FirebaseAuth mAuth;
    private EditText userID, userFullname, userEmail, userPhone, userPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_page, container, false);
        Button btnReg = view.findViewById(R.id.registerPage_registerButton);
        userID = view.findViewById(R.id.ID_register);
        userFullname = view.findViewById(R.id.fullname_register);
        userEmail = view.findViewById(R.id.email_register);
        userPhone = view.findViewById(R.id.phone_register);
        userPassword = view.findViewById(R.id.password_register);


        //After register succeed, you need to login!
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userID.getText().toString().trim();
                String fullname = userFullname.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                String phone = userPhone.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if(phone.isEmpty())
                {
                    userPhone.setError("Phone number is required!");
                    userPhone.requestFocus();
                    return;
                }
                if(id.isEmpty())
                {
                    userID.setError("ID is required!");
                    userID.requestFocus();
                    return;
                }
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

                mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            User user = new User(fullname, email, id, phone);
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(getActivity(), "Registered successfully!", Toast.LENGTH_LONG).show();
                                                                Navigation.findNavController(view).navigate(R.id.action_registerPage_to_loginPage);
                                                            }else{
                                                                Toast.makeText(getActivity(), "Failed to register! try again!", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                        }else{
                                            Toast.makeText(getActivity(), "Failed to register! try again!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

            }
        });
    return view;
    }
}