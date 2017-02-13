package com.taf.shuvayatra.ui.deeplink;

import android.net.Uri;

import com.taf.model.ScreenModel;
import com.taf.shuvayatra.ui.interfaces.HomeDeeplinkInteraction;
import com.taf.shuvayatra.ui.interfaces.IDeeplinkHandler;
import com.taf.util.MyConstants.Deeplink;

import java.util.List;

/**
 *
 */
public class ScreenDeeplink implements IDeeplinkHandler {

    HomeDeeplinkInteraction callback;
    List<ScreenModel> screenList;

    public ScreenDeeplink(HomeDeeplinkInteraction callback, List<ScreenModel> screenList) {
        this.callback = callback;
        this.screenList = screenList;
    }

    @Override
    public void handleUri(Uri uri) {
        ScreenModel screenModel = new ScreenModel();
        screenModel.setId(Long.parseLong(uri.getQueryParameter(Deeplink.PARAM_SCREEN_ID)));
        int index = screenList.indexOf(screenModel);
        callback.onRedirectScreen(screenModel, index);
    }
}
