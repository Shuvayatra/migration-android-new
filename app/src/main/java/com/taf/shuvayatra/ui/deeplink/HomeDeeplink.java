package com.taf.shuvayatra.ui.deeplink;

import android.net.Uri;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.ui.interfaces.DeeplinkInteraction;
import com.taf.shuvayatra.ui.interfaces.IDeeplinkHandler;

/**
 *
 */
public class HomeDeeplink implements IDeeplinkHandler {

    DeeplinkInteraction callback;

    public HomeDeeplink(DeeplinkInteraction callback) {
        this.callback = callback;
    }

    @Override
    public void handleUri(Uri uri) {
        callback.onIdentifierRetrieved(R.id.nav_home);
    }
}
