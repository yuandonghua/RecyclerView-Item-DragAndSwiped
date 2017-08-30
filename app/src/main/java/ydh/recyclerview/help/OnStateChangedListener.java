package ydh.recyclerview.help;

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