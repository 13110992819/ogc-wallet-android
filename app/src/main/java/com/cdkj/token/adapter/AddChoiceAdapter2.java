package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.CoinModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.token.utils.LocalCoinDBUtils.getCoinENameWithCurrency;
import static com.cdkj.token.utils.LocalCoinDBUtils.getCoinWatermarkWithCurrency;


/**
 * Created by cdkj on 2018/5/25.
 */

public class AddChoiceAdapter2 extends BaseQuickAdapter<CoinModel.AccountListBean, BaseViewHolder> {


    public AddChoiceAdapter2(@Nullable List<CoinModel.AccountListBean> data) {
        super(R.layout.item_add_choice, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinModel.AccountListBean item) {

        helper.setText(R.id.tv_name, item.getCurrency() + "--" + getCoinENameWithCurrency(item.getCurrency()));

        ImageView ivCoin = helper.getView(R.id.iv_watermark);
        ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 1), ivCoin);

        LinearLayout llChoice = helper.getView(R.id.linLayout_choose);
        ImageView ivChoice = helper.getView(R.id.iv_choice);
        llChoice.setOnClickListener(view -> {

            if (TextUtils.equals(ivChoice.getTag().toString(), "false")) {
                ivChoice.setBackgroundResource(R.mipmap.choice_confirm);
                ivChoice.setTag("true");
            } else {
                ivChoice.setBackgroundResource(R.mipmap.choice_cancel);
                ivChoice.setTag("false");
            }

        });
    }
}
