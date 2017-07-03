package com.example.pk.tpmresolution.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.model.InformationItem;
import java.util.List;

/**
 * Created by kien on 07/29/2016.
 */
public class InfomationAdapter extends RecyclerView.Adapter<InfomationAdapter.MyViewHolder> {

    private List<InformationItem> mListEmail;
    private Context context;
    NavigationClickListener listener;
    private int focusedItem = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomFontTextView txtName ;
        public AppCompatImageView image;
        public LinearLayout layoutView;
        public MyViewHolder(View view) {
            super(view);
            image = (AppCompatImageView) view.findViewById(R.id.image_icon);
            txtName = (CustomFontTextView) view.findViewById(R.id.txt_title);
            layoutView=(LinearLayout)view.findViewById(R.id.expandablelayout);
            view.setClickable(true);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface NavigationClickListener{
        void onItemClick(View v, int position);
    }

    public InfomationAdapter(Context context, List<InformationItem> moviesList, NavigationClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.mListEmail = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_infomation_item, parent, false);
        final MyViewHolder pvh = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, pvh.getLayoutPosition());
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

                // Return false if scrolled to the bounds and allow focus to move off the list
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

        // If still within valid bounds, move the selection, notify to redraw, and scroll
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        InformationItem menu = mListEmail.get(position);
        holder.txtName.setText(menu.getDisplayName());
        //holder.image.setBackground(menu.getIcon());
        //Picasso.with(context).load().into(holder.image);
        holder.layoutView.setVisibility(View.VISIBLE);
        holder.itemView.setSelected(focusedItem == position);
    }

    @Override
    public int getItemCount() {
        return mListEmail.size();
    }

}