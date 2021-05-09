package com.example.sa_assistant.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sa_assistant.R;
import com.example.sa_assistant.ui.utils.Utils;

import java.text.DecimalFormat;

import static android.content.Context.LOCATION_SERVICE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private LocationManager locationManager;
    private TextView textView;
    private double lat = 55.9977, lon = 37.8649;
    private Location loc;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        loc = new Location(LocationManager.GPS_PROVIDER);
        loc.setLatitude(lat);
        loc.setLongitude(lon);

        return root;
    }
}
