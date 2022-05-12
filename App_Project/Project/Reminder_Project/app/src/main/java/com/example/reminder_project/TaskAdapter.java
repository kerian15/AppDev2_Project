package com.example.reminder_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{

    private ArrayList<Task> task;
//    private ItemClickListener clickListener;

    public TaskAdapter(ArrayList<Task> task) {
        this.task = task;
    }

    @NonNull
    @Override
    public TaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_task, parent, false);
        return new TaskAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyViewHolder holder, int position) {
//        MyViewHolder mholder = (MyViewHolder) holder;
        switch(task.get(position).getPriority()){
            case "Low":
                holder.priority.setText("!");
                break;
            case "Medium":
                holder.priority.setText("!!");
                break;
            case "High":
                holder.priority.setText("!!!");
                break;
            default:
                holder.priority.setText("");
                break;
        }
        holder.taskName.setText(task.get(position).getName());
        holder.note.setText(task.get(position).getNote());

        holder.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to set this 'Task' to 'Done'?\nThis will permanently remove it from the list");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = holder.getAdapterPosition();
                        Tasks_Page.ref.child(Tasks_Page.maxid.get(position)).removeValue();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.doneBtn.setChecked(false);
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.notifs.setText(task.get(position).getDate() + ", "
                + task.get(position).getTime() + ", "
                + task.get(position).getRepeat());
    }

    @Override
    public int getItemCount() {
        return task.size();
    }

//    public void setClickListener(ItemClickListener itemClickListener) {
//        this.clickListener = itemClickListener;
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //implements View.OnClickListener

//        Button listBtn;
        CheckBox doneBtn;
        TextView taskName, note, priority, notifs;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = (TextView) itemView.findViewById(R.id.textView8);
            note = (TextView) itemView.findViewById(R.id.textView10);
            doneBtn = (CheckBox) itemView.findViewById(R.id.checkBox);
            priority = (TextView) itemView.findViewById(R.id.textView9);
            notifs = (TextView) itemView.findViewById(R.id.textView11);

//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View view) {
//            clickListener.onClick(view, getPosition());
//        }
    }
}
