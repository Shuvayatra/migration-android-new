package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Notification;
import com.taf.repository.IBaseRepository;
import com.taf.repository.INotificationRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetNotificationListUseCase extends UseCase<List<Notification>> {

    private final IBaseRepository mRepository;

    @Inject
    public GetNotificationListUseCase(INotificationRepository pRepository,
                                      ThreadExecutor pThreadExecutor, PostExecutionThread
                                              pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable<List<Notification>> buildUseCaseObservable(UseCaseData pData) {
        int offset = pData.getInteger(UseCaseData.OFFSET, 0);
        int limit = pData.getInteger(UseCaseData.LIMIT, -1);
        return mRepository.getList(limit, offset);
    }
}
