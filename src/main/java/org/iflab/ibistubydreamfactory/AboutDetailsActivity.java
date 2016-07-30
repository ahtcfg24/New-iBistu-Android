package org.iflab.ibistubydreamfactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class AboutDetailsActivity extends AppCompatActivity {
    private WebView webView;
    private Intent intent;
    private String aboutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);
        webView = (WebView) findViewById(R.id.webView);
        intent = getIntent();
        aboutContent = intent.getStringExtra("aboutContent");//获取传过来的模块内容
        getSupportActionBar().setTitle(intent.getStringExtra("aboutName"));//获取传过来的标题并设为activity的标题
            /*使用loadData会乱码，原因未知*/
        webView.loadDataWithBaseURL(null, aboutContent, "text/html", "utf-8", null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

}
