package org.lakulishdham.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_contact_support.*
import org.lakulishdham.R
import org.lakulishdham.helper.closeScreen


class ContactSupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_support)



    }

    fun goBack(view: View) {
        closeScreen()
    }

    fun clickOnCall(view: View) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + txtCall.text))
        startActivity(intent)
    }

    fun clickOnWeb(view: View) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://" + txtWeb.text.toString())))
    }

    fun clickOnEmail(view: View) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + txtEmail.text.toString()))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }


}