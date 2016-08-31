package ydh.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;


/**
 * @description:与电影资源有关的网络请求
 * @author:袁东华 created at 2016/8/22 0022 下午 5:08
 */
public class FilmDataHttp {

    /**
     * 删除历史记录
     * @param his_id
     * @param handler
     * @param success
     * @param error
     */
    public void resHistoryDelWt(String his_id, final Handler handler, final int success, final int error) {
        RequestParams params = new RequestParams("http://edifc.dressbook.cn/resHistoryDelWt.json");
        params.addBodyParameter("his_id", his_id);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {

            @Override
            public void onSuccess(JSONObject result) {
                try {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    msg.setData(data);
                    String message = result.optString("message");
                    if ("success".equals(message)) {
                        msg.what = success;

                    } else {
                        data.putString("message", message);
                        msg.what = error;

                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {

                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    /**
     * 获取播放记录列表
     *
     * @param user_id 用户id
     * @param handler 接收结果
     * @param success 成功标识
     * @param error   错误标识
     */
    public void getResHistoryList(String user_id, final Handler handler, final int success, final int error) {
        RequestParams params = new RequestParams("http://edifc.dressbook.cn/resHistoryList.json");
        params.addBodyParameter("user_id", user_id);

        x.http().get(params, new Callback.CommonCallback<JSONObject>() {

            @Override
            public void onSuccess(JSONObject result) {

                try {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    msg.setData(data);
                    String message = result.optString("message");
                    if ("success".equals(message)) {
                        msg.what = success;
                        if (!result.isNull("info")) {
                            JSONArray info = result.getJSONArray("info");
                            ArrayList<Film> filmList = new ArrayList<Film>();

                            for (int i = 0; i < info.length(); i++) {
                                JSONObject obji = info.getJSONObject(i);

                                Film film = new Film();
                                film.setHistoryId(obji.optString("id"));
                                if (!obji.isNull("resCourse")) {
                                    JSONObject resCourse = obji.getJSONObject("resCourse");

                                    film.setId(resCourse.optString("id"));
                                    film.setTitle(resCourse.optString("title"));
                                    film.setDescr(resCourse.optString("descr"));
                                    film.setUrl(resCourse.optString("url"));
                                    film.setAddTimeShow(resCourse.optString("addTimeShow"));
                                    film.setSort(resCourse.optString("sort"));
                                    film.setThumb("http://eddt.dressbook.cn" + resCourse.optString("thumb"));
                                    film.setSfrom("http://eddt.dressbook.cn" + resCourse.optString("sfrom"));
                                    film.setTimeShow(resCourse.optString("timeShow"));
                                    film.setAuth(resCourse.optString("auth"));
                                    film.setCanShow(resCourse.optString("canShow"));
                                    film.setHasFavourite(resCourse.optString("hasFavourite"));
                                    filmList.add(film);
                                }
                            }
                            data.putParcelableArrayList("list", filmList);
                        }

                    } else {
                        data.putString("message", message);
                        msg.what = error;

                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {

                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }



    private static FilmDataHttp instance;

    private FilmDataHttp() {
    }

    public static FilmDataHttp getInstance() {

        if (instance == null) {
            synchronized (FilmDataHttp.class) {
                if (instance == null) {
                    instance = new FilmDataHttp();
                }
            }
        }
        return instance;
    }
}
