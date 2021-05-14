package com.example.securitycamera.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.securitycamera.R;
import com.example.securitycamera.data.model.ScheduleContainer;
import com.example.securitycamera.viewmodel.ScheduleViewModel;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel scheduleViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scheduleViewModel =
                new ViewModelProvider(this).get(ScheduleViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        final TextView textView = rootView.findViewById(R.id.text_description);
        final Switch switchView = rootView.findViewById(R.id.switch_activeness);

        scheduleViewModel.getContainer().observe(getViewLifecycleOwner(), new Observer<ScheduleContainer>() {
            @Override
            public void onChanged(@Nullable ScheduleContainer cont) {
                if (cont == null) {
                    return;
                }

                for (int i = 0; i < cont.size(); ++i) {
                    textView.setText(cont.get(i).getDescription());
                    switchView.setChecked(cont.get(i).getActiveness());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ScheduleViewModel scheduleViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);

        view.findViewById(R.id.switch_activeness).setOnClickListener(view1 -> {
            Toast helloToast = Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT);
            helloToast.show();
        });
    }
}