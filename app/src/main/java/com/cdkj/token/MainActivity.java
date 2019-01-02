package com.cdkj.token;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.databinding.ActivityMainBinding;
import com.cdkj.token.invest.InvestFragment;
import com.cdkj.token.trade.TradeFragment;
import com.cdkj.token.user.UserFragment;
import com.cdkj.token.wallet.WalletFragment2;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

@Route(path = CdRouteHelper.APPMAIN)
public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;

    public static final int INVEST = 0;
    public static final int TRADE = 1;
    public static final int WALLET = 2;
//    public static final int CONSULT = 1;
    public static final int USER = 3;
    private List<Fragment> fragments;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initListener();
        initViewPager();

        init();
    }


    private void init() {
        UIStatusBarHelper.setStatusBarDarkMode(this);

        setShowIndex(WALLET);
//        CoinListService.open(this);
    }

    /**
     * 初始化事件
     */
    private void initListener() {

//        mBinding.layoutMainBottom.llConsult.setOnClickListener(v -> {
//            setShowIndex(CONSULT);
//        });

        mBinding.layoutMainBottom.llInvest.setOnClickListener(v -> {
            setShowIndex(INVEST);
        });

        mBinding.layoutMainBottom.llTrade.setOnClickListener(v -> {
            setShowIndex(TRADE);
        });

        mBinding.layoutMainBottom.llWallet.setOnClickListener(v -> {
            setShowIndex(WALLET);
        });

        mBinding.layoutMainBottom.llMy.setOnClickListener(v -> {
            setShowIndex(USER);
        });

    }


    public void setTabIndex(int index) {
        setTabDark();
        switch (index) {
//            case CONSULT:
//                mBinding.layoutMainBottom.ivConsult.setImageResource(R.mipmap.main_consult_light);
//                mBinding.layoutMainBottom.tvConsult.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
//                break;

            case INVEST:
                mBinding.layoutMainBottom.ivInvest.setImageResource(R.mipmap.main_invest_light);
                mBinding.layoutMainBottom.tvInvest.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case TRADE:
                mBinding.layoutMainBottom.ivTrade.setImageResource(R.mipmap.main_trade_light);
                mBinding.layoutMainBottom.tvTrade.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case WALLET:
                mBinding.layoutMainBottom.ivWallet.setImageResource(R.mipmap.main_wallet_light);
                mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case USER:
                mBinding.layoutMainBottom.ivMy.setImageResource(R.mipmap.main_my_light);
                mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
        }

    }

    private void setTabDark() {
//        mBinding.layoutMainBottom.ivConsult.setImageResource(R.mipmap.main_consult_dark);
//        mBinding.layoutMainBottom.tvConsult.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivInvest.setImageResource(R.mipmap.main_invest_dark);
        mBinding.layoutMainBottom.tvInvest.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivTrade.setImageResource(R.mipmap.main_trade_dark);
        mBinding.layoutMainBottom.tvTrade.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivWallet.setImageResource(R.mipmap.main_wallet_dark);
        mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivMy.setImageResource(R.mipmap.main_my_dark);
        mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mBinding.pagerMain.setPagingEnabled(false);//禁止左右切换

        //设置fragment数据
        fragments = new ArrayList<>();

        fragments.add(InvestFragment.getInstance());
        fragments.add(TradeFragment.getInstance());
        fragments.add(WalletFragment2.getInstance());
        fragments.add(UserFragment.getInstance());

        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());

        mBinding.pagerMain.setCurrentItem(0);
    }


    /**
     * 设置要显示的界面
     *
     * @param index
     */
    private void setShowIndex(int index) {
        if (index < 0 && index >= fragments.size()) {
            return;
        }
        mBinding.pagerMain.setCurrentItem(index, false);
        setTabIndex(index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = fragments.get(fragments.size() - 1);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        showDoubleWarnListen(getStrRes(R.string.exit_confirm), view -> {
            EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
            finish();
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        CoinListService.close(this);
    }


}
