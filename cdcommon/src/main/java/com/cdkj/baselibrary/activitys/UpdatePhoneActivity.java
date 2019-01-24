package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.databinding.ActivityModifyPhoneBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.SendVerificationCode;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 更换手机号码
 * Created by cdkj on 2017/6/16.
 */

public class UpdatePhoneActivity extends AbsActivity implements SendCodeInterface {

    private ActivityModifyPhoneBinding mBinding;

    private SendPhoneCodePresenter mSendCodePresenter;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UpdatePhoneActivity.class);

        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_modify_phone, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTopTitle(getString(R.string.activity_mobile_title));
        setSubLeftImgState(true);

        mSendCodePresenter = new SendPhoneCodePresenter(this, this);

        initListener();
    }

    private void initListener() {
        //发送验证码
        mBinding.btnSendNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhoneNew.getText())) {
                    UITipDialog.showInfoNoIcon(UpdatePhoneActivity.this, getString(com.cdkj.baselibrary.R.string.activity_paypwd_mobile_hint));
                    return;
                }

                String phone = mBinding.edtPhoneNew.getText().toString().trim();

                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        phone, "805061", AppConfig.USERTYPE, SPUtilHelper.getCountryInterCode());

                mSendCodePresenter.openVerificationActivity(sendVerificationCode);
            }
        });

        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhoneNew.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_mobile_hint));
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtCodeNew.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_code_hint));
                    return;
                }

                updatePhone();

            }
        });

    }


    /**
     * 更换手机号
     */
    private void updatePhone() {

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("newMobile", mBinding.edtPhoneNew.getText().toString());
        map.put("smsCaptcha", mBinding.edtCodeNew.getText().toString());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805061", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    showToast(getString(R.string.activity_mobile_modify_success));
                    finish();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    @Override
    public void CodeSuccess(String msg,int req) {
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSendNew));
    }

    @Override
    public void CodeFailed(String code, String msg,int req) {
        showToast(msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendCodePresenter != null) {
            mSendCodePresenter.clear();
            mSendCodePresenter = null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSendCodePresenter != null) {
            mSendCodePresenter.onActivityResult(requestCode, resultCode, data);
        }
    }


}
