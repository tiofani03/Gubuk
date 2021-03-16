package com.tegar.gubuk.utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tegar.gubuk.R
import io.github.achmadhafid.lottie_dialog.core.*
import io.github.achmadhafid.lottie_dialog.model.LottieDialogInput
import io.github.achmadhafid.lottie_dialog.model.LottieDialogTheme
import io.github.achmadhafid.lottie_dialog.model.LottieDialogType
import io.github.achmadhafid.lottie_dialog.model.withProperties

object DialogHelper {
    inline fun <reified T> T.d(message: String) = Log.d("TAG", message)

    val requestSomething =
        lottieConfirmationDialogBuilder {
            onShow { d("showing dialog") }
            onCancel { d("dialog canceled") }
            onDismiss { d("dialog dismissed") }
        }

    val doSomething =
        lottieLoadingDialogBuilder {
            onShow { d("showing dialog") }
            onCancel { d("dialog canceled") }
            onDismiss { d("dialog dismissed") }
        }

    val askSomething =
        lottieInputDialogBuilder {
            onShow { d("showing dialog") }
            onCancel { d("dialog canceled") }
            onDismiss { d("dialog dismissed") }
        }


    fun AppCompatActivity.showLoading() {
        lottieLoadingDialog(Int.MAX_VALUE, doSomething) {
            type = LottieDialogType.BOTTOM_SHEET
            showTimeOutProgress = true

            withAnimation {
                fileRes = R.raw.loading
                withProperties {
                    speed = 2.0f
                }
            }
            theme = LottieDialogTheme.DAY_NIGHT
            withTitle {
                text = "Memuat.."
            }

            withCancelOption {
                onBackPressed = false
                onTouchOutside = false
            }

            timeout = 5000
            showTimeOutProgress = false
        }
    }

}