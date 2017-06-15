package fr.husta.android.dockersearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ImageWebViewActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_web);

        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle("Docker Hub view");
        }

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarWeb);
        progressBar.setVisibility(View.INVISIBLE);

        WebView webView = (WebView) findViewById(R.id.image_webview);
        // Javascript in WebView : https://developer.android.com/guide/webapps/webview.html#UsingJavaScript
        webView.getSettings().setJavaScriptEnabled(false);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(uri.toString());
        // String currentPageUrl = webView.getUrl();
    }

}
