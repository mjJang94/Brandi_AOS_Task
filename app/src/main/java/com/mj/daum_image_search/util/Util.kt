package com.mj.daum_image_search.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mj.daum_image_search.impl.DialogClickListener

open class Util {

    /**
     * 키보드 사출
     */
    open fun showKeyboard(context: Context) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * 키보드 숨기기
     */
    open fun hideKeyBoard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 에러 다이얼로그
     */
    open fun showErrorDialog(
        activity: Activity,
        title: String,
        msg: String,
        positBtnText: String,
        negetiBtnText: String,
        clickListener: DialogClickListener
    ) {
        val dialog = AlertDialog.Builder(activity)
        dialog
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(positBtnText) { dialogInterface: DialogInterface, _: Int ->
                clickListener.positiveClick(dialogInterface)
            }
            .setNegativeButton(negetiBtnText) { dialogInterface: DialogInterface, _: Int ->
                clickListener.negetiveClick(dialogInterface)
            }
            .setCancelable(true)
            .show()
    }
}