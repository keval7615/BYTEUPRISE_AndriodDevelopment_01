package com.example.todo_list.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo_list.model.TodoModel;

import java.util.ArrayList;
import java.util.List;

 public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "todolistdb";
    private static final String TODO_TABLE = "todo";
    private static final String TASK = "task";
    private static final String ID = "id";
    private static final String STATUS = "status";
     private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "("
             + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
             + TASK + " TEXT, "
             + STATUS + " INTEGER)";

     private static DatabaseHandler instance;
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }
    //>>>>>>>>>>>>>>>>>>>
    public void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();

        }
    }
    public void insertTask(TodoModel task) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
        closeDatabase();
    }

    public List<TodoModel> getAllTasks() {
        List<TodoModel> taskList = new ArrayList<>();
        openDatabase();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        TodoModel task = new TodoModel();
                        int idIndex = cur.getColumnIndex(ID);
                        int statusIndex = cur.getColumnIndex(STATUS);
                        int taskIndex = cur.getColumnIndex(TASK);
                        task.setId(cur.getInt(idIndex));
                         task.setStatus(cur.getInt(statusIndex));
                         task.setTask(cur.getString(taskIndex));
                        taskList.add(task);

                    } while (cur.moveToNext());
                }
            }
        } finally {
//            db.endTransaction();
            if (cur != null) {
                cur.close();
            }
            closeDatabase();
        }
        return taskList;
    }

    public void updateStatus(int id, int status) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
        closeDatabase();
    }

    public void updateTask(int id, String task) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
        closeDatabase();
    }

    public void deleteTask(int id) {
        openDatabase();
        db.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(id)});
        closeDatabase();
    }
}
