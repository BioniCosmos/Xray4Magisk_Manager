package io.github.xtls.xray4magisk.ui.activity;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import com.google.android.material.snackbar.Snackbar;
import io.github.xtls.xray4magisk.R;
import io.github.xtls.xray4magisk.adapters.AppListAdapter;
import io.github.xtls.xray4magisk.ui.activity.base.BaseActivity;
import io.github.xtls.xray4magisk.databinding.ActivityAppListBinding;

import io.github.xtls.xray4magisk.util.LinearLayoutManagerFix;
import rikka.recyclerview.RecyclerViewKt;

public class AppListActivity extends BaseActivity {
    private SearchView searchView;
    private AppListAdapter appListAdapter;

    private SearchView.OnQueryTextListener searchListener;
    public ActivityAppListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAppListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setAppBar(binding.appBar,binding.toolbar);
        binding.appBar.setRaised(true);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        ActionBar bar= getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(R.string.applist_title);
        bar.setSubtitle(R.string.applist_subtitle);
        appListAdapter = new AppListAdapter(this);
        appListAdapter.setHasStableIds(true);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManagerFix(this));
        RecyclerViewKt.addFastScroller(binding.recyclerView,binding.recyclerView);
        RecyclerViewKt.fixEdgeEffect(binding.recyclerView, false, true);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> appListAdapter.refresh(this));
    }

    public void onDataReady() {
        runOnUiThread(() -> {
            binding.progress.setIndeterminate(false);
            binding.swipeRefreshLayout.setRefreshing(false);
            String queryStr = searchView != null ? searchView.getQuery().toString() : "";
            appListAdapter.getFilter().filter(queryStr);
        });
    }

    public void makeSnackBar(String text, @Snackbar.Duration int duration) {
        Snackbar.make(binding.snackbar, text, duration).show();
    }

    public void makeSnackBar(@StringRes int text, @Snackbar.Duration int duration) {
        Snackbar.make(binding.snackbar, text, duration).show();
    }
}
