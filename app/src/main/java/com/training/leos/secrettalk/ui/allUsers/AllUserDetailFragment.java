package com.training.leos.secrettalk.ui.allUsers;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.training.leos.secrettalk.AllUserDetailContract;
import com.training.leos.secrettalk.R;
import com.training.leos.secrettalk.data.model.Credential;
import com.training.leos.secrettalk.presenter.AllUserDetailPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllUserDetailFragment extends DialogFragment
        implements AllUserDetailContract.View, View.OnClickListener{
    @BindView(R.id.cimg_account_detail_thumb) CircleImageView cimgThumbImage;
    @BindView(R.id.tv_account_detail_name) TextView tvName;
    @BindView(R.id.tv_account_detail_email) TextView tvEmail;
    @BindView(R.id.tv_account_detail_about_me) TextView tvAbout;
    @BindView(R.id.tv_friend_request_description) TextView tvFriendReqDesc;
    @BindView(R.id.btn_account_detail_friend_request_state) Button btnRequestState;

    public static final String TAG = AllUserDetailFragment.class.getName();
    private ProgressDialog progressBar;
    private AllUserDetailContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_dialog_all_user_detail,
                container,
                false);
        ButterKnife.bind(this, v);
        progressBar = new ProgressDialog(getActivity());
        if (presenter == null) {
            presenter = new AllUserDetailPresenter(this);
        }

        btnRequestState.setOnClickListener(this);
        presenter.onInitialize(getArguments().getString("userId"));
        presenter.onCheckUserFriendState(getArguments().getString("userId"));
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_account_detail_friend_request_state:
                presenter.onRequestClicked(
                        getArguments().getString("userId"),
                        btnRequestState.getTag().toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void showAccountInformation(Credential data) {
        Log.w(TAG, "showAccountInformation: " + data.getName());
        String thumbImageUrl = data.getThumbImageUrl();
        if (thumbImageUrl != "default") {
            Picasso.with(getContext())
                    .load(data.getThumbImageUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(cimgThumbImage);
        }
        tvName.setText(data.getName());
        tvEmail.setText(data.getEmail());
        tvAbout.setText(data.getAbout());
    }

    @Override
    public void reloadInformation() {

    }

    @Override
    public void inflateUserStateView(String desc, String state, String tag){
        tvFriendReqDesc.setText(desc);
        btnRequestState.setText(state);
        btnRequestState.setTag(tag);
    }

    @Override
    public void disableUserStateView(){
        btnRequestState.setEnabled(false);
        btnRequestState.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
    }

    @Override
    public void enableUserStateView(){
        btnRequestState.setEnabled(true);
        btnRequestState.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary_light));
    }

    @Override
    public void showProgressBar() {
        progressBar.setMessage("Loading");
        progressBar.setCanceledOnTouchOutside(true);
        progressBar.show();
    }

    @Override
    public void hideProgressBar() {
        progressBar.dismiss();
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(@StringRes int message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
