package com.sisindia.ai.android.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity.FRAGMENT_REPLACE
import com.sisindia.ai.android.commons.SpinnersListener

fun AppCompatActivity.loadExtFragment(@IdRes resId: Int, fragment: Fragment,
    allowBackStack: Boolean, transactionType: Int) {
    supportFragmentManager.inTransaction {
        if (allowBackStack)
            addToBackStack(null)
        if (transactionType == FRAGMENT_REPLACE)
            replace(resId, fragment)
        else
            add(resId, fragment)
    }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitAllowingStateLoss()
}

fun AppCompatActivity.unloadFragment(): Boolean {
    return if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStackImmediate()
    else
        false
}

fun AppCompatActivity.fragmentContainerId(id: Int): Fragment? {
    return supportFragmentManager.findFragmentById(id)
}

fun AppCompatActivity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.toast(msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(msg: String) {
    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
}

/*fun View.hideKeybord() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    val imm =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
}*/

fun String.toHtmlSpan(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
else
    Html.fromHtml(this)

//@BindingAdapter(value = ["emergencyTypeList", "listener"])
@BindingAdapter(value = ["emergencyListener"])
fun AppCompatSpinner.emergencyType(listener: SpinnersListener) {
    val reasonList = arrayListOf("Select Type", "Temper Alarm",
        "Panic Alarm",
        "Fire Alarm",
        "Medical Emergency Alarm",
        "Hold Up Alarm",
        "Burglary Alarm",
        "False Alarm")

    reasonList.apply {
        val reasonAdapter: SpinnerAdapter = ArrayAdapter(context,
            R.layout.support_simple_spinner_dropdown_item, reasonList)
        adapter = reasonAdapter
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                listener.onSpinnerOptionSelected(pos)
            }
        }
    }
}

@BindingAdapter(value = ["finalStatusListener"])
fun AppCompatSpinner.finalStatus(listener: SpinnersListener) {
    val reasonList = arrayListOf("Select Status", "Real Alarm", "False Alarm", "Customer Test")
    reasonList.apply {
        val reasonAdapter: SpinnerAdapter = ArrayAdapter(context,
            R.layout.support_simple_spinner_dropdown_item, reasonList)
        adapter = reasonAdapter
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                listener.onSpinnerOptionSelected(pos)
            }
        }
    }
}

@BindingAdapter(value = ["isPending", "isSynced"])
fun AppCompatImageView.showStatus(isPending: Boolean, isSynced: Int) {

    if (isPending)
        setImageResource(R.drawable.ic_pending)
    else {
        if (isSynced == 1)
            setImageResource(R.drawable.ic_completed)
        else
            setImageResource(R.drawable.ic_status_not_synced)
    }
}

@BindingAdapter(value = ["guardStatus"])
fun AppCompatImageView.guardStatus(guardStatus: Int) {
    when (guardStatus) {
        2 -> setImageResource(R.drawable.ic_status_pending)
        4 -> setImageResource(R.drawable.ic_group)
    }
}