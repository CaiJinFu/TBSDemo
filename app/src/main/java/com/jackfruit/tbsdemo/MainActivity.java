package com.jackfruit.tbsdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private TextView mTv;
  private TextView mTv2;
  private TextView mTv3;

  public static File createDocumentsFile(Context context) {
    File file;
    if (isSdCardAvailable()) {
      // /sdcard/Android/data/<application package>/files/Download
      file = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    } else {
      // /data/data/<application package>/files
      file = context.getFilesDir();
    }
    if (!file.exists()) {
      file.mkdirs();
    }
    return file;
  }

  /**
   * SdCard是否存在
   *
   * @return true存在
   */
  public static boolean isSdCardAvailable() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
    createDocumentsFile(this);
  }

  private void initView() {
    mTv = (TextView) findViewById(R.id.tv);
    mTv.setOnClickListener(this);
    mTv2 = (TextView) findViewById(R.id.tv2);
    mTv2.setOnClickListener(this);
    mTv3 = (TextView) findViewById(R.id.tv3);
    mTv3.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv:
        QbSdk.openFileReader(
            this,
            "/sdcard/Android/data/com.jackfruit.tbsdemo/files/Documents/24049_预案公告.pdf",
            null,
            new ValueCallback<String>() {
              @Override
              public void onReceiveValue(String s) {}
            });
        break;
      case R.id.tv2:
        WebActivity.start(this);
        break;
      case R.id.tv3:
        Log.i("QbSdk", "是否可以加载X5内核: " + QbSdk.canLoadX5(this));
        // 重新下载
        // TbsDownloader.startDownload(this);
        // 如果可以加载X5内核，则直接重启，否则重置x5
        // 如果只是需要下载的话则调用TbsDownloader.startDownload(this);
        // 可能会有意想不到加载不了的问题，重启是最简单粗暴的方法。
        if (QbSdk.canLoadX5(this)) {
          rebootApp();
        } else {
          // 重置X5的配置
          QbSdk.reset(this);
          rebootApp();
        }
        break;
      default:
        break;
    }
  }

  private void rebootApp() {
    Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    finish();
    // 从操作系统中结束掉当前程序的进程
    android.os.Process.killProcess(android.os.Process.myPid());
    // 退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
    System.exit(0);
  }
}
