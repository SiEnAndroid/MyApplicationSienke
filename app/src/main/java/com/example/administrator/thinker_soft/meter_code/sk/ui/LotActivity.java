package com.example.administrator.thinker_soft.meter_code.sk.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 移动报装
 * Created by Administrator on 2018/7/19.
 */

public class LotActivity  extends BaseActivity{
    //储存
    private SharedPreferences sharedPreferences_login;
    private String userName,userId;
    private View mErrorView;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.back)
    TextView home;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_lot;
    }

    @Override
    protected void initView() {

        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        userName = sharedPreferences_login.getString("user_name", "");
        userId = sharedPreferences_login.getString("userId", "");
        final String urlLoad = new StringBuffer().append(SkUrl.SKWEB).append("&id=").append(userId)
                .append("&name=").append(userName).append("&address=").toString();


//        //webView加载业务办理网页
//        webView.setWebViewClient(new MyWebClient());
//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        webView.setBackgroundColor(Color.TRANSPARENT);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setDefaultTextEncodingName("UTF-8");
//        // 让WebView能够执行javaScript
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setBlockNetworkImage(true);
//        // 让JavaScript可以自动打开windows
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        // 设置缓存
//        webSettings.setAppCacheEnabled(true);
//        // 设置缓存模式,一共有四种模式
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        // 设置缓存路径
////        webSettings.setAppCachePath("");
//        // 支持缩放(适配到当前屏幕)
//        webSettings.setSupportZoom(true);
//        // 将图片调整到合适的大小
//        webSettings.setUseWideViewPort(true);
//        // 支持内容重新布局,一共有四种方式
//        // 默认的是NARROW_COLUMNS
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        // 设置可以被显示的屏幕控制
//        webSettings.setDisplayZoomControls(true);
//        webSettings.setAllowContentAccess(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setSavePassword(true);
//        webSettings.setSaveFormData(true);
//
//        webSettings.setLoadsImagesAutomatically(true);
//
//        //webview加载进度
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
//                    home.setVisibility(View.VISIBLE);
//                } else {
//                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
//                    progressBar.setProgress(newProgress);//设置进度值
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//        });
//        webView.loadUrl(urlLoad);


        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setBlockNetworkImage(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                    home.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });
        //加入线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebSettings wSet = webView.getSettings();
                wSet.setJavaScriptEnabled(true);

                webView.loadUrl(urlLoad);
            }
        });


    }

private class MyWebClient extends WebViewClient{
    //执行页面加载之前的回调函数
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.i("shouldOverrideUrlLoad","request"+request);
        //return super.shouldOverrideUrlLoading(view, request);
        return false;
    }
    //页面开始加载监听
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.i("onPageStarted","url"+url+"favicon"+favicon);
        super.onPageStarted(view, url, favicon);
    }
    //页面加载结束监听  此时页面加载完成但是图片可能还在加载中
    @Override
    public void onPageFinished(WebView view, String url) {
        Log.i("onPageFinished","url"+url);
        super.onPageFinished(view, url);
    }
    //加载网页资源的时候调用
    @Override
    public void onLoadResource(WebView view, String url) {
        Log.i("onLoadResource","url"+url);
        super.onLoadResource(view, url);
    }

    //通知主机应用程序WebView内容遗留下来的前一页导航将不再。
    @Override
    public void onPageCommitVisible(WebView view, String url) {
        Log.i("onPageCommitVisible","url"+url);
        super.onPageCommitVisible(view, url);
    }
    //应用程序加载访问网页而返回得到的数据
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        Log.i("shouldInterceptRequest","request"+request);
        return super.shouldInterceptRequest(view, request);
    }
    //访问某个网页返回错误是回调
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.i("onReceivedError","request"+request+"error"+error);
        super.onReceivedError(view, request, error);
    }
    //http请求错误返回
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        Log.i("onReceivedError","request"+request+"errorResponse"+errorResponse);
        super.onReceivedHttpError(view, request, errorResponse);
    }
    //接受访问安全连接的错误
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Log.i("onReceivedSslError","handler"+handler+"error"+error);
        super.onReceivedSslError(view, handler, error);
    }
}

    @OnClick(R.id.back)
    public void onClickBack(View view){
        finish();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen","是否有上一个页面:"+webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


}
