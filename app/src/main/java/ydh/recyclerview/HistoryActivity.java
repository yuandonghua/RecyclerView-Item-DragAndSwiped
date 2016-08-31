package ydh.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * @description:播放历史记录界面
 * @author:袁东华 created at 2016/8/25 0025 下午 5:12
 */
@ContentView(R.layout.activity_history)
public class HistoryActivity extends AppCompatActivity {
    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<Film> list;
    private Activity activity=HistoryActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getSupportActionBar().setTitle("播放记录");
        getSupportActionBar().setSubtitle("长按拖动条目,滑动删除条目");
        initView();
        //获取历史记录
        FilmDataHttp.getInstance().getResHistoryList("9", handler, 1,-1);
    }


    public void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        historyAdapter = new HistoryAdapter(activity, handler);
        recyclerView.setAdapter(historyAdapter);
        //点击条目
        historyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(activity, "点击了条目"+position, Toast.LENGTH_SHORT).show();

            }
        });

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



    }



    public Handler handler = new Handler() {
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //获取电影列表成功
                case 1:
                    Bundle data = msg.getData();
                    if (data != null) {
                        list = data.getParcelableArrayList("list");
                        historyAdapter.setList(list);
                    }

                    break;
                //获取电影列表失败
                case -1:
                    Bundle errorData = msg.getData();
                    if (errorData != null) {
                        String message = errorData.getString("message");
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


}
