package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityMyManageMoneyDetailsBinding;
import com.cdkj.token.model.MyManagementMoney;
import com.cdkj.token.model.MyManamentMoneyProduct;
import com.cdkj.token.utils.AmountUtil;

/**
 * 我的理财详情
 * Created by cdkj on 2018/8/9.
 */

public class MyManagementMoneyDetailsActivity extends AbsLoadActivity {

    private ActivityMyManageMoneyDetailsBinding mBinding;

    private MyManamentMoneyProduct moneyProduct;

    public static void open(Context context, MyManamentMoneyProduct product) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyManagementMoneyDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, product);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_my_manage_money_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        if (getIntent() != null) {
            moneyProduct = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        }

        initListener();
        setShowData(moneyProduct);


    }

    private void initListener() {
        mBinding.ivFinish.setOnClickListener(view -> {
            finish();
        });
    }

    /**
     * @param moneyProduct
     */
    private void setShowData(MyManamentMoneyProduct moneyProduct) {

        MyManagementMoney myManagementMoney = moneyProduct.getProductInfo();

        if (moneyProduct == null || myManagementMoney == null) {
            return;
        }


        mBinding.tvName.setText(myManagementMoney.getName());

        mBinding.tvCode.setText(moneyProduct.getCode());
        mBinding.tvTransctionTime.setText(DateUtil.formatStringData(moneyProduct.getCreateDatetime(), DateUtil.DEFAULT_DATE_FMT));
        mBinding.tvProductLimit.setText(getString(R.string.product_days, myManagementMoney.getLimitDays() + ""));

        mBinding.tvIncomeRate.setText(StringUtils.showformatPercentage(myManagementMoney.getExpectYield()));

        mBinding.tvIncome.setText(AmountUtil.transformFormatToString(moneyProduct.getExpectIncome(), myManagementMoney.getSymbol(), AmountUtil.ALLSCALE) + myManagementMoney.getSymbol());

        mBinding.tvBuyAmount.setText(AmountUtil.transformFormatToString(moneyProduct.getInvestAmount(), myManagementMoney.getSymbol(), AmountUtil.ALLSCALE) + myManagementMoney.getSymbol());

        mBinding.tvStartTime.setText(DateUtil.formatStringData(myManagementMoney.getIncomeDatetime(), DateUtil.DEFAULT_DATE_FMT));
        mBinding.tvEndTime.setText(DateUtil.formatStringData(myManagementMoney.getArriveDatetime(), DateUtil.DEFAULT_DATE_FMT));

        mBinding.tvState.setText(getStateString(moneyProduct.getStatus()));
    }

    /**
     * 获取状态描述
     *
     * @param state
     * @return
     */
    public String getStateString(String state) {

        if (TextUtils.isEmpty(state)) {
            return "";
        }

        switch (state) {
            case "0":
                return getString(R.string.product_buy_state_0);
            case "1":
                return getString(R.string.product_buy_state_1);
            case "2":
                return getString(R.string.product_buy_state_2);
            case "3":
                return getString(R.string.product_buy_state_3);
        }

        return "";

    }


}
