package com.apper.sarwar.tmsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.tmsapp.R;
import com.apper.sarwar.tmsapp.viewmodel.MyTaskList;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MyTaskListAdapter extends RecyclerView.Adapter<MyTaskListAdapter.ViewHolderTaskList> {


    private List<MyTaskList> taskLists;
    private Context context;
    private static final String TAG = "MyTaskListAdapter";
    PopupWindow popupWindow;

    public MyTaskListAdapter(List<MyTaskList> myTaskLists, Context context) {
        this.taskLists = myTaskLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderTaskList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_task_list, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(context, "This Toast clicke!", Toast.LENGTH_SHORT).show();*/
                ShowTaskInPopUp(v);
            }
        });
        return new ViewHolderTaskList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTaskListAdapter.ViewHolderTaskList viewHolderTaskList, int position) {
        MyTaskList tasks = taskLists.get(position);
        viewHolderTaskList.taskTitleView.setText(tasks.getTaskTitle());
        viewHolderTaskList.taskDateView.setText(tasks.getTaskDate());

        Log.d(TAG, "onBindViewHolder: " + tasks.getTaskTitle());

    }

    @Override
    public int getItemCount() {
        return taskLists.size();
    }

    /**
     *
     */
    public void ShowTaskInPopUp(View view) {

        try {
            TextView textTaskTitle = (TextView) view.findViewById(R.id.textTaskTitle);
            String title = textTaskTitle.getText().toString();
            /*Toast.makeText(context, "This Toast clicke!" + title, Toast.LENGTH_SHORT).show();*/

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View popView = inflater.inflate(R.layout.popup_task_window, null, false);
            int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            PopupWindow pw = new PopupWindow(popView,
                    width,
                    height,
                    true);
            pw.showAtLocation(view, Gravity.CENTER, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class ViewHolderTaskList extends RecyclerView.ViewHolder {

        public TextView taskTitleView;
        public TextView taskDateView;

        public ViewHolderTaskList(@NonNull View itemView) {

            super(itemView);

            taskTitleView = (TextView) itemView.findViewById(R.id.textTaskTitle);
            taskDateView = (TextView) itemView.findViewById(R.id.textTaskDate);
        }
    }


}
