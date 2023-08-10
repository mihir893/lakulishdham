package org.lakulishdham.helper

import android.text.TextUtils
import android.util.Log
import org.lakulishdham.BuildConfig


object AppLogger {

    private val TAG = "AppLogger"

    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg)
        }
    }

    fun i(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg)
        }
    }

    fun i(tag: String, msg: Boolean) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg.toString() + "")
        }
    }

    fun i(msg: Boolean) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg.toString() + "")
        }
    }


    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun e(msg: String) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(msg)) {
            Log.e(TAG, msg)
        }
    }

    fun e(tag: String, msg: Boolean) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg.toString() + "")
        }
    }

    fun e(msg: Boolean) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg.toString() + "")
        }
    }

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg)
        }
    }

    fun d(tag: String, msg: Boolean) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg.toString() + "")
        }
    }

    fun d(msg: Boolean) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg.toString() + "")
        }
    }


    fun i(tag: String, msg: Int) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg.toString())
        }
    }

    fun i(msg: Int) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg.toString())
        }
    }

    fun e(tag: String, msg: Int) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg.toString())
        }
    }

    fun e(tag: Int, msg: Int) {
        if (BuildConfig.DEBUG) {
            Log.e(tag.toString(), msg.toString())
        }
    }

    fun e(msg: Int) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg.toString())
        }
    }

    fun d(tag: String, msg: Int) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg.toString())
        }
    }

    fun d(msg: Int) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg.toString())
        }
    }

    fun i(tag: String, obj: Any) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, jsonString(obj))
        }
    }

    fun i(obj: Any) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, jsonString(obj))
        }
    }

    fun e(tag: String, obj: Any) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, jsonString(obj))
        }
    }

    fun e(obj: Any) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, jsonString(obj))
        }
    }

    fun e(tag: String, obj: Float) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, obj.toString() + "")
        }
    }

    fun e(obj: Float) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, obj.toString() + "")
        }
    }

    fun d(tag: String, obj: Any) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, jsonString(obj))
        }
    }

    fun d(obj: Any) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, jsonString(obj))
        }
    }

    fun jsonString(obj: Any): String {
        return AppApplication.gson!!.toJson(obj)
    }

}