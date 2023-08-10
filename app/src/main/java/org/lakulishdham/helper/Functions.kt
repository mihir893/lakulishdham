package org.lakulishdham.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class Functions {
    companion object {

        fun isInternetConnected(context: Context): Boolean {
            var result = false
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }

        fun openBrowser(context: Context, url1: String) {
            var url = url1
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://$url"
            }
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }

        fun getFont(context: Context, fontName: String): Typeface {
            return Typeface.createFromAsset(context.assets, "font/$fontName")
        }

        fun createYouTiqueFolder(context: Context) : File {
            val dir = Environment.DIRECTORY_PICTURES + "/YouTique/Images/"
            val file = File(dir)
            file.mkdirs()
            return  file
        }

        fun getUriFromBitmap(context: Context,bitmap: Bitmap): Uri {
            val file = File(createYouTiqueFolder(context).path + "/" + System.currentTimeMillis().toString()+ ".jpeg")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fileOutputStream)
            fileOutputStream.close()
            return Uri.parse(file.path)
        }

        fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
            var cursor: Cursor? = null
            var result : String? = null
            try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
                cursor?.let {
                    cursor.moveToFirst()
                    val column_index: Int = cursor.getColumnIndex(proj[0])
                    result = cursor.getString(column_index)
                }

            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
            return result
        }

        /* @SuppressLint("HardwareIds")
         fun getDeviceObject(context: Context): LoginRequest.Device {
             val device: LoginRequest.Device = LoginRequest.Device()
             device.device_id = Settings.Secure.getString(
                 context.applicationContext.contentResolver,
                 Settings.Secure.ANDROID_ID
             )
             device.device_manufacture = Build.MANUFACTURER
             device.device_name = Build.MODEL
             device.device_os = "A" //stands for android
             device.fcm_token = "Any_dummy_token_for_now" // todo change it when fcm comes
             return device
         }*/

        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String? {
            return Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }

        fun getUUID() : String {
            return UUID.randomUUID().toString().toUpperCase();
        }

        fun getBitmapFromView(view: View): Bitmap? {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }

        fun diffDateTime(commentDate: Date) : String {

            val curDate:Date = Date()

            val diff = curDate.time - commentDate.time
            val seconds = diff/1000
            val minutes = seconds/60
            val hours  = minutes/60
            val days = hours/24
            val week = days/7
            val months = days/30
            val years = months/12

            if (years >= 2){
                return "${years}y"
            }else if (years==1L){
                return "1y"
            }else if (months >= 2) {
                return "${months}m"
            }else if (months == 1L) {
                return "1m"
            }else  if (week >= 2) {
                return "${week}w"
            } else if (week == 1L){
                return "1w"
            } else if (days >= 2) {
                return "${days}d"
            } else if (days == 1L){
                return "1d"
            } else if (hours >= 2) {
                return "${hours}h"
            } else if (hours == 1L){
                return "1h"
            } else if (minutes >= 2) {
                return "${minutes}min"
            } else if (minutes == 1L){
                return "1min"
            } else if (seconds >= 5) {
                return "${seconds}s"
            } else {
                return "Now"
            }

        }


        fun diffDateTimes(commentDate: Date) : String {

            val curDate:Date = Date()

            val diff = curDate.time - commentDate.time
            val seconds = diff/1000
            val minutes = seconds/60
            val hours  = minutes/60
            val days = hours/24
            val week = days/7
            val months = days/30
            val years = months/12

            if (years >= 2){
                return "${years}y"
            }else if (years==1L){
                return "1y"
            }else if (months >= 2) {
                return "${months}m"
            }else if (months == 1L) {
                return "1m"
            }else  if (week >= 2) {
                return "${week}w"
            } else if (week == 1L){
                return "1w"
            } else if (days >= 2) {
                return "${days}d"
            } else if (days == 1L){
                return "1d"
            } else if (hours >= 2) {
                return "${hours}h"
            } else if (hours == 1L){
                return "1h"
            } else if (minutes >= 2) {
                return "${minutes}min"
            } else if (minutes == 1L){
                return "1min"
            } else {
                return "${seconds}s"
            }

        }

        fun convertTimestampToDatetime(date: Date?,format:String?) : String {
            try {
                if (date != null) {
                    val sfd = SimpleDateFormat(format)
                    sfd.setTimeZone(TimeZone.getTimeZone("UTC"));
                    return sfd.format(date!!)
                } else {
                    return ""
                }
            }
            catch (e:Exception) {
                e.printStackTrace()

                return ""
            }
        }

        fun convertStringToDatetime(date: String?,format:String?) : Date? {
            try {
                if (date != null) {
                    val sfd = SimpleDateFormat(format)
                    sfd.setTimeZone(TimeZone.getTimeZone("UTC"));
                    return sfd.parse(date)
                } else {
                    return null
                }
            }
            catch (e:Exception) {
                e.printStackTrace()

                return null
            }
        }

        fun convertDateToUTCDate(date: Date?,format:String?) : Date? {
            try {
                if (date != null) {
                    val sfd = SimpleDateFormat(format!!)
                    sfd.setTimeZone(TimeZone.getTimeZone("UTC"));
                    return sfd.parse(sfd.format(date))
                } else {
                    return date
                }
            }
            catch (e:Exception) {
                e.printStackTrace()

                return date
            }
        }



    }







    interface PermissionListener {
        fun onPermissionGranted()
        fun onPermissionDenied(deniedPermissions: ArrayList<String>)
    }

    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
        var mPattern: Pattern
        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            val matcher: Matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else null
        }

        init {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")
        }
    }




}