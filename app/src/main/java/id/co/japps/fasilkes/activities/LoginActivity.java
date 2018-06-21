package id.co.japps.fasilkes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.utilities.SharedPrefManager;

/**
 * Created by OiX on 04/02/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    Context context;
    int logouting = 0;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        sharedPrefManager = new SharedPrefManager(this);

        Intent intent = getIntent();





        //GET KEYSTORE HASH
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.delaroystudios.facebookloginuserdetails",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(getApplicationContext(), "Selamat Datang "+sharedPrefManager.getSPNama(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);



        //Google Login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // Set the dimensions of the sign-in button.
        if(intent.hasExtra("status")){
            if(getIntent().getStringExtra("status").equals("logout")){
                signOut();
            }
        }
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT<=19){
                    Toast.makeText(LoginActivity.this, "Maaf, Google Login hanya dapat digunakan pada Android Version Kitkat (4.4) ke Atas, Silahkan gunakan Facebook Login", Toast.LENGTH_LONG).show();
                }else{
                    signIn();
                }
            }
        });
        updateUI(account);
    }

    private void signIn() {
        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS, "Google");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START signOut]
    void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        String id = sharedPrefManager.getSPID().substring(0, sharedPrefManager.getSPID().indexOf("@"));
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(id);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS, "spStatus");
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, "spID");
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                        updateUI(null);
                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        finish();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, account.getEmail());
            sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, account.getEmail());
            String id = sharedPrefManager.getSPID().substring(0, sharedPrefManager.getSPID().indexOf("@"));
            FirebaseMessaging.getInstance().subscribeToTopic(id);
            //sharedPrefManager.saveSPString(SharedPrefManager.SP_IMAGE, profile.getProfilePictureUri(200,200).toString());
            // Shared Pref ini berfungsi untuk menjadi trigger session login
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(main);

            //mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));

            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void nextActivity(Profile profile){
        if(profile != null){
            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, profile.getName());
            sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, profile.getId());
            FirebaseMessaging.getInstance().subscribeToTopic(sharedPrefManager.getSPID());
            sharedPrefManager.saveSPString(SharedPrefManager.SP_IMAGE, profile.getProfilePictureUri(200,200).toString());
            // Shared Pref ini berfungsi untuk menjadi trigger session login
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
        }
    }
}
