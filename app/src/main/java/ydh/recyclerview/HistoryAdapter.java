package ydh.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;


/**
 *@description:历史记录的适配器
 *@author:袁东华
 *created at 2016/8/30 0030 下午 2:00
 */
public class HistoryAdapter extends Adapter<HistoryAdapter.ViewHolder> implements OnMoveAndSwipedListener {
    private OnItemClickListener mOnItemClickListener;
    private Activity activity;
    private ArrayList<Film> list;
    private Handler handler;

    public HistoryAdapter(Activity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
    }

    public void setList(ArrayList<Film> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        viewHolder = new ViewHolder(view, mOnItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        x.image().bind(holder.imageView, list.get(position).getThumb());
        holder.title_tv.setText(list.get(position).getTitle());
        holder.time_tv.setText(list.get(position).getTimeShow());
        holder.desc_tv.setText(list.get(position).getDescr());


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView title_tv;
        private TextView time_tv, desc_tv;

        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
            desc_tv = (TextView) itemView.findViewById(R.id.desc_tv);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getLayoutPosition());

            }
        }
    }

    /**
     * @Description:设置条目点击监听,供外部调用
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;

    }
}
