package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.CountryWidgetData;
import com.taf.repository.IWidgetComponentRepository;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by rakeeb on 10/20/16.
 */

public class GetWidgetComponentUseCase extends UseCase<CountryWidgetData.Component> {

    private final IWidgetComponentRepository mWidgetRepository;

    @Inject
    public GetWidgetComponentUseCase(IWidgetComponentRepository repository, ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mWidgetRepository = repository;
    }

    @Override
    protected Observable<CountryWidgetData.Component> buildUseCaseObservable(UseCaseData pData) {
        return mWidgetRepository.getComponent(pData.getInteger(UseCaseData.COMPONENT_TYPE), pData);
    }
}
