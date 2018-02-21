package com.hanbit.app.katalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Network extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network);

        WebView webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);      // 멀티 터치 확대 enable
        settings.setJavaScriptEnabled(true);    // JSON 통신을 위해서 웹뷰에 자바스크립트를 enable 한다.
        webView.setWebChromeClient(new WebChromeClient());      // default 웹브로우저 설정
        webView.addJavascriptInterface(new JavascriptInterface(this), "hybrid");        // web 을 만들기 전에 장비 인터페이스 하는 javascriptInterface 가 먼저 정의 되어야 한다, hybrid 는 인터페이스의 키값이 된다
        webView.loadUrl("file:///android_asset/www/index.html");      // android_asset 안드로이드에서 정해진 경로값 [package].assets

    }
}
