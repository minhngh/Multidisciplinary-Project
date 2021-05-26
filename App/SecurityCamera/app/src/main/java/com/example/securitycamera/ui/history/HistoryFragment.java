package com.example.securitycamera.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.securitycamera.R;
import com.example.securitycamera.data.model.UserInfo;
import com.example.securitycamera.data.model.UserInfoAdapter;
import com.example.securitycamera.ui.MainActivity;
import com.example.securitycamera.viewmodel.HistoryViewModel;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private HistoryViewModel dashboardViewModel;
    ListView listViewHistory;
    TextView textViewDateTime;
    ArrayList<UserInfo> userInfoList;
    UserInfoAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        textViewDateTime = (TextView) root.findViewById(R.id.textViewDateTime);
        listViewHistory = (ListView) root.findViewById(R.id.listViewHistory);

        userInfoList = new ArrayList<>();
        userInfoList.add(new UserInfo("Guest", "13:50", R.drawable.nam2));
        userInfoList.add(new UserInfo("Guest", "9:12", R.drawable.nu1));

        adapter = new UserInfoAdapter(requireContext(), R.layout.user_info_item, userInfoList);

        listViewHistory.setAdapter(adapter);



        return root;
    }
}