package io.github.xtls.xray4magisk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;
import io.github.xtls.xray4magisk.ConfigManager;
import io.github.xtls.xray4magisk.R;
import io.github.xtls.xray4magisk.databinding.ActivityMainBinding;
import io.github.xtls.xray4magisk.ui.activity.base.BaseActivity;
import io.github.xtls.xray4magisk.util.GlideHelper;
import rikka.core.res.ResourcesKt;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.proxy.setOnClickListener(v -> {
            if (ConfigManager.isProxying()) {
                ConfigManager.stopProxy();
            } else {
                ConfigManager.startProxy();
            }
            setProxyCard();
        });
        binding.apps.setOnClickListener(new StartActivityListener(AppListActivity.class));
        binding.about.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.app_version)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    })
                    .show();
        });
        Glide.with(binding.appIcon)
                .load(GlideHelper.wrapApplicationInfoForIconLoader(getApplicationInfo()))
                .into(binding.appIcon);
        if ("".equals(ConfigManager.getModuleVersionCode(getString(R.string.module_dir_name)))) {
            int cardBackgroundColor;
            cardBackgroundColor = ResourcesKt.resolveColor(getTheme(), R.attr.colorWarning);
            binding.statusTitle.setText(R.string.disabled);
            binding.statusIcon.setImageResource(R.drawable.ic_info);
            binding.statusSummary.setText(R.string.install_required);
            binding.proxy.setCardBackgroundColor(cardBackgroundColor);
        } else {
            setProxyCard();
        }
    }

    private void setProxyCard() {
        int cardBackgroundColor;
        if (ConfigManager.isProxying()) {
            cardBackgroundColor = ResourcesKt.resolveColor(getTheme(), R.attr.colorAccent);
            binding.statusTitle.setText(R.string.enabled);
            binding.statusIcon.setImageResource(R.drawable.ic_check_circle);
            binding.statusSummary.setText(ConfigManager.getModuleVersion(getString(R.string.module_dir_name)));
        } else {
            binding.statusTitle.setText(R.string.disabled);
            cardBackgroundColor = ResourcesKt.resolveColor(getTheme(), R.attr.colorInactive);
            binding.statusIcon.setImageResource(R.drawable.ic_info);
            binding.statusSummary.setText(ConfigManager.getModuleVersion(getString(R.string.module_dir_name)));
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
}
