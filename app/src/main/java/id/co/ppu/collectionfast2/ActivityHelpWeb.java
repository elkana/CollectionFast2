package id.co.ppu.collectionfast2;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import id.co.ppu.collectionfast2.component.BasicActivity;
import id.co.ppu.collectionfast2.util.Storage;
import id.co.ppu.collectionfast2.util.Utility;
import okhttp3.HttpUrl;

import static id.co.ppu.collectionfast2.util.Storage.KEY_SERVER_ID;

public class ActivityHelpWeb extends BasicActivity {

    private static final String TAG = "ActivityHelpWeb";
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_web);

//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title_doc);

//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);  //must
//        getSupportActionBar().setLogo(ContextCompat.getDrawable(this, R.drawable.bfi_titlebar));

        web = (WebView) findViewById(R.id.webview);

        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());
/*
        ntah kenapa kode berikut malah buka link gambar ke Web Browser Tester
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            web.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                    if (webResourceRequest.getUrl().getScheme().equals("file")) {
                        webView.loadUrl(webResourceRequest.getUrl().toString());
                    } else {
                        // If the URI is not pointing to a local file, open with an ACTION_VIEW Intent
                        webView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, webResourceRequest.getUrl()));
                    }
                    return true; // in both cases we handle the link manually
                }
            });
        } else {
            web.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    if (Uri.parse(url).getScheme().equals("file")) {
                        webView.loadUrl(url);
                    } else {
                        // If the URI is not pointing to a local file, open with an ACTION_VIEW Intent
                        webView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                    return true; // in both cases we handle the link manually
                }
            });
        }
*/
//        web.getSettings().setUseWideViewPort(true);
//        web.getSettings().setLoadWithOverviewMode(true);

        web.getSettings().setSupportZoom(true);
//        web.getSettings().setBuiltInZoomControls(true);
//        web.getSettings().setDisplayZoomControls(false);

//        web.loadUrl("file:///android_asset/helpweb.html");

        String hardcodeUrl = "http://cmobile.radanafinance.co.id:7001/docs/helpweb.html";
        try {
            HttpUrl httpUrl = Utility.buildUrl(Storage.getPrefAsInt(KEY_SERVER_ID, 0));
//            String serverUrl = Utility.includeTrailingPath(httpUrl.url().toString(), '/');
//            URL url = httpUrl.url();
            String serverUrl = httpUrl.scheme() + "://" + httpUrl.host() + ":" + httpUrl.port() + "/";

            Log.e(TAG, "server:" + serverUrl);

            web.loadUrl(serverUrl + "docs/helpweb.html");
        } catch (Exception e) {
            e.printStackTrace();
            web.loadUrl(hardcodeUrl);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (web.canGoBack()) {
                        web.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
