package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.ScreenModel;
import com.taf.shuvayatra.ui.activity.HomeActivity;

/**
 *
 */

public interface HomeDeeplinkInteraction extends DeeplinkInteraction {

    /**
     * only used in {@link HomeActivity}
     */
    public void onRedirectScreen(ScreenModel screenModel, int listIndex);
}
