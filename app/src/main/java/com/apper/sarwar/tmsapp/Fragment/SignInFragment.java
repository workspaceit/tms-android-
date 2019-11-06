package com.apper.sarwar.tmsapp.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.apper.sarwar.tmsapp.R;
import com.apper.sarwar.tmsapp.apiclients.auth.AuthClient;
import com.apper.sarwar.tmsapp.apiclients.auth.AuthRequestListener;
import com.apper.sarwar.tmsapp.entity.AuthUser;
import com.apper.sarwar.tmsapp.utils.LoadingMessageUtil;
import com.apper.sarwar.tmsapp.utils.SharedPreferenceUtil;
import com.apper.sarwar.tmsapp.viewmodel.SignUpForm;
import com.apper.sarwar.tmsapp.viewmodel.UserForm;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SignInFragment extends Fragment implements AuthRequestListener {

    public static final String TITLE = "Sign In";
    public static final String LOGIN_DATA = "";
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String TAG = "Google Login";
    private static final int RC_SIGN_IN = 9001;
    private static final String login_type = "app";

    Button signInButton;
    LoginButton facebookButton;
    SignInButton googleButton;
    EditText userNameText;
    EditText userPasswordText;
    AuthClient authClient;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    ImageView imageView;
    GoogleSignInClient mGoogleSignInClient;


    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container, Bundle savedInstanceState) {


        View fragmentSignInView = inflater.inflate(R.layout.fragment_signin, container, false);
        authClient = new AuthClient(this.getActivity());

        signInButton = fragmentSignInView.findViewById(R.id.button_sign_in);
        userNameText = (EditText) fragmentSignInView.findViewById(R.id.sign_in_user_name);
        userPasswordText = (EditText) fragmentSignInView.findViewById(R.id.sign_in_password);


        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userName = userNameText.getText().toString().trim();
                String userPassword = userPasswordText.getText().toString().trim();
                signIn(login_type,userName,userPassword);
            }

        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        googleButton = fragmentSignInView.findViewById(R.id.google_sign_in_button);
        googleButton.setSize(SignInButton.SIZE_STANDARD);

        try {
            googleButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    googleSignIn();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            facebookButton = fragmentSignInView.findViewById(R.id.login_button_facebook);
            facebookButton.setReadPermissions(Arrays.asList(EMAIL));
            // If you are using in a fragment, call loginButton.setFragment(this);
            facebookButton.setFragment(this);
            // Callback registration
            facebookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            System.out.println(loginResult.getAccessToken());
                            AccessToken getUserProfile = AccessToken.getCurrentAccessToken();
                            getUserProfile(getUserProfile);
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            System.out.println(exception.toString());
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragmentSignInView;
    }



    private void signIn(String login_type,String userName,String userPassword) {


        if(login_type=="app"){
            if (isValidForm(userName, userPassword)) {
                LoadingMessageUtil.startLoadingDialog(getActivity(),"Authenticating...");
                authClient.login(userName, userPassword);
            }
        }else{
            LoadingMessageUtil.startLoadingDialog(getActivity(),"Authenticating...");
            authClient.login(userName, userPassword);
        }

    }

    private void googleSignIn() {
        try {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Get all success data after facebook sign-in
     *
     * @param currentAccessToken
     */
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";


                            String[] personUserName = email.split("@");
                            String userName=personUserName[0];
                            String personPassword = "123456";

                            String personDob = "1989-09-03";
                            String personPhone = "01713523713";

                            UserForm userForm = new UserForm(first_name, last_name, userName, email, personPassword, personPassword);
                            SignUpForm SignUpForm = new SignUpForm(personDob, personPhone, userForm);

                            authClient.signUp(SignUpForm);

                            signIn("facebook",userName,personPassword);


                            /**
                             * Facebook Log out
                             */
                            boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
                            if (!loggedOut) {
                                LoginManager.getInstance().logOut();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }


    /**
     * Get all success data after facebook sign in
     *
     * @param completedTask
     */

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {

                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                Uri personPhoto = account.getPhotoUrl();

                String[] personUserName = personEmail.split("@");
                String userName=personUserName[0];

                String personPassword = "123456";

                String personDob = "1989-09-03";
                String personPhone = "01713523713";

                UserForm userForm = new UserForm(personGivenName, personFamilyName, userName, personEmail, personPassword, personPassword);
                SignUpForm SignUpForm = new SignUpForm(personDob, personPhone, userForm);

                authClient.signUp(SignUpForm);
                signIn("facebook",userName,personPassword);
                mGoogleSignInClient.signOut();



            }
        } catch (ApiException e) {

            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


    public boolean isValidForm(String name, String password) {
        boolean validName = !name.isEmpty();

        if (!validName) {
            userNameText.setError("Please enter valid name");
        }

        boolean validPass = !password.isEmpty();
        if (!validPass) {
            userPasswordText.setError("Please enter valid password");
        }
        return validName && validPass;
    }


    @Override
    public void onLoginSuccess(AuthUser authUser, String authorization) {
       // LoadingMessageUtil.endLoadingDialog();
        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.orgName, authUser.getOrg_name(), getActivity());

        System.out.println(authUser.toString());
    }

    @Override
    public void onLoginFailed(JSONObject jsonObject) {
        System.out.println(jsonObject.toString());

    }

    @Override
    public void onSignUpSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onSignUpFailure(JSONObject jsonObject) {

    }

    @Override
    public void onOrganizationSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onOrganizationFailure(JSONObject jsonObject) {

    }
}
