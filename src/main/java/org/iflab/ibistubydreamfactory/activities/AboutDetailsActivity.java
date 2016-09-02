package org.iflab.ibistubydreamfactory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import org.iflab.ibistubydreamfactory.R;

public class AboutDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);
        WebView webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String aboutContent = intent.getStringExtra("aboutContent");
        setTitle(intent.getStringExtra("aboutName"));//获取传过来的标题并设为activity的标题
            /*使用loadData会乱码，原因未知*/
        webView.loadDataWithBaseURL(null, aboutContent, "text/html", "utf-8", null);
    }



}
