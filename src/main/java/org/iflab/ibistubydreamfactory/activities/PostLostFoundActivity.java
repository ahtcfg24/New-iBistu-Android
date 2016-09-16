package org.iflab.ibistubydreamfactory.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.adapters.PhotoAdapter;
import org.iflab.ibistubydreamfactory.adapters.RecyclerItemClickListener;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.UploadFilesAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.UploadFileRequestBody;
import org.iflab.ibistubydreamfactory.models.UploadSuccessModel;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLostFoundActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.edit_title)
    EditText editTitle;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.button_post)
    Button buttonPost;
    @BindView(R.id.selectButton)
    Button selectButton;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    private View rootView;
    private UploadFilesAPI uploadFilesAPI;
    private PhotoAdapter photoAdapter;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.activity_post_lost_found, null);
        setContentView(rootView);
        initView();
        ButterKnife.bind(this, rootView);
    }

    private void initView() {
        user = (User) aCache.getAsObject("user");
        uploadFilesAPI = APISource.getInstance().getAPIObject(UploadFilesAPI.class);

        RecyclerView recyclerViewSelectPhoto = (RecyclerView) rootView.findViewById(R.id.recyclerView_selectPhoto);
        recyclerViewSelectPhoto.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recyclerViewSelectPhoto.setAdapter(photoAdapter);
        recyclerViewSelectPhoto.addOnItemTouchListener(new RecyclerItemClickListener(PostLostFoundActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PhotoPreview.builder()
                            .setPhotos(selectedPhotos)
                            .setCurrentItem(position)
                            .start(PostLostFoundActivity.this);
            }
        }));
    }


    @OnClick({R.id.button_post, R.id.selectButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_post:
                List<UploadFileRequestBody.UploadResource> uploadResourceList = new ArrayList<>();
                for (String imgPath : selectedPhotos) {
                    UploadFileRequestBody.UploadResource uploadResource = new UploadFileRequestBody.UploadResource();
                    uploadResource.setName(user.getId() + System.currentTimeMillis() + imgPath.substring(imgPath
                            .length() - 8));
                    uploadResource.setContent(FileUtil.ImageToBase64Content(imgPath));
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
                PhotoPicker.builder()
                           .setPhotoCount(9)
                           .setGridColumnCount(4)
                           .start(PostLostFoundActivity.this);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            if (photos != null) {
                selectedPhotos.addAll(photos);
                for (String s : selectedPhotos) {
                    System.out.println(s.substring(s.length() - 4));
                }
            }
            photoAdapter.notifyDataSetChanged();
        }
    }
}
