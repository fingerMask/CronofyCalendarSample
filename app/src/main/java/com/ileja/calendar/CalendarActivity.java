package com.ileja.calendar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ileja.calendar.cronofy.Config;
import com.ileja.calendar.cronofy.PrefHelper;
import com.ileja.calendar.cronofy.api.CalenderApi;
import com.ileja.calendar.cronofy.api.CalenderService;
import com.ileja.calendar.cronofy.bean.CalendarBean;
import com.ileja.calendar.cronofy.bean.CalendarItem;
import com.ileja.calendar.cronofy.bean.EventBean;
import com.ileja.calendar.cronofy.bean.EventItem;
import com.ileja.calendar.cronofy.bean.RefreshTokenBean;
import com.ileja.calendar.cronofy.response.AccessTokenResponse;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class CalendarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAccessToken(PrefHelper.getRefreshToken(MainApp.get()));
                Snackbar.make(view, "Refresh Token ing", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnGetCalendar)
    public void doGetCalendar() {
        listCalendars();
    }

    @OnClick(R.id.btnGetEvent)
    public void doGetEvent() {
        listEvents();
    }

    /**
     * 获取日历列表
     */
    private void listCalendars() {
        CalenderApi calenderApi = CalenderService.createCalenderService(PrefHelper.getAccessToken(MainApp.get()));
        calenderApi.getCalendarList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CalendarBean>() {
            @Override
            public void onCompleted() {
                Timber.d("on complete");
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("on error");
            }

            @Override
            public void onNext(CalendarBean calendarBean) {
                if (calendarBean != null) {
                    for (CalendarItem _canlendar : calendarBean.calendars) {
                        Timber.d("calendar_id: %s", _canlendar.calendar_id);
                        Timber.d("provider_name: %s", _canlendar.provider_name);
                        Timber.d("profile_name: %s", _canlendar.profile_name);
                        Timber.d("calendar_name: %s", _canlendar.calendar_name);
                    }
                }

            }
        });
    }

    /**
     * 获取Event列表
     */
    @DebugLog
    private void listEvents() {
        CalenderApi calenderApi = CalenderService.createCalenderService(PrefHelper.getAccessToken(MainApp.get()));
        calenderApi.getEventList("2016-05-01", "2016-06-05")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventBean>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("on complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException httpException = (HttpException)e;

                        try {
                            Timber.d("on error %s", httpException.response().errorBody().string());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(EventBean eventBean) {
                        if (eventBean != null) {
                            Timber.d("page current %d, total %d", eventBean.pages.current, eventBean.pages.total);
                            for(EventItem eventItem : eventBean.events){
                                Timber.d("****************************************");
                                Timber.d("calendar_id = %s", eventItem.calendar_id);
                                Timber.d("description = %s", eventItem.description);
                                Timber.d("summary = %s", eventItem.summary);
                                Timber.d("start = %s", eventItem.start.toString());
                                Timber.d("end = %s", eventItem.end.toString());
                                Timber.d("event_uid = %s", eventItem.event_uid);
                                Timber.d("participation_status = %s", eventItem.participation_status);
                                Timber.d("transparency = %s", eventItem.transparency);
                            }
                        }

                    }
                });

    }

    /**
     * 刷新accessToken
     *
     * @param refreshToken
     */
    private void refreshAccessToken(final String refreshToken) {
        CalenderApi calenderApi = CalenderService.createCalenderService("");

        calenderApi.refreshAccessToken(new RefreshTokenBean(Config.Account.Client_ID, Config.Account.Client_Secret, refreshToken)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AccessTokenResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(AccessTokenResponse response) {

                Timber.d("refresh token");
                if (response == null) {
                    return;
                }
                Timber.d("token = " + response.access_token);
                Timber.d("refresh token = " + response.refresh_token);
                Timber.d("expire = " + response.expires_in);
                Timber.d("token type = " + response.token_type);
                Timber.d("scope = " + response.scope);

                PrefHelper.setAccessToken(MainApp.get(), response.access_token);
                PrefHelper.setRefreshToken(MainApp.get(), response.refresh_token);
                PrefHelper.setExpireTime(MainApp.get(), response.expires_in);
                PrefHelper.setTokenTime(MainApp.get());
            }
        });

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

}
