package org.iflab.ibistubydreamfactory.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.adapters.PhotoSelectorAdapter;
import org.iflab.ibistubydreamfactory.adapters.RecyclerItemClickListener;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.LostFoundAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.LostFound;
import org.iflab.ibistubydreamfactory.models.LostFoundImageURL;
import org.iflab.ibistubydreamfactory.models.UploadFileRequestBody;
import org.iflab.ibistubydreamfactory.models.UploadSuccessModel;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.AndroidUtils;
import org.iflab.ibistubydreamfactory.utils.ImageUtil;
import org.iflab.ibistubydreamfactory.utils.JsonUtils;
import org.iflab.ibistubydreamfactory.utils.RegexConfirmUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLostFoundActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.button_post)
    Button buttonPost;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    @BindView(R.id.edit_contact)
    EditText editPhone;
    private static final int PERMISSION_CODE_STORAGE = 002;
    private View parentView;
    private String details, phone;
    private LostFoundAPI lostFoundAPI;
    private PhotoSelectorAdapter photoSelectorAdapter;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private User user;
    private UploadSuccessModel uploadSuccessModel;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {//图片上传成功后再将返回的图片路径和lostfound信息一起发布
            LostFound lostFound = new LostFound();
            lostFound.setDetails(details);
            lostFound.setPhone(phone);
            lostFound.setAuthor(user.getName());
            List<LostFoundImageURL> imgUrlList = new ArrayList<>();
            for (UploadSuccessModel.UploadedResource resource : uploadSuccessModel.getResource()) {
                imgUrlList.add(new LostFoundImageURL("files/" + resource
                        .getPath()));
            }
            lostFound.setImgUrlList(JsonUtils.list2Json(imgUrlList));
            LostFound[] lostFounds = new LostFound[]{lostFound};
            Call<ResponseBody> call = lostFoundAPI.postNewLostFound(lostFounds);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {//如果成功
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//清除activity栈中该activity之上的所有的activity，最后清除该activity以前的对象，然后重建该activity
                        intent.setClass(PostLostFoundActivity.this, LostFoundActivity.class);
                        startActivity(intent);
                        finish();
                    } else {//失败
                        ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                        onFailure(call, e.toException());
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
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
        photoSelectorAdapter = new PhotoSelectorAdapter(this, selectedPhotos);
        recyclerViewSelectPhoto.setAdapter(photoSelectorAdapter);
        recyclerViewSelectPhoto.addOnItemTouchListener(new RecyclerItemClickListener(PostLostFoundActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == selectedPhotos.size()) {//点击+选择图片
                    if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(PostLostFoundActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        PhotoPicker.builder()
                                   .setPhotoCount(PhotoSelectorAdapter.MAX_SELECT_PHOTOS_COUNT)
                                   .setGridColumnCount(3)
                                   .setSelected(selectedPhotos)
                                   .start(PostLostFoundActivity.this);
                    } else {
                        ActivityCompat.requestPermissions(PostLostFoundActivity.this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, PERMISSION_CODE_STORAGE);
                    }


                } else {
                    PhotoPreview.builder()//预览图片
                                .setPhotos(selectedPhotos)
                                .setCurrentItem(position)
                                .start(PostLostFoundActivity.this);
                }
            }
        }));
    }

    /**
     * 授权回调
     *
     * @param grantResults grantResults[i] 对应的权限用户是否授权
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE_STORAGE://同类权限只要有一个被允许，其他都算允许
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//用户授权了
                    PhotoPicker.builder()
                               .setPhotoCount(PhotoSelectorAdapter.MAX_SELECT_PHOTOS_COUNT)
                               .setGridColumnCount(3)
                               .setSelected(selectedPhotos)
                               .start(PostLostFoundActivity.this);
                } else {
                    Toast.makeText(PostLostFoundActivity.this, "没有访问存储权限，无法选择图片！", Toast.LENGTH_SHORT)
                         .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick({R.id.button_post})
    public void onClick(View view) {
        AndroidUtils.hideSoftInput(PostLostFoundActivity.this);
        switch (view.getId()) {
            case R.id.button_post:
                details = editContent.getText().toString();
                phone = editPhone.getText().toString();
                if (!RegexConfirmUtils.isLengthRight(details, 4, 120)) {
                    Snackbar.make(parentView, "描述长度在5-120之间！", Snackbar.LENGTH_SHORT).show();
                } else if (!RegexConfirmUtils.isMobile(phone)) {
                    Snackbar.make(parentView, "请填写正确的手机号！", Snackbar.LENGTH_SHORT).show();
                } else if (selectedPhotos.size() == 0) {
                    Snackbar.make(parentView, "请至少上传一张图片！", Snackbar.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    List<UploadFileRequestBody.UploadResource> uploadResourceList = new ArrayList<>();
                    for (String imgPath : selectedPhotos) {
                        UploadFileRequestBody.UploadResource uploadResource = new UploadFileRequestBody.UploadResource();
                        uploadResource.setName(user.getId() + System.currentTimeMillis() + imgPath.substring(imgPath
                                .length() - 8));//为防止服务器端出现同名图片，使用用户名+时间+原图片名称后三位为图片命名
                        uploadResource.setContent(ImageUtil.ImageToBase64Content(imgPath));
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
                            progressBar.setVisibility(View.GONE);
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
            photoSelectorAdapter.notifyDataSetChanged();
        }
    }
}
