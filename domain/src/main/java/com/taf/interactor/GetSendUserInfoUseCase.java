package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.UserInfoModel;
import com.taf.repository.IUserAccountRepository;

import rx.Observable;

/**
 * Created by umesh on 1/13/17.
 */

public class GetSendUserInfoUseCase extends UseCase<Boolean> {

    IUserAccountRepository mRepository;

    public GetSendUserInfoUseCase(IUserAccountRepository repository,
                                  ThreadExecutor pThreadExecutor,
                                  PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);

        mRepository = repository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(UseCaseData pData) {
        UserInfoModel userInfo = (UserInfoModel) pData.getSerializable(UseCaseData.USER_INFO);
        return mRepository.saveUserInfo(userInfo);
    }
}
