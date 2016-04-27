package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.repository.ISectionRepository;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Nirazan-PC on 4/21/2016.
 */
public class GetSectionCategoryUseCase extends UseCase {

    private final ISectionRepository mRepository;
    private final MyConstants.DataParent mParent;
    private final boolean mIsCategory;

    @Inject
    public GetSectionCategoryUseCase(boolean pIsCategory, MyConstants.DataParent pParent,
                                     ISectionRepository
            pRepository, ThreadExecutor pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
        mParent = pParent;
        mIsCategory = pIsCategory;
    }

    @Override
    protected Observable buildUseCaseObservable(UseCaseData pData) {
        if (mParent == MyConstants.DataParent.JOURNEY)
            return mRepository.getListBySectionName(MyConstants.SECTION.JOURNEY, mIsCategory);
        else if (mParent == MyConstants.DataParent.COUNTRY)
            return mRepository.getListBySectionName(MyConstants.SECTION.COUNTRY, mIsCategory);
        else
            return Observable.error(new IllegalArgumentException());
    }
}
