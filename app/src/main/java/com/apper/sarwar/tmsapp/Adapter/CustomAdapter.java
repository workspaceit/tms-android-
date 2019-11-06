package com.apper.sarwar.tmsapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apper.sarwar.tmsapp.R;
import com.apper.sarwar.tmsapp.viewmodel.MyTaskList;

import java.util.List;

/**
 * Created by Belal on 29/09/16.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<MyTaskList> list;
    private Context mCtx;

    public CustomAdapter(List<MyTaskList> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_task_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
        MyTaskList myList = list.get(position);
        holder.textViewHead.setText(myList.getTaskTitle());
        holder.textViewDesc.setText(myList.getTaskDate());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView buttonViewOption;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textTaskTitle);
            textViewDesc = (TextView) itemView.findViewById(R.id.textTaskDate);
        }
    }
}
