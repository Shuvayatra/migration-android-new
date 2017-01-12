package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Post;
import com.taf.repository.IUserAccountRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by umesh on 1/11/17.
 */

public class GetFavouritePostUseCase extends UseCase<List<Post>> {

    IUserAccountRepository mRepository;

    public GetFavouritePostUseCase(IUserAccountRepository repository, ThreadExecutor pThreadExecutor,
                                   PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<List<Post>> buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getFavouritePost();
    }
}
