package com.training.leos.secrettalk.presenter;

import com.training.leos.secrettalk.SignInContract;
import com.training.leos.secrettalk.R;
import com.training.leos.secrettalk.data.DataManager;
import com.training.leos.secrettalk.data.model.Credential;
import com.training.leos.secrettalk.ui.signIn.SignInActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class SignInPresenter implements SignInContract.Presenter {
    static final String TAG = SignInPresenter.class.getSimpleName();
    private SignInContract.View view = new SignInActivity();
    private CompositeDisposable compositeDisposable;
    private DataManager authentication;

    public SignInPresenter(SignInContract.View view) {
        this.view = view;
        this.compositeDisposable = new CompositeDisposable();
        this.authentication = DataManager.getInstance();
    }

    @Override
    public void onSignIn() {
        String email = view.getEmail();
        String password = view.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            view.showToast(R.string.error_empty_input);
        }
        if (!email.isEmpty() && !email.contains("@")) {
            view.showToast(R.string.error_invalid_email);
        } else {
            view.showProgressBar();
            Credential credential = new Credential();
            credential.setEmail(email);
            credential.setPassword(password);
            attemptSignIn(credential);
        }
    }

    private void attemptSignIn(final Credential credential) {
        compositeDisposable.add(authentication.signIn(credential)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        view.hideProgressBar();
                        view.startMainActivity();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideProgressBar();
                        view.showToast(e.getMessage());
                    }
                })
        );
    }

    @Override
    public void onAccountCreationButtonClicked() {
        view.startRegisterActivity();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }
}
