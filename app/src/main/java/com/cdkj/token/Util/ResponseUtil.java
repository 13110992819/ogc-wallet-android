package com.cdkj.token.Util;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.token.model.CoinModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lei on 2018/2/8.
 */

public class ResponseUtil {

    /**
     * 根据币种配置筛选结果
     * @param object
     * @return
     */
    public static Object screeningDataWithConfig(Object object){
        // 允许配置的Coin
        List<String> coinTypeList = Arrays.asList(MyConfig.COIN_TYPE);

        if (object instanceof CoinModel){

            // 筛选配置允许的Coin账户
            List<CoinModel.AccountListBean> list = new ArrayList<>();

            CoinModel model = (CoinModel) object;

            for (CoinModel.AccountListBean bean : model.getAccountList()){
                if (coinTypeList.contains(bean.getCurrency())){
                    list.add(bean);
                }
            }

            return list;

        }

        return null;

    }

}
