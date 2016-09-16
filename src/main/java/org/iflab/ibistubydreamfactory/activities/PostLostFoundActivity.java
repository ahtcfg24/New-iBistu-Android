package org.iflab.ibistubydreamfactory.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import org.iflab.ibistubydreamfactory.apis.LostFoundAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.LostFound;
import org.iflab.ibistubydreamfactory.models.LostFoundImageURL;
import org.iflab.ibistubydreamfactory.models.PostLostFoundSuccessModel;
import org.iflab.ibistubydreamfactory.models.UploadFileRequestBody;
import org.iflab.ibistubydreamfactory.models.UploadSuccessModel;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.FileUtil;
import org.iflab.ibistubydreamfactory.utils.JsonUtils;
import org.iflab.ibistubydreamfactory.utils.RegexConfirmUtils;

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
    ArrayList<String> selectedPhotos = new ArrayList<>();
    @BindView(R.id.edit_contact)
    EditText editContact;
    private View parentView;
    private String title, details, phone;
    private LostFoundAPI lostFoundAPI;
    private PhotoAdapter photoAdapter;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private User user;
    private UploadSuccessModel uploadSuccessModel;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LostFound lostFound = new LostFound();
            lostFound.setTitle(title);
            lostFound.setDetails(details);
            lostFound.setPhone(phone);
            lostFound.setAuthor(user.getName());
            List<LostFoundImageURL> imgUrlList = new ArrayList<>();
            for (UploadSuccessModel.UploadedResource resource : uploadSuccessModel.getResource()) {
                imgUrlList.add(new LostFoundImageURL(MyApplication.INSTANCE_URL + "files/" + resource
                        .getPath()));
            }
            lostFound.setImgUrlList(JsonUtils.list2Json(imgUrlList));
            LostFound[] lostFounds = new LostFound[]{lostFound};
            Call<PostLostFoundSuccessModel> call = lostFoundAPI.postNewLostFound(lostFounds);
            call.enqueue(new Callback<PostLostFoundSuccessModel>() {
                @Override
                public void onResponse(Call<PostLostFoundSuccessModel> call, Response<PostLostFoundSuccessModel> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {//如果成功
                        Intent intent = new Intent();
                        intent.putExtra("needRefresh", "yes");
                        intent.setClass(PostLostFoundActivity.this, LostFoundActivity.class);
                        startActivity(intent);
                        finish();
                    } else {//失败
                        ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                        onFailure(call, e.toException());
                    }

                }

                @Override
                public void onFailure(Call<PostLostFoundSuccessModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(parentView, "失败：" + t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_post_lost_found, null);
        setContentView(parentView);
        initView();
        ButterKnife.bind(this, parentView);
    }

    private void initView() {
        user = (User) aCache.getAsObject("user");
        lostFoundAPI = APISource.getInstance().getAPIObject(LostFoundAPI.class);

        RecyclerView recyclerViewSelectPhoto = (RecyclerView) parentView.findViewById(R.id.recyclerView_selectPhoto);
        recyclerViewSelectPhoto.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recyclerViewSelectPhoto.setAdapter(photoAdapter);
        recyclerViewSelectPhoto.addOnItemTouchListener(new RecyclerItemClickListener(PostLostFoundActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == selectedPhotos.size()) {
                    PhotoPicker.builder()
                               .setPhotoCount(3)
                               .setGridColumnCount(3)
                               .start(PostLostFoundActivity.this);
                } else {
                    PhotoPreview.builder()
                                .setPhotos(selectedPhotos)
                                .setCurrentItem(position)
                                .start(PostLostFoundActivity.this);
                }
            }
        }));
    }


    @OnClick({R.id.button_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_post:
                title = editTitle.getText().toString();
                details = editContent.getText().toString();
                phone = editContact.getText().toString();
                if (!RegexConfirmUtils.isMobile(phone)) {
                    Snackbar.make(parentView, "请填写正确的手机号！", Snackbar.LENGTH_SHORT).show();
                } else if (!RegexConfirmUtils.isLengthRight(title, 5, 30)) {
                    Snackbar.make(parentView, "标题长度在5-30之间！", Snackbar.LENGTH_SHORT).show();
                } else if (!RegexConfirmUtils.isLengthRight(details, 5, 250)) {
                    Snackbar.make(parentView, "描述长度在5-250之间！", Snackbar.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    List<UploadFileRequestBody.UploadResource> uploadResourceList = new ArrayList<>();
                    for (String imgPath : selectedPhotos) {
                        UploadFileRequestBody.UploadResource uploadResource = new UploadFileRequestBody.UploadResource();
                        uploadResource.setName(user.getId() + System.currentTimeMillis() + imgPath.substring(imgPath
                                .length() - 8));//为防止服务器端出现同名图片，使用用户名+时间+原图片名称后三位为图片命名
                        uploadResource.setContent(FileUtil.ImageToBase64Content(imgPath));
                        uploadResource.setIs_base64(true);
                        uploadResource.setType("file");
                        uploadResourceList.add(uploadResource);
                    }
                    UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody();
                    uploadFileRequestBody.setResource(uploadResourceList);
                    Call<UploadSuccessModel> call = lostFoundAPI.uploadFile(uploadFileRequestBody);
                    call.enqueue(new Callback<UploadSuccessModel>() {
                        @Override
                        public void onResponse(Call<UploadSuccessModel> call, Response<UploadSuccessModel> response) {
                            if (response.isSuccessful()) {//如果成功
                                uploadSuccessModel = response.body();
                                handler.post(runnable);

                            } else {//失败
                                ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                                onFailure(call, e.toException());
                            }
                        }

                        @Override
                        public void onFailure(Call<UploadSuccessModel> call, Throwable t) {
                            Snackbar.make(parentView, "上传失败：" + t.getMessage(), Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
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
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }
}
