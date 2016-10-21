package com.taf.interactor.deprecated;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Notification;
import com.taf.repository.deprecated.INotificationRepository;

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
