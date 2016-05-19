package com.ileja.calendar.cronofy.api;

import com.ileja.calendar.cronofy.bean.AccessTokenBean;
import com.ileja.calendar.cronofy.bean.CalendarBean;
import com.ileja.calendar.cronofy.bean.EventBean;
import com.ileja.calendar.cronofy.bean.RefreshTokenBean;
import com.ileja.calendar.cronofy.bean.RevokeTokenBean;
import com.ileja.calendar.cronofy.response.AccessTokenResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chentao on 16/5/16.
 */
public interface CalenderApi {

    /**
     * 获取AccessToken
     * @param accessTokenBean
     * @return
     */
    @Headers("Content-Type: application/json" )
    @POST("/oauth/token")
    Observable<AccessTokenResponse> getAccessToken(@Body AccessTokenBean accessTokenBean);

    /**
     * 获取RefreshToken
     * @param refreshTokenBean
     * @return
     */
    @Headers("Content-Type: application/json" )
    @POST("/oauth/token")
    Observable<AccessTokenResponse> refreshAccessToken(@Body RefreshTokenBean refreshTokenBean);

    /**
     * 取消授权
     * @param revokeTokenBean
     * @return
     */
    @Headers("Content-Type: application/json" )
    @POST("/oauth/token")
    Observable<AccessTokenResponse> revokeToken(@Body RevokeTokenBean revokeTokenBean);

    /**
     * 获取日历列表
     * @return
     */
    @GET("/v1/calendars")
    Observable<CalendarBean> getCalendarList();

    /**
     * 获取日历时间列表
     * @param fromDate
     * @param toDate
     * @return
     */
    @GET("/v1/events?tzid=Etc/UTC")
    Observable<EventBean> getEventList(@Query("from") String fromDate, @Query("to") String toDate);

}
