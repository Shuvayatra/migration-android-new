package com.taf.shuvayatra.presenter;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.interfaces.DeletedInfoView;
import com.taf.shuvayatra.ui.interfaces.MvpView;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

public class DeletedContentPresenter implements Presenter {

    private final UseCase mUseCase;
    DeletedInfoView mView;

    @Inject
    public DeletedContentPresenter(@Named("deleted") UseCase pUseCase) {
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
    }

    @Override
    public void initialize(UseCaseData pData) {
        deleteContent(pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (DeletedInfoView) view;
    }

    private void deleteContent(UseCaseData pData) {
        this.mUseCase.execute(new DeleteContentSubscriber(), pData);
    }

    private final class DeleteContentSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Logger.e("DeleteContentSubscriber_onError", "factory_message: " + ErrorMessageFactory
                    .create(mView.getContext(), new DefaultErrorBundle((Exception) e)
                            .getException()));
            e.printStackTrace();
            mView.showErrorView(ErrorMessageFactory.create(mView
                    .getContext(), new DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(Boolean pFlag) {
            if (pFlag != null) {
                long timestamp = Calendar.getInstance().getTimeInMillis();
                ((BaseActivity) mView.getContext()).getPreferences().setLastUpdateStamp(
                        (timestamp / 1000L));
            }
            mView.deletedInfoFetched();
            onCompleted();
        }
    }
}
