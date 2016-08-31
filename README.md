![image](https://github.com/yuandonghua/RecyclerView-Item-DragAndSwiped/blob/308721ae7bef8950149fbb05109e570a6e2d3800/screenshot/GIF.gif?raw=true)
## 效果:RecyclerView实现滑动删除条目,长按拖拽条目,拖拽和滑动时可以改变条目背景色和透明度,操作完成后条目的背景色透明度等复原.
其实很简单,需要用到android.support.v7.widget.helper.ItemTouchHelper,这个工具类已经实现了需要的功能,只需要知道如何使用就ok了.
使用步骤:
1. 写个RecyclerView展示数据的demo,可以是ListView效果也可以是GridView效果,这里列出的是ListView效果,这个很简单,无需多说,以下几步是重点
2. 先看下Activity中增加的代码:
```java
//Activity中只需写这么多代码就可以实现想要的效果
        //historyAdapter就是RecyclerView的适配器,historyAdapter实现了接口OnMoveAndSwipedListener
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(historyAdapter);
        //ItemTouchHelper的构造器需要传入callback,拖拽和滑动事件需要回调callback中的3个方法
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //把RecyclerView和ItemTouchHelper关联起来用此方法
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //设置条目拖拽接口
        historyAdapter.setOnDragListener(new OnDragListener() {
            /**
             * @param viewHolder
             * @description:当条目需要拖拽的时候,适配器调用onDrag
             * @author:袁东华 created at 2016/8/31 0031 下午 1:26
             */
            @Override
            public void startDrag(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });
```
Activity中只需写这么多代码就可以实现想要的效果,需要新增这几个接口和类:
- ItemTouchHelperCallback类,继承ItemTouchHelper.Callback,它是ItemTouchHelper构造器的参数
- OnDragListener接口,实现拖拽条目中的图片来拖拽条目的效果
- OnMoveAndSwipedListener接口,当拖拽条目,滑动删除条目时,回调此接口可以在adapter中更新数据,更新条目
- OnStateChangedListener接口,当拖拽条目,滑动删除条目时,改变条目背景色,透明度等

代码如下:
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


/**
 *@description:适配器条目拖拽接口,用来回调ItemTouchHelper.startDrag(viewHolder);
 *@author:袁东华
 *created at 2016/8/31 0031 下午 1:25
 */
public interface OnDragListener {
    /**
     *@description:当条目需要拖拽的时候,适配器调用onDrag
     *@author:袁东华
     *created at 2016/8/31 0031 下午 1:26
     */
   void startDrag(RecyclerView.ViewHolder viewHolder);
}


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




/**
 * @description:条目状态改变的接口,只需要让ViewHoulder的itemView实现这个接口就ok了,目的是改变itemView的背景色
 * @author:袁东华 created at 2016/8/31 0031 下午 1:50
 */
public interface OnStateChangedListener {
    /**
     * @description:拖拽,滑动item时调用,可以做改变背景色操作
     * @author:袁东华 created at 2016/8/31 0031 下午 1:51
     */
    void onSelectedChanged();
    /**
     *@description:条目正常状态:拖拽,滑动结束后,背景色恢复正常
     *@author:袁东华
     *created at 2016/8/31 0031 下午 2:37
     */
    void clearView();
}
```
3. 上面列出了新增的一个callback和几个接口,并写了详细的注释,下面就讲讲步骤:
先实现长按拖拽条目和滑动删除条目
- callback中重写getMovementFlags方法,设置拖动方向和滑动方向
```java
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
```
- 写个接口OnMoveAndSwipedListener,接口中增加2个方法,一个删除条目,一个拖拽条目
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
- adapter实现此接口
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
- 在Activity中把实现了OnMoveAndSwipedListener接口的adapter传递给callback的构造器
```java
//historyAdapter就是RecyclerView的适配器,historyAdapter实现了接口OnMoveAndSwipedListener
ItemTouchHelperCallback callback = new ItemTouchHelperCallback(historyAdapter);
```
- 在callback中重写2个方法onMove,onSwiped,当拖拽和滑动时会调用这两个方法,在这两个方法里回调adapter中的onItemMove和onItemDelete方法,更新列表数据和条目
```java

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

```
- 最后在Activity中增加如下代码:
```java
//historyAdapter就是RecyclerView的适配器,historyAdapter实现了接口OnMoveAndSwipedListener
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(historyAdapter);
        //ItemTouchHelper的构造器需要传入callback,拖拽和滑动事件需要回调callback中的3个方法
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //把RecyclerView和ItemTouchHelper关联起来用此方法
        itemTouchHelper.attachToRecyclerView(recyclerView);
```
- 现在运行下看看效果,就会发现滑动删除条目和长按拖拽条目就是实现了

4. 实现拖拽条目中的图片来拖拽条目效果
- 先写个OnDragListener接口
```java
/**
 *@description:适配器条目拖拽接口,用来回调ItemTouchHelper.startDrag(viewHolder);
 *@author:袁东华
 *created at 2016/8/31 0031 下午 1:25
 */
public interface OnDragListener {
    /**
     *@description:当条目需要拖拽的时候,适配器调用onDrag
     *@author:袁东华
     *created at 2016/8/31 0031 下午 1:26
     */
   void startDrag(RecyclerView.ViewHolder viewHolder);
}
```
- 在adapter中接收传入的接口:
```java

    private OnDragListener onDragListener;

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }

