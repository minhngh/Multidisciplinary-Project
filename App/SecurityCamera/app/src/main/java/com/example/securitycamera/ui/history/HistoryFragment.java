package com.example.securitycamera.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.securitycamera.ui.main.MainActivity;
import com.example.securitycamera.viewmodel.HistoryViewModel;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    ListView listViewHistory;
    TextView textViewDateTime;
    ArrayList<UserInfo> userInfoList;
    UserInfoAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        historyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        findViews(root);

        userInfoList = new ArrayList<>();
        userInfoList.add(new UserInfo("Guest", "13:50", R.drawable.nam2));
        userInfoList.add(new UserInfo("Guest", "9:12", R.drawable.nu1));

        adapter = new UserInfoAdapter(requireContext(), R.layout.user_info_item, userInfoList);

        listViewHistory.setAdapter(adapter);

        listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UserInfoHistoryActivity.class);
                intent.putExtra("user_info", (Serializable) userInfoList.get(position));
                startActivity(intent);
            }
        });


        return root;
    }

    private void findViews(View root)
    {
        textViewDateTime = (TextView) root.findViewById(R.id.textViewDateTime);
        listViewHistory = (ListView) root.findViewById(R.id.listViewHistory);
    }

    private ArrayList<UserInfo> getImageHistory(String token)
    {
        return historyViewModel.getImageHistory(token);
    }
}