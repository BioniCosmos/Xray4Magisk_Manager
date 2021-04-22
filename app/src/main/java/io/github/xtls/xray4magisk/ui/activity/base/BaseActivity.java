package io.github.xtls.xray4magisk.ui.activity.base;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import io.github.xtls.xray4magisk.R;
import io.github.xtls.xray4magisk.ConfigManager;
import io.github.xtls.xray4magisk.util.NavUtil;
import io.github.xtls.xray4magisk.util.theme.ThemeUtil;
import rikka.core.res.ResourcesKt;
import rikka.material.app.MaterialActivity;

public class BaseActivity extends MaterialActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String moduleVersionCode = ConfigManager.getModuleVersionCode();
        if ("".equals(moduleVersionCode)) {
            // module install check
            new AlertDialog.Builder(this)
                    .setMessage(R.string.need_install_module)
                    .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                        NavUtil.startURL(this, getString(R.string.module_repo_url));
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        } else {
            if (Integer.parseInt(moduleVersionCode) < Integer.parseInt(getString(R.string.min_module_version))) {
                // module version check
                new AlertDialog.Builder(this)
                        .setMessage(String.format(getString(R.string.need_update_module), getString(R.string.min_module_version)))
                        .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                            NavUtil.startURL(this, getString(R.string.module_repo_url));
                            finish();
                        })
                        .setCancelable(false)
                        .show();
            }
        }
    }

    @Override
    public void onApplyUserThemeResource(Resources.Theme theme, boolean isDecorView) {
        theme.applyStyle(ThemeUtil.getNightThemeStyleRes(this), true);
        theme.applyStyle(ThemeUtil.getColorThemeStyleRes(), true);
    }

    @Override
    public String computeUserThemeKey() {
        return ThemeUtil.getColorTheme() + ThemeUtil.getNightTheme(this);
    }

    @Override
    public void onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars();
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);

        window.getDecorView().post(() -> {
            if (window.getDecorView().getRootWindowInsets().getSystemWindowInsetBottom() >= Resources.getSystem().getDisplayMetrics().density * 40) {
                window.setNavigationBarColor(ResourcesKt.resolveColor(getTheme(), android.R.attr.navigationBarColor) & 0x00ffffff | -0x20000000);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.setNavigationBarContrastEnforced(false);
                }
            } else {
                window.setNavigationBarColor(Color.TRANSPARENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.setNavigationBarContrastEnforced(true);
                }
            }
        });
    }
}
