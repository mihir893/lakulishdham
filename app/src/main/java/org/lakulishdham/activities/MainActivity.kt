package org.lakulishdham.activities

import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.adcreators.youtique.helper.PrefUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.helper.fireIntent

class MainActivity : BaseActivity() {

    lateinit var splashAnim : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        splashAnim = AnimationUtils.loadAnimation(this,
            R.anim.splash_animation
        )
        imgMain.animation = splashAnim

        Handler().postDelayed(Runnable {
            moveToNextPage()
        },1800)

    }

    fun moveToNextPage() {
        if (PrefUtils.isUserLogin(this)) {
            fireIntent(DashboardActivity::class.java,true)
        }
        else {
            fireIntent(LoginActivity::class.java,true)
        }
    }



}