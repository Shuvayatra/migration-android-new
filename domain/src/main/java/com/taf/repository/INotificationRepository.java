package com.taf.repository;

import com.taf.model.Notification;

import rx.Observable;

public interface INotificationRepository extends IBaseRepository<Notification> {
    Observable<Boolean> saveNotification(Notification pNotification);
}
