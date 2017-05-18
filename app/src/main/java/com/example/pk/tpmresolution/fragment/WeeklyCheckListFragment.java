package com.example.pk.tpmresolution.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pk.tpmresolution.MainActivity;
import com.example.pk.tpmresolution.R;

public class WeeklyCheckListFragment extends Fragment {


    public WeeklyCheckListFragment() {
        // Required empty public constructor
    }


    public static WeeklyCheckListFragment newInstance() {
        return new WeeklyCheckListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getString(R.string.checklist_weekly));
        return inflater.inflate(R.layout.fragment_weekly_check_list, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
