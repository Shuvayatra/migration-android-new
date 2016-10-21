package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Block;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.JourneyView;
import com.taf.shuvayatra.ui.views.MvpView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class JourneyPresenter implements Presenter {

    UseCase mUseCase;
    JourneyView mView;

    @Inject
    public JourneyPresenter(@Named("journey") UseCase useCase){
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
        mUseCase.execute(new JourneySubscriber(),pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (JourneyView) view;
    }

    public class JourneySubscriber extends DefaultSubscriber<List<Block>>{

        @Override
        public void onCompleted() {
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(List<Block> blocks) {
            onCompleted();
            mView.renderContents(blocks);
        }
    }


}
