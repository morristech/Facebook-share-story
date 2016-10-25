# Facebook-share-story
A sample how to share a custom story on Facebook

Don't forget to add an Object, Action and story in the developer console of your app.
https://developers.facebook.com/

You should also send created items for review. To do it open a section "App review" and click on button "Start submission".
In the other case you will see following stacktrace

    Error publishing message
    10-25 21:58:06.049 6391-6391/com.example.sashatinkoff.fbshare W/System.err:     at com.facebook.internal.NativeProtocol.getExceptionFromErrorData(NativeProtocol.java:788)
    10-25 21:58:06.049 6391-6391/com.example.sashatinkoff.fbshare W/System.err:     at com.facebook.share.internal.ShareInternalUtility.handleActivityResult(ShareInternalUtility.java:166)
    10-25 21:58:06.049 6391-6391/com.example.sashatinkoff.fbshare W/System.err:     at com.facebook.share.internal.ShareInternalUtility$3.onActivityResult(ShareInternalUtility.java:258)
    10-25 21:58:06.049 6391-6391/com.example.sashatinkoff.fbshare W/System.err:     at com.facebook.internal.CallbackManagerImpl.onActivityResult(CallbackManagerImpl.java:82)
    10-25 21:58:06.049 6391-6391/com.example.sashatinkoff.fbshare W/System.err:     at com.example.sashatinkoff.fbshare.FacebookSDK4.onResult(FacebookSDK4.java:122)
    10-25 21:58:06.049 6391-6391/com.example.sashatinkoff.fbshare W/System.err:     at com.example.sashatinkoff.fbshare.MainActivity.onActivityResult(MainActivity.java:59)


(Facebook can review your app up to 5 business days, so be patient)
