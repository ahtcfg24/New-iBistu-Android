package org.iflab.ibistubydreamfactory.utils;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.iflab.ibistubydreamfactory.R;

/**
 * 用于快速构建spinner
 */
public class SpinnerBuildUtil {

    private Activity activity;
    private SparseArray<String> data;
    private ClickHandler clickHandler;
    private int defaultKey;

    public interface ClickHandler{
        void onSpinnerClicked(int index, int key, String value);
    }

    public SpinnerBuildUtil(Activity activity) {
        this.activity = activity;
    }

    public SpinnerBuildUtil setDefault(int key){
        defaultKey = key;
        return this;
    }

    public SpinnerBuildUtil setData(SparseArray<String> data){
        this.data = data;
        return this;
    }

    public SpinnerBuildUtil setClickHandler(ClickHandler clickHandler){
        this.clickHandler = clickHandler;
        return this;
    }

    public void init(int id){
        if (data == null){
            return;
        }

        Spinner spinner = (Spinner) activity.findViewById(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.item_simple_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < data.size(); i++){
            String value = data.valueAt(i);
            adapter.add(value);
        }

        spinner.setAdapter(adapter);
        int index = data.indexOfKey(defaultKey);
        index = index == -1 ? 0 : index;
        spinner.setSelection(index);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int key = data.keyAt(position);
                String value = data.valueAt(position);
                if (clickHandler != null){
                    clickHandler.onSpinnerClicked(position,key,value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static SpinnerBuildUtil with(Activity activity){
        return new SpinnerBuildUtil(activity);
    }
}
