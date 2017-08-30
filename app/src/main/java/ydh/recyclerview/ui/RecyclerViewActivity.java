package ydh.recyclerview.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.icu.util.Calendar;
import android.os.Build;
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

import java.util.ArrayList;

import ydh.recyclerview.data.Weather;
import ydh.recyclerview.adapter.HistoryAdapter;
import ydh.recyclerview.help.ItemTouchHelperCallback;
import ydh.recyclerview.help.OnDragListener;
import ydh.recyclerview.help.OnItemClickListener;
import ydh.recyclerview.R;


/**
 * @description:RecyclerView条目操作演示界面
 * @author:袁东华 created at 2016/8/25 0025 下午 5:12
 */

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<Weather> list;
    private Activity activity = RecyclerViewActivity.this;
    private int[] imgs = {R.drawable.icon_cloudy, R.drawable.icon_cloudy_nighttime,
            R.drawable.icon_gale, R.drawable.icon_heavy_rain,
            R.drawable.icon_heavy_snow, R.drawable.icon_meteor,
            R.drawable.icon_moon, R.drawable.icon_mostly_cloudy,
            R.drawable.icon_rain, R.drawable.icon_setting_sun,
            R.drawable.icon_snow, R.drawable.icon_stars,
            R.drawable.icon_sun, R.drawable.icon_sunrise_by_the_sea,
            R.drawable.icon_tornado, R.drawable.icon_rainbow,
            R.drawable.icon_rain_nighttime,R.drawable.icon_thundershower,
            R.drawable.icon_thunderstorm,R.drawable.icon_heavysnow};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("RecyclerView条目操作");
        getSupportActionBar().setSubtitle("长按拖动条目,滑动删除条目");
        initView();
        initData();
    }

    /**
     * 设置数据
     */
    @TargetApi(Build.VERSION_CODES.N)
    private void initData() {
        // 获取Calendar实例
        Calendar calendar = Calendar.getInstance();

        ArrayList<Weather> weatherList = new ArrayList<>();


        for (int i = 0; i < 20; i++) {
            Weather weather = new Weather();
            weather.setTitle("我是条目" +(i+1)+ "的标题");
            weather.setDescr("我是条目" +(i+1)+ "的描述");
            weather.setThumb(imgs[i]);
            weather.setTimeShow(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                    calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));
            weatherList.add(weather);
        }
        historyAdapter.setList(weatherList);
    }


    public void initView() {
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        historyAdapter = new HistoryAdapter(activity, handler);
        recyclerView.setAdapter(historyAdapter);
        //点击条目
        historyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(activity, "点击了条目" + (position+1), Toast.LENGTH_SHORT).show();

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
