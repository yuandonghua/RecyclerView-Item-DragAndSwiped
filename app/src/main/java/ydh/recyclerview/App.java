package ydh.recyclerview;

import android.app.Application;

import org.xutils.x;

/**
 *@description:项目的Application
 *@author:袁东华
 *created at 2016/8/22 0022 上午 10:34
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志, 开启debug会影响性能.
    }
}
