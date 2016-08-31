#### RecyclerView实现滑动删除条目,长按拖拽条目效果,其实很简单,需要用到android.support.v7.widget.helper.ItemTouchHelper,这个工具类已经实现了需要的功能,只需要知道如何使用就ok了.
使用步骤:
1. 写个RecyclerView展示列表数据demo,下面是关键
2. 创建ItemTouchHelperCallback继承ItemTouchHelper.Callback
```java

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
}
```
3. 为什么要写这个类?因为当滑动和拖拽条目的时候,会回调这个callback内部重写的方法,此callback使用如下:
```java
//historyAdapter就是RecyclerView的适配器,historyAdapter实现了接口OnMoveAndSwipedListener
ItemTouchHelperCallback callback = new ItemTouchHelperCallback(historyAdapter);
//ItemTouchHelper的构造器需要传入callback,拖拽和滑动事件需要回调callback中的3个方法
ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//把RecyclerView和ItemTouchHelper关联起来用此方法
itemTouchHelper.attachToRecyclerView(recyclerView);
```
4. 上面的callback的构造器需要传入RecyclerView的adapter,adapter需要实现此接口,接口如下:
```java

/**
 * @description:当拖拽或者侧滑的时候,需要回调此接口中的方法,来操作adapter中的数据
 * @author:袁东华 created at 2016/8/30 0030 上午 11:23
 */
public interface OnMoveAndSwipedListener {
    /**
     *@description:拖拽条目
     *@author:袁东华
     *created at 2016/8/30 0030 上午 11:26
     */
    boolean onItemMove(int fromPosition, int toPosition);
    /**
     *@description:删除条目
     *@author:袁东华
     *created at 2016/8/30 0030 上午 11:48
     */
    void onItemDelete(int position);
}
```
5. 当操作条目的时候,在callback中回调此接口中的方法,adapter就可以更新数据,代码如下:
```java
public class HistoryAdapter extends Adapter<HistoryAdapter.ViewHolder> implements OnMoveAndSwipedListener {
 /**
     * @param fromPosition
     * @param toPosition
     * @description:拖拽条目
     * @author:袁东华 created at 2016/8/30 0030 上午 11:26
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //交换数据的位置
        Collections.swap(list, fromPosition, toPosition);
        //交换RecyclerView中条目的位置
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * @param position
     * @description:删除条目
     * @author:袁东华 created at 2016/8/30 0030 上午 11:26
     */
    @Override
    public void onItemDelete(int position) {
        //删除该历史记录
//        FilmDataHttp.getInstance().resHistoryDelWt(list.get(position).getHistoryId(), handler,2,-2);
        //删除该条目的数据
        list.remove(position);
        //删除recyclerView中的该条目
        notifyItemRemoved(position);
    }
}
```
以上代码就可以实现RecyclerView的条目拖拽和滑动删除效果,有木有很简单的感觉,具体是怎么实现的android.support.v7.widget.helper.ItemTouchHelper已经帮我们解决了,只需要拿来用就ok了


























