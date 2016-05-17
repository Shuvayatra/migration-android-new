package com.taf.data.repository;

import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.utils.Logger;
import com.taf.repository.IBaseRepository;

import java.util.List;

import rx.Observable;

public class DeletedContentRepository implements IBaseRepository<Boolean> {

    private final DataStoreFactory mDataStoreFactory;

    public DeletedContentRepository(DataStoreFactory pDataStoreFactory) {
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<Boolean>> getList(int pLimit, int pOffset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Boolean> getSingle(Long pLastUpdateStamp) {
        Logger.d("DeletedContentRepository_getSingle", "stamp: " + pLastUpdateStamp);
        try {
            return mDataStoreFactory.createRestDataStore()
                    .getDeletedContent((pLastUpdateStamp == null || pLastUpdateStamp == -1)
                            ? null
                            : pLastUpdateStamp
                    )
                    .map(pDeletedEntity -> {
                        if (pDeletedEntity != null) {
                            if ((pDeletedEntity.getPosts() != null && !pDeletedEntity.getPosts()
                                    .isEmpty()) || (pDeletedEntity.getSections() != null &&
                                    !pDeletedEntity.getSections().isEmpty())) {
                                return true;
                            }
                            return false;
                        }
                        return null;
                    });
        } catch (Exception e) {
            return Observable.just(false);
        }
    }
}
