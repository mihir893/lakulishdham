package org.lakulishdham.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_url_display.wv_url
import org.lakulishdham.R

class UrlDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url_display)

        wv_url.settings.javaScriptEnabled = true
        wv_url.settings.allowFileAccess = true
        wv_url.loadUrl("file:///android_asset/payment_button.html");
    }
}