package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Notification;
import com.taf.repository.INotificationRepository;

import javax.inject.Inject;

import rx.Observable;

public class SaveNotificationListUseCase extends UseCase<Boolean> {

    private final INotificationRepository mRepository;

    @Inject
    public SaveNotificationListUseCase(INotificationRepository pRepository,
                                       ThreadExecutor pThreadExecutor, PostExecutionThread
                                              pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(UseCaseData pData) {
        Notification notification = (Notification) pData.getSerializable(UseCaseData
                .SUBMISSION_DATA);
        if (notification == null) {
            throw new IllegalStateException("Notification Submission data not provided");
        }
        return mRepository.saveNotification(notification);
    }
}
