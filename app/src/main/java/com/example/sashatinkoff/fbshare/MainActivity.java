package com.example.sashatinkoff.fbshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.loginButton)
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        updateUI();
    }

    private void updateUI() {
        boolean loggedIn = FacebookSDK4.getInstance().isLoggedIn();

        loginButton.setVisibility(loggedIn ? View.GONE : View.VISIBLE);
        fab.setVisibility(loggedIn ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        shareMessage();
    }

    @OnClick(R.id.loginButton)
    public void onLoginClick(){
        FacebookSDK4.getInstance().login(this);
    }

    private void shareMessage() {
        if (!FacebookSDK4.getInstance().isLoggedIn()) {
            FacebookSDK4.log( "Not logged it");
            FacebookSDK4.getInstance().login(this);
        } else {
            FacebookSDK4.getInstance().updateStatus(this, null, "This is my message",
                    new FacebookSDK4.FacebookResultsListener() {
                        @Override
                        public void onFacebookResponse() {
                            FacebookSDK4.log( "onFacebookResponse");
                        }

                        @Override
                        public void onFacebookError() {
                            FacebookSDK4.log( "onFacebookError");
                        }

                        @Override
                        public void onFacebookUnauthorized() {
                            FacebookSDK4.log( "onFacebookUnauthorized");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookSDK4.getInstance().onResult(requestCode, resultCode, data);
        updateUI();

        super.onActivityResult(requestCode, resultCode, data);
    }
}
