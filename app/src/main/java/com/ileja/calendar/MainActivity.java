package com.ileja.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ileja.calendar.cronofy.Config;
import com.ileja.calendar.cronofy.PrefHelper;
import com.ileja.calendar.cronofy.api.CalenderApi;
import com.ileja.calendar.cronofy.api.CalenderService;
import com.ileja.calendar.cronofy.bean.AccessTokenBean;
import com.ileja.calendar.cronofy.bean.CalendarBean;
import com.ileja.calendar.cronofy.bean.CalendarItem;
import com.ileja.calendar.cronofy.bean.RefreshTokenBean;
import com.ileja.calendar.cronofy.response.AccessTokenResponse;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class MainActivity extends BaseActivity {

    private final boolean debugAccess = false;
    private String appCode;
    private boolean isRefreshToken = false;

    @Bind(R.id.txLogin)
    TextView txLogin;

    @Bind(R.id.btnLogin)
    Button btnLogin;

    @Bind(R.id.btnGetToken)
    Button btnGetToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        appCode = PrefHelper.getCode(MainApp.get());
        if (TextUtils.isEmpty(PrefHelper.getAccessToken(MainApp.get())) == false) {
            long curTime = System.currentTimeMillis();
            long tokenTime = PrefHelper.getTokenTime(MainApp.get());
            if (curTime - tokenTime > PrefHelper.getExpireTime(MainApp.get()) * 1000) {
                setCanGetToken();
                btnGetToken.setText("refresh token");
                isRefreshToken = true;
            } else {
                goCalendar();
            }
        } else if (!TextUtils.isEmpty(appCode) && !debugAccess) {
            setCanGetToken();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.Const.Request_Login_Code) {
            if (resultCode == RESULT_OK) {
                appCode = data.getStringExtra(Config.Const.Auth_Code);

                PrefHelper.setCode(MainApp.get(), appCode);

                goCalendar();
            }
        }
    }

    /**
     * 设置获取token
     */
    private void setCanGetToken() {
        txLogin.setText(appCode);
        btnLogin.setVisibility(View.GONE);
        btnGetToken.setVisibility(View.VISIBLE);
    }

    private void goCalendar() {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btnLogin)
    public void doLogin() {
        String url = makeRequestUrl();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Config.Const.Request_Url, url);
        startActivityForResult(intent, Config.Const.Request_Login_Code);
    }

    @OnClick(R.id.btnGetToken)
    public void doGetAccessToken() {
        if (isRefreshToken) {
            refreshAccessToken(PrefHelper.getRefreshToken(MainApp.get()));
        } else {
            getAccessToken(appCode);
        }
    }

    private void doTest() {
        final OkHttpClient client = new OkHttpClient();
        String token = String.format("Bearer %s", PrefHelper.getAccessToken(MainApp.get()));

        final Request request = new Request.Builder().url("https://api.cronofy.com//v1/calendars").get().addHeader("content-type", "application/json").addHeader("authorization", token).addHeader("cache-control", "no-cache").addHeader("postman-token", "3c7b14f7-b7da-3413-95ab-70583c706182").build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Response response = client.newCall(request).execute();
                    String content = response.body().string();
                    Timber.d("result = " + content);
                    if (TextUtils.isEmpty(content) == false) {
                        Gson gson = new Gson();
                        CalendarBean calendarBean = gson.fromJson(content, CalendarBean.class);
                        if (calendarBean != null) {
                            for (CalendarItem _canlendar : calendarBean.calendars) {
                                Timber.d("calendar_id: %s", _canlendar.calendar_id);
                                Timber.d("provider_name: %s", _canlendar.provider_name);
                                Timber.d("profile_name: %s", _canlendar.profile_name);
                                Timber.d("calendar_name: %s", _canlendar.calendar_name);
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 获取accessToken
     *
     * @param code
     */
    private void getAccessToken(final String code) {
        CalenderApi calenderApi = CalenderService.createCalenderService("");

        calenderApi.getAccessToken(new AccessTokenBean(Config.Account.Client_ID, Config.Account.Client_Secret, code, Config.Account.Redirect_Url)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AccessTokenResponse>() {
            @Override
            public void onCompleted() {
                Timber.d("on complete");
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("on error");
            }

            @Override
            public void onNext(AccessTokenResponse response) {
                Timber.d("list CalendarItem");
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

                goCalendar();
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

                goCalendar();
            }
        });

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    private String makeRequestUrl() {
        StringBuffer sb = new StringBuffer();
        sb.append(Config.URL.Request_App_Url);
        sb.append("?");
        sb.append("response_type=code");
        sb.append("&client_id=").append(Config.Account.Client_ID);
        sb.append("&redirect_uri=").append(Config.Account.Redirect_Url);
        sb.append("&scope=").append(Config.Account.Scope);

        return sb.toString();
    }
}