```
- 当触摸条目中的图片时,调用onDragListener.startDrag(holder),目的是回调itemTouchHelper.startDrag(viewHolder),实现拖拽效果,在adapter中的onBindViewHolder方法中写如下代码:
```java
//实现拖拽图片时拖拽条目
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //如果手指按下图片
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    //回调 itemTouchHelper.startDrag(viewHolder);
                    if (onDragListener != null) {
                        onDragListener.startDrag(holder);
                    }
                }

                return false;
            }
        });
```
给adapter传入接口,在activity中的代码如下:
```java
//设置条目拖拽接口
historyAdapter.setOnDragListener(new OnDragListener() {
            /**
             * @param viewHolder
             * @description:当条目需要拖拽的时候,适配器调用onDrag
             * @author:袁东华 created at 2016/8/31 0031 下午 1:26
             */
            @Override
            public void startDrag(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });
```
- 写到这里,拖拽条目中的图片来拖拽条目效果就实现了
5. 下面讲讲操作条目时增加透明度效果
- 写个接口OnStateChangedListener,增加2个方法,一个操作条目时调用,一个操作完成后调用
```java
/**
 * @description:条目状态改变的接口,只需要让ViewHoulder的itemView实现这个接口就ok了,目的是改变itemView的背景色
 * @author:袁东华 created at 2016/8/31 0031 下午 1:50
 */
public interface OnStateChangedListener {
    /**
     * @description:拖拽,滑动item时调用,可以做改变背景色操作
     * @author:袁东华 created at 2016/8/31 0031 下午 1:51
     */
    void onSelectedChanged();
    /**
     *@description:条目正常状态:拖拽,滑动结束后,背景色恢复正常
     *@author:袁东华
     *created at 2016/8/31 0031 下午 2:37
     */
    void clearView();
}
```
- adapter中ViewHolder实现此接口
```java
 public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,OnStateChangedListener {
   /**
         * @description:拖拽,滑动item时调用,可以做改变背景色操作
         * @author:袁东华 created at 2016/8/31 0031 下午 1:51
         */
        @Override
        public void onSelectedChanged() {
            itemView.setAlpha(0.5f);
        }

        /**
         * @description:条目正常状态:拖拽,滑动结束后,背景色恢复正常
         * @author:袁东华 created at 2016/8/31 0031 下午 2:37
         */
        @Override
        public void clearView() {

            itemView.setAlpha(1.0f);
        }
 }
```
- callback中重写3个方法
```java

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
```
- 当条目的状态改变时调用onSelectedChanged,也就是说当条目不是空闲状态时(正在拖拽或滑动时),在此方法中回调ViewHolder中的onSelectedChanged方法,设置条目透明度
- 当条目操作完成后,回复正常状态,调用callback中的clearView方法,在此方法中回调ViewHolder中的clearView方法,恢复条目透明度
- callback中onChildDraw这个方法,滑动删除条目时,可以绘制itemview,在这里个可以设置条目透明度
- 这样就完成了,操作条目时,改变透明度效果,当然也可以做其他改变,比如背景色等
#### 总结:以上就是RecyclerView拖拽条目,滑动删除条目效果的步骤了,如果有看不懂的请下载代码
