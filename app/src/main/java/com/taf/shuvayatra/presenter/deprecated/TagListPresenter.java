package com.taf.shuvayatra.presenter.deprecated;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.presenter.Presenter;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.deprecated.interfaces.TagListView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class TagListPresenter implements Presenter {

    UseCase mUseCase;
    TagListView mView;

    @Inject
    public TagListPresenter(@Named("tagList") UseCase pUseCase) {
        mUseCase = pUseCase;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        mUseCase.unSubscribe();
    }

    @Override
    public void initialize(UseCaseData pData) {
        loadTagList(pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (TagListView) view;
    }

    private void loadTagList(UseCaseData pData) {
        mView.showLoadingView();
        this.mUseCase.execute(new PostSubscriber(), pData);
    }

    private final class PostSubscriber extends DefaultSubscriber<List<String>> {

        @Override
        public void onCompleted() {
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
//            Logger.e("PostSubscriber", "error message: "+e.getMessage());
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(List<String> pTags) {
            mView.renderTagList(pTags);
            onCompleted();
        }
    }

}
