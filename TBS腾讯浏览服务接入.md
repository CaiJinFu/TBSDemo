# TBS腾讯浏览服务接入

[TBS文档接入地址](https://x5.tencent.com/docs/access.html)

基本上的话照着这个文档接入是没有什么问题的，但是打开本地文件的时候，还是出现了一点小问题，因为文档里面没有说明。



### 基础配置

现在的Android开发都使用Android Studio了，所以只需要在app的build.gradle里面添加依赖，这份文章的日期是2020/9/30，最新id版本是下面这个

```java
api 'com.tencent.tbs.tbssdk:sdk:43939'
```

#### 权限配置

```java
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

#### 混淆配置

```java
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}
```

#### 异常上报配置

为了提高合作方的webview场景稳定性，及时发现并解决x5相关问题，当客户端发生crash等异常情况并上报给服务器时请务必带上x5内核相关信息。x5内核异常信息获取接口为：com.tencent.smtt.sdk.WebView.getCrashExtraMessage(context)。以bugly日志上报为例：

```java
UserStrategy strategy = new UserStrategy(appContext);
　　strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
　　　　public Map<String, String> onCrashHandleStart(
            int crashType, 
            String errorType, 
            String errorMessage, 
            String errorStack) {

            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            String x5CrashInfo = com.tencent.smtt.sdk.WebView.getCrashExtraMessage(appContext);
            map.put("x5crashInfo", x5CrashInfo);
            return map;
　　　　}
　　　　@Override
　　　　public byte[] onCrashHandleStart2GetExtraDatas(
            int crashType, 
            String errorType, 
            String errorMessage, 
            String errorStack) {
            try {
                return "Extra data.".getBytes("UTF-8");
            } catch (Exception e) {
                return null;
            }
　　　　}
　　});

　　CrashReport.initCrashReport(appContext, APPID, true, strategy);
```

#### 首次初始化冷启动优化

TBS内核首次使用和加载时，ART虚拟机会将Dex文件转为Oat，该过程由系统底层触发且耗时较长，很容易引起anr问题，解决方法是使用TBS的 ”dex2oat优化方案“。

（1）. 设置开启优化方案

```java
// 在调用TBS初始化、创建WebView之前进行如下配置 
HashMap map = new HashMap(); 
map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true); 
map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true); 
QbSdk.initTbsSettings(map);
```

（2）. 增加Service声明

1. 在AndroidManifest.xml中增加内核首次加载时优化Service声明。
2. 该Service仅在TBS内核首次Dex加载时触发并执行dex2oat任务，任务完成后自动结束。

```java
<service 
android:name="com.tencent.smtt.export.external.DexClassLoaderProviderService"
android:label="dexopt"
android:process=":dexopt" >
</service>
```

#### 初始化X5内核

```java
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
          }

          @Override
          public void onCoreInitFinished() {}
        };

    // x5内核初始化接口
    QbSdk.initX5Environment(getApplicationContext(), cb);
```

### WebView接入

这部分其实就是将原生webview的东西全部换成带有com.tencent.smtt包名的，TBS文档上有介绍，这里就不做说明了。

### 文件接入

这个应该才是重点。

#### 支持格式

TBS已提供9种主流文件格式的本地打开，如果您需要使用更高级的能力请使用QQ浏览器打开文件

- 接入TBS可支持打开文件格式：`doc、docx、ppt、pptx、xls、xlsx、pdf、txt、epub`
- 调用QQ浏览器可打开：`rar（包含加密格式）、zip（包含加密格式）、tar、bz2、gz、7z（包含加密格式）、doc、docx、ppt、pptx、xls、xlsx、txt、pdf、epub、chm、html/htm、xml、mht、url、ini、log、bat、php、js、lrc、jpg、jpeg、png、gif、bmp、tiff 、webp、mp3、m4a、aac、amr、wav、ogg、mid、ra、wma、mpga、ape、flac`

#### 接口介绍

```Java
public static int openFileReader(
    Context context, 
    String filePath, 
    HashMap<String, String> extraParams,
    ValueCallback<String> callback
)
```

（1）此方法在`Qbsdk`类下

（2）调用之后，优先调起 QQ 浏览器打开文件。如果没有安装 QQ 浏览器，在 X5 内核下调起简版 QB 打开文 件。如果使用的系统内核，则调起文件阅读器弹框。

（3）暂时只支持本地文件打开

`context`: 调起 `miniqb` 的 `Activity` 的 `context`。此参数只能是 `activity` 类型的 `context`，不能设置为 `Application` 的 context。 `filePath`: 文件路径。格式为 `android` 本地存储路径格式，例如:`/sdcard/Download/xxx.doc`. 不支持 `file:///` 格式。暂不支持在线文件。 `extraParams`: `miniqb` 的扩展功能。为非必填项，若无需特殊配置，`默认可传入null`。扩展功能参考“文件功能定制” `ValueCallback`: 提供 `miniqb` 打开/关闭时给调用方回调通知,以便应用层做相应处理，您可以在出现以下回调时结束您的进程，节约内存占用。主要回调值如下：

