package org.lakulishdham.helper

import org.lakulishdham.api.Apis

class AppConstants {

    companion object {

        const val RAZOR_PAY_TEST_KEY_ID = "rzp_test_24MB3EQeBrL5Dp"
        const val RAZOR_PAY_LIVE_KEY_ID = "rzp_live_4jppZI5fiz9FWi"

        fun getRazorPayKey() : String {
            if (Apis.isProduction) {
                return RAZOR_PAY_LIVE_KEY_ID
            }
            else {
                return RAZOR_PAY_TEST_KEY_ID
            }
        }

        const val DEFAULT_EMAIL = "lakulishdham@gmail.com"
        const val DEFAULT_REFERBY = "lakulishdham_app"
        const val TERMS_CONDITIONS = "https://lakulishsanskriti.org/termsandcondition.html"

        const val FROM_LOGIN = "login"
        const val FROM_REGISTER = "register"
        const val FROM_FORGOT_PASSWORD = "forgot_password"

        const val RUPEE_ICON = "\u20B9"

        const val TYPE_COUNTRY = 1
        const val TYPE_STATE = 2
        const val TYPE_DISCTRICT = 3

        const val TYPE_ADD = 4
        const val TYPE_EDIT = 5

    }

}