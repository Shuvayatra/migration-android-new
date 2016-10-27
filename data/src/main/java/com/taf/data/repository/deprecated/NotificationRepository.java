package com.taf.data.repository.deprecated;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Notification;
import com.taf.repository.deprecated.INotificationRepository;

import java.util.List;

import rx.Observable;

public class NotificationRepository implements INotificationRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public NotificationRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<Notification>> getList(int pLimit, int pOffset) {
        return mDataStoreFactory.createDBDataStore()
                .getNotifications()
                .map(pDbNotifications -> mDataMapper.transformNotificationFromDb(pDbNotifications));
    }

    @Override
    public Observable<Notification> getSingle(Long pId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Boolean> saveNotification(Notification pNotification) {
        return mDataStoreFactory.createDBDataStore()
                .saveNotification(pNotification);
    }
}
