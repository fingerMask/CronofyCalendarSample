package com.ileja.calendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ileja.calendar.cronofy.Config;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "Login";
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    private String mUrl;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private RelativeLayout webViewContainer;
    private RelativeLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra(Config.Const.Request_Url);
        if (TextUtils.isEmpty(mUrl)) {
            throw new RuntimeException("the request url is empty");
        }


        mSpinner = new ProgressDialog(this);
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContent = new RelativeLayout(this);

        setUpWebView();

        // setUpCloseBtn();

        addContentView(mContent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void setUpWebView() {
        webViewContainer = new RelativeLayout(this);
        // webViewContainer.setOrientation(LinearLayout.VERTICAL);

        // webViewContainer.addView(title, new
        // LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
        // getContext().getResources().getDimensionPixelSize(R.dimen.dialog_title_height)));

        mWebView = new WebView(this);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WeiboWebViewClient());
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        mWebView.setVisibility(View.INVISIBLE);

        webViewContainer.addView(mWebView);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        Resources resources = getResources();
        lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_left_margin);
        lp.topMargin = resources.getDimensionPixelSize(R.dimen.dialog_top_margin);
        lp.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_right_margin);
        lp.bottomMargin = resources.getDimensionPixelSize(R.dimen.dialog_bottom_margin);
        mContent.addView(webViewContainer, lp);
    }

    private class WeiboWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirect URL: " + url);
            // 待后台增加对默认重定向地址的支持后修改下面的逻辑
            if (url.startsWith(Config.Account.Redirect_Url)) {
                Intent intent = handleRedirectUrl(url);
                view.stopLoading();
                if(intent != null) {
                    LoginActivity.this.setResult(RESULT_OK, intent);
                }else{
                    LoginActivity.this.setResult(RESULT_CANCELED);
                }
                finish();
                return true;
            }
//            // launch non-dialog URLs in a full browser
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
//            mListener.onError(new DialogError(description, errorCode, failingUrl));
//            WeiboDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted URL: " + url);
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished URL: " + url);
            super.onPageFinished(view, url);
            mSpinner.dismiss();

            mContent.setBackgroundColor(Color.TRANSPARENT);
            //webViewContainer.setBackgroundResource(R.drawable.dialog_bg);
            mWebView.setVisibility(View.VISIBLE);
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        /**
         * 处理redirect返回结果
         *
         * @param url
         */
        private Intent handleRedirectUrl(String url) {
            Log.d(TAG, "redirect URL: " + url);
            int index = url.indexOf("code=");
            if(index != -1){
                String codeString = url.substring(index + "code=".length(), url.length());
                codeString = codeString.substring(0, codeString.indexOf("&"));

                Intent intent = new Intent();
                intent.putExtra(Config.Const.Auth_Code, codeString);
                return intent;
            }

            return null;
        }

    }
}
