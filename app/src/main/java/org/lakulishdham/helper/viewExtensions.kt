package org.lakulishdham.helper

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_toast.view.*
import org.lakulishdham.R
import java.util.regex.Pattern

fun Activity.fireIntent(cls: Class<*>, clearStack: Boolean) {
    val i = Intent(this, cls)
    if (clearStack) {
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    } else {
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(i)
    overridePendingTransition(R.anim.enter, R.anim.exit)
}

fun Activity.fireIntentWithData(intent: Intent, clearStack: Boolean) {
    if (clearStack) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    } else {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
    overridePendingTransition(R.anim.enter, R.anim.exit)
}

fun Context.openBrowser(url1: String) {
    var url = url1
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        url = "http://$url"
    }
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    this.startActivity(i)
}

fun Activity.closeScreen() {
    finish()
    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
}

fun String.isValidPhone() : Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    else return this.length == 10
}

fun String.isValidPassword() : Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    else return this.length >= 6
}


fun String.isValidEmail() : Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    else return Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),this)
}

fun Context.showRedError(msg: String?) {
    if (TextUtils.isEmpty(msg)) {
        return
    }

    val view = View.inflate(this, R.layout.layout_toast, null)

    view.txtTopError.text = msg

    val toast = Toast(this)
    toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 60);
    toast.duration = Toast.LENGTH_LONG
    toast.view = view
    toast.show()
}

fun Context.showToast(msg: String?) {
    if (TextUtils.isEmpty(msg)) {
        return
    }

    val view = View.inflate(this, R.layout.layout_toast, null)

    view.txtTopError.text = msg
    view.txtTopError.setBackgroundColor(Color.BLACK)

    val toast = Toast(this)
    toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 60);
    toast.duration = Toast.LENGTH_LONG
    toast.view = view
    toast.show()
}

fun EditText.Text() : String {
    return this.text.toString()
}

fun Activity.hideKeypad() {
    if (currentFocus != null && currentFocus!!.windowToken != null) {
        val inputMethodManager = getSystemService(
                Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken, 0
        )
    }
}

fun Context.hideKeypad() {
    if ((this as AppCompatActivity).currentFocus != null && currentFocus!!.windowToken != null) {
        val inputMethodManager = getSystemService(
                Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken, 0
        )
    }
}

fun ShapeableImageView.glideLoad(uri: Uri, context: Context, placeholder: Int) {
    Glide.with(context)
        .load(uri)
        .centerCrop()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .placeholder(placeholder)
        .into(this)
}

fun ShapeableImageView.glideLoadWithCache(uri: Uri, context: Context, placeholder: Int) {
    Glide.with(context)
        .load(uri)
        .centerCrop()
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(placeholder)
        .into(this)
}

fun String.GetInitials() : String {

    val initials = this
        .split(' ')
        .mapNotNull { it.firstOrNull()?.toString() }
        .reduce { acc, s -> acc + s }

    return initials.toUpperCase();
}

fun TextView.makeLinks(context: Context,vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                // use this to change the link color
                textPaint.color = ContextCompat.getColor(context,R.color.orange_dark)
                // toggle below value to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = true
            }
            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
//      if(startIndexOfLink == -1) continue // todo if you want to verify your texts contains links text
        spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}


fun Activity.showAlert(message: String?, positivebuttonText: String?,negativebuttonText: String?, listener: DialogOptionsSelectedListener,listenerNeg: DialogOptionsSelectedListener) {
    val dialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        .setMessage(message)
        .setPositiveButton(positivebuttonText) { dialog, which ->
            dialog.dismiss()
            listener.onSelect(true)
        }
        .setNegativeButton(negativebuttonText) { dialog, which ->
            dialog.dismiss()
            listenerNeg.onSelect(true)
        }.create()
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()

}

fun Activity.showAlert(title : String?,message: String?, positivebuttonText: String?,negativebuttonText: String?, listener: DialogOptionsSelectedListener,listenerNeg: DialogOptionsSelectedListener) {
    val dialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positivebuttonText) { dialog, which ->
            dialog.dismiss()
            listener.onSelect(true)
        }
        .setNegativeButton(negativebuttonText) { dialog, which ->
            dialog.dismiss()
            listenerNeg.onSelect(true)
        }.create()
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()

}

fun Activity.showAlert(message: String?, positivebuttonText: String?, listener: DialogOptionsSelectedListener) {
    val dialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setMessage(message)
            .setPositiveButton(positivebuttonText) { dialog, which ->
                dialog.dismiss()
                listener.onSelect(true)
            }.create()
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
}

/*fun ViewPager2.setCurrentItem(
    item: Int,
    duration: Long,
    interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
    pagePxWidth: Int = width // Default value taken from getWidth() from ViewPager2 view
) {
    val pxToDrag: Int = pagePxWidth * (item - currentItem)
    val animator = ValueAnimator.ofInt(0, pxToDrag)
    var previousValue = 0
    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        val currentPxToDrag = (currentValue - previousValue).toFloat()
        fakeDragBy(-currentPxToDrag)
        previousValue = currentValue
    }
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) { beginFakeDrag() }
        override fun onAnimationEnd(animation: Animator?) { endFakeDrag() }
        override fun onAnimationCancel(animation: Animator?) { *//* Ignored *//* }
        override fun onAnimationRepeat(animation: Animator?) { *//* Ignored *//* }
    })
    animator.interpolator = interpolator
    animator.duration = duration
    animator.start()
}*/

interface DialogOptionsSelectedListener {
    fun onSelect(isYes: Boolean)
}