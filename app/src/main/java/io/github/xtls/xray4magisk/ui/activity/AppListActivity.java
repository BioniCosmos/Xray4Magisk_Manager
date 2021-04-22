package io.github.xtls.xray4magisk.ui.activity;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.ActionBar;
import io.github.xtls.xray4magisk.R;
import io.github.xtls.xray4magisk.ui.activity.base.BaseActivity;
import io.github.xtls.xray4magisk.databinding.ActivityAppListBinding;

import io.github.xtls.xray4magisk.util.LinearLayoutManagerFix;
import rikka.recyclerview.RecyclerViewKt;

public class AppListActivity extends BaseActivity {
    private SearchView searchView;

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
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManagerFix(this));
        RecyclerViewKt.addFastScroller(binding.recyclerView,binding.recyclerView);
        RecyclerViewKt.fixEdgeEffect(binding.recyclerView, false, true);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> scopeAdapter.refresh(true));
    }
}
