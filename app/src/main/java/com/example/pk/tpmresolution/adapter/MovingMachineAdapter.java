package com.example.pk.tpmresolution.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.model.MachineForMoving;
import java.util.List;

/**
 * Created by kien on 07/29/2016.
 */
public class MovingMachineAdapter extends RecyclerView.Adapter<MovingMachineAdapter.MyViewHolder> {

    private List<MachineForMoving> mListMaintenace;
    private Context context;
    NavClickAdapter listener;
    private int focusedItem = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomFontTextView mName, mId;
        public AppCompatImageView mImages;
        public MyViewHolder(View view) {
            super(view);
            mImages = (AppCompatImageView) view.findViewById(R.id.imamages);
            mName = (CustomFontTextView) view.findViewById(R.id.txt_name);
            mId = (CustomFontTextView) view.findViewById(R.id.txt_machine_id);
            view.setClickable(true);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public MovingMachineAdapter(Context context, List<MachineForMoving> moviesList, NavClickAdapter listener) {
        this.context = context;
        this.listener = listener;
        this.mListMaintenace = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_moving_machine, parent, false);
        final MovingMachineAdapter.MyViewHolder pvh = new MovingMachineAdapter.MyViewHolder(itemView);
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
        MachineForMoving menu = mListMaintenace.get(position);
        holder.mName.setText(menu.getName());
        holder.mId.setText(menu.getMachineId());
        //Picasso.with(context).load().into(holder.image);
        holder.itemView.setSelected(focusedItem == position);


    }

    @Override
    public int getItemCount() {
        return mListMaintenace.size();
    }

}