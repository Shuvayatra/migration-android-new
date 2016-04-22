package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.repository.IBaseRepository;
import com.taf.repository.ISectionRepository;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Nirazan-PC on 4/21/2016.
 */
public class GetSectionCategoryUseCase extends UseCase{

    private final ISectionRepository mRepository;

    @Inject
    public GetSectionCategoryUseCase(ISectionRepository pRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread){
        super(pThreadExecutor,pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getListBySectionName(pData.getString(UseCaseData.SECTION_NAME));
    }
}