```java
filepath error
TbsReaderDialogClosed 
default browser
fileReaderClosed
```

返回值：

```java
1:用 QQ 浏览器打开
2:用 MiniQB 打开
3:调起阅读器弹框
-1:filePath 为空 打开失败 
```

```java
public static void closeFileReader(Context context)
```

主动关闭文件打开ui,并清理相应内存占用。

#### 接入示例

```java
HashMap<String, String> params = new HashMap<String, String>(); 
params.put("style", "1"); 
params.put("local", "true"); 
params.put("memuData", jsondata); 
QbSdk.openFileReader(ctx,”/sdcard/xxx.doc”, params,callback);
```

到这你以为就能顺利打开浏览文件了吗？还是太年轻了，腾讯的文档不给你挖点坑，让你养成自己解决bug的习惯是不会罢休的。当你满心欢喜调用QbSdk.openFileReade的时候，log会给你报一个错。

```java
QbSdk: Must declare com.tencent.smtt.utils.FileProvider in AndroidManifest above Android 7.0,please view document in x5.tencent.com
```

意思就是让你在AndroidManifest.xml中添加provider，因为Android版本7.0访问文件的方式改变了。这时候添加一下就可以了。

首先在AndroidManifest.xml添加以下代码

```java
    <provider
      android:name="com.tencent.smtt.utils.FileProvider"
      android:authorities="${applicationId}.provider"
      android:exported="false"
      android:grantUriPermissions="true">
      <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/x5webview_file_paths" />
    </provider>

```

然后在res文件夹下创建xml文件夹，如果有了则跳过，接着再创建一个x5webview_file_paths.xml的文件，内容如下

```java
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path name="sdcard" path="."/>
</paths>
```

再运行，应该是没有问题的了，这里需要保证的是文件的路径是要对的，因为不能在线浏览，只能把文件下载到本地才能调用。这个错误我看了很久的文档都没有找到，后面在网上搜索，给出了以下的代码，

```java
<provider
    android:name="com.tencent.smtt.utils.FileProvider"
    android:authorities="包名.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/xml文件名" />
</provider>
```

这错就错在android:authorities="包名.fileprovider"，因为后来我去调用服务开启的QbSdk的类中，看这个错误发生的时机，发现他们是通过类型+“.provider”来校验的，所以只要改成android:authorities="包名.provider"就可以了，这个错误调试了很久，还是得在源码中才能发现春天。

### 其他问题

#### targetAPI为29时

如果Android的targetSdkVersion是29的话，需要在application下面配置android:requestLegacyExternalStorage="true"

#### targetAPI为Android P时无法下载内核？

由于内核下载安装和查询是否可用需要向后台发送请求，目前还有部分请求为http格式，当targetAPI为28时非Https请求将会被block，会导致部分内核功能异常。您可以手动降低targetAPi到27及以下或者在您的AndroidManifst.xml中的Application标签中添加

```java
android:networkSecurityConfig="@xml/network_security_config"
```

并在app的res/xml目录中添加network_security_config.xml文件，文件内容为

```java
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
<base-config cleartextTrafficPermitted="true" />
</network-security-config>
```

由于内核初始化需要时间，在这段时间里需要等待一会再打开文件浏览，否则会加载失败。假如出现加载失败，卸载重新安装。