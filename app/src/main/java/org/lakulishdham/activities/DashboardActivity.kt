package org.lakulishdham.activities

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.adcreators.youtique.helper.PrefUtils
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import com.google.gson.Gson
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.android.synthetic.main.activity_dashboard.indicator_layout
import kotlinx.android.synthetic.main.activity_dashboard.scrollViewSlogan
import kotlinx.android.synthetic.main.activity_dashboard.tv_justified_paragraph
import kotlinx.android.synthetic.main.activity_dashboard.txtSlogan
import kotlinx.android.synthetic.main.activity_dashboard.txtWelcomeUser
import kotlinx.android.synthetic.main.activity_dashboard.viewPagerText
import kotlinx.android.synthetic.main.activity_dashboard.viewPgerSlider
import org.json.JSONObject
import org.lakulishdham.BaseActivity
import org.lakulishdham.BuildConfig
import org.lakulishdham.R
import org.lakulishdham.adapters.GalleryImageAdapter
import org.lakulishdham.adapters.TextSliderAdapter
import org.lakulishdham.factories.DashboardViewModelFactory
import org.lakulishdham.fragment.DonationOptionDialogFragment
import org.lakulishdham.fragment.OneTimeDonationDialogFragment
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.helper.AppLogger
import org.lakulishdham.helper.DialogOptionsSelectedListener
import org.lakulishdham.helper.Functions
import org.lakulishdham.helper.fireIntent
import org.lakulishdham.helper.fireIntentWithData
import org.lakulishdham.helper.showAlert
import org.lakulishdham.helper.showRedError
import org.lakulishdham.model.AddDonationRequest
import org.lakulishdham.model.DonationListData
import org.lakulishdham.model.GalleryData
import org.lakulishdham.model.OrderData
import org.lakulishdham.model.UserData
import org.lakulishdham.utility.CustomProgressUtils
import org.lakulishdham.utility.DateFormatterUtils
import org.lakulishdham.viewmodels.DashboardViewModel
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.atomic.AtomicBoolean


class DashboardActivity : BaseActivity(), DashboardViewModel.DashboardViewModelCallback,
    DonationOptionDialogFragment.OnOptionDialogListeners, PaymentResultWithDataListener,
    OneTimeDonationDialogFragment.OnProceedDonationDialogListeners {

    lateinit var viewModel: DashboardViewModel
    lateinit var imageAdapter: GalleryImageAdapter
    lateinit var textSliderAdapter: TextSliderAdapter

    private var NUM_PAGES: Int = 0
    private var currentPage = 0
    private val handler = Handler()
    private val handler1 = Handler()

    private var swipeTimer: Timer = Timer()
    private var swipeTimer1: Timer = Timer()

    private var NUM_TEXT_PAGES: Int = 0
    private var currentTextPage = 0

    lateinit var checkout: Checkout

    lateinit var userData: UserData

    var amount: String = ""

    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Checkout.clearUserData(this)

        Checkout.preload(applicationContext)

        init()
    }


    private fun init() {
        AppLogger.e("AUTH_KEY ${PrefUtils.getAuthKey(this)}")

        viewModel = ViewModelProvider(
            this,
            DashboardViewModelFactory(this, this)
        ).get(DashboardViewModel::class.java)

        viewModel.fetchGalleryImages()

        tv_justified_paragraph.movementMethod = ScrollingMovementMethod()
        scrollViewSlogan.isSmoothScrollingEnabled = true

//        setSlider()
    }

    private fun setSlider() {

        val textList = arrayListOf<String>()

        textList.add(resources.getString(R.string.slogan_1))
        textList.add(resources.getString(R.string.slogan_2))
        textList.add(resources.getString(R.string.slogan_3))
        textList.add(resources.getString(R.string.slogan_4))
        textList.add(resources.getString(R.string.slogan_5))
        textList.add(resources.getString(R.string.slogan_6))

        textSliderAdapter = TextSliderAdapter(this, textList)
        viewPagerText.adapter = textSliderAdapter

        NUM_TEXT_PAGES = textList.size

        val Update = Runnable {
            if (currentTextPage == NUM_TEXT_PAGES) {
                currentTextPage = 0
            }
            viewPagerText.setCurrentItem(currentTextPage++, true)
        }
        swipeTimer1.schedule(object : TimerTask() {
            override fun run() {
                handler1.post(Update)
            }
        }, 500, 500)
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
        checkForUpdates()
    }

    fun OnUserInfoClick(view: View) {
        fireIntent(MyAccountActivity::class.java, false)
    }

    override fun onGetData(galleryData: GalleryData?) {
        galleryData?.let {

//            checkUpdateAvailable(galleryData.version_code, galleryData.version_name)

            if (!galleryData.data.isNullOrEmpty()) {
                imageAdapter = GalleryImageAdapter(this, galleryData.data!!)
                viewPgerSlider.adapter = imageAdapter

                viewPgerSlider.setClipToPadding(false);
                viewPgerSlider.setClipChildren(false);
                viewPgerSlider.setOffscreenPageLimit(3);
                viewPgerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                NUM_PAGES = galleryData.data!!.size
                setViewPagerSlider()
                setupIndicator()
            }
        }

        //--------------- This code is for sliding Textview content automatically------------------
        /*val slide = TranslateAnimation(0F, 0F, 0F, -txtSlogan.getBottom().toFloat())
        slide.setDuration(50000)
        slide.setRepeatCount(Animation.INFINITE)
        slide.setRepeatMode(Animation.RESTART)
        slide.setInterpolator(LinearInterpolator())
        txtSlogan.startAnimation(slide)*/
        // --------------------------------------------------------------------------
    }

    override fun onError(err: String) {
        showRedError(err)
    }

    fun setViewPagerSlider() {

        viewPgerSlider.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                setupCurrentIndicator(position)
            }
        })

        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            viewPgerSlider.setCurrentItem(currentPage++, true)
        }
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 20000, 20000)

    }

    private fun setupIndicator() {
        val indicator: Array<ImageView?> = arrayOfNulls<ImageView>(imageAdapter.getItemCount())
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicator.indices) {
            indicator[i] = ImageView(applicationContext)
            indicator[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.indicator_inactive
                )
            )
            indicator[i]?.setLayoutParams(layoutParams)
            indicator_layout.addView(indicator[i])
        }
    }

    private fun setupCurrentIndicator(index: Int) {
        val itemcildcount: Int = indicator_layout.getChildCount()
        for (i in 0 until itemcildcount) {
            val imageView: ImageView = indicator_layout.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }


    private fun setUserInfo() {
        userData = PrefUtils.getUserData(this)
        userData.let {
            txtWelcomeUser.text = "Welcome,\n${userData.name}"
        }
    }

    fun ClickOnDonate(view: View) {
        //commented this code because one-time donation has disabled
//        val dialog = DonationOptionDialogFragment(this)
//        dialog.show(supportFragmentManager,"DONATION_OPTION")

        fireIntent(SubscriptionPlanListActivity::class.java, false)
    }

    fun ClickOnOneTimeDonate(view: View) {
        fireIntent(UrlDisplayActivity::class.java, false)
    }

    fun startPayment(data: OrderData?) {

        checkout = Checkout()
        checkout.setKeyID(AppConstants.getRazorPayKey())
        checkout.setImage(R.mipmap.ic_launcher)
        try {
            val options = JSONObject()
            options.put("name", resources.getString(R.string.app_name))
            options.put("description", "Donation to Lakulish Dham")
            options.put("order_id", data?.id) //from response of step 3.
            options.put("theme.color", "#F98404")
            options.put("currency", data?.currency)
            options.put("amount", data?.amount) //pass amount in currency subunits
            options.put("prefill.name", userData.name)
            options.put(
                "prefill.email",
                if (userData.email.isNullOrEmpty()) AppConstants.DEFAULT_EMAIL else userData.email
            )
            options.put("prefill.contact", userData.phone_number)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)

            options.put("retry", retryObj)
            checkout.open(this, options)
        } catch (e: Exception) {
            AppLogger.e("Error in starting Razorpay Checkout")
        }

    }

    override fun onSingleOptionSelected() {
        val dialog = OneTimeDonationDialogFragment(this)
        dialog.show(supportFragmentManager, "SINGLE_DONATION")
    }

    override fun onMultipleOptionSelected() {
        fireIntent(SubscriptionPlanListActivity::class.java, false)
    }

    override fun onGetOrderData(data: OrderData?) {
        data?.let {
            startPayment(it)
        }

    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        AppLogger.e("PAYMENT_FAIL_DATA : ${Gson().toJson(paymentData)}")

        val intent = Intent(this, DonationStatusActivity::class.java)
        intent.putExtra(DonationStatusActivity.INTENT_TRANSACTION_STATUS, false)
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIPTION, false)
        intent.putExtra(DonationStatusActivity.INTENT_TRANS_ID, "")
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIP_AMOUNT, "0")
        fireIntentWithData(intent, true)
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?, paymentData: PaymentData?) {
        AppLogger.e("PAYMENT_SUCCESS : ${razorpayPaymentID}")
        AppLogger.e("PAYMENT_DATA : ${Gson().toJson(paymentData)}")
        addDonation(paymentData)
    }

    override fun onProceedToCheckout(a: String) {
        amount = a
        viewModel.generateOrder(a)
    }

    fun addDonation(paymentData: PaymentData?) {

        paymentData?.let {
            val addDonationRequest = AddDonationRequest()
            addDonationRequest.amount = amount
            addDonationRequest.transaction_id = paymentData.paymentId
            addDonationRequest.razorpay_paymentId = paymentData.paymentId
            addDonationRequest.pay_status = "1"
            addDonationRequest.razorpay_orderId = paymentData.orderId
            addDonationRequest.razorpay_signature = paymentData.signature
            addDonationRequest.transaction_date =
                DateFormatterUtils.parseDate(Date(), DateFormatterUtils.ymdFormat)

            viewModel.AddDonation(addDonationRequest)
        }
    }

    override fun onDonationSuccessful(data: DonationListData?) {

        val intent = Intent(this, DonationStatusActivity::class.java)
        intent.putExtra(DonationStatusActivity.INTENT_TRANSACTION_STATUS, true)
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIPTION, false)
        intent.putExtra(DonationStatusActivity.INTENT_TRANS_ID, data?.transaction_id)
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIP_AMOUNT, "0")
        fireIntentWithData(intent, true)
    }

    fun onShareApp(view: View) {
        shareAppOnSocialMedia()
    }

    fun onContactApp(view: View) {
        fireIntent(ContactSupportActivity::class.java, false)
    }

    fun shareAppOnSocialMedia() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            var shareMessage = resources.getString(R.string.app_name)
            shareMessage =
                "${shareMessage}\n\nसनातन संस्कृति पुनरुत्थान और परमार्थ कार्यो हेतु अपना उत्तम योगदान दे।\n\n"
            shareMessage =
                """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Share application on"))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun checkUpdateAvailable(versionCode: String, versionName: String) {

        val code = versionCode.toInt()
        val appCode = BuildConfig.VERSION_CODE
        if (appCode < code) {
            showUpdateAlert()
        }
    }

    fun showUpdateAlert() {
        showAlert(
            "Update App?",
            "\nA new version of Lakulishdham App is now available.\n\nWould you like to update now?\n",
            "UPDATE NOW",
            "LATER",
            object :
                DialogOptionsSelectedListener {
                override fun onSelect(isYes: Boolean) {
                    val s =
                        "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
                    Functions.openBrowser(this@DashboardActivity, s)

                }
            },
            object : DialogOptionsSelectedListener {
                override fun onSelect(isYes: Boolean) {
                }
            })
    }

    private fun checkForUpdates() {
        try {
            CustomProgressUtils.showProgress(this)

            val appUpdaterUtils = AppUpdaterUtils(this)
            appUpdaterUtils.setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
            appUpdaterUtils.withListener(object : AppUpdaterUtils.UpdateListener {
                override fun onSuccess(update: Update?, isUpdateAvailable: Boolean?) {
                    CustomProgressUtils.hideProgress()

                    if (isUpdateAvailable != null && isUpdateAvailable) {
                        update?.apply {
                            val latestVerList = this.latestVersion?.split(".")
                            val curVerList = BuildConfig.VERSION_NAME.split(".")
                            Log.e("List", "==> $latestVerList")

                            if (Integer.valueOf(latestVerList!![1]) > Integer.valueOf(curVerList[1])
                                || Integer.valueOf(latestVerList[0]) > Integer.valueOf(curVerList[0])
                            ) {
                                if (dialog == null) {
                                    val builder = AlertDialog.Builder(this@DashboardActivity)
                                        .setCancelable(false)
                                        .setMessage(R.string.msg_mandatory_update)
                                        .setPositiveButton(R.string.lbl_update_now,
                                            DialogInterface.OnClickListener { dialogInterface, i ->
                                                dialogInterface.dismiss()
                                                openPlayStore()
                                            })
                                    dialog = builder.create();
                                }

                                if (!this@DashboardActivity.isFinishing) {
                                    if (!dialog!!.isShowing) {
                                        dialog!!.show()
                                    }
                                }

                            } else {
                                if (dialog == null) {
                                    val builder = AlertDialog.Builder(this@DashboardActivity)
                                        .setCancelable(true)
                                        .setMessage(R.string.msg_update_available)
                                        .setPositiveButton(R.string.lbl_update_now,
                                            DialogInterface.OnClickListener { dialogInterface, i ->
                                                dialogInterface.dismiss()
                                                openPlayStore()
                                            })
                                        .setNegativeButton(R.string.lbl_remind_me_later,
                                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                                    dialog = builder.create()
                                }

                                if (!this@DashboardActivity.isFinishing) {
                                    if (!dialog!!.isShowing) {
                                        dialog!!.show()
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onFailed(error: AppUpdaterError?) {
                    Log.e("AppUpdate Error", "==> ${error?.name}")
                    CustomProgressUtils.hideProgress()
                }
            })
            appUpdaterUtils.start()
        }
        catch (e : java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun openPlayStore() {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
            )
            intent.setPackage("com.android.vending")
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "No application found to perform this action.", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun justify(textView: TextView) {
        val isJustify = AtomicBoolean(false)
        val textString = textView.text.toString()
        val textPaint = textView.paint
        val builder = SpannableStringBuilder()
        textView.post {
            if (!isJustify.get()) {
                val lineCount = textView.lineCount
                val textViewWidth = textView.width
                for (i in 0 until lineCount) {
                    val lineStart = textView.layout.getLineStart(i)
                    val lineEnd = textView.layout.getLineEnd(i)
                    val lineString = textString.substring(lineStart, lineEnd)
                    if (i == lineCount - 1) {
                        builder.append(SpannableString(lineString))
                        break
                    }
                    val trimSpaceText = lineString.trim { it <= ' ' }
                    val removeSpaceText = lineString.replace(" ".toRegex(), "")
                    val removeSpaceWidth = textPaint.measureText(removeSpaceText)
                    val spaceCount = (trimSpaceText.length - removeSpaceText.length).toFloat()
                    val eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount
                    val spannableString = SpannableString(lineString)
                    for (j in 0 until trimSpaceText.length) {
                        val c = trimSpaceText[j]
                        if (c == ' ') {
                            val drawable: Drawable = ColorDrawable(0x00ffffff)
                            drawable.setBounds(0, 0, eachSpaceWidth.toInt(), 0)
                            val span = ImageSpan(drawable)
                            spannableString.setSpan(
                                span,
                                j,
                                j + 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    }
                    builder.append(spannableString)
                }
                textView.text = builder
                isJustify.set(true)
            }
        }
    }


}