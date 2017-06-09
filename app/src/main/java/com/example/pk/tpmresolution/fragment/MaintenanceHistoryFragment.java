package com.example.pk.tpmresolution.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.pk.tpmresolution.MainActivity;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.adapter.HistoryAdapter;
import com.example.pk.tpmresolution.adapter.NavClickAdapter;
import com.example.pk.tpmresolution.model.HistoryItem;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.DialogAcceptClickListener;
import com.example.pk.tpmresolution.utils.Validation;
import java.util.ArrayList;
import butterknife.ButterKnife;

public class MaintenanceHistoryFragment extends Fragment {
    AppCompatImageView imgGo;
    RecyclerView recycler;
    HistoryAdapter historyAdapter;
    ArrayList<HistoryItem>list;
    Dialog mDialogError;
    SharedPreferences prefs;

    public MaintenanceHistoryFragment() {
    }

    public static MaintenanceHistoryFragment newInstance() {
        MaintenanceHistoryFragment fragment = new MaintenanceHistoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void validationText(TextView tv, String s){
        if(!Validation.checkNullOrEmpty(s))
            tv.setText(s);
        else tv.setText(AppConstants.DEFAULT_NULL_TEXT);
    }

    private void loadList(){

        recycler.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        historyAdapter = new HistoryAdapter(getActivity(), list, new NavClickAdapter() {
            @Override
            public void onItemClick(View v, int position) {
                Fragment frag = new RequestMaintenanceFragment(list.get(position));
                AppTransaction.replaceFragmentWithAnimation(getActivity().getSupportFragmentManager(), frag);
               // showDialogUpdate(list.get(position).getPartbook_price(), list.get(position).getPartbook_qty(), list.get(position).getPartbook_name(), list.get(position).getPartbook_unit_name(), position);
            }

            @Override
            public void onLongItemClick(View v, final int position) {
                AppDialogManager.onCreateDialogConfirm(getActivity(), "Do you want to delete?", new DialogAcceptClickListener() {
                    @Override
                    public void onAcceptClick(View v) {
                        list.remove(position);
                        historyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCloseClick(View v) {

                    }
                });
            }

        });
        recycler.setAdapter(historyAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_maintenance_history, container, false);
        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getTextArray(R.array.navigation_array_tile)[2]);
        ButterKnife.bind(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        imgGo = (AppCompatImageView) root.findViewById(R.id.img_go);
        imgGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = RequestMaintenanceFragment.newInstance(null);
                AppTransaction.replaceFragmentWithAnimation(getActivity().getSupportFragmentManager(), frag);
            }
        });

        recycler = (RecyclerView) root.findViewById(R.id.recycler_history);
        list = new ArrayList<>();
        for (int i=0; i<5; i++){
            HistoryItem item = new HistoryItem();
            item.setMachine_id("M22170808");
            item.setRequest_user("Nguyễn Văn Kiên");
            item.setPwic_user("Nguyễn Văn Kiên");
            item.setReason("Nguyễn Văn Kiên test 6/6/2017");
            item.setCreated_date("25/7/2017");
            item.setRequest_date("25/7/2017");
            item.setUpdate_date("25/7/2017");
            item.setStatus("Normal");
            list.add(item);
        }
        loadList();

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
