package com.jackfruit.tbslocalwebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.jackfruit.tbslocalwebview.databinding.ActivityTbsDebugWebBinding;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class TBSDebugWebActivity extends AppCompatActivity {

  private ActivityTbsDebugWebBinding mBinding;
  private FrameLayout mFrameLayout;
  private WebSettings mWebSettings;
  private com.tencent.smtt.sdk.WebView mWebView;

  public static void start(Context context) {
    Intent starter = new Intent(context, TBSDebugWebActivity.class);
    context.startActivity(starter);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = ActivityTbsDebugWebBinding.inflate(getLayoutInflater());
    setContentView(mBinding.getRoot());
    mFrameLayout = mBinding.webview;
    mWebView = new com.tencent.smtt.sdk.WebView(this);
    if (mWebView != null) {
      Log.i("TAG", "可以使用: ");
    }else{
      Log.i("TAG", "不可以使用: ");
    }
    mFrameLayout.addView(mWebView);
    setWebSettings();
    mWebView.loadUrl("http://debugtbs.qq.com");
  }

  @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
  public void setWebSettings() {

    mWebSettings = mWebView.getSettings();
    // 支持屏幕缩放
    mWebSettings.setSupportZoom(true);
    // 设置内置的缩放控件。若为false，则该WebView不可缩放
    mWebSettings.setBuiltInZoomControls(true);
    // 不显示webview缩放按钮
    mWebSettings.setDisplayZoomControls(false);
    // 设置自适应屏幕宽度
    mWebSettings.setUseWideViewPort(true);
    mWebSettings.setLoadWithOverviewMode(true);
    mWebSettings.setAppCacheEnabled(true);
    mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    // 设置缓存模式
    mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    // 允许android调用javascript
    mWebSettings.setDomStorageEnabled(true);
    // 设置WebView对象的编码格式为UTF_8
    mWebSettings.setDefaultTextEncodingName("utf-8");
    // 解决图片不显示
    mWebSettings.setBlockNetworkImage(false);
    // 支持自动加载图片
    mWebSettings.setLoadsImagesAutomatically(true);
    // 设置不用系统浏览器打开,直接显示在当前WebView
    mWebView.setWebViewClient(new WebViewClient() {

      @Override
      public boolean shouldOverrideUrlLoading(final WebView view, String url) {
        // try {
        //  if (url.startsWith("http:") || url.startsWith("https:")) {
        //    view.loadUrl(url);
        //  } else {
        //    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        //    startActivity(intent);
        //  }
        //  return true;
        // } catch (Exception e) {
        //  return false;
        // }
        return false;
      }

      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // 加载完页面之后的处理
      }
    });
    mWebView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView webView, int i) {

      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    destroy(mWebView);
  }

  public void destroy(WebView webView) {
    if (webView != null) {
      // 再次打开页面时，若界面没有消亡，会导致进度条不显示并且界面崩溃
      webView.stopLoading();
      webView.onPause();
      webView.clearCache(true);
      webView.clearHistory();
      webView.removeAllViews();
      webView.destroyDrawingCache();
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        ViewGroup parent = (ViewGroup) webView.getParent();
        if (parent != null) {
          parent.removeView(webView);
        }
        webView.removeAllViews();
        webView.destroy();
      } else {
        webView.removeAllViews();
        webView.destroy();
        ViewGroup parent = (ViewGroup) webView.getParent();
        if (parent != null) {
          parent.removeView(webView);
        }
      }
      webView = null;
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (mFrameLayout != null) {
      mWebView.onPause();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mFrameLayout != null) {
      mWebView.onResume();
    }
  }
}