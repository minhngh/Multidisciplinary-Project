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

    private class ViewHolder
    {
        TextView txtType;
        TextView txtTime;
        ImageView imgUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            viewHolder.txtType = (TextView) convertView.findViewById(R.id.textViewType);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.textViewTime);
            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.imageViewUser);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        UserInfo userInfo = arrUserInfo.get(position);
        String time = userInfo.getTime();
        String[] time_infos = time.split(",");

        viewHolder.txtType.setText(userInfo.getType());
        viewHolder.txtTime.setText(time_infos[0]);
        viewHolder.imgUser.setImageBitmap(userInfo.getImage());
        return convertView;
    }
}