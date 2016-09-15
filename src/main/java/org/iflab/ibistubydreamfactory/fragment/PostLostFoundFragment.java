package org.iflab.ibistubydreamfactory.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.UploadFilesAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.UploadFileRequestBody;
import org.iflab.ibistubydreamfactory.models.UploadSuccessModel;
import org.iflab.ibistubydreamfactory.utils.UriUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLostFoundFragment extends Fragment {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.edit_title)
    EditText editTitle;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.gridView_selectedPhoto)
    GridView gridViewSelectedPhoto;
    @BindView(R.id.pathEdit)
    EditText pathEdit;
    @BindView(R.id.button_post)
    Button buttonPost;
    @BindView(R.id.selectButton)
    Button selectButton;
    @BindView(R.id.baseButton)
    Button baseButton;
    private View rootView;
    private String base64Content = "";
    private UploadFilesAPI uploadFilesAPI;


    public PostLostFoundFragment() {
    }

    public static PostLostFoundFragment newInstance() {
        return new PostLostFoundFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_post_lost_found, container, false);
        initView();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initView() {
        uploadFilesAPI = APISource.getInstance().getAPIObject(UploadFilesAPI.class);

    }


    @OnClick({R.id.button_post, R.id.selectButton, R.id.baseButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_post:
                List<UploadFileRequestBody.UploadResource> uploadResourceList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    UploadFileRequestBody.UploadResource uploadResource = new UploadFileRequestBody.UploadResource();
                    uploadResource.setName("img_" + i + ".jpg");
                    uploadResource.setContent(base64Content);
                    uploadResource.setIs_base64(true);
                    uploadResource.setType("file");
                    uploadResourceList.add(uploadResource);
                }
                UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody();
                uploadFileRequestBody.setResource(uploadResourceList);
                Call<UploadSuccessModel> call = uploadFilesAPI.uploadFile(uploadFileRequestBody);
                call.enqueue(new Callback<UploadSuccessModel>() {
                    @Override
                    public void onResponse(Call<UploadSuccessModel> call, Response<UploadSuccessModel> response) {
                        if (response.isSuccessful()) {//如果登录成功
                            UploadSuccessModel successModel = response.body();
                            editContent.setText(successModel.toString());

                        } else {//登录失败
                            ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                            onFailure(call, e.toException());
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadSuccessModel> call, Throwable t) {
                        Snackbar.make(rootView, "上传失败：" + t.getMessage(), Snackbar.LENGTH_LONG)
                                .show();
                    }
                });

                break;
            case R.id.selectButton:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");//设置打开类型
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.baseButton:
                String imagePath = pathEdit.getText().toString();
                File imageFile = new File(imagePath);
                try {
                    InputStream inputStream = new FileInputStream(imageFile);
                    byte[] buffer = new byte[(int) imageFile.length()];
                    inputStream.read(buffer);
                    inputStream.close();
                    base64Content = Base64.encodeToString(buffer, Base64.DEFAULT);

                    Log.i("base64:", base64Content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri
            String imagePath = UriUtils.getPathByUri4kitkat(getActivity(), uri);
            pathEdit.setText(imagePath);
        }
    }
}
