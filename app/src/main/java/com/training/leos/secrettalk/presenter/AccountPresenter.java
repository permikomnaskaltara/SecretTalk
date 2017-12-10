package com.training.leos.secrettalk.presenter;

import com.training.leos.secrettalk.AccountContract;
import com.training.leos.secrettalk.data.firebase.FirebaseAuthDataStore;
import com.training.leos.secrettalk.data.model.Credential;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class AccountPresenter implements AccountContract.Presenter {
    private AccountContract.View view;
    private CompositeDisposable compositeDisposable;
    private FirebaseAuthDataStore authentication;

    public AccountPresenter(AccountContract.View view){
        this.view = view;
        this.authentication = FirebaseAuthDataStore.getInstance();
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onInitialize() {
        String uId = authentication.getCurrentUserId();
        compositeDisposable.add(authentication.getUserCredential(uId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableMaybeObserver<Credential>() {
                    @Override
                    public void onSuccess(@NonNull Credential credential) {
                        view.showMyAccount(credential);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.showToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
    @Override
    public void onAccountClicked(String id) {
        view.startDetailAccountFragment(id);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

}
