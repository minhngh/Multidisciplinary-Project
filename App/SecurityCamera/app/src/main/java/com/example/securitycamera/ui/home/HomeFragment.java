package com.example.securitycamera.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.securitycamera.R;
import com.example.securitycamera.ui.login.LoginActivity;
import com.example.securitycamera.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView doorStateTv;
    private TextView doorTimeTv;
    private ImageView doorIv;
    private androidx.cardview.widget.CardView turnOffspeaker;
    private ImageButton reloadDoorStateIb;
    private ImageView logoutIv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);

        homeViewModel.getDoorState().observe(getViewLifecycleOwner(), doorState -> {
            if (doorState.isOpen()){
                doorIv.setImageResource(R.drawable.ic_opened_door_aperture);
                doorStateTv.setText("OPEN");
            }
            else{
                doorIv.setImageResource(R.drawable.ic_closed_filled_rectangular_door);
                doorStateTv.setText("CLOSE");
            }
        });

        logoutIv.setOnClickListener(v -> {
            homeViewModel.logout();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
//        homeViewModel.setDoorState(false);
    }
    private void findView(View view){
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        doorStateTv = view.findViewById(R.id.tv_door_state);
        doorTimeTv = view.findViewById(R.id.tv_door_time);
        doorIv = view.findViewById(R.id.iv_door);
        reloadDoorStateIb = view.findViewById(R.id.ib_reload_door_state);
        turnOffspeaker =  view.findViewById(R.id.fr_warning);
        logoutIv = view.findViewById(R.id.iv_logout);

    }
}