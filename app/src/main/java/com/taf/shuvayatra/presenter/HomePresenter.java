package com.taf.shuvayatra.presenter;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Block;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.deprecated.interfaces.MvpView;
import com.taf.shuvayatra.ui.views.HomeView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by julian on 10/18/16.
 */

public class HomePresenter implements Presenter {

    public static final String TAG = "HomePresenter";

    final UseCase mUseCase;
    HomeView mView;

    @Inject
    public HomePresenter(@Named("home") UseCase useCase) {
        mUseCase = useCase;
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
        mUseCase.execute(new HomeSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        this.mView = (HomeView) view;
    }

    private final class HomeSubscriber extends DefaultSubscriber<List<Block>> {
        @Override
        public void onCompleted() {
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(List<Block> blocks) {
            mView.renderBlocks(blocks);
            onCompleted();
        }
    }
}
