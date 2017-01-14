package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.ScreenDataModel;
import com.taf.repository.IScreenRepository;
import com.taf.util.MyConstants;

import javax.activation.UnsupportedDataTypeException;

import rx.Observable;

/**
 * Created by umesh on 1/13/17.
 */

public class GetScreenDataUseCase extends UseCase<ScreenDataModel> {

    IScreenRepository mRepository;
    long mId;

    public GetScreenDataUseCase(Long id,
                                IScreenRepository repository,
                                ThreadExecutor pThreadExecutor,
                                PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = repository;
        mId = id;
    }

    @Override
    protected Observable<ScreenDataModel> buildUseCaseObservable(UseCaseData pData) {
        String endPoint = pData.getString(UseCaseData.END_POINT);
        String type = pData.getString(UseCaseData.SCREEN_DATA_TYPE);
        if (type.equalsIgnoreCase(MyConstants.SCREEN.TYPE_BLOCK)) {
            return mRepository.getScreenBlockData(mId, endPoint);
        } else if (type.equalsIgnoreCase(MyConstants.SCREEN.TYPE_FEED)) {
            int page = pData.getInteger(UseCaseData.NEXT_PAGE);
            return mRepository.getScreenFeedData(mId,page, endPoint);
        } else
            return Observable.error(new UnsupportedDataTypeException());
    }
}
