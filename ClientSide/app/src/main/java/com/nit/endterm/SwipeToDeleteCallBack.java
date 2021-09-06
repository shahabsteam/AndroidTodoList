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

public abstract class SwipeToDeleteCallBack extends ItemTouchHelper.SimpleCallback {
    private Context context;
    private Drawable deleteIcon;
    private int backgroundColor;
    private ColorDrawable background;
    private int icWidth;
    private int icHeight;
    Paint clearPaint = new Paint();
    SwipeToDeleteCallBack(Context context){
        super(0,ItemTouchHelper.LEFT);
        this.context=context;
        deleteIcon = ContextCompat.getDrawable(context,R.drawable.ic_delete_icon);
        backgroundColor = Color.parseColor("RED");
        background = new ColorDrawable();
        icWidth= deleteIcon.getIntrinsicWidth();
        icHeight = deleteIcon.getIntrinsicHeight();
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
                itemView.getRight() +(int)dX,
                itemView.getTop(),
                itemView.getRight(),
                itemView.getBottom()
        );
        background.draw(c);
        int editIconTop = itemView.getTop()+(itemHeight-icWidth)/2;
        int editIconMargin =(itemHeight-icHeight);
        int editIconLeft =itemView.getRight()-editIconMargin-icWidth;
        int editIconRight = itemView.getRight()-editIconMargin;
        int editIconBottom = editIconTop+icHeight;
        deleteIcon.setBounds(editIconLeft,editIconTop,editIconRight,editIconBottom);
        deleteIcon.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
