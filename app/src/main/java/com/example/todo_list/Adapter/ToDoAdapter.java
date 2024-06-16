package com.example.todo_list.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.AddNewTask;
import com.example.todo_list.MainActivity;
import com.example.todo_list.R;
import com.example.todo_list.Util.*;
import com.example.todo_list.model.TodoModel;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>
{
    private List<TodoModel> todolist;
    private MainActivity activity;
    private DatabaseHandler db;
    public  ToDoAdapter(DatabaseHandler db, MainActivity activity)
    {
        this.db = this.db;
        this.activity=activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasklayout,parent,false);
        return new ViewHolder(itemView);

    }

    @Override


    public void  onBindViewHolder(ViewHolder holder,int position)
    {
        db.openDatabase();
        TodoModel item = todolist.get(position);
                holder.task.setText(item.getTask());
                holder.task.setChecked(toBoolean(item.getStatus()));
                holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b)
                        {
                            db.updateStatus(item.getId(),1);
                        }
                        else {
                            db.updateStatus(item.getId(),0);
                        }
                    }
                });

    }

    public int getItemCount() {
    return todolist.size();
}
    private Boolean toBoolean( int n)
    {
        return n!=0;
    }
    public void settask(List<TodoModel> todolist){
        this.todolist = todolist;
        notifyDataSetChanged();


    }
    public Context getContext(){
        return activity;
    }
    public void deleteItem(int position)
    {
        TodoModel item = todolist.get(position);
        db.deleteTask(item.getId());
        todolist.remove(position);
        notifyItemRemoved(position);

    }


    public void editItem(int position){
        TodoModel item = todolist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fargment =  new AddNewTask();
         fargment.setArguments(bundle);
         fargment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view)
        {
            super(view);
            task= view.findViewById(R.id.todocheckbox);
        }
    }

}
