package org.lakulishdham.api

class Apis {

    companion object{

        const val OK_RESPONSE = 200

        const val CODE_VALIDATION_ERROR = 1001
        const val CODE_INVALID_LOGIN = 1002
        const val CODE_INVALID_TOKEN = 1003
        const val CODE_UNAUTHORIZED_REQUEST = 1004

        const val isProduction = true

        fun getBaseURL() : String {
            var url = HTTPS_URL
            if (isProduction) { url += LIVE_SUBDOMAIN_URL }
            else { url = "http://${DEMO_SUBDOMAIN_URL}" }
            url += BASE_URL
            return url
        }

        const val HTTPS_URL = "https://"
        const val DEMO_SUBDOMAIN_URL = "demo"
        const val LIVE_SUBDOMAIN_URL = "app"

        const val BASE_URL = ".lakulishsanskriti.org/"

        const val NEW_DATA_URL = "https://countriesnow.space/api/v0.1/"
        const val GET_STATES = "countries/states"
        const val GET_CITIES = "countries/state/cities"

        const val API_PREFIX = "api/"

        const val LOGIN = "${API_PREFIX}login"
        const val SIGN_UP = "${API_PREFIX}register"
        const val SEND_OTP = "${API_PREFIX}send-otp"
        const val VERIFY_OTP = "${API_PREFIX}verify-otp"
        const val RESET_PASSWORD = "${API_PREFIX}reset-password"
        const val GALLERY_LIST = "${API_PREFIX}gallery-list"
        const val LOGOUT = "${API_PREFIX}logout"
        const val GENERATE_ORDER = "${API_PREFIX}generate-orderid"
        const val SUBSCRIPTION_LIST = "${API_PREFIX}get-subscription-plans"
        const val GENERATE_SUBSCRIPTION = "${API_PREFIX}generate-subscription"
        const val COUNTRY_LIST = "${API_PREFIX}countries"
        const val UPDATE_PROFILE = "${API_PREFIX}update-details"
        const val UPDATE_PASSWORD = "${API_PREFIX}change-password"
        const val MEMBER_LIST = "${API_PREFIX}get-members"
        const val ADD_MEMBER = "${API_PREFIX}add-member"
        const val UPDATE_MEMBER = "${API_PREFIX}update-member"
        const val DELETE_MEMBER = "${API_PREFIX}delete-member"

        const val ADD_DONATION = "${API_PREFIX}add-donation"
        const val CANCEL_SUBSCRIPTION = "${API_PREFIX}cancel-subscription"
        const val GET_DONATIONS = "${API_PREFIX}get-donations"


    }

}