package org.iflab.ibistubydreamfactory.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CasActivity extends AppCompatActivity {

    @BindView(R.id.webView_cas)
    WebView webViewCas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cas);
        ButterKnife.bind(this);

        webViewCas.loadUrl(MyApplication.CAS_URL);
    }


}
