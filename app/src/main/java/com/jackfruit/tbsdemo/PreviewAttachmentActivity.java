package com.jackfruit.tbsdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

public class PreviewAttachmentActivity extends AppCompatActivity {

  private RelativeLayout mFlRoot;
  private TbsReaderView.ReaderCallback readerCallback =
      new TbsReaderView.ReaderCallback() {
        @Override
        public void onCallBackAction(Integer integer, Object o, Object o1) {}
      };

  private File mFile;
  private TbsReaderView mTbsReaderView;

  public static void start(Context context, String fileUrl) {
    Intent starter = new Intent(context, PreviewAttachmentActivity.class);
    starter.putExtra("fileUrl", fileUrl);
    context.startActivity(starter);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preview_attachment);
    initView();
    String fileUrl = getIntent().getStringExtra("fileUrl");
    mFile = new File(fileUrl);
    addTbsReaderView();
  }

  private void initView() {
    mFlRoot = findViewById(R.id.fl_container);
  }

  private void addTbsReaderView() {
    mTbsReaderView = new TbsReaderView(this, readerCallback);
    mFlRoot.addView(
        mTbsReaderView,
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      //不使用黑暗模式
      mTbsReaderView.setForceDarkAllowed(false);
    }
    String extensionName = FileUtils.getFileType(mFile.getPath());
    Bundle bundle = new Bundle();
    bundle.putString(TbsReaderView.KEY_FILE_PATH, mFile.getPath());
    bundle.putString(TbsReaderView.KEY_TEMP_PATH, FileUtils.createCachePath(this));
    boolean result = mTbsReaderView.preOpen(extensionName, false);
    if (result) {
      mTbsReaderView.openFile(bundle);
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    int rot = getWindowManager().getDefaultDisplay().getRotation();
    Log.d("TAG", "onConfigurationChanged : " + newConfig + ", rot : " + rot);
    if (rot == Surface.ROTATION_90 || rot == Surface.ROTATION_270) {
      mFlRoot.post(
          () -> {
            int height = mFlRoot.getHeight();
            int width = mFlRoot.getWidth();
            mTbsReaderView.onSizeChanged(width, height);
          });
    } else if (rot == Surface.ROTATION_0) {
      mFlRoot.post(
          () -> {
            int height = mFlRoot.getHeight();
            int width = mFlRoot.getWidth();
            mTbsReaderView.onSizeChanged(width, height);
          });
    }
  }

  @Override
  public void onDestroy() {
    if (mTbsReaderView != null) {
      mTbsReaderView.onStop();
    }
    super.onDestroy();
  }
}
