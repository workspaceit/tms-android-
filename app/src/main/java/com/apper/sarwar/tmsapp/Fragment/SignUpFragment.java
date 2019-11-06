package com.apper.sarwar.tmsapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.apper.sarwar.tmsapp.R;
import com.apper.sarwar.tmsapp.apiclients.auth.AuthClient;
import com.apper.sarwar.tmsapp.apiclients.auth.AuthRequestListener;
import com.apper.sarwar.tmsapp.entity.AuthUser;
import com.apper.sarwar.tmsapp.viewmodel.SignUpForm;
import com.apper.sarwar.tmsapp.viewmodel.UserForm;

import org.json.JSONObject;

public class SignUpFragment extends Fragment {

    public static final String TITLE = "Sign Up";
    EditText sign_up_first_name,
            sign_up_last_name,
            sign_up_user_name,
            sign_up_email,
            sign_up_dob,
            sign_up_phone,
            sign_up_password,
            sign_up_confirm_password;

    String userNameBag, emailBag,passwordBag ,confirmPasswordBag ,firstName ,lastName ,dob ,phone_number;
    Button sign_button;
    private int mYear, mMonth, mDay;
    AuthClient authClient;



    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentSignUpView = inflater.inflate(R.layout.fragment_signup, container, false);
        authClient = new AuthClient(this.getActivity());


        sign_up_user_name = (EditText) fragmentSignUpView.findViewById(R.id.sign_up_user_name);
        sign_up_email = (EditText) fragmentSignUpView.findViewById(R.id.sign_up_email);
        sign_up_password = (EditText) fragmentSignUpView.findViewById(R.id.sign_up_password);
        sign_up_confirm_password = (EditText) fragmentSignUpView.findViewById(R.id.sign_up_confirm_password);
        sign_up_first_name = (EditText) fragmentSignUpView.findViewById(R.id.sign_up_first_name);
        sign_up_last_name = (EditText) fragmentSignUpView.findViewById(R.id.sign_up_last_name);
        sign_up_dob = (EditText) fragmentSignUpView.findViewById(R.id.sign_up_dob);
        sign_up_phone = (EditText) fragmentSignUpView.findViewById(R.id.sign_up_phone);


        sign_button = (Button) fragmentSignUpView.findViewById(R.id.sign_up_button);

        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = sign_up_first_name.getText().toString().trim();
                String lastName = sign_up_last_name.getText().toString().trim();

                String userNameBag = sign_up_user_name.getText().toString().trim();
                String emailBag = sign_up_email.getText().toString().trim();

                String passwordBag = sign_up_password.getText().toString().trim();
                String confirmPasswordBag = sign_up_confirm_password.getText().toString().trim();

                String dob = sign_up_dob.getText().toString().trim();
                String phone_number = sign_up_phone.getText().toString().trim();

                UserForm userForm = new UserForm(firstName, lastName, userNameBag, emailBag, passwordBag, confirmPasswordBag);
                SignUpForm SignUpForm = new SignUpForm(dob, phone_number, userForm);

                if (isValidForm(firstName, lastName,userNameBag,emailBag,passwordBag,confirmPasswordBag)) {
                    authClient.signUp(SignUpForm);
                }
            }
        });


        return fragmentSignUpView;

    }
    public boolean isValidForm(String firstName, String lastName,String userName,String email,String password,String confirmPassword) {
        boolean validFirstName = !firstName.isEmpty();

        if (!validFirstName) {
            sign_up_first_name.setError("Please enter first name");
        }

        boolean validLastName= !lastName.isEmpty();
        if (!validLastName) {
            sign_up_last_name.setError("Please enter valid last name");
        }

        boolean validUserName= !userName.isEmpty();
        if (!validUserName) {
            sign_up_user_name.setError("Please enter valid user name");
        }

        boolean validEmail= !email.isEmpty();
        if (!validEmail) {
            sign_up_email.setError("Please enter valid email");
        }

        boolean validPassword= !password.isEmpty();
        if (!validPassword) {
            sign_up_password.setError("Please enter valid password");
        }

        /*boolean validConfirmPassword= !password.equals(confirmPassword);
        if (!validConfirmPassword) {
            sign_up_password.setError("Confirm password does not match");
        }*/

        return validFirstName && validLastName && validUserName && validEmail && validPassword;
    }

}
