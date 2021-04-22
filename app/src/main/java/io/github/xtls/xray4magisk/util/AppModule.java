package io.github.xtls.xray4magisk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
// import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
// import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.caverock.androidsvg.SVG;
import io.github.xtls.xray4magisk.R;
import io.github.xtls.xray4magisk.util.svg.ExternalFileResolver;
import io.github.xtls.xray4magisk.util.svg.SvgDecoder;
import io.github.xtls.xray4magisk.util.svg.SvgDrawableTranscoder;
import me.zhanghai.android.appiconloader.glide.AppIconModelLoader;

import java.io.InputStream;

@GlideModule
public class AppModule extends AppGlideModule {
    @Override
    public void registerComponents(Context context, @NonNull Glide glide, Registry registry) {
        int iconSize = context.getResources().getDimensionPixelSize(R.dimen.app_icon_size);
        registry.prepend(PackageInfo.class, Bitmap.class, new AppIconModelLoader.Factory(iconSize,
                context.getApplicationInfo().loadIcon(context.getPackageManager()) instanceof AdaptiveIconDrawable, context));
        // OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(App.getOkHttpClient());
        // registry.replace(GlideUrl.class, InputStream.class, factory);
        SVG.registerExternalFileResolver(new ExternalFileResolver());
        registry.register(SVG.class, Drawable.class, new SvgDrawableTranscoder(context))
                .append(InputStream.class, SVG.class, new SvgDecoder());
    }
}

