package ydh.recyclerview;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @description:重写ItemTouchHelper.Callback
 * @author:袁东华 created at 2016/8/30 0030 上午 11:05
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private OnMoveAndSwipedListener adapter;

    public ItemTouchHelperCallback(OnMoveAndSwipedListener onMoveAndSwipedListener) {
        this.adapter = onMoveAndSwipedListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //这个方法是用来设置条目拖动方向和侧滑方向的
        int dragFlags = 0, swipeFlags = 0;
        //LinearLayout样式
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            //设置拖拽方向为上下
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            //设置侧滑方向为左右
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            //GridLayout样式
            //设置拖拽方向为上下
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            //不支持侧滑
            swipeFlags = 0;
        }

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //拖拽item时会调用此方法
        //如果两个条目不是同一类型,不可拖拽
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        //回调adapter中的onItemMove方法,更新数据
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //侧滑item时会调用此方法
        //回调adapter中的onItemDelete方法,更新数据
        adapter.onItemDelete(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //当条目的状态改变时调用此方法(拖拽,滑动状态)
        //ACTION_STATE_IDLE = 0;
        //ACTION_STATE_SWIPE = 1;
        //ACTION_STATE_DRAG = 2;
        //当条目不是空闲状态时(正在拖拽或滑动时)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            //可以改变item的背景色
            //判断下ViewHolder是否实现了OnStateChangedListener
            if (viewHolder instanceof OnStateChangedListener) {
                OnStateChangedListener onStateChangedListener = (OnStateChangedListener) viewHolder;
                //回调ViewHolder中的onItemSelected()方法改变背景色
                onStateChangedListener.onSelectedChanged();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //当拖拽,滑动完成后调用此方法
        //判断下ViewHolder是否实现了OnStateChangedListener
        if (viewHolder instanceof OnStateChangedListener) {
            OnStateChangedListener onStateChangedListener = (OnStateChangedListener) viewHolder;
            //回调ViewHolder中的onItemSelected()方法改变背景色
            onStateChangedListener.clearView();
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
       //滑动删除条目时执行这个判断
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
           float alpha= 1-Math.abs(dX)/(float)viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
