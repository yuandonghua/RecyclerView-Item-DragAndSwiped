package ydh.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @description:重写ItemTouchHelper.Callback
 * @author:袁东华 created at 2016/8/30 0030 上午 11:05
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    //
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
}
