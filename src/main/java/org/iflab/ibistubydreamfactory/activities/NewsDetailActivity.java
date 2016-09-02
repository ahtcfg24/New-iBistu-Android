package org.iflab.ibistubydreamfactory.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.NewsDetail;
import org.iflab.ibistubydreamfactory.utils.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NewsDetailActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView newsDetailTitle, newsDetailArticle, newsDetailTime;
    private String newsDetailURL;
    private List<NewsDetail.ImgBean> imgList;
    private List<String> imageUrlList;
    private ConvenientBanner<String> newsBannerView;//新闻图片Banner

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        init();
        initView();
        getNewsDataByURL(newsDetailURL);

    }

    /**
     * 初始化
     */
    private void init() {
        Intent intent = getIntent();
        setTitle(intent.getStringExtra("fragmentName"));
        String newsLink = intent.getStringExtra("newsLink");
        newsDetailURL = MyApplication.newsDetailBaseURL + "?link=" + newsLink;
        imgList = new ArrayList<>();
        imageUrlList = new ArrayList<>();
    }

    private void initView() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        newsDetailTitle = (TextView) findViewById(R.id.newsDetailTitle_textView);
        newsDetailTime = (TextView) findViewById(R.id.newsDetailTime_textView);
        newsDetailArticle = (TextView) findViewById(R.id.newsContent_textView);
        newsBannerView = (ConvenientBanner<String>) findViewById(R.id.newsDetail_bannerView);


    }

    /**
     * 从网络获取新闻数据
     */
    private void getNewsDataByURL(final String url) {
        HttpUtil.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                NewsDetail newsDetail = new NewsDetail();
                try {
                    newsDetail.setTitle(response.getString("title"));
                    newsDetail.setArticle(response.getString("article"));
                    newsDetail.setTime(response.getString("time"));
                    JSONArray imgArray = (response.getJSONArray("imgList"));
                    if (imgArray.toString().equals("[]")) {
                        newsBannerView.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < imgArray.length(); i++) {
                            NewsDetail.ImgBean image = new NewsDetail.ImgBean();
                            JSONObject imageObject = imgArray.getJSONObject(i);
                            image.setUrl(imageObject.getString("url"));
                            imgList.add(image);
                        }
                    }
                    newsDetail.setImgList(imgList);
                    loadData(newsDetail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(url, throwable.getMessage());
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
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        /**
         * 结合Fresco加载图片
         *
         * @param data 图片从图片链接线性表中遍历出的一条图片地址
         */
        @Override
        public void UpdateUI(Context context, final int position, String data) {
            Picasso.with(context).load(Uri.parse(data)).into(imageView);

        }
    }
}
