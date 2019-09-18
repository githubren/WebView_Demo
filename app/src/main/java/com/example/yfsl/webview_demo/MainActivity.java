package com.example.yfsl.webview_demo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private Button networkBtn,cacheBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initData() {
//        mWebView.loadUrl("http://www.baidu.com/");
        //使用webview显示html代码
        mWebView.loadDataWithBaseURL(null,"<html><head><title> 欢迎您 </title></head>" +
                "<body><h2>使用webview显示 html代码</h2></body></html>", "text/html" , "utf-8", null);
        mWebView.addJavascriptInterface(this,"android");
        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(webChromeClient);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(LOAD_NO_CACHE);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

    }

    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {//url拦截
            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(mWebView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定",null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();
            /*
                注意:
                必须要这一句代码:result.confirm()表示:
                处理结果为确定状态同时唤醒WebCore线程
                否则不能继续点击按钮
             */
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.e("TAG","title:"+title);
        }

        //进度条回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setProgress(newProgress);
        }
    };

    /**
     * 返回键事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 释放资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        mWebView = null;
    }

    private void initView() {
        mWebView = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.progress);
        networkBtn = findViewById(R.id.btn_network);
        cacheBtn = findViewById(R.id.btn_cache);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_network:
                mWebView.loadUrl("http://www.baidu.com/");
                break;
            case R.id.btn_cache:
                break;
        }
    }
}
