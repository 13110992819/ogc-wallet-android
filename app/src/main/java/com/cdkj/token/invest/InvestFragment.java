package com.cdkj.token.invest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.InvestListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityRefreshMoneyManagerBinding;
import com.cdkj.token.find.product_application.management_money.BijiaBaoDetailsActivity;
import com.cdkj.token.find.product_application.management_money.MyInvestmentDetails;
import com.cdkj.token.model.InvestmentAmountModel;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/12/27.
 */

public class InvestFragment extends BaseLazyFragment {

    private ActivityRefreshMoneyManagerBinding mBinding;

    private RefreshHelper mRefreshHelper;

    // 投资总额
    private InvestmentAmountModel investment;

    private int tabPosition = 0;
    private String symbol = "BTC";

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static InvestFragment getInstance() {
        InvestFragment fragment = new InvestFragment();
        return fragment;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.activity_refresh_money_manager, null, false);

        initClickListener();
        initRefreshHelper();
        getUsrInvestAmount();
//        reflex(mBinding.tlWay);

        mRefreshHelper.onDefaluteMRefresh(true);

        return mBinding.getRoot();
    }

    private void initClickListener() {

        mBinding.tvMyInvesment.setOnClickListener(view -> MyInvestmentDetails.open(mActivity));

        mBinding.llVisible.setOnClickListener(view -> {
            if (mBinding.tvTotalInvest.getText().toString().equals("****** BTC")) {
                mBinding.tvTotalInvest.setText("≈ " + AmountUtil.transformFormatToString2(investment.getTotalInvest(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4) + " BTC");
                mBinding.ivEye.setImageResource(R.mipmap.eye_open_white);
            } else {
                mBinding.tvTotalInvest.setText("****** BTC");
                mBinding.ivEye.setImageResource(R.mipmap.eye_close_white);
            }
        });

        mBinding.tlWay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabPosition = tab.getPosition();

                symbol = tabPosition == 0 ? "BTC" : "USDT";
                mRefreshHelper.onDefaluteMRefresh(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    void initRefreshHelper() {
        mRefreshHelper = new RefreshHelper(mActivity, new BaseRefreshCallBack(mActivity) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerView;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return getListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getListRequest(pageindex, limit, isShowDialog);
            }
        });

        mRefreshHelper.init(RefreshHelper.LIMITE);
    }

    /**
     * 获取数据适配器
     *
     * @param listData
     * @return
     */
    public RecyclerView.Adapter getListAdapter(List listData) {

        InvestListAdapter investListAdapter = new InvestListAdapter(listData);

        investListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ManagementMoney managementMoney = investListAdapter.getItem(position);
            if (managementMoney == null) return;
            BijiaBaoDetailsActivity.open(mActivity, managementMoney.getCode());
        });

        return investListAdapter;
    }


    /**
     * 获取数据
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("status", "appDisplay");
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("language", SPUtilHelper.getLanguage());
        if (isShowDialog) {
            showLoadingDialog();
        }

        Call<BaseResponseModel<ResponseInListModel<ManagementMoney>>> call = RetrofitUtils.createApi(MyApi.class).getMoneyManageProductList("625510", StringUtils.getRequestJsonString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<ManagementMoney>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<ManagementMoney> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_product), 0);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 获取用户投资总额
     */
    public void getUsrInvestAmount() {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());

        Call<BaseResponseModel<InvestmentAmountModel>> call = RetrofitUtils.createApi(MyApi.class).getUserInvestAmount("625527", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<InvestmentAmountModel>(mActivity) {
            @Override
            protected void onSuccess(InvestmentAmountModel data, String SucMessage) {
                investment = data;
                mBinding.tvTotalInvest.setText("≈ " + AmountUtil.transformFormatToString2(investment.getTotalInvest(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4) + " BTC");
            }

            @Override
            protected void onFinish() {

            }
        });


    }

//    public void reflex(final TabLayout tabLayout){
//        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //拿到tabLayout的mTabStrip属性
//                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
//
//
//                    int dp10 = dip2px(tabLayout.getContext(), 10);
//
//                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
//                        View tabView = mTabStrip.getChildAt(i);
//
//                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
//                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
//                        mTextViewField.setAccessible(true);
//
//                        TextView mTextView = (TextView) mTextViewField.get(tabView);
//
//                        tabView.setPadding(0, 0, 0, 0);
//
//                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
//                        int width = 0;
//                        width = mTextView.getWidth();
//                        if (width == 0) {
//                            mTextView.measure(0, 0);
//                            width = mTextView.getMeasuredWidth();
//                        }
//
//                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
//                        params.width = width ;
//                        params.leftMargin = dp10;
//                        params.rightMargin = dp10;
//                        tabView.setLayoutParams(params);
//
//                        tabView.invalidate();
//                    }
//
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 根据手机分辨率从DP转成PX
//     * @param context
//     * @param dpValue
//     * @return
//     */
//    public static int dip2px(Context context, float dpValue) {
//        float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRefreshHelper != null) {
            mRefreshHelper.onDestroy();
        }
    }
}
