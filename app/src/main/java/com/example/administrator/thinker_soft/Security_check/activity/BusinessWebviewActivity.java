package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
public class BusinessWebviewActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_webview);

        bindView();  //绑定控件ID
        setOnClickListener();//点击事件
        //webView加载业务办理APP网页
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        //webSettings.setDefaultFontSize(12);
        //webView.loadUrl("http://www.baidu.com/");
        webView.loadUrl("http://www.thksoft.cc/");
    }

    //绑定控件ID
    private void bindView() {
        webView = (WebView) findViewById(R.id.business_webview);
    }

    //点击事件
    private void setOnClickListener() {

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
    };

    class MyWebClient extends WebViewClient{
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
        //如果浏览器重新发送数据请求某个页面时，默认是不重新发送请求
        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            Log.i("onReceivedError","dontResend"+dontResend+"resend"+resend);
            super.onFormResubmission(view, dontResend, resend);
        }
        //通知浏览器更新链接到数据库
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            Log.i("doUpdateVisitedHistory","url"+url+"isReload"+isReload);
            super.doUpdateVisitedHistory(view, url, isReload);
        }
        //通知主机来处理ssl客户证书的请求
        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            Log.i("onReceivedClientCertRe","request"+request);
            super.onReceivedClientCertRequest(view, request);
        }
        //接收webview对应http身份验证的请求，
        //host 身份认证的主机
        //realm 一种描述用来帮助存储用户凭据以为将来访问所使用
        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            Log.i("onReceivedHttpAuth","handler"+handler+"host"+host+"realm"+realm);
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }
        //通知主机应用程序输入事件不是由WebView处理。
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            Log.i("shouldOverrideKeyEvent","event"+event);
            return super.shouldOverrideKeyEvent(view, event);
        }
        /**
         * 当尺寸大小改变
         * oldScale  就的缩放比
         * newScale 新的缩放比
         */
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            Log.i("onScaleChanged","oldScale"+oldScale+"newScale"+newScale);
            super.onScaleChanged(view, oldScale, newScale);
        }
        /**
         * 接收到登陆请求，也只有服务起要求登陆的时候，才会回调此方法
         * realm 一种描述用来帮助存储用户凭据以为将来访问所使用
         * account 账户
         * args 用于登录的用户的认证的具体参数
         */
        @Override
        public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
            Log.i("onReceivedLoginRequest","realm"+realm+"account"+account+"args"+args);
            super.onReceivedLoginRequest(view, realm, account, args);
        }
    }

    //按键点击监听回调
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK  && webView.canGoBack()){
            //如果点击的按钮是返回键的话 且浏览器能返回上一页
            //浏览器回到上一页
            webView.goBack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
