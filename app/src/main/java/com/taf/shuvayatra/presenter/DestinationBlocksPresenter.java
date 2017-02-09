package com.taf.shuvayatra.presenter;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Block;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.DestinationDetailView;
import com.taf.shuvayatra.ui.views.MvpView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class DestinationBlocksPresenter implements Presenter {

    public static final String TAG = "DestinationBlocksPresenter";

    UseCase mUseCase;
    DestinationDetailView mView;

    @Inject
    public DestinationBlocksPresenter(@Named("destination-blocks") UseCase useCase){
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
        mView.showLoadingView();
        mUseCase.execute(new DestinationBlocksSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (DestinationDetailView) view;
    }

    public class DestinationBlocksSubscriber extends DefaultSubscriber<List<Block>>{

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
        public void onNext(List<Block> pT) {
            onCompleted();
            mView.renderBlocks(pT);
        }
    }
}
