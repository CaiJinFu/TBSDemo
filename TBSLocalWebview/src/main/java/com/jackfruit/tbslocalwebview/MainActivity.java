package com.jackfruit.tbslocalwebview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jackfruit.tbslocalwebview.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(mBinding.getRoot());

    //从Assets中将文件复制到本地
    copyfile(FileUtils
        .createDocumentsFile(this)
        .getAbsolutePath(), "1609127970839.pdf", "1609127970839.pdf");
    copyfile(FileUtils
        .createDocumentsFile(this)
        .getAbsolutePath(), "1609128525690.docx", "1609128525690.docx");
  }

  public void copyfile(String filepath, String fileName, String assetsName) {
    try {
      if (!new File(filepath + "/" + fileName).exists()) {

        InputStream is = getResources()
            .getAssets()
            .open(assetsName);

        FileOutputStream fos = new FileOutputStream(filepath + "/" + fileName);
        byte[] buffer = new byte[7168];
        int count = 0;
        while ((count = is.read(buffer)) > 0) {
          fos.write(buffer, 0, count);
        }
        fos.close();
        is.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void openDebug(View view) {
    TBSDebugWebActivity.start(this);
  }

  public void openPdfFile(View view) {
    File documentsFile = FileUtils.createDocumentsFile(this);
    File file = new File(documentsFile, "1609127970839.pdf");
    PreviewAttachmentActivity.start(this, file.getAbsolutePath());
  }

  public void openDoxFile(View view) {
    File documentsFile = FileUtils.createDocumentsFile(this);
    File file = new File(documentsFile, "1609128525690.docx");
    PreviewAttachmentActivity.start(this, file.getAbsolutePath());
  }
}