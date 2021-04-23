package io.github.xtls.xray4magisk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;
import io.github.xtls.xray4magisk.App;
import io.github.xtls.xray4magisk.ConfigManager;
import io.github.xtls.xray4magisk.R;
import io.github.xtls.xray4magisk.databinding.ActivityMainBinding;
import io.github.xtls.xray4magisk.ui.activity.base.BaseActivity;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //binding.status.setOnClickListener();
        binding.apps.setOnClickListener(new StartActivityListener(AppListActivity.class));
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