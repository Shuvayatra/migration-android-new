package com.taf.shuvayatra.ui.deprecated.interfaces;

import com.taf.model.Notification;
import com.taf.shuvayatra.ui.views.LoadDataView;

import java.util.List;

public interface NotificationView extends LoadDataView {
    void renderNotifications(List<Notification> pNotifications);
}
