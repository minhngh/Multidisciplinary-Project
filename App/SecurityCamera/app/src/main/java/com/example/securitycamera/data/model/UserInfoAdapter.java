package com.example.securitycamera.data.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.securitycamera.R;

import java.util.List;

public class UserInfoAdapter extends BaseAdapter
{
    private Context context;
    private int layout;
    private List<UserInfo> arrUserInfo;

    public UserInfoAdapter(Context context, int layout, List<UserInfo> arrUserInfo) {
        this.context = context;
        this.layout = layout;
        this.arrUserInfo = arrUserInfo;
    }

    @Override
    public int getCount() {
        return arrUserInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);

        TextView txtType = (TextView) convertView.findViewById(R.id.textViewType);
        TextView txtTime = (TextView) convertView.findViewById(R.id.textViewTime);
        ImageView imgUser = (ImageView) convertView.findViewById(R.id.imageViewUser);

        UserInfo userInfo = arrUserInfo.get(position);

        txtType.setText(userInfo.getType());
        txtTime.setText(userInfo.getTime());
        imgUser.setImageResource(userInfo.getImageId());
        return convertView;
    }
}