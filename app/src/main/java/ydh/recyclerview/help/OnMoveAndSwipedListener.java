package ydh.recyclerview.help;

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
