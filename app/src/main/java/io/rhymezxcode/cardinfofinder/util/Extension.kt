package io.rhymezxcode.cardinfofinder.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.addCallback
import com.google.android.material.snackbar.Snackbar
import io.rhymezxcode.cardinfofinder.R

private var alertDialog: AlertDialog? = null
private lateinit var snack: Snackbar
private var backPass: Long? = 0

fun Activity.showSnack(info: String? = Constants.SOMETHING_IS_WRONG) {
    Snackbar.make(
        this.findViewById(android.R.id.content),
        "$info",
        Snackbar.LENGTH_LONG
    ).show()
}

@SuppressLint("InflateParams")
fun Activity.showLoader() {
    val builder = AlertDialog.Builder(this)
    val inflater = this.layoutInflater
    builder.setView(inflater.inflate(R.layout.custom_loader, null))
    builder.setCancelable(false)

    alertDialog = builder.create()
    alertDialog?.show()
}

fun dismissLoader() {
    alertDialog?.dismiss()
}

fun Activity.launchActivity(intent: Intent?, finish: Boolean) {
    this.startActivity(intent)
    if (finish) this.finish()
}


//For Toast
fun Context.showToast(message: String? = Constants.SOMETHING_IS_WRONG) {
    Toast.makeText(this, message, Toast.LENGTH_LONG)
        .show()
}

fun androidx.activity.ComponentActivity.configureBackPress() {
    snack = Snackbar.make(
        findViewById(android.R.id.content),
        "Touch again to exit",
        Snackbar.LENGTH_SHORT
    )

    val view = snack.view
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    view.layoutParams = params

    onBackPressedDispatcher.addCallback(owner = this) {
        if (backPass!! + 2000 > System.currentTimeMillis()) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
            finishAffinity()
        } else {
            snack.show()
            backPass = System.currentTimeMillis()
        }
    }
}