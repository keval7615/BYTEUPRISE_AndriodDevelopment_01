package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todo_list.Adapter.ToDoAdapter;
import com.example.todo_list.Util.DatabaseHandler;
import com.example.todo_list.model.TodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListner{
private RecyclerView taskRecyclerView;
private ToDoAdapter taskadapter;
private FloatingActionButton fab;

private List<TodoModel> taskList;

private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>();

        taskRecyclerView  = findViewById(R.id.taskrecycleview);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskadapter =  new ToDoAdapter( db,this);
        taskRecyclerView.setAdapter(taskadapter);
        taskadapter.settask(taskList);


        fab = findViewById(R.id.fab);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskadapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

//        TodoModel task = new TodoModel();
//        task.setTask(" this is task ");
//        task.setStatus(0);
//        task.setId(1);
//
//        taskList.add(task);
//        taskList.add(task);
//        taskList.add(task);
//        taskList.add(task);
//        taskList.add(task);
        Collections.reverse(taskList);
        taskadapter.settask(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager() ,AddNewTask.TAG);
            }
        });
    }
    @Override
    public void handleDialogClose(DialogInterface dialog)
    {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskadapter.settask(taskList);
        taskadapter.notifyDataSetChanged();
    }
}