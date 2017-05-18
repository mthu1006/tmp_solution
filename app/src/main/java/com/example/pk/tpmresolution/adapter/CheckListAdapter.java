package com.example.pk.tpmresolution.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.fragment.DailyCheckListFragment;
import com.example.pk.tpmresolution.model.CheckListItem;

import java.util.List;

/**
 * Created by kien on 03/29/2017.
 */
public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {

    private List<CheckListItem> mListStatus;
    private Context context;
    SpinnerItemClickListener listener;
    private int focusedItem = -1;
    String[] arr, arrCode;
    DailyCheckListFragment dailyCheckListFragment;
    ArrayAdapter<String> adapter;
    int type;
    boolean isNew;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomFontTextView txtName, txtActionName ;
        public AppCompatSpinner spnStatusCheckList;
//        public LinearLayout layoutView;

        public MyViewHolder(View view) {
            super(view);
            txtName = (CustomFontTextView) view.findViewById(R.id.txt_checklist_name);
            txtActionName = (CustomFontTextView) view.findViewById(R.id.txt_description);
            spnStatusCheckList = (AppCompatSpinner) view.findViewById(R.id.spn_status_checklist);
//            layoutView=(LinearLayout)view.findViewById(R.id.expandablelayout);
            view.setClickable(true);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface SpinnerItemClickListener{
        void onItemClick(String name, String id, int position);
    }


    public CheckListAdapter(Context context, List<CheckListItem> moviesList, String[] arr, String[] arrCode, boolean isNew, int type, DailyCheckListFragment daily, SpinnerItemClickListener listener) {
        this.context = context;
        this.arr = arr;
        this.arrCode = arrCode;
        this.dailyCheckListFragment = daily;
        this.isNew = isNew;
        this.type = type;
        this.listener = listener;
        this.mListStatus = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arr);
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CheckListItem menu = mListStatus.get(position);
        holder.txtName.setText(menu.getChecklist_name());
        holder.txtActionName.setText(menu.getChecklist_action_name());
        holder.spnStatusCheckList.setAdapter(adapter);
        for(int i=0; i<arr.length; i++){
            if(menu.getChecklist_status_name().equals(arr[i])) holder.spnStatusCheckList.setSelection(i);
        }
        if(isNew) {
            holder.spnStatusCheckList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    listener.onItemClick(arr[i], arrCode[i], position);
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
//        holder.layoutView.setVisibility(View.VISIBLE);
        holder.itemView.setSelected(focusedItem == position);
    }


    @Override
    public int getItemCount() {
        return mListStatus.size();
    }

}