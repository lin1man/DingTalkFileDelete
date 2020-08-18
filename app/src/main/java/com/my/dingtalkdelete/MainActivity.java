package com.my.dingtalkdelete;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.my.dingtalkdelete.dingtalk.models.DentryInfo;
import com.my.dingtalkdelete.dingtalk.models.UserInfo;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static SoftReference<MainActivity> reference;
    private Context context;

    private TextView tvUserInfo;
    private ListView lvDentryInfo;
    private LinearLayout llEditBar;

    private UserInfo userInfo;
    private List<DentryInfo> dentryInfoAllList = new ArrayList<>();
    private List<DentryInfo> dentryInfoList = new ArrayList<>();
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();
    private boolean isSeletectAll = true;
    private DentryInfoAdapter dentryInfoAdapter;
    private Button btnGetDentryInfo;
    private Button btnGetCacheDentryInfo;
    private Button btnClearDentryInfo;
    private Button btnDeleteDentryInfo;
    private Button btnShowAll;
    private boolean isFilterCreateEmail = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        initView();
        dentryInfoAdapter = new DentryInfoAdapter(this, dentryInfoList, stateCheckedMap);
        lvDentryInfo.setAdapter(dentryInfoAdapter);
        setOnGetUserInfoClickListener();
        setOnListViewItemClickListener();
        setOnDentryButtonClickListener();
    }

    private void initView() {
        tvUserInfo = findViewById(R.id.tvUserInfo);
        reference = new SoftReference<>(this);

        lvDentryInfo = findViewById(R.id.lvDentryInfo);
        lvDentryInfo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        llEditBar = findViewById(R.id.ll_edit_bar);

        findViewById(R.id.btnSeletectAll).setOnClickListener(this);
        findViewById(R.id.btnSeletectAllRec).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);

        btnGetDentryInfo = findViewById(R.id.btnGetDentryInfo);
        btnGetCacheDentryInfo = findViewById(R.id.btnGetCacheDentryInfo);
        btnClearDentryInfo = findViewById(R.id.btnClearDentryInfo);
        btnDeleteDentryInfo = findViewById(R.id.btnDeleteDentryInfo);
        btnShowAll = findViewById(R.id.btnShowAll);
    }

    private void setOnGetUserInfoClickListener() {
        Button btnGetUserInfo = findViewById(R.id.btnGetUserInfo);
        btnGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplicationContext().sendBroadcast(new Intent(GlobalConfig.DINGTALK_GET_USER_INFO));
            }
        });
    }

    private void setOnDentryButtonClickListener() {
        btnGetDentryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDentryInfoList();
                setDentryInfoCacheStatus(false);//在没有获取到文件列表时禁止相关按键
                Toast("获取文件列表中");
                context.sendBroadcast(new Intent(GlobalConfig.DINGTALK_CLEAR_CACHE_DENTRY_INFO));//获取文件列表前先删除缓存
                context.sendBroadcast(new Intent(GlobalConfig.DINGTALK_GET_DENTRY_INFO));
            }
        });
        btnGetCacheDentryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDentryInfoList();
                context.sendBroadcast(new Intent(GlobalConfig.DINGTALK_GET_CACHE_DENTRY_INFO));
            }
        });
        btnClearDentryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDentryInfoList();
                context.sendBroadcast(new Intent(GlobalConfig.DINGTALK_CLEAR_CACHE_DENTRY_INFO));
            }
        });
        btnDeleteDentryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<DentryInfo> seletedDentry = new ArrayList<>();
                for (int i = 0; i < dentryInfoList.size(); i++) {
                    if (stateCheckedMap.get(i)) {
                        seletedDentry.add(dentryInfoList.get(i));
                    }
                }
                if (seletedDentry.size() < 1) {
                    Toast("请选择需要删除的文件");
                }else {
                    btnDeleteDentryInfo.setEnabled(false);
                    Intent intent = new Intent(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO);
                    intent.putParcelableArrayListExtra(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_KEY, seletedDentry);
                    context.sendBroadcast(intent);
                }
            }
        });
        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFilterCreateEmail) {
                    isFilterCreateEmail = false;
                    btnShowAll.setText(getText(R.string.txtShowAll));
                } else {
                    isFilterCreateEmail = true;
                    btnShowAll.setText(getText(R.string.txtShowUser));
                }
                notifyDentryInfoChangeed();
            }
        });
    }

    private void setOnListViewItemClickListener() {
        lvDentryInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateCheckBoxStatus(view, position);
            }
        });
        lvDentryInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                llEditBar.setVisibility(View.VISIBLE);
                dentryInfoAdapter.setShowCheckBox(true);
                dentryInfoAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public static MainActivity getRef() {
        if (reference != null) {
            return reference.get();
        }
        return null;
    }

    private void notifyDentryInfoChangeed() {
        if (userInfo == null) {
            Toast("请先获取用户信息以过滤用户上传文件");
            return;
        }
        dentryInfoList.clear();
        if (isFilterCreateEmail) {
            String strUserId = "" + userInfo.openId;
            for (DentryInfo info : dentryInfoAllList) {
                if (info.creatorEmail.equals(strUserId) || info.ownerId.equals(strUserId)) {
                    dentryInfoList.add(info);
                }
            }
        } else {
            dentryInfoList.addAll(dentryInfoAllList);
        }
        dentryInfoAdapter.notifyDataSetChanged();
    }

    private void clearDentryInfoList() {
        dentryInfoList.clear();
        dentryInfoAdapter.notifyDataSetChanged();
    }

    public void setDentryInfoData(List<DentryInfo> data) {
        dentryInfoAllList.clear();
        if (data != null) {
            dentryInfoAllList.addAll(data);
        }
        Log.d(utils.TAG, "DentryInfoData.size() = " + dentryInfoAllList.size());
        notifyDentryInfoChangeed();
    }

    public void deleteDentryInfo(List<DentryInfo> data) {
        if (data == null) {
            Log.d(utils.TAG, "deleteDentryInfo end");
            btnDeleteDentryInfo.setEnabled(true);
            return;
        }
        dentryInfoAllList.removeAll(data);
        notifyDentryInfoChangeed();
        for (DentryInfo info : data) {
            Log.d(utils.TAG, "deleteDentryInfo:" + info.name);
        }
    }

    public void SetUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        String strUserInfo = userInfo.userName + "(" + userInfo.openId + ")";
        tvUserInfo.setText(strUserInfo);
    }

    public void Toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                cancel();
                break;
            case R.id.btnSeletectAll:
                selectAll();
                break;
            case R.id.btnSeletectAllRec:
                inverse();
                break;
        }
    }

    private void setCheckedMap(boolean selectAll) {
        for (int i = 0; i < dentryInfoList.size(); i++) {
            stateCheckedMap.put(i, selectAll);
            lvDentryInfo.setItemChecked(i, selectAll);
        }
    }

    private void cancel() {
        setCheckedMap(false);
        llEditBar.setVisibility(View.GONE);
        dentryInfoAdapter.setShowCheckBox(false);
        dentryInfoAdapter.notifyDataSetChanged();
    }

    private void inverse() {
        for (int i = 0; i < dentryInfoList.size(); i++) {
            if (stateCheckedMap.get(i)) {
                stateCheckedMap.put(i, false);
            } else {
                stateCheckedMap.put(i, true);
            }
            lvDentryInfo.setItemChecked(i, stateCheckedMap.get(i));
        }
        dentryInfoAdapter.notifyDataSetChanged();
    }

    private void selectAll() {
        if (isSeletectAll) {
            setCheckedMap(true);
            isSeletectAll = false;
        } else {
            setCheckedMap(false);
            isSeletectAll = true;
        }
        dentryInfoAdapter.notifyDataSetChanged();
    }

    private void updateCheckBoxStatus(View view, int position) {
        DentryInfoAdapter.ViewHolder holder = (DentryInfoAdapter.ViewHolder) view.getTag();
        holder.checkBox.toggle();
        lvDentryInfo.setItemChecked(position, holder.checkBox.isChecked());
        stateCheckedMap.put(position, holder.checkBox.isChecked());
        dentryInfoAdapter.notifyDataSetChanged();
    }

    public void setDentryInfoCacheStatus(boolean isCache) {
        btnGetCacheDentryInfo.setEnabled(isCache);
        btnClearDentryInfo.setEnabled(isCache);
        btnDeleteDentryInfo.setEnabled(isCache);
        btnShowAll.setEnabled(isCache);
    }
}
