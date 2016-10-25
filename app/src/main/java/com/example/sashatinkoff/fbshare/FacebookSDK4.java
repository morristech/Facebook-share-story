package com.example.sashatinkoff.fbshare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sashatinkoff on 25.10.16.
 */

public class FacebookSDK4 {
    private static final String NAMESPACE = "t_watchcat";
    private static final String SHARE_OBJECT_TYPE = "a_cat";
    private static final String SHARE_ACTION_TYPE = "kiss";
    private static final String ACTION_OBJECT = "the_cat";
    public static final String TAG = "FacebookSDK4";

    private static FacebookSDK4 instance;
    private final CallbackManager callbackManager;
    private ArrayList<FacebookMeDetailListener> meDetailListener = new ArrayList<>();

    public static FacebookSDK4 getInstance() {
        if (instance == null)
            instance = new FacebookSDK4();

        return instance;
    }

    private FacebookSDK4() {
        callbackManager = CallbackManager.Factory.create();
    }

    public void login(Activity activity) {
        onLogin();
        LoginManager.getInstance().logInWithPublishPermissions(activity, Arrays.asList("publish_actions"));
    }

    public FacebookSDK4 addMeDetailListener(FacebookMeDetailListener facebookMeDetailListener) {
        meDetailListener.add(facebookMeDetailListener);
        return this;
    }

    private void onLogin() {
        FacebookCallback<LoginResult> fbLoginCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                AccessToken.setCurrentAccessToken(accessToken);

                requestMe();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        };

        LoginManager.getInstance().registerCallback(callbackManager, fbLoginCallback);
    }

    private void requestMe() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String name = object.getString("name");
                            String id = object.getString("id");

                            for (FacebookMeDetailListener listener : meDetailListener)
                                listener.onMyNameReady();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public boolean onResult(int requestCode, int resultCode, Intent data) {
        return callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired();
    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }

    private FacebookCallback<Sharer.Result> shareCallback(final FacebookResultsListener facebookResultListener) {
        return new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                log("onSuccess " + result.getPostId());
                facebookResultListener.onFacebookResponse();
            }

            @Override
            public void onCancel() {
                log("onCancel");
                facebookResultListener.onFacebookUnauthorized();
            }

            @Override
            public void onError(FacebookException error) {
                log("onError " + (error != null ? error.getMessage() : null));
                if (error != null) error.printStackTrace();

                facebookResultListener.onFacebookError();
            }
        };
    }

    public static void log(String message) {
        Log.d(TAG,  message);
        Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void updateStatus(Activity activity, File myPath, String message, FacebookResultsListener facebookResultListener) {
        updateStatus(getShareDialog(activity), myPath, message, facebookResultListener);
    }

    public void updateStatus(Fragment fragment, String message, FacebookResultsListener facebookResultListener) {
        updateStatus(getShareDialog(fragment), null, message, facebookResultListener);
    }

    private void updateStatus(ShareDialog shareDialog, File myPath, String message, FacebookResultsListener facebookResultListener) {
        ShareContent shareContent;

        SharePhoto photo = null;
        if (myPath != null) {
            Bitmap image = BitmapFactory.decodeFile(myPath.getAbsolutePath());

            photo = new SharePhoto.Builder()
                    .setCaption(message)
                    .setBitmap(image)
                    .build();
        }

        // Create an object
        ShareOpenGraphObject.Builder objectBuilder = new ShareOpenGraphObject.Builder()
                .putString("og:type", NAMESPACE + ":" + SHARE_OBJECT_TYPE)
                .putString("og:title", "A super title")
                .putString("og:description", message);

        if (photo != null) objectBuilder.putPhoto("og:image", photo);
        ShareOpenGraphObject object = objectBuilder.build();

        // Create an action
        ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                .setActionType(NAMESPACE + ":" + SHARE_ACTION_TYPE)
                .putObject(ACTION_OBJECT, object)
                .build();

        // Create the content
        shareContent = new ShareOpenGraphContent.Builder()
                .setPreviewPropertyName(ACTION_OBJECT)
                .setAction(action)
                .build();

        shareDialog.show(shareContent, com.facebook.share.widget.ShareDialog.Mode.AUTOMATIC);
        shareDialog.registerCallback(callbackManager,
                shareCallback(facebookResultListener)
        );
    }

    private ShareDialog getShareDialog(Activity activity) {
        return new ShareDialog(activity);
    }

    private ShareDialog getShareDialog(Fragment fragment) {
        return new ShareDialog(fragment);
    }

    public interface FacebookMeDetailListener {
        void onMyNameReady();
    }


    public interface FacebookResultsListener {

        void onFacebookResponse();

        void onFacebookError();

        void onFacebookUnauthorized();
    }
}
