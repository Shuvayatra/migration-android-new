package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.ScreenModel;
import com.taf.repository.IScreenRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by umesh on 1/14/17.
 */

public class GetScreensUseCase extends UseCase<List<ScreenModel>> {

    IScreenRepository mRepository;

    public GetScreensUseCase(IScreenRepository repository,
                                ThreadExecutor pThreadExecutor,
                                PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);

        mRepository = repository;
    }

    @Override
    protected Observable<List<ScreenModel>> buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getScreens();
    }
}
