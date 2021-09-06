package com.nit.endterm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SwipeToEditCallBack extends ItemTouchHelper.SimpleCallback {
    private Context context;
    private Drawable editIcon;
    private int backgroundColor;
    private ColorDrawable background;
    private int icWidth;
    private int icHeight;
    Paint clearPaint = new Paint();
    SwipeToEditCallBack(Context context){
        super(0,ItemTouchHelper.RIGHT);
        this.context=context;
          editIcon = ContextCompat.getDrawable(context,R.drawable.ic_edit_icon);
         backgroundColor = Color.parseColor("#24AE05");
          background = new ColorDrawable();
         icWidth=editIcon.getIntrinsicWidth();
         icHeight = editIcon.getIntrinsicHeight();
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getBottom()-itemView.getTop();
        background.setColor(backgroundColor);
        System.out.println(background.getColor());
        background.setBounds(
                itemView.getLeft() +(int)dX,
                itemView.getTop(),
                itemView.getLeft(),
                itemView.getBottom()
        );
        background.draw(c);
        int editIconTop = itemView.getTop()+(itemHeight-icWidth)/2;
        int editIconMargin =(itemHeight-icHeight);
        int editIconLeft =itemView.getLeft()+editIconMargin-icWidth;
        int editIconRight = itemView.getLeft()+editIconMargin;
        int editIconBottom = editIconTop+icHeight;
        editIcon.setBounds(editIconLeft,editIconTop,editIconRight,editIconBottom);
        editIcon.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}

