package com.jackfruit.tbsdemo;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/**
 * @author 猿小蔡
 * @name TBSDemo
 * @class name：com.jackfruit.tbsdemo
 * @class describe
 * @createTime 2020/11/3 18:01
 * @change
 * @changTime
 */
public class FileUtils {

  /**
   * * 获取文件扩展名
   *
   * @param filename 文件名
   * @return 扩展名
   */
  public static String getFileType(String filename) {
    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length() - 1))) {
        return filename.substring(dot + 1);
      }
    }
    return null;
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
   * 创建根缓存目录
   *
   * @return 目录地址
   */
  public static String createCachePath(Context context) {
    String cacheRootPath;
    if (isSdCardAvailable()) {
      // /sdcard/Android/data/<application package>/cache
      cacheRootPath = context.getExternalCacheDir().getPath();
    } else {
      // /data/data/<application package>/cache
      cacheRootPath = context.getCacheDir().getPath();
    }
    File file = new File(cacheRootPath);
    if (!file.exists()) {
      file.mkdirs();
    }
    return cacheRootPath;
  }
  /**
   * SdCard是否存在
   *
   * @return true存在
   */
  public static boolean isSdCardAvailable() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }
}
