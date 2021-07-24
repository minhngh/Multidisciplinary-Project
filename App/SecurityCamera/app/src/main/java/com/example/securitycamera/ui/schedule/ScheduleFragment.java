
package com.example.securitycamera.ui.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;

import com.example.securitycamera.R;
import com.example.securitycamera.data.local.ScheduleManager;
import com.example.securitycamera.data.model.Result;
import com.example.securitycamera.data.model.schedule.Schedule;
import com.example.securitycamera.data.model.schedule.change.ScheduleChangeLog;
import com.example.securitycamera.viewmodel.GetScheduleChangeViewModel;
import com.example.securitycamera.viewmodel.HomeViewModel;
import com.example.securitycamera.worker.AlarmReceiver;
import com.example.securitycamera.worker.NotifyModeChangeWorker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class ScheduleFragment extends Fragment {
    private Context context;
    private AlarmManager alarmMgr;

    private GetScheduleChangeViewModel getScheduleChangeViewModel;

    private LinearLayout llSchedules;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getScheduleChangeViewModel = new ViewModelProvider(this).get(GetScheduleChangeViewModel.class);

        findView(view);

        context = requireContext();
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        getScheduleChangeViewModel.checkSchedules();
        getScheduleChangeViewModel.getResult().observe(getViewLifecycleOwner(), result -> {
            if (result instanceof Result.Success) {
                if (getActivity() != null) {
                    resetSchedules();

                    Schedule[] schedules = ((Result.Success<Schedule[]>) result).getData();
                    populateSchedules(schedules);

                    cancelAlarms();

                    for (int i = 0; i < schedules.length; i++) {
                        Schedule schedule = schedules[i];

                        addScheduleRowToScrollView(schedule);

                        if (!schedule.isEnabled())
                            continue;

                        setAlarm(schedule, i * 7);
                    }
                }
            }
            else if (result instanceof Result.Error) {


                Toast.makeText(context,
                        ((Result.Error<Schedule[]>) result).getError().getMessage(),
                        Toast.LENGTH_SHORT).show();

                Toast.makeText(context,
                        "You are viewing an offline version of schedules",
                        Toast.LENGTH_SHORT).show();

                ArrayList<Schedule> schedules = ScheduleManager.getInstance().getSchedules();
                for (int i = 0; i < schedules.size(); i++) {
                    Schedule schedule = schedules.get(i);

                    addScheduleRowToScrollView(schedule);
                }
            }
        });
    }

    private void populateSchedules(Schedule[] schedules) {
        ScheduleManager.getInstance().setSchedules(new ArrayList<>(Arrays.asList(schedules)));
    }

    private void resetSchedules() {
        ScheduleManager.getInstance().setSchedules(new ArrayList<>());
    }

    private void cancelAlarms() {
        ArrayList<Schedule> schedules = ScheduleManager.getInstance().getSchedules();

        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            boolean[] isActives = schedule.getIsActives();

            for (int j = 0 ; j < isActives.length; j++) {
                boolean isActive = isActives[j];

                if (!isActive)
                    continue;

                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra(
                        AlarmReceiver.KEY_IS_TO_CAUTIOUS_MODE,
                        schedule.isToCautiousMode());
                PendingIntent alarmIntent = PendingIntent
                        .getBroadcast(context, i * 7 + j, intent, 0);

                alarmMgr.cancel(alarmIntent);
            }
        }
    }

    private void setAlarm(Schedule schedule, int baseRequestCode) {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTimeInMillis(System.currentTimeMillis());

        Calendar scheduleCalendar = Calendar.getInstance();
        scheduleCalendar.setTimeInMillis(System.currentTimeMillis());

        scheduleCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        scheduleCalendar.set(Calendar.DAY_OF_WEEK, scheduleCalendar.getFirstDayOfWeek());

        String[] timeUnits = schedule.getTime().split(":");
        int hour = Integer.parseInt(timeUnits[0]);
        int minute = Integer.parseInt(timeUnits[1]);

        scheduleCalendar.set(Calendar.HOUR_OF_DAY, hour);
        scheduleCalendar.set(Calendar.DAY_OF_WEEK, minute);

        for (int i = 0; i < schedule.getIsActives().length; i++) {
            boolean isActive = schedule.getIsActives()[i];

            if (!isActive)
                continue;

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(
                    AlarmReceiver.KEY_IS_TO_CAUTIOUS_MODE,
                    schedule.isToCautiousMode());
            PendingIntent alarmIntent = PendingIntent
                    .getBroadcast(context, baseRequestCode + i, intent, 0);

            if (scheduleCalendar.getTimeInMillis() > todayCalendar.getTimeInMillis()) {
                alarmMgr.setInexactRepeating(
                        AlarmManager.RTC,
                        scheduleCalendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY * 7,
                        alarmIntent);
            }
            else {
                scheduleCalendar.add(Calendar.DAY_OF_YEAR, 7);

                alarmMgr.setInexactRepeating(
                        AlarmManager.RTC,
                        scheduleCalendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY * 7,
                        alarmIntent);

                scheduleCalendar.add(Calendar.DAY_OF_YEAR, -7);
            }

            scheduleCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void addScheduleRowToScrollView(Schedule schedule) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.schedule_row, null);
        llSchedules.addView(v);

        TextView tvTime = v.findViewById(R.id.tv_schedule_time);
        tvTime.setText(schedule.getTime());

        TextView tvDescription = v.findViewById(R.id.tv_schedule_description);
        tvDescription.setText(schedule.getDescription());
        tvDescription.setTextColor(getResources().getColor(R.color.disabled_text));

        TextView tvMode = v.findViewById(R.id.tv_schedule_mode);
        boolean isToCautiousMode = schedule.isToCautiousMode();
        tvMode.setText(isToCautiousMode? "C": "N");
        tvMode.setTextColor(
            getResources().getColor(isToCautiousMode? R.color.cautious_mode: R.color.normal_mode));

        TextView[] tvsDay = new TextView[] {
                v.findViewById(R.id.tv_sunday),
                v.findViewById(R.id.tv_monday),
                v.findViewById(R.id.tv_tuesday),
                v.findViewById(R.id.tv_wednesday),
                v.findViewById(R.id.tv_thursday),
                v.findViewById(R.id.tv_friday),
                v.findViewById(R.id.tv_saturday),
        };
        boolean[] isActives = schedule.getIsActives();

        for (int i = 0; i < isActives.length; i++) {
            tvsDay[i].setTextColor(
                    getResources().getColor(
                            isActives[i]? R.color.enabled_text: R.color.disabled_text));
        }

        Switch swEnable = v.findViewById(R.id.sw_enable);

        swEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tvTime.setTextColor(getResources().getColor(R.color.enabled_text));
                tvMode.setTextColor(
                        getResources().getColor(
                                isToCautiousMode? R.color.cautious_mode: R.color.normal_mode));
                for (int i = 0; i < schedule.getIsActives().length; i++) {
                        tvsDay[i].setTextColor(
                            getResources().getColor(
                                isActives[i]? R.color.enabled_text: R.color.disabled_text));
                }
            }
            else {
                tvTime.setTextColor(getResources().getColor(R.color.disabled_text));
                tvMode.setTextColor(getResources().getColor(R.color.disabled_text));
                for (int i = 0; i < schedule.getIsActives().length; i++) {
                        tvsDay[i].setTextColor(getResources().getColor(R.color.disabled_text));
                }
            }
        });

        swEnable.setChecked(schedule.isEnabled());
    }

    private void findView(View view) {
        llSchedules = view.findViewById(R.id.ll_schedules);
    }
}