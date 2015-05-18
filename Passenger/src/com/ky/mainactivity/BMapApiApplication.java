package com.ky.mainactivity;

import android.app.Application;
import android.widget.Toast;

public class BMapApiApplication extends Application {
    
//    public static BMapApiApplication mDemoApp;
//    
//    public static float mDensity;
//    
//    //百度MapAPI的管理类
//    public BMapManager mBMapMan = null;
//    
//    // 授权Key
//    // TODO: 请输入您的Key,
//    // 申请地址：http://dev.baidu.com/wiki/static/imap/key/
//    public String mStrKey = "F9FBF9FAA9BA37C0C6085BD280723A659EC51B20";
//    public boolean m_bKeyRight = true;    // 授权Key正确，验证通过
//    
//    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
//    public static class MyGeneralListener implements MKGeneralListener {
//        @Override
//        public void onGetNetworkState(int iError) {
//            Toast.makeText(BMapApiApplication.mDemoApp.getApplicationContext(), "您的网络出错啦！",
//                    Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onGetPermissionState(int iError) {
//            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
//                // 授权Key错误：
//                Toast.makeText(BMapApiApplication.mDemoApp.getApplicationContext(), 
//                        "请在BMapApiDemoApp.java文件输入正确的授权Key！",
//                        Toast.LENGTH_LONG).show();
//                BMapApiApplication.mDemoApp.m_bKeyRight = false;
//            }
//        }
//        
//    }
//    
//    @Override
//    public void onCreate() {
//        mDemoApp = this;
//        mBMapMan = new BMapManager(this);
//        mBMapMan.init(this.mStrKey, new MyGeneralListener());
//        
//        mDensity = getResources().getDisplayMetrics().density;
//        
//        super.onCreate();
//    }
//    
//    @Override
//    //建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
//    public void onTerminate() {
//        // TODO Auto-generated method stub
//        if (mBMapMan != null) {
//            mBMapMan.destroy();
//            mBMapMan = null;
//        }
//        super.onTerminate();
//    }

}
