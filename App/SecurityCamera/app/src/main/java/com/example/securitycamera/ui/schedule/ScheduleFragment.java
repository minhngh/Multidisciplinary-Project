package com.example.securitycamera.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.securitycamera.R;
import com.example.securitycamera.viewmodel.ScheduleViewModel;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel scheduleViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scheduleViewModel =
                new ViewModelProvider(this).get(ScheduleViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        final TextView textView = rootView.findViewById(R.id.text_schedule);

        scheduleViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.switch_test).setOnClickListener(view1 -> {
            Toast helloToast = Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT);
            helloToast.show();
        });
    }
}