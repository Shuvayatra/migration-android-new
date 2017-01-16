package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Info;
import com.taf.repository.IInfoRepository;
import com.taf.util.MyConstants;

import rx.Observable;

/**
 * Created by yipl on 1/16/17.
 */

public class GetInfoUseCase extends UseCase<Info> {
    IInfoRepository iInfoRepository;
    public GetInfoUseCase(IInfoRepository pIInfoRepository,
                             ThreadExecutor pThreadExecutor,
                             PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        iInfoRepository = pIInfoRepository;
    }

    @Override
    protected Observable<Info> buildUseCaseObservable(UseCaseData pData) {
        return iInfoRepository.getInfo(pData.getString(MyConstants.Extras.KEY_INFO));
    }
}
