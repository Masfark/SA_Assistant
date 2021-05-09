package com.example.sa_assistant.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sa_assistant.R;

public class ReportMenuFragment extends Fragment implements View.OnClickListener {

    Button btn_rep, btn_kbsa, btn_displacement, btn_print_rep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report_menu, container, false);

        btn_rep = root.findViewById(R.id.btn_menu_reports);
        btn_rep.setOnClickListener(this);
        btn_kbsa = root.findViewById(R.id.btn_menu_kbsa);
        btn_kbsa.setOnClickListener(this);
        btn_displacement = root.findViewById(R.id.btn_menu_displacement);
        btn_displacement.setOnClickListener(this);
        btn_print_rep = root.findViewById(R.id.btn_menu_print_rep);
        btn_print_rep.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_menu_reports) {
            NavHostFragment.findNavController(ReportMenuFragment.this).navigate(R.id.action_nav_reports_to_nav_report_fragment);
        }
        else if (id == R.id.btn_menu_kbsa) {
            NavHostFragment.findNavController(ReportMenuFragment.this).navigate(R.id.action_nav_reports_to_nav_bot_report_fragment);
        }
        else if (id == R.id.btn_menu_displacement) {
            NavHostFragment.findNavController(ReportMenuFragment.this).navigate(R.id.action_nav_reports_to_nav_displacement_fragment);
        }
        else if (id == R.id.btn_menu_print_rep) {
            NavHostFragment.findNavController(ReportMenuFragment.this).navigate(R.id.action_nav_reports_to_nav_print_report_fragment);
        }
    }
}
