package com.example.pk.tpmresolution.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.fragment.DailyCheckListFragment;
import com.example.pk.tpmresolution.model.CheckListItem;
import com.example.pk.tpmresolution.model.CommonClass;
import com.example.pk.tpmresolution.utils.Validation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kien on 03/29/2017.
 */
public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {

    private List<CheckListItem> mListStatus;
    private Context context;
    SpinnerItemClickListener listener;
    private int focusedItem = -1;
    ArrayList<CommonClass> list_stt;
    DailyCheckListFragment dailyCheckListFragment;
    Map<Integer, Integer> myMap = new HashMap<Integer, Integer>();

    int type;
    boolean isNew;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomFontTextView txtName, txtActionName ;
        public AppCompatSpinner spnStatusCheckList;

        public MyViewHolder(View view) {
            super(view);
            txtName = (CustomFontTextView) view.findViewById(R.id.txt_checklist_name);
            txtActionName = (CustomFontTextView) view.findViewById(R.id.txt_description);
            spnStatusCheckList = (AppCompatSpinner) view.findViewById(R.id.spn_status_checklist);
            view.setClickable(true);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface SpinnerItemClickListener{
        void onItemClick(int position, AppCompatSpinner spn);
    }

    public CheckListAdapter(Context context, List<CheckListItem> moviesList, ArrayList<CommonClass> list_stt, boolean isNew, int type, DailyCheckListFragment daily, SpinnerItemClickListener listener) {
        this.context = context;
        this.list_stt = list_stt;
        this.dailyCheckListFragment = daily;
        this.isNew = isNew;
        this.type = type;
        this.listener = listener;
        this.mListStatus = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_daily_item, parent, false);
        final MyViewHolder pvh = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notifyItemChanged(focusedItem);
                focusedItem = pvh.getLayoutPosition();
                notifyItemChanged(focusedItem);

            }
        });
        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }
                return false;
            }
        });
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int tryFocusItem = focusedItem + direction;
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);
            return true;
        }
        return false;
    }

    public int getFocusedItem(){
        return focusedItem;
    }

    public void setFocusedItem(int pos){
        this.focusedItem = pos;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CommonAdapter adapter = new CommonAdapter(list_stt, context);;
        final CheckListItem menu = mListStatus.get(position);
        holder.txtName.setText(menu.getChecklist_name());
        holder.txtActionName.setText(menu.getChecklist_action_name());
        holder.spnStatusCheckList.setAdapter(adapter);
        if(!Validation.checkNullOrEmpty(menu.getChecklist_status_name()))
        for(int i=0; i<list_stt.size(); i++){
            if(menu.getChecklist_status_name().equals(list_stt.get(i).getName())) holder.spnStatusCheckList.setSelection(i);
        }
        if (myMap.containsKey(position)) {
            holder.spnStatusCheckList.setSelection(myMap.get(position));
        }
        if(isNew) {
            holder.spnStatusCheckList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    myMap.put(position, i);
                    listener.onItemClick(position, holder.spnStatusCheckList);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            holder.spnStatusCheckList.setEnabled(false);
            holder.spnStatusCheckList.setFocusable(false);
            holder.spnStatusCheckList.setClickable(false);
        }
        holder.itemView.setSelected(focusedItem == position);
    }

    @Override
    public int getItemCount() {
        return mListStatus.size();
    }
}