package org.iflab.ibistubydreamfactory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.LostFound;

public class LostFoundDetailActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found_detail);

        init();
    }

    private void init() {
        intent = new Intent();
        LostFound lostFound = (LostFound) intent.getSerializableExtra("lostFound");
        setTitle(lostFound.getTitle());

    }
}
