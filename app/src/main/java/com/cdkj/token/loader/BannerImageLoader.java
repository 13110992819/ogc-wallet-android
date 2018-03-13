package com.cdkj.token.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.token.model.BannerModel;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        if (path instanceof BannerModel) {

            BannerModel banner = (BannerModel) path;

            Glide.with(context)
                    .load(MyConfig.IMGURL + banner.getPic())
                    .into(imageView);

            return;
        }

        if (path.toString().indexOf("http") != -1) {
            Glide.with(context)
                    .load(path.toString())
                    .into(imageView);
        } else {

            Glide.with(context)
                    .load(MyConfig.IMGURL + path.toString())
                    .into(imageView);
        }

    }
}
