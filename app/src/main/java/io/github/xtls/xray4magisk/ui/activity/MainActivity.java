package io.github.xtls.xray4magisk.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;
import io.github.xtls.xray4magisk.R;
import io.github.xtls.xray4magisk.databinding.ActivityMainBinding;
import io.github.xtls.xray4magisk.ui.activity.base.BaseActivity;
import io.github.xtls.xray4magisk.util.GlideHelper;
import io.github.xtls.xray4magisk.util.module.ProxyUtil;
import rikka.core.res.ResourcesKt;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.proxy.setOnClickListener(v -> {
            if (BaseActivity.isProxying) {
                ProxyUtil.stop();
                setProxyCard("disabled");
                BaseActivity.isProxying=false;
            } else {
                ProxyUtil.start();
                setProxyCard("enabled");
                BaseActivity.isProxying=true;
            }
        });
        binding.apps.setOnClickListener(new StartActivityListener(AppListActivity.class));
        binding.about.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setMessage(R.string.app_version)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                })
                .show());
        Glide.with(binding.appIcon)
                .load(GlideHelper.wrapApplicationInfoForIconLoader(getApplicationInfo()))
                .into(binding.appIcon);
        if ("".equals(MODULE_VERSION_CODE)) {
            setProxyCard("");
        } else if (BaseActivity.isProxying) {
            setProxyCard("enabled");
            binding.appsSummary.setText(String.format(getString(R.string.app_count_in_list), UIDS.size(),whiteListMode?getString(R.string.whitelist):getString(R.string.blacklist)));
        } else {
            setProxyCard("disabled");
            binding.appsSummary.setText(String.format(getString(R.string.app_count_in_list), UIDS.size(),whiteListMode?getString(R.string.whitelist):getString(R.string.blacklist)));
        }
    }

    private void setProxyCard(String status) {
        int cardBackgroundColor;
        switch (status) {
            case "enabled":
                cardBackgroundColor = ResourcesKt.resolveColor(getTheme(), R.attr.colorAccent);
                binding.statusTitle.setText(R.string.enabled);
                binding.statusIcon.setImageResource(R.drawable.ic_check_circle);
                binding.statusSummary.setText(MODULE_VERSION);
                break;
            case "disabled":
                binding.statusTitle.setText(R.string.disabled);
                cardBackgroundColor = ResourcesKt.resolveColor(getTheme(), R.attr.colorInactive);
                binding.statusIcon.setImageResource(R.drawable.ic_error);
                binding.statusSummary.setText(MODULE_VERSION);
                break;
            default:
                cardBackgroundColor = ResourcesKt.resolveColor(getTheme(), R.attr.colorWarning);
                binding.statusTitle.setText(R.string.disabled);
                binding.statusIcon.setImageResource(R.drawable.ic_info);
                binding.statusSummary.setText(R.string.install_required);
        }
        binding.proxy.setCardBackgroundColor(cardBackgroundColor);
    }

    private class StartActivityListener implements View.OnClickListener {
        Class<?> clazz;

        StartActivityListener(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, clazz);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.appsSummary.setText(String.format(getString(R.string.app_count_in_list), UIDS.size(),whiteListMode?getString(R.string.whitelist):getString(R.string.blacklist)));
    }
}
