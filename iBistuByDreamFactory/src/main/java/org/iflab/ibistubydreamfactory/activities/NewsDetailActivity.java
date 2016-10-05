package org.iflab.ibistubydreamfactory.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.NewsAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.NewsDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView newsDetailTitle, newsDetailArticle, newsDetailTime;
    private ConvenientBanner<String> newsBannerView;//新闻图片Banner
    private String newsLink;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        init();
        initView();
        getNewsDetail();

    }

    /**
     * 初始化
     */
    private void init() {
        Intent intent = getIntent();
        setTitle(intent.getStringExtra("fragmentName"));
        newsLink = intent.getStringExtra("newsLink");
    }

    private void initView() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        newsDetailTitle = (TextView) findViewById(R.id.newsDetailTitle_textView);
        newsDetailTime = (TextView) findViewById(R.id.newsDetailTime_textView);
        newsDetailArticle = (TextView) findViewById(R.id.newsContent_textView);
        newsBannerView = (ConvenientBanner<String>) findViewById(R.id.newsDetail_bannerView);


    }

    public void getNewsDetail() {
        NewsAPI newsAPI = APISource.getInstance().getAPIObject(NewsAPI.class);
        Call<NewsDetail> call = newsAPI.getNewsDetail(newsLink);
        call.enqueue(new Callback<NewsDetail>() {
            @Override
            public void onResponse(Call<NewsDetail> call, Response<NewsDetail> response) {
                if (response.isSuccessful()) {
                    NewsDetail newsDetail = response.body();
                    System.out.println(newsDetail.toString());
                    loadData(newsDetail);
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<NewsDetail> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                System.out.println("error：" + t.toString());
                Toast.makeText(NewsDetailActivity.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG)
                     .show();
            }
        });
    }


    /**
     * 填充新闻数据到控件里
     *
     * @param newsDetail 新闻对象
     */
    private void loadData(NewsDetail newsDetail) {
        progressBar.setVisibility(View.GONE);
        newsDetailTitle.setText(newsDetail.getTitle());
        newsDetailTime.setText(newsDetail.getTime());
        newsDetailArticle.setText(newsDetail.getArticle());

        int imageCount = newsDetail.getImgList().size();
        if (imageCount > 0) {//如果资源线性表不为空就执行
            List<String> imageUrlList = new ArrayList<>();
            for (NewsDetail.ImgBean imgBean : newsDetail.getImgList()) {
                imageUrlList.add(imgBean.getUrl());
            }
            Log.i("imageUrlList", imageUrlList.toString());
            //使用Android-ConvenientBanner库设置banner
            newsBannerView.setPages(new CBViewHolderCreator<ImageHolderView>() {
                @Override
                public ImageHolderView createHolder() {
                    return new ImageHolderView();
                }
            }, imageUrlList)
                          //设置两个点图片作为翻页指示器，不设置则没有指示器
                          .setPageIndicator(new int[]{
                                  R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused
                          });
            if (imageCount > 1) {//如果图片数量超过一张就执行
                newsBannerView.startTurning(3000);
            }

        } else {
            newsBannerView.setVisibility(View.GONE);
        }
    }

    /**
     * 停止图片自动滚动
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (newsBannerView.isTurning()) {
            newsBannerView.stopTurning();
        }
    }

    /**
     * 通过URL加载网络上的新闻图片
     * 使用Android-ConvenientBanner库所需的类
     */
    private class ImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            return imageView;
        }

        /**
         * 加载图片
         *
         * @param data 图片从图片链接线性表中遍历出的一条图片地址
         */
        @Override
        public void UpdateUI(Context context, final int position, String data) {
            Glide.with(context)
                 .load(data)
                 .fitCenter()
                 .placeholder(R.drawable.ic_image_loading_picture)
                 .error(R.drawable.ic_image_error_picture)
                 .into(imageView);
        }
    }
}
