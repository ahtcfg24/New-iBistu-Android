package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.News;
import org.iflab.ibistubydreamfactory.models.NewsDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 新闻接口
 */
public interface NewsAPI {
    /**
     * 获取新闻列表
     */
    @GET("newsapi/newslist")
    Call<List<News>> getNewsList(@Query("category")String category, @Query("page")String page);

    /**
     * 获取新闻详细信息
     * @param link 要获取的新闻信息链接
     * @return*
     */
    @GET("newsapi/newsdetail")
    Call<NewsDetail> getNewsDetail(@Query("link") String link);
}
