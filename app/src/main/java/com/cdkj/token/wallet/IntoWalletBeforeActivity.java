package com.cdkj.token.wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityIntoMainBeforeBinding;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.wallet.create_guide.CreatePassWordActivity;
import com.cdkj.token.wallet.import_guide.WalletImportWordsInputActivity;

/**
 * 本地没有钱包时 导入和创建引导钱包界面
 * Created by cdkj on 2018/6/5.
 */

public class IntoWalletBeforeActivity extends AbsLoadActivity {

    private ActivityIntoMainBeforeBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, IntoWalletBeforeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_into_main_before, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initClickListener();

//        mBinding.tvRead.setOnClickListener(view -> {
//            WebViewActivity.openkey(this, getString(R.string.privacy_agreement), "reg_protocol");
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        WalletHelper.clearCache(); //用户回退到这个界面时清除所有数据
    }

    /**
     * 设置点击事件
     */
    private void initClickListener() {
        //创建钱包
        mBinding.btnCreateWallet.setOnClickListener(view -> CreatePassWordActivity.open(this));

        mBinding.btnImportWallet.setOnClickListener(view -> WalletImportWordsInputActivity.open(this));
    }

}
