package ar.edu.utn.frba.ddam.homie.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.edu.utn.frba.ddam.homie.R

class Permissions(context: Context, requesCode : Int) {
    private val context : Context = context
    private val REQUEST_CODE = requesCode

    fun isPermissionsAllowed(permission : String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun askForPermissions(permission: String): Boolean {
        if (!isPermissionsAllowed(permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), REQUEST_CODE)
            }
            return false
        }
        return true
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(context)
            .setTitle(context.resources.getString(R.string.permission_denied))
            .setMessage(context.resources.getString(R.string.permission_denied_message))
            .setPositiveButton(context.resources.getString(R.string.settings),
                DialogInterface.OnClickListener { _, _ ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                })
            .setNegativeButton(context.resources.getString(R.string.cancel),null)
            .show()
    }
}