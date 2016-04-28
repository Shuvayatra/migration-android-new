package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.Notification;

import java.util.List;

public interface NotificationView extends LoadDataView {
    void renderNotifications(List<Notification> pNotifications);
}
