package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.databinding.ActivityAppBuildTypeBinding;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.GlideApp;
import com.cdkj.baselibrary.utils.LogUtil;
import com.umeng.analytics.AnalyticsConfig;

import org.greenrobot.eventbus.EventBus;

import static com.cdkj.baselibrary.appmanager.AppConfig.BUILD_TYPE_DEBUG;
import static com.cdkj.baselibrary.appmanager.AppConfig.BUILD_TYPE_TEST;

/**
 * Created by lei on 2017/12/1.
 */

public class AppBuildTypeActivity extends AbsActivity {

    private ActivityAppBuildTypeBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, AppBuildTypeActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_app_build_type, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (!LogUtil.isLog) {
            finish();
        }
        setTopTitle("环境切换");
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
    }

    private void init() {

        mBinding.llBuildDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuildType(v, BUILD_TYPE_DEBUG);
            }
        });

        mBinding.llBuildTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuildType(v, BUILD_TYPE_TEST);
            }
        });

        mBinding.linlayoutOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String channeName = "当前渠道：" + getChannelName(AppBuildTypeActivity.this) + "\n当前appKey:\n"
                        + AnalyticsConfig.getAppkey(AppBuildTypeActivity.this);

                showDoubleWarnListen(channeName, new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {

                    }
                });
            }
        });


        GlideApp.with(this).asGif().load(R.mipmap.pu).diskCacheStrategy(DiskCacheStrategy.NONE).into(mBinding.ivDebug);
        GlideApp.with(this).asGif().load(R.mipmap.pu).diskCacheStrategy(DiskCacheStrategy.NONE).into(mBinding.ivTest);
    }

    public void setBuildType(View view, String type) {

        if (SPUtilHelper.getAPPBuildType().equals(type)) {

            popupWqbb(view, type);

        } else {
            popupMa(view, type);
        }

    }

    /**
     * 获取渠道名
     *
     * @param context 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    public String getChannelName(Context context) {
        if (context == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo，因为友盟设置的meta-data是在application标签中
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        //这里的UMENG_CHANNEL要与manifest中的配置文件标识一致
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    private void popupWqbb(View view, final String type) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_wqbb, null);

        final TextView tvTip = (TextView) mView.findViewById(R.id.tv_tip);
        TextView tvCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        final TextView tvConfirm = (TextView) mView.findViewById(R.id.tv_confirm);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        switch (type) {
            case BUILD_TYPE_DEBUG:
                tvTip.setText("已经是研发环境了，您还要我怎样?");
                tvConfirm.setText("对不起，那切换到测试吧");
                break;

            case BUILD_TYPE_TEST:
                tvTip.setText("已经是测试环境了，您还要我怎样?");
                tvConfirm.setText("对不起，那切换到研发吧");
                break;
        }


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                switch (type) {
                    case BUILD_TYPE_DEBUG:
                        SPUtilHelper.setAPPBuildType(BUILD_TYPE_TEST);
                        break;

                    case BUILD_TYPE_TEST:
                        SPUtilHelper.setAPPBuildType(BUILD_TYPE_DEBUG);
                        break;
                }

                close();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void popupMa(View view, final String type) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_ma, null);

        final TextView tvTip = (TextView) mView.findViewById(R.id.tv_tip);
        TextView tvCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        final TextView tvConfirm = (TextView) mView.findViewById(R.id.tv_confirm);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        switch (type) {
            case BUILD_TYPE_DEBUG:
                tvTip.setText("大佬，您确定要切换到研发环境吗?");
                break;

            case BUILD_TYPE_TEST:
                tvTip.setText("大佬，您确定要切换到测试环境吗?");
                break;
        }


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                switch (type) {
                    case BUILD_TYPE_DEBUG:
                        SPUtilHelper.setAPPBuildType(BUILD_TYPE_DEBUG);
                        break;

                    case BUILD_TYPE_TEST:
                        SPUtilHelper.setAPPBuildType(BUILD_TYPE_TEST);
                        break;
                }

                close();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void close() {
        SPUtilHelper.logOutClear();
        EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
        finish();

        // 初始化Retrofit
        RetrofitUtils.reSetInstance();

        CdRouteHelper.openStar();
    }

}
