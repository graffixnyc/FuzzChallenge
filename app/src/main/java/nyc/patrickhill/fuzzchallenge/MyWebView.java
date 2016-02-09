package nyc.patrickhill.fuzzchallenge;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebView extends AppCompatActivity {
    private  WebView webview;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ProgressDialog progress = new ProgressDialog(MyWebView.this);
        webview=(WebView)findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                // TODO show you progress image

                super.onPageStarted(view, url, favicon);
                progress.setTitle("Loading");
                progress.setMessage("Please wait while loading...");
                progress.show();
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                // TODO hide your progress image
                super.onPageFinished(view, url);
                progress.dismiss();
            }
        });

        webview.getSettings().setJavaScriptEnabled(true);
        openURL();
    }

    /** Opens the URL in a browser */
    private void openURL() {
        webview.loadUrl("http://www.fuzzproductions.com");
        webview.requestFocus();
    }
}
