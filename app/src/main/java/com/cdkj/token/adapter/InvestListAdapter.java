package com.cdkj.token.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ProgressBar;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 理财列表
 * Created by cdkj on 2018/5/25.
 */

public class InvestListAdapter extends BaseQuickAdapter<ManagementMoney, BaseViewHolder> {


    public InvestListAdapter(@Nullable List<ManagementMoney> data) {
        super(R.layout.item_manager_money2, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ManagementMoney item) {

        if (item == null) return;


        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_income, StringUtils.showformatPercentage(item.getExpectYield()));


        helper.setText(R.id.tv_state, getStateString(item));
        helper.setTextColor(R.id.tv_state, Color.parseColor("#4064E6"));

        helper.setText(R.id.tv_limite_days, mContext.getString(R.string.product_days, item.getLimitDays() + ""));
        helper.setText(R.id.tv_avil_amount,
                AmountUtil.transformFormatToString(item.getAvilAmount(), item.getSymbol(), AmountUtil.ALLSCALE) + item.getSymbol());

        ProgressBar progressBar = helper.getView(R.id.progressbar);
        BigDecimal f = BigDecimalUtils.div(item.getSaleAmount(), item.getAmount(), 2);
        progressBar.setProgress((int) (f.floatValue() * 100));
    }

    private int getStateTextColor(ManagementMoney item) {
        if (TextUtils.isEmpty(item.getStatus())) {  //默认灰色
            return Color.parseColor("#ff999999");
        }

        switch (item.getStatus()) {
            case "4":                       //蓝色
                return Color.parseColor("#FF4064E6");
            case "8":
                //灰色
                return Color.parseColor("#ff999999");
        }
        //橘色
        return Color.parseColor("#ffff6400");
    }

    /*（0草稿，1待审核，2审核通过，3审核不通过，4即将开始，5募集期，6停止交易，7产品封闭期，8还款成功，9募集失败)*/

    /**
     * 根据状态显示
     *
     * @param data
     */
    public CharSequence getStateString(ManagementMoney data) {

        if (TextUtils.isEmpty(data.getStatus())) {
            return "";
        }

        switch (data.getStatus()) {
            case "4":
                return mContext.getString(R.string.management_money_state_4);
            case "5":
                return mContext.getString(R.string.management_money_state_5);
            case "6":
                return mContext.getString(R.string.management_money_state_6);
            case "7":
                if (data.isIncomeFlag()){
                    return mContext.getString(R.string.management_money_state_jxz);
                }else {
                    return mContext.getString(R.string.management_money_state_7);
                }
            case "8":
                return mContext.getString(R.string.management_money_state_8);
            case "9":
                return mContext.getString(R.string.management_money_state_9);
        }

        return "";

    }


}
