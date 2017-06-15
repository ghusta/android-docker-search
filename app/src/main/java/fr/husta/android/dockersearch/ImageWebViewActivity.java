package fr.husta.android.dockersearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.Objects;

public class ImageWebViewActivity extends AppCompatActivity
{

    private static final String TAG = "ImageWebViewActivity";

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
        // should support zoom
        webView.getSettings().setSupportZoom(true);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_image_view, menu);

        MenuItem openInChromeItem = menu.findItem(R.id.menu_open_in_chrome);

        // Update option item title if browser is not Chrome
        String defaultUserAgent = WebSettings.getDefaultUserAgent(this);
        Log.d(TAG, "UserAgent: " + defaultUserAgent);
        // not sure if it really works...
        boolean hasChrome = defaultUserAgent.contains("Chrome");
        if (!hasChrome)
        {
            openInChromeItem.setTitle(R.string.menu_open_in_browser);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean ret;
        switch (item.getItemId())
        {
            case R.id.menu_open_in_chrome:
                WebView webView = (WebView) findViewById(R.id.image_webview);
                String currentPageUrl = webView.getUrl();
                openUrlInBrowser(Uri.parse(currentPageUrl));
                ret = true;
                break;

            default:
                ret = false;
        }

        return ret;
    }

    public void openUrlInBrowser(Uri uri)
    {
        Objects.requireNonNull(uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);
    }

}
