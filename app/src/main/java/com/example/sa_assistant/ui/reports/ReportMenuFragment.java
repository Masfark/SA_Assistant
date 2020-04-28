package com.example.sa_assistant.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sa_assistant.R;

public class ReportMenuFragment extends Fragment implements View.OnClickListener {

    Button btn_rep, btn_kbsa, btn_displacement;
    Fragment fragmentReports, fragmentKBSA, fragmentDisplacement;
    FragmentTransaction fTrans;

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

        fragmentReports = new ReportsFragment();
        fragmentKBSA = new BotReportFragment();
        fragmentDisplacement = new DisplacementFragment();


        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        fTrans = getChildFragmentManager().beginTransaction();

        if (id == R.id.btn_menu_reports) {
            fTrans.add(R.id.fragment_report_menu, fragmentReports);
        }
        else if (id == R.id.btn_menu_kbsa) {
            fTrans.add(R.id.fragment_report_menu, fragmentKBSA);
        }
        else if (id == R.id.btn_menu_displacement) {
            fTrans.add(R.id.fragment_report_menu, fragmentDisplacement);
        }
        fTrans.addToBackStack("Reports menu");
        fTrans.commit();
    }
}
