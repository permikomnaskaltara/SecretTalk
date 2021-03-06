package com.training.leos.secrettalk.ui.signIn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.training.leos.secrettalk.SignInContract;
import com.training.leos.secrettalk.R;
import com.training.leos.secrettalk.presenter.SignInPresenter;
import com.training.leos.secrettalk.ui.register.RegistrationActivity;
import com.training.leos.secrettalk.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements SignInContract.View, View.OnClickListener {
    @BindView(R.id.actv_login_actv_email) AutoCompleteTextView actvEmail;
    @BindView(R.id.actv_login_act_password) EditText actvPassword;
    @BindView(R.id.btn_login_act_signin) Button btnSignIn;
    @BindView(R.id.tv_btn_sign_up) TextView tvBtnSignUp;

    private ProgressDialog progressBar;
    SignInContract.Presenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ButterKnife.bind(this);
        progressBar = new ProgressDialog(this);
        if (loginPresenter == null){
            loginPresenter = new SignInPresenter(this);
        }

        btnSignIn.setOnClickListener(this);
        tvBtnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_act_signin :
                loginPresenter.onSignIn();
                break;
            case R.id.tv_btn_sign_up :
                loginPresenter.onAccountCreationButtonClicked();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.unsubscribe();
    }

    @Override
    public void startRegisterActivity() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showProgressBar() {
        progressBar.setTitle("Signing in");
        progressBar.setMessage("Please wait a moment!");
        progressBar.setCanceledOnTouchOutside(true);
        progressBar.show();
    }

    @Override
    public void hideProgressBar() {
        progressBar.dismiss();
    }

    @Override
    public String getEmail() {
        return actvEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return actvPassword.getText().toString();
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
