package com.example.todo_list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.Adapter.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ToDoAdapter adapter;
    public RecyclerItemTouchHelper(ToDoAdapter adapter)
    {
        super(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }
    @Override
    public boolean onMove(RecyclerView recyclerView ,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target)
    {
return false;
    }
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
    {
        final int position = viewHolder.getAdapterPosition();
            if(direction == ItemTouchHelper.LEFT){
                AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
                builder.setTitle("Delete Task");
                builder.setMessage("Are you  sure want to delete a task");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.deleteItem(position);

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.notifyItemChanged(viewHolder.getLayoutPosition());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                 adapter.editItem(position);
            }
    }
    @Override
    public void onChildDraw(Canvas c,RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder, float dx,float dy,int actionState,boolean currentlyActive)
    {
        super.onChildDraw(c, recyclerView,viewHolder,dx,dy,actionState,currentlyActive);
        Drawable icon;
        ColorDrawable background;
        View itemview = viewHolder.itemView;
        int bagroundCornerOffset = 20;
        if(dx>0)
        {
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.borderedit);
            background = new ColorDrawable( ContextCompat.getColor(adapter.getContext(),R.color.color1));

        }
        else {
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.baselinedelete);
            background = new ColorDrawable(Color.RED);
        }
        int iconMargin = (itemview.getHeight() - icon.getIntrinsicHeight())/2;
        int iconTop = itemview.getTop() + (itemview.getRight()-icon.getIntrinsicHeight())/2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if(dx>0)
        {
            int iconLeft = itemview.getLeft()+iconMargin;
            int iconRight = itemview.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop,iconRight,iconBottom);
            background.setBounds(itemview.getLeft(),itemview.getTop(),itemview.getLeft() + ((int)dx) + bagroundCornerOffset,itemview.getBottom());}
            else if(dx<0)
            {
                int iconLeft = itemview.getRight()-iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemview.getRight() - iconMargin ;
                icon.setBounds(iconLeft, iconTop,iconRight,iconBottom);
                background.setBounds(itemview.getRight() + ((int)dx)-bagroundCornerOffset,itemview.getTop(),itemview.getRight()
                        ,itemview.getBottom());
            }
            else {
                background.setBounds(0,0,0,0);
        }
            background.draw(c);
            icon.draw(c);
        }


    }


