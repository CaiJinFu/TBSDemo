package com.jackfruit.tbsdemo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import java.util.HashMap;

/**
 * @author 猿小蔡
 * @name TBSDemo
 * @class name：com.jackfruit.tbsdemo
 * @class describe
 * @createTime 2020/9/30 15:47
 * @change
 * @changTime
 */
public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    initTBS();
  }

  public void initTBS() {
    HashMap map = new HashMap();
    map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
    map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
    QbSdk.initTbsSettings(map);

    QbSdk.setDownloadWithoutWifi(true);
    QbSdk.setTbsListener(
        new TbsListener() {
          @Override
          public void onDownloadFinish(int i) {
            Log.d("QbSdk", "onDownloadFinish -->下载X5内核完成：" + i);
          }

          @Override
          public void onInstallFinish(int i) {
            Log.d("QbSdk", "onInstallFinish -->安装X5内核进度：" + i);
          }

          @Override
          public void onDownloadProgress(int i) {
            Log.d("QbSdk", "onDownloadProgress -->下载X5内核进度：" + i);
          }
        });

    QbSdk.PreInitCallback cb =
        new QbSdk.PreInitCallback() {
          @Override
          public void onViewInitFinished(boolean arg0) {
            // x5內核初始化完成的回调，true表x5内核加载成功，否则表加载失败，会自动切换到系统内核。
            Log.d("QbSdk", " 内核加载 " + arg0);
            Toast.makeText(App.this, " 内核加载 " + arg0, Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onCoreInitFinished() {}
        };

    // x5内核初始化接口
    QbSdk.initX5Environment(getApplicationContext(), cb);
    Log.i("QbSdk", "是否可以加载X5内核: " + QbSdk.canLoadX5(this));
    Log.i("QbSdk", "app是否主动禁用了X5内核: " + QbSdk.getIsSysWebViewForcedByOuter());
  }
}
