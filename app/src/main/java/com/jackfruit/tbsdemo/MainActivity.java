package com.jackfruit.tbsdemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private TextView mTv;
  private TextView mTv2;

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
  }

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
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv:
        // TODO 20/09/30
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
        // TODO 20/09/30
        WebActivity.start(this);
        break;
      default:
        break;
    }
  }
}
