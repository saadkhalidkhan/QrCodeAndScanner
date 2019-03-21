package com.saad.qrcodeandscanner

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.net.ConnectivityManager
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.util.TypedValue
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import org.jetbrains.anko.indeterminateProgressDialog

/**
 * Created by Mirza Arslan on 20-Feb-18 at 5:32 PM
 *  Company  : Tower Technologies private Limited
 *  E-mail : mirzaarslan450@gmail.com
 *  appToolbar : function to add toolbbar in activity/ fragment
 *  @param context use to access the context of current activity
 *  @param toolbar use to pass the toolbar as parameter and add perform certain toolbar functionality
 *  @param isSetDisplayHomeAsUpEnabled used to set back button visibility for current activity
 *  @param isSetDisplayShowHomeEnabled used to set whether the arrow button as home button visible
 *  @param isSetDisplayShowTitleEnabled used to set whether the title should show on the toolbar
 */
//fun appToolBar(activity: Context, toolbar: Toolbar?, isSetDisplayHomeAsUpEnabled: Boolean = true
//               , isSetDisplayShowHomeEnabled: Boolean = true, isSetDisplayShowTitleEnabled: Boolean = false): Unit {
//
//    (activity as AppCompatActivity).setSupportActionBar(toolbar!!)
//    (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(isSetDisplayHomeAsUpEnabled)
//    (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(isSetDisplayShowHomeEnabled)
//    (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(isSetDisplayShowTitleEnabled)
//}

/*
* navigate : extension function to move from one activity or screen to another activity
*
* */


/*
* showToast : extension function to sohw toast
* @param message : show the message of the toast
* @Param  duration : use to specify the the time duration for showing toast
* */
fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT): Unit {
    Toast.makeText(this, message, duration).show()
}


/*
* showLogoutAlert : extension  function to show the alert message
* @param alertTitle : set the title of alert
* @param alertMessage : set the message of alert
* @param alerttICon : set the icon of alert
* @param isAlertCanceable : set the alert is alert may cancel while click on screen
* @param positiveButtonText : set the text of yes button
* @param negativeButtonText : set the text of no button
* 
* */


/*
* logoutUser : extension function used to logout user and destroy the user session from the current device
* */


/*
* getDeviceID : function used to get the mac address of the current device (used in logoutUser function)
* @return device_Id : returns the device mac id
* */


@SuppressLint("MissingPermission")
fun Activity.getDeviceID(): String? {
    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var device: String
    var device_Id: String? = null
    try {
        device_Id = telephonyManager.deviceId

        if (device_Id !== "") {

        } else {
            device = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            device_Id = device
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return device_Id
}

/**
 * checkNetworkConnectivity : utitlity method to check the connectivity of internet
 * @return : true or false on the basis of current connectivity status to the internet
 */


fun checkNetworkConnectivity(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting && (netInfo.type == ConnectivityManager.TYPE_MOBILE || netInfo.type == ConnectivityManager.TYPE_WIFI)
}

/*
* inflateView : extension function used to inflate a resource file into a View object
* @Param layoutRes : layout resource file
* @return view : returns the view  object of the layout file
* */
fun ViewGroup.inflateView(layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

//@param mobileNo : mobile no of student
//mobileNoFormat : Changes mobile no 0 to 92
fun Activity.mobileNoFormat(mobileNo: String): String {
    var subString: String
    subString = mobileNo.substring(1, 11)
    subString = "92" + subString
    return subString
}

/*
* changeFragmet : extension function to start a new fragment form activity or fragment
* @param  f : this parameter is used to set the fragment you want to replace
* @ cleanStack : check whether the backstack is clear or not
*
* */
fun FragmentActivity.changeFragment(f: Fragment, frame: Int, cleanStack: Boolean = false): Unit {
    val ft = supportFragmentManager.beginTransaction()
    if (cleanStack) {
        clearBackStack()
    }
    ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit)
    ft.replace(frame, f)
    ft.addToBackStack(null)
    ft.commit()
}


/*
* clearBackStack : extension function to clear the backstack of fragments
*
* */
fun FragmentActivity.clearBackStack(): Unit {
    val manager = supportFragmentManager
    if (manager.backStackEntryCount > 0) {
        val first = manager.getBackStackEntryAt(0)
        manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

/*
* setIndicatorRight : set the icon of down indicator at right side
* @param activity : this parameter get the context of curent activity's fragment
* @param explistview : this parameter get the expandable listview object
* */

fun Fragment.setIndicatorRight(activity: Context, expListView: ExpandableListView): Unit {
    val display: Display = (activity as AppCompatActivity).windowManager.defaultDisplay
    val size: Point = Point()
    display.getSize(size)
    val width: Int = size.x
    val r: Resources = resources
    val px: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        50F, r.displayMetrics
    ).toInt().toInt()
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        expListView.setIndicatorBounds(width - px, width)
    } else {
        expListView.setIndicatorBoundsRelative(width - px, width)
    }
}

fun Activity.setIndicatorRightActivity(activity: Context, expListView: ExpandableListView): Unit {
    val display: Display = windowManager.defaultDisplay
    val size: Point = Point()
    display.getSize(size)
    val width: Int = size.x
    val r: Resources = resources
    val px: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        50F, r.displayMetrics
    ).toInt().toInt()
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        expListView.setIndicatorBounds(width - px, width)
    } else {
        expListView.setIndicatorBoundsRelative(width - px, width)
    }
}


var mProgressDialog: ProgressDialog? = null
fun Activity.showDialog(context: Context,message: String = "Please wait", title: String = "QRCode") {
    mProgressDialog = ProgressDialog(context)
    mProgressDialog!!.setTitle(title)
    mProgressDialog!!.setMessage(message)
    mProgressDialog?.setCancelable(false)
    mProgressDialog?.show()
}

fun Activity.dismissDialog() {
    mProgressDialog?.cancel()
}



