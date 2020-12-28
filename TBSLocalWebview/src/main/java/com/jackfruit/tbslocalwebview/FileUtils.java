package com.jackfruit.tbslocalwebview;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;

/** 常用操作文件工具类 */
public class FileUtils {

  private static final String TAG = FileUtils.class.getSimpleName();

  /**
   * 创建外部文档目录地址
   *
   * @return 文档目录地址
   */
  public static String createExternalDocumentsPath() {
    File file = new File(createXincaidongPath(), Environment.DIRECTORY_DOCUMENTS);
    if (!file.exists()) {
      file.mkdirs();
    }
    return file.getPath();
  }

  /**
   * 创建根缓存图片地址
   *
   * @return 图片地址
   */
  public static File createXincaidongPath() {
    String directoryPath = "";
    if (isSdCardAvailable()) {
      // /storage/emulated/0
      directoryPath = Environment
          .getExternalStorageDirectory()
          .getPath();
    } else {
      // /data
      directoryPath = Environment
          .getDataDirectory()
          .getPath();
    }
    File file = new File(directoryPath, "xincaidong");
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


  /**
   * 创建外部文档目录
   *
   * @return 文档File
   */
  public static File createExternalDocumentsFile() {
    File file = new File(createXincaidongPath(), Environment.DIRECTORY_DOCUMENTS);
    if (!file.exists()) {
      file.mkdirs();
    }
    return file;
  }

  /**
   * 获取系统文档地址
   *
   * @return 系统文档File
   */
  public static File createSystemDocumentsFile() {
    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    if (!file.exists()) {
      file.mkdirs();
    }
    return file;
  }

  /**
   * 获取系统下载地址
   *
   * @return 系统下载File
   */
  public static File createSystemDownloadFile() {
    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    if (!file.exists()) {
      file.mkdirs();
    }
    return file;
  }

  /**
   * 创建外部图片地址
   *
   * @return 图片地址
   */
  public static String createExternalPicturePath() {
    File file = new File(createXincaidongPath(), Environment.DIRECTORY_PICTURES);
    if (!file.exists()) {
      file.mkdirs();
    }
    return file.getPath();
  }

  /**
   * 创建外部图片文件
   *
   * @return 图片文件
   */
  public static File createExternalPictureFile() {
    File file = new File(createXincaidongPath(), Environment.DIRECTORY_PICTURES);
    if (!file.exists()) {
      file.mkdirs();
    }
    return file;
  }

  /**
   * 创建根缓存图片地址
   *
   * @return 图片地址
   */
  public static String createPicturePath(Context context) {
    String picturePath;
    if (isSdCardAvailable()) {
      // /sdcard/Android/data/<application package>/files/Pictures
      picturePath = context
          .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
          .getPath();
    } else {
      // /data/data/<application package>/files
      picturePath = context
          .getFilesDir()
          .getPath();
    }
    File file = new File(picturePath);
    if (!file.exists()) {
      file.mkdirs();
    }
    return picturePath;
  }

  /**
   * 创建根缓存图片文件
   *
   * @return 存储文件
   */
  public static File createPictureFile(Context context) {
    File file;
    if (isSdCardAvailable()) {
      // /sdcard/Android/data/<application package>/files/Pictures
      file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
   * 创建根缓存下载地址
   *
   * @return 下载地址
   */
  public static String createDownLoadPath(Context context) {
    String cacheRootPath = "";
    if (isSdCardAvailable()) {
      // /sdcard/Android/data/<application package>/files/Download
      cacheRootPath = context
          .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
          .getPath();
    } else {
      // /data/data/<application package>/files
      cacheRootPath = context
          .getFilesDir()
          .getPath();
    }
    File file = new File(cacheRootPath);
    if (!file.exists()) {
      file.mkdirs();
    }
    return cacheRootPath;
  }

  /**
   * 创建用户文档目录文件
   *
   * @return 下载文件
   */
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

  /** 创建简历下载的目录 */
  public static File createDownLoadResumeFile(Context context) {
    File file = new File(createDownLoadFile(context), "resume");
    if (!file.exists()) {
      file.mkdirs();
    }
    return file;
  }

  /**
   * 创建根缓存下载目录文件
   *
   * @return 下载文件
   */
  public static File createDownLoadFile(Context context) {
    File file;
    if (isSdCardAvailable()) {
      // /sdcard/Android/data/<application package>/files/Download
      file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
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
   * 创建根缓存音频文件目录
   *
   * @return 缓存文件
   */
  public static File createAudioFile(Context context) {
    File cachedir;
    if (isSdCardAvailable()) {
      // /sdcard/Android/data/<application package>/files/Music
      cachedir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
    } else {
      // /data/data/<application package>/files
      cachedir = context.getFilesDir();
    }
    if (!cachedir.exists()) {
      cachedir.mkdirs();
    }
    return cachedir;
  }

  /**
   * 递归创建文件夹
   *
   * @return 创建失败返回""
   */
  public static String createDir(String dirPath) {
    try {
      File file = new File(dirPath);
      if (file
          .getParentFile()
          .exists()) {
        file.mkdir();
        return file.getAbsolutePath();
      } else {
        createDir(file
            .getParentFile()
            .getAbsolutePath());
        file.mkdir();
      }
    } catch (Exception e) {
      Log.e(TAG, "Exception：", e);
    }
    return dirPath;
  }

  /** 重命名文件名称，oldPath 和 newPath必须是新旧文件的绝对路径 */

  /**
   * 递归创建文件夹
   *
   * @return 创建失败返回""
   */

  /**
   * 检查文件是否完整
   *
   * @param lifecycleOwner 被检查的fragment或者activity
   * @param file 需要检查的文件
   * @param resourceId 文件的资源id
   * @param listener 检查完成后回调
   */

  /**
   * 打开或者下载文件
   *
   * @param activity 当前的activity
   * @param autoCancel 实现了AutoCancel的类，用于不存在的时候停止下载
   * @param file 需要打开或下载的文件
   * @param url 下载的地址
   */

  /**
   * 打开或者下载文件
   *
   * @param activity 当前的activity
   * @param autoCancel 实现了AutoCancel的类，用于不存在的时候停止下载
   * @param file 需要打开或下载的文件
   * @param url 下载的地址
   * @param title 打开文件显示的标题，不需要可以传""
   * @param isOpen 是否打开文件
   * @param isShowDownloadBtn 是否在显示文件的页面显示下载文件的按钮
   */

  /**
   * 删除指定文件
   *
   * @throws IOException
   */
  public static boolean deleteFile2(File file) {
    if (file != null && file.exists()) {
      return file.delete();
    }
    return false;
  }

  /**
   * 获取图片缓存目录
   *
   * @return 创建失败, 返回""
   */
  public static String getImageCachePath(Context context) {
    File imgDir = new File(createCachePath(context), Environment.DIRECTORY_DOWNLOADS);
    File file = new File(imgDir.getAbsolutePath());
    if (!file.exists()) {
      file.mkdirs();
    }
    return file.getPath();
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
      cacheRootPath = context
          .getExternalCacheDir()
          .getPath();
    } else {
      // /data/data/<application package>/cache
      cacheRootPath = context
          .getCacheDir()
          .getPath();
    }
    File file = new File(cacheRootPath);
    if (!file.exists()) {
      file.mkdirs();
    }
    return cacheRootPath;
  }

  public static String getMusicCachePath(Context context) {
    File imgDir = new File(createCachePath(context), Environment.DIRECTORY_MUSIC);
    File file = new File(imgDir.getAbsolutePath());
    if (!file.exists()) {
      file.mkdirs();
    }
    return file.getPath();
  }

  /**
   * 获取图片裁剪缓存目录
   *
   * @return 创建失败, 返回""
   */
  public static String getImageCropCachePath(Context context) {
    File imgDir = new File(createCachePath(context), "imgCrop");
    File file = new File(imgDir.getAbsolutePath());
    if (!file.exists()) {
      file.mkdirs();
    }
    return file.getPath();
  }

  /**
   * 将内容写入文件
   *
   * @param filePath eg:/mnt/sdcard/demo.txt
   * @param content 内容
   */
  public static void writeFileSdcard(
      String filePath, String content, boolean isAppend) {
    try {
      FileOutputStream fout = new FileOutputStream(filePath, isAppend);
      byte[] bytes = content.getBytes();
      fout.write(bytes);
      fout.close();
    } catch (Exception e) {
      Log.e(TAG, "Exception：", e);
    }
  }

  /**
   * 打开Asset下的文件
   */
  public static InputStream openAssetFile(
      Context context, String fileName) {
    AssetManager am = context.getAssets();
    InputStream is = null;
    try {
      is = am.open(fileName);
    } catch (IOException e) {
      Log.e(TAG, "Exception：", e);
    }
    return is;
  }

  public static byte[] readAudioFile(
      Context context, String filename) {
    try {
      InputStream ins = context
          .getAssets()
          .open(filename);
      byte[] data = new byte[ins.available()];

      ins.read(data);
      ins.close();

      return data;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }

  /**
   * 将字节缓冲区按照固定大小进行分割成数组
   *
   * @param buffer 缓冲区
   * @param length 缓冲区大小
   * @param spsize 切割块大小
   */
  public static ArrayList< byte[] > splitBuffer(
      byte[] buffer, int length, int spsize) {
    ArrayList< byte[] > array = new ArrayList< byte[] >();
    if (spsize <= 0 || length <= 0 || buffer == null || buffer.length < length) return array;
    int size = 0;
    while (size < length) {
      int left = length - size;
      if (spsize < left) {
        byte[] sdata = new byte[spsize];
        System.arraycopy(buffer, size, sdata, 0, spsize);
        array.add(sdata);
        size += spsize;
      } else {
        byte[] sdata = new byte[left];
        System.arraycopy(buffer, size, sdata, 0, left);
        array.add(sdata);
        size += left;
      }
    }
    return array;
  }

  /**
   * 获取Raw下的文件内容
   *
   * @return 文件内容
   */
  public static String getFileFromRaw(
      Context context, int resId) {
    if (context == null) {
      return null;
    }

    StringBuilder s = new StringBuilder();
    try {
      InputStreamReader in = new InputStreamReader(context
          .getResources()
          .openRawResource(resId));
      BufferedReader br = new BufferedReader(in);
      String line;
      while ((line = br.readLine()) != null) {
        s.append(line);
      }
      return s.toString();
    } catch (IOException e) {
      Log.e(TAG, "Exception：", e);
      return null;
    }
  }

  /**
   * 文件拷贝
   *
   * @param src 源文件
   * @param desc 目的文件
   */
  public static void fileChannelCopy(
      File src, File desc) {
    FileInputStream fi = null;
    FileOutputStream fo = null;
    try {
      fi = new FileInputStream(src);
      fo = new FileOutputStream(desc);
      // 得到对应的文件通道
      FileChannel in = fi.getChannel();
      // 得到对应的文件通道
      FileChannel out = fo.getChannel();
      // 连接两个通道，并且从in通道读取，然后写入out通道
      in.transferTo(0, in.size(), out);
    } catch (IOException e) {
      Log.e(TAG, "IOException：", e);
    } finally {
      try {
        if (fo != null) {
          fo.close();
        }
        if (fi != null) {
          fi.close();
        }
      } catch (IOException e) {
        Log.e(TAG, "IOException：", e);
      }
    }
  }

  /**
   * 转换文件大小
   *
   * @param fileLen 单位B
   */
  public static String formatFileSizeToString(long fileLen) {
    DecimalFormat df = new DecimalFormat("#.00");
    String fileSizeString = "";
    if (fileLen < 1024) {
      fileSizeString = df.format((double) fileLen) + "B";
    } else if (fileLen < 1048576) {
      fileSizeString = df.format((double) fileLen / 1024) + "K";
    } else if (fileLen < 1073741824) {
      fileSizeString = df.format((double) fileLen / 1048576) + "M";
    } else {
      fileSizeString = df.format((double) fileLen / 1073741824) + "G";
    }
    return fileSizeString;
  }

  /**
   * 获取文件的大小
   */
  public static String GetFileSize(File file) {
    String size = "";
    if (file.exists() && file.isFile()) {
      long fileS = file.length();
      DecimalFormat df = new DecimalFormat("#.00");
      if (fileS < 1024) {
        size = df.format((double) fileS) + "BT";
      } else if (fileS < 1048576) {
        size = df.format((double) fileS / 1024) + "KB";
      } else if (fileS < 1073741824) {
        size = df.format((double) fileS / 1048576) + "MB";
      } else {
        size = df.format((double) fileS / 1073741824) + "GB";
      }
    } else if (file.exists() && file.isDirectory()) {
      size = "";
    } else {
      size = "0BT";
    }
    return size;
  }

  /**
   * 删除指定文件
   *
   * @throws IOException
   */
  public static boolean deleteFile(File file) throws IOException {
    return deleteFileOrDirectory(file);
  }

  /**
   * 删除指定文件，如果是文件夹，则递归删除
   *
   * @throws IOException
   */
  public static boolean deleteFileOrDirectory(File file) throws IOException {
    try {
      if (file != null && file.isFile()) {
        return file.exists() && file.delete();
      }
      if (file != null && file.isDirectory()) {
        File[] childFiles = file.listFiles();
        // 删除空文件夹
        if (childFiles == null || childFiles.length == 0) {
          return file.delete();
        }
        // 递归删除文件夹下的子文件
        for (int i = 0; i < childFiles.length; i++) {
          deleteFileOrDirectory(childFiles[i]);
        }
        return file.delete();
      }
    } catch (Exception e) {
      Log.e(TAG, "Exception：", e);
    }
    return false;
  }

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

  /*
   * Java文件操作 获取不带扩展名的文件名
   * */
  public static String getFileNameNoEx(String filename) {
    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length()))) {
        return filename.substring(0, dot);
      }
    }
    return filename;
  }

  /**
   * 获取文件内容
   */
  public static String getFileOutputString(String path) {
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(path), 8192);
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        sb
            .append("\n")
            .append(line);
      }
      bufferedReader.close();
      return sb.toString();
    } catch (IOException e) {
      Log.e(TAG, "IOException：", e);
    }
    return null;
  }

}
