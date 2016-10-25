package com.example.sashatinkoff.fbshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMessage();
            }
        });
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

        super.onActivityResult(requestCode, resultCode, data);
    }
}
