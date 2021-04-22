package io.github.xtls.xray4magisk.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.checkbox.MaterialCheckBox;
import io.github.xtls.xray4magisk.App;
import io.github.xtls.xray4magisk.ConfigManager;
import io.github.xtls.xray4magisk.R;
import io.github.xtls.xray4magisk.ui.activity.AppListActivity;
import io.github.xtls.xray4magisk.util.GlideApp;
import io.github.xtls.xray4magisk.util.ProxyListUtil;
import rikka.widget.switchbar.SwitchBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ApplistAdapter extends RecyclerView.Adapter<ApplistAdapter.ViewHolder> implements Filterable {

    private final AppListActivity activity;
    private final PackageManager pm;
    private final SharedPreferences preferences;
    private final HashSet<ApplicationWithEquals> checkedList = new HashSet<>();
    private final List<AppInfo> searchList = new ArrayList<>();
    private final SwitchBar.OnCheckedChangeListener switchBarOnCheckedChangeListener = new SwitchBar.OnCheckedChangeListener() {
        @Override
        public boolean onCheckedChanged(SwitchBar view, boolean isChecked) {
            // ProxyListUtil
            // TODO: 2021/4/21
        }
    };
    private List<AppInfo> showList = new ArrayList<>();
    private ApplicationInfo selectedInfo;
    private boolean refreshing = false;
    private boolean whiteList = true;

    public ApplistAdapter(AppListActivity activity) {
        this.activity = activity;
        preferences = App.getPreferences();
        pm = activity.getPackageManager();
        refresh(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(v);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View root;
        ImageView appIcon;
        TextView appName;
        TextView appDescription;
        MaterialCheckBox checkBox;

        ViewHolder(View itemView){
            super(itemView);
            root=itemView.findViewById(R.id.item_root);
            appIcon=itemView.findViewById(R.id.app_icon);
            appName=itemView.findViewById(R.id.app_name);
            appDescription=itemView.findViewById(R.id.description);
            checkBox=itemView.findViewById(R.id.checkbox);
            checkBox.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.root.setAlpha(whiteList ? 1.0f : .5f);
        AppInfo appInfo = showList.get(position);
        boolean android = appInfo.packageName.equals("android");
        CharSequence appName;
        int userId = appInfo.applicationInfo.uid / 100000;
        if (userId != 0) {
            appName = String.format("%s (%s)", appInfo.label, userId);
        } else {
            appName = android ? activity.getString(R.string.android_framework) : appInfo.label;
        }
        holder.appName.setText(appName);
        GlideApp.with(holder.appIcon)
                .load(appInfo.packageInfo)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.appIcon.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        holder.appIcon.setImageDrawable(pm.getDefaultActivityIcon());
                    }
                });
        SpannableStringBuilder sb = new SpannableStringBuilder(android ? "" : activity.getString(R.string.app_description, appInfo.packageName, appInfo.packageInfo.versionName));
        holder.appDescription.setVisibility(View.VISIBLE);
        if (android) {
            holder.appDescription.setVisibility(View.GONE);
        }
        holder.appDescription.setText(sb);

        holder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            activity.getMenuInflater().inflate(R.menu.menu_app_item, menu);
            if (userId != 0) {
                menu.removeItem(R.id.menu_app_store);
                menu.removeItem(R.id.menu_app_info);
            }
            if (android) {
                menu.removeItem(R.id.menu_app_store);
                menu.removeItem(R.id.menu_app_info);
            }
        });

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(checkedList.contains(appInfo.application));

        holder.checkbox.setOnCheckedChangeListener((v, isChecked) -> onCheckedChange(v, isChecked, appInfo));
        holder.itemView.setOnClickListener(v -> {
            if (enabled) holder.checkbox.toggle();
        });
        holder.itemView.setOnLongClickListener(v -> {
            selectedInfo = appInfo.applicationInfo;
            return false;
        });
    }

    @Override
    public long getItemId(int position) {
        PackageInfo info = showList.get(position).packageInfo;
        return (info.packageName + "!" + info.applicationInfo.uid / 100000).hashCode();
    }

    @Override
    public Filter getFilter() {
        return new ApplicationFilter();
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }


    public void refresh(boolean force) {
        synchronized (this) {
            if (refreshing) {
                return;
            }
            refreshing = true;
        }
        if (!force) {
            activity.binding.progress.setVisibility(View.INVISIBLE);
            activity.binding.progress.setIndeterminate(true);
            activity.binding.progress.setVisibility(View.VISIBLE);
        }
        activity.binding.masterSwitch.setOnCheckedChangeListener(null);
        // TODO: 2021/4/21
        // whiteList = ProxyListUtil.isWhiteListMode
        // activity.binding.masterSwitch.setChecked(whiteList);
        activity.binding.masterSwitch.setOnCheckedChangeListener(switchBarOnCheckedChangeListener);
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> loadApps(force));
    }

    private class ApplicationFilter extends Filter {

        private boolean lowercaseContains(String s, String filter) {
            return !TextUtils.isEmpty(s) && s.toLowerCase().contains(filter);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint.toString().isEmpty()) {
                showList = searchList;
            } else {
                ArrayList<AppInfo> filtered = new ArrayList<>();
                String filter = constraint.toString().toLowerCase();
                for (AppInfo info : searchList) {
                    if (lowercaseContains(info.label.toString(), filter)
                            || lowercaseContains(info.packageName, filter)) {
                        filtered.add(info);
                    }
                }
                showList = filtered;
            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }

    public static class AppInfo {
        public PackageInfo packageInfo;
        public ApplicationWithEquals application;
        public ApplicationInfo applicationInfo;
        public String packageName;
        public CharSequence label = null;
    }

    public static class ApplicationWithEquals extends Application{
        public ApplicationWithEquals(String packageName,int userId){
            this.packageName = packageName;
            this.userId = userId;
        }
        public ApplicationWithEquals(Application application) {
            packageName = application.packageName;
            userId = application.userId;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof Application)) {
                return false;
            }
            return packageName.equals(((Application) obj).packageName) && userId == ((Application) obj).userId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(packageName, userId);
        }
    }

    private void loadApps(Context context) {
        List<PackageInfo> appList = AppHelper.getAppList(context);
        checkedList.clear();
        searchList.clear();
        showList.clear();

        checkedList.addAll(ConfigManager.getProxyList());
        HashSet<ApplicationWithEquals> installedList = new HashSet<>();
        boolean emptyCheckedList = checkedList.isEmpty();
        for (PackageInfo info : appList) {
            int uid = info.applicationInfo.uid;
            if (info.packageName.equals("android") && uid / 100000 != 0) {
                continue;
            }

            ApplicationWithEquals application = new ApplicationWithEquals(info.packageName, uid / 100000);

            installedList.add(application);

            if (scopeList != null && scopeList.contains(info.packageName)) {
                recommendedList.add(application);
                if (emptyCheckedList) {
                    checkedList.add(application);
                }
            }

            if (shouldHideApp(info, application)) {
                continue;
            }

            AppInfo appInfo = new AppInfo();
            appInfo.packageInfo = info;
            appInfo.label = getAppLabel(info.applicationInfo, pm);
            appInfo.application = application;
            appInfo.packageName = info.packageName;
            appInfo.applicationInfo = info.applicationInfo;
            searchList.add(appInfo);
        }
        checkedList.retainAll(installedList);
        if (emptyCheckedList) {
            ConfigManager.setModuleScope(modulePackageName, checkedList);
        }
        showList = sortApps(searchList);
        synchronized (this) {
            refreshing = false;
        }
        activity.onDataReady();
    }
}
