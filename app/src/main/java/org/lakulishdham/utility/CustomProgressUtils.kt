package org.lakulishdham.utility

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.lakulishdham.R
import java.lang.Exception


class CustomProgressUtils {

    companion object {

        private var progressDialog : Dialog? = null
        fun showProgress(context: Context?) {
            if (progressDialog == null) {

                progressDialog = Dialog(context!!,R.style.full_screen_dialog)
                progressDialog!!.setContentView(R.layout.custom_loading_dialog)
                progressDialog!!.setCanceledOnTouchOutside(false)
                progressDialog!!.setCancelable(false)

            }
            progressDialog!!.show()
        }

        fun hideProgress() {
            try {
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                    progressDialog = null
                }
            }
            catch (e : Exception) {
                e.printStackTrace()
                progressDialog = null
            }
        }
    }
}