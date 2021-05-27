
package com.example.securitycamera.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
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
    private ImageButton reloadDoorStateIb;
    private ImageButton speakerIb;
    private TextView warningText;
    private ImageView logoutIv;
    private TextView instructText;
    private boolean mute = true;
    private RelativeLayout speakerArea;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        homeViewModel.checkDoorState();

        homeViewModel.getDoorState().observe(getViewLifecycleOwner(), doorState -> {
            if (doorState == null)
                return;
            if (doorState.isOpen()){
                doorIv.setImageResource(R.drawable.ic_opened_door_aperture);
                doorStateTv.setText("OPEN");

            }
            else{
                doorIv.setImageResource(R.drawable.ic_closed_filled_rectangular_door);
                doorStateTv.setText("CLOSE");
            }
            doorTimeTv.setText(doorState.getTime());
        });
//        homeViewModel.setDoorState(false);

        reloadDoorStateIb.setOnClickListener(v -> {
            homeViewModel.checkDoorState();
        });
        speakerIb.setOnClickListener(v ->{
            toggleSpeakerState();
        });

        logoutIv.setOnClickListener(v -> {
            homeViewModel.logout();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void toggleSpeakerState() {
        if (mute){
            speakerArea.setBackgroundColor(Color.parseColor("#EE4E4E"));
            speakerIb.setBackgroundColor(Color.parseColor("#EE4E4E"));
            warningText.setText("Detected a stranger opening the door");
            instructText.setText("Press here to turn off speaker");
            homeViewModel.unmute();
            mute = false;
        }
        else{
            speakerArea.setBackgroundColor(Color.parseColor("#FF03DAC5"));
            speakerIb.setBackgroundColor(Color.parseColor("#FF03DAC5"));
            warningText.setText("Everything is still good");
            instructText.setText("Press here to turn on speaker");
            homeViewModel.mute();
            mute = true;
        }
    }

    private void findView(View view){
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        doorStateTv = view.findViewById(R.id.tv_door_state);
        doorTimeTv = view.findViewById(R.id.tv_door_time);
        doorIv = view.findViewById(R.id.iv_door);
        reloadDoorStateIb = view.findViewById(R.id.ib_reload_door_state);
        speakerIb = view.findViewById(R.id.ib_notif);
        speakerArea = view.findViewById(R.id.speaker_area);
        warningText = view.findViewById(R.id.tv_warning);
        instructText = view.findViewById(R.id.tv_insruct);
        logoutIv = view.findViewById(R.id.iv_logout);
    }
}