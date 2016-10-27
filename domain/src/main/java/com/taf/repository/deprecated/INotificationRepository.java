package com.taf.repository.deprecated;

import com.taf.model.Notification;
import com.taf.repository.IBaseRepository;

import rx.Observable;

public interface INotificationRepository extends IBaseRepository<Notification> {
    Observable<Boolean> saveNotification(Notification pNotification);
}
