package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.LocalCoinBill;
import com.cdkj.token.utils.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_MMddHHmm;
import static com.cdkj.token.utils.AccountUtil.ETHSCALE;
import static com.cdkj.token.utils.CoinUtil.getMoneyStateByState;
import static com.cdkj.token.utils.CoinUtil.getStataIconByState;

/**
 * 去中心钱包流水
 * Created by lei on 2017/10/25.
 */

public class CoinDetailsListAdapter extends BaseQuickAdapter<LocalCoinBill, BaseViewHolder> {

    public CoinDetailsListAdapter(@Nullable List<LocalCoinBill> data) {
        super(R.layout.item_local_coin_bill, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalCoinBill item) {
        if (item == null) return;

        helper.setText(R.id.tv_bill_hash, getShowHashText(item.getTxHash()));
        helper.setText(R.id.tv_date, DateUtil.formatStringData(item.getTransDatetime(), DATE_MMddHHmm));
        helper.setText(R.id.tv_amount, getMoneyStateByState(item.getDirection()) + AccountUtil.amountFormatUnitForShow(item.getValue(), ETHSCALE));
        helper.setImageResource(R.id.img_state, getStataIconByState(item.getDirection()));
    }

    /**
     * 获取哈希值显示格式
     *
     * @param hash
     * @return
     */
    private String getShowHashText(String hash) {
        if (TextUtils.isEmpty(hash)) {
            return "";
        }

        if (hash.length() > 12) {
            return hash.substring(0, 6) + "..." + hash.substring(hash.length() - 6, hash.length());
        }

        return hash;
    }
}