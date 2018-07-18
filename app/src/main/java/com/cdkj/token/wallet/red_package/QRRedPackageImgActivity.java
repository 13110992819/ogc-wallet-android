package com.cdkj.token.wallet.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityQrredPackageImgBinding;
import com.cdkj.token.model.RedPackageEventBusBean;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.MyConfig.ENGLISH;


public class QRRedPackageImgActivity extends AbsLoadActivity {

    ActivityQrredPackageImgBinding mBinding;
    private String redPackageCode;

    public static void open(Context context, String redPackageCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, QRRedPackageImgActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, redPackageCode);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_qrred_package_img, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            redPackageCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }
        mBinding.llOther.setOnClickListener(view -> {
            EventBus.getDefault().post(new RedPackageEventBusBean());
            finish();
        });
        getRedPacketShareUrlRequest();
    }


    /**
     * 获取红包分享路径
     *
     * @param redPackageCode
     * @return
     */
    public static String getRedPacketShareUrl(String redPackageCode, String inviteCode) {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("/redPacket/receive.html?code=" + redPackageCode);//红包码

        stringBuffer.append("&inviteCode=" + inviteCode);// 邀请码

        if (TextUtils.equals(SPUtilHelper.getLanguage(), ENGLISH)) { //国际化
            stringBuffer.append("&lang=en");
        } else {
            stringBuffer.append("&lang=cn");
        }

        return stringBuffer.toString();
    }


    public void getRedPacketShareUrlRequest() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "redPacketShareUrl");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                Bitmap bitmap = CodeUtils.createImage(data.getCvalue() + getRedPacketShareUrl(redPackageCode, SPUtilHelper.getSecretUserId()), 500, 500, null);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] datas = baos.toByteArray();

                ImgUtils.loadByte(QRRedPackageImgActivity.this, datas, mBinding.ivQrImg);

                mBinding.ivQrImg.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

}
