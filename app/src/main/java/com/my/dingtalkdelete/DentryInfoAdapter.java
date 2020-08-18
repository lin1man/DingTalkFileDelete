package com.my.dingtalkdelete;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my.dingtalkdelete.dingtalk.models.DentryInfo;

import java.util.List;


public class DentryInfoAdapter extends BaseAdapter {
    private Context mContext;
    private List<DentryInfo> data;
    private ViewHolder holder;
    private boolean isShowCheckBox = false;
    private SparseBooleanArray checkedMap = new SparseBooleanArray();

    public DentryInfoAdapter(Context context, List<DentryInfo> data, SparseBooleanArray statecheckedMap) {
        mContext = context;
        this.data = data;
        this.checkedMap = statecheckedMap;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < 0 || position >= data.size()) {
            return null;
        }
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.dentry_info_item, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox = convertView.findViewById(R.id.chb_select);
        holder.tvData = convertView.findViewById(R.id.tvDentryData);
        checkBoxShowOrHide();

        DentryInfo dentryInfo = data.get(position);
        String strInfo = "Name:" + dentryInfo.name + "\nCreatorID:" + dentryInfo.creatorEmail + " title:" + dentryInfo.title;
        holder.tvData.setText(strInfo);
        holder.checkBox.setChecked(checkedMap.get(position));

        return convertView;
    }
    public class ViewHolder {
        public TextView tvData;
        public AppCompatCheckBox checkBox;
    }

    private void checkBoxShowOrHide() {
        if (isShowCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
    }
}
