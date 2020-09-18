package kg.kyrgyzcoder.primedoc02.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


/**
 * Shared pref keys
 */

const val USER_STATUS = "USER_STATUS"
const val USER_STATUS_KEY = "USER_STATUS_KEy"

const val USER_CREDENTIALS = "USER_CREDENTIALS"
const val USER_PHONE = "USER_PHONE"
const val USER_PWD = "USR_PWD"

const val USER_PIN = "USER_PIN"
const val USER_PIN_KEY = "USER_PIN_KEY"

const val USER_CRED = "USER_CRED"
const val USER_ID_KEY = "USER_ID_KEY"
const val USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN"
const val USER_CHAT_TOKEN = "USER_CHAT_TOKEN"
const val USER_REFRESH_TOKEN = "USER_REFRESH_TOKEN"
const val USER_CHAT_EXP = "USER_CHAT_EXP"
const val USER_REFRESH_EXP = "USER_REFRESH_EXP"


/**
 * Extras
 */

const val EXTRA_USER_PHONE = "EXTRA_USER_PHONE"
const val EXTRA_USER_PWD = "EXTRA_USER_PWD"
const val EXTRA_CODE_TYPE = "EXTRA_CODE_TYPE"

const val EXTRA_PIN = "EXTRA_PIN"

const val EXTRA_CHAT_REF = "EXTRA_CHAT_REF"
const val EXTRA_PHONE = "EXTRA_PHONE"

const val EXTRA_IMG_URL = "EXTRA_IMG_URL"

const val EXTRA_RECEIVE_ID = "EXTRA_RECEIVE_ID"
const val EXTRA_CALL_TYPE = "EXTRA_CALL_TYPE"
const val EXTRA_USER_ID = "EXTRA_USER_ID"

const val EXTRA_CALL_REF = "EXTRA_CALL_REF"
const val EXTRA_USER_NAME = "EXTRA_USER_NAME"


const val REQUEST_CODE_IMG = 11
const val CHOOSE_IMAGE_REQUEST = 112


//Functions to hide keyBoard easily aster typing is done
//hideKeyboard start...
fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyboard(it)
    }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
//hideKeyboard end...
