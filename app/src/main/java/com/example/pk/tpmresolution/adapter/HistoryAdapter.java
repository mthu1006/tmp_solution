package com.example.pk.tpmresolution.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.model.HistoryItem;
import com.example.pk.tpmresolution.model.PartBookItem;

import java.util.List;

/**
 * Created by kien on 07/29/2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<HistoryItem> mList;
    private Context context;
    NavClickAdapter listener;
    private int focusedItem = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomFontTextView txtID, txtUserName, txtDate, txtStatus ;
        public AppCompatImageView mImages;
        public MyViewHolder(View view) {
            super(view);
            mImages = (AppCompatImageView) view.findViewById(R.id.imamages);
            txtID = (CustomFontTextView) view.findViewById(R.id.txt_machine_id);
            txtUserName = (CustomFontTextView) view.findViewById(R.id.txt_name);
            txtDate = (CustomFontTextView) view.findViewById(R.id.txt_date);
            txtStatus = (CustomFontTextView) view.findViewById(R.id.txt_status);
            view.setClickable(true);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public HistoryAdapter(Context context, List<HistoryItem> moviesList, NavClickAdapter listener) {
        this.context = context;
        this.listener = listener;
        this.mList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history, parent, false);
        final HistoryAdapter.MyViewHolder pvh = new HistoryAdapter.MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, pvh.getLayoutPosition());
                notifyItemChanged(focusedItem);
                focusedItem = pvh.getLayoutPosition();
                notifyItemChanged(focusedItem);

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongItemClick(v, pvh.getLayoutPosition());
                return true;
            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HistoryItem menu = mList.get(position);
        holder.txtID.setText(menu.getMachine_id());
        holder.txtUserName.setText(menu.getRequest_user());
        holder.txtDate.setText(menu.getCreated_date());
        holder.txtStatus.setText(menu.getStatus());
        //Picasso.with(context).load().into(holder.image);
        holder.itemView.setSelected(focusedItem == position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}