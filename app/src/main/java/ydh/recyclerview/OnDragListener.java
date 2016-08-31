package ydh.recyclerview;

import android.support.v7.widget.RecyclerView;

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
