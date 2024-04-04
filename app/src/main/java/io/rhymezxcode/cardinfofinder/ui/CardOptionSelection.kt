package io.rhymezxcode.cardinfofinder.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.rhymezxcode.cardinfofinder.R
import io.rhymezxcode.cardinfofinder.databinding.ActivityCardOptionSelectionBinding
import io.rhymezxcode.cardinfofinder.ui.base.BaseActivity
import io.rhymezxcode.cardinfofinder.util.launchActivity
import io.rhymezxcode.cardinfofinder.util.showSnack
import lens24.intent.Card
import lens24.intent.ScanCardCallback
import lens24.intent.ScanCardIntent

@AndroidEntryPoint
class CardOptionSelection : BaseActivity<ActivityCardOptionSelectionBinding>(),
    View.OnClickListener {

    private var activityResultCallback = ScanCardCallback.Builder()
        .setOnSuccess { card: Card, _: Bitmap? -> setCard(card) }
        .setOnError {
            this.showSnack()
        }
        .build()

    private var startActivityIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        activityResultCallback
    )

    fun getCardOptionSelectionActivityIntent(context: Context?) =
        Intent(context, CardOptionSelection::class.java)

    override fun getViewBinding() = ActivityCardOptionSelectionBinding.inflate(layoutInflater)

    override fun setViews() {
        installSplashScreen()
        binding?.cardNumber?.setOnClickListener(this)
        binding?.cardOcr?.setOnClickListener(this)
    }

    private fun setCard(card: Card) {
        val bundle = intent.extras
        bundle?.putString("cardNumber", card.cardNumber)
        launchActivity(
            intent = CardOcrConfirm().getOcrConfirmActivityIntent(this)
                .putExtras(bundle ?: Bundle()),
            finish = false
        )
    }

    private fun scanCard() {
        val intent: Intent = ScanCardIntent.Builder(this)
            // customize these values to suit your needs
            .setVibrationEnabled(true)
            .setHint("Please kindly scan your card")
            .setToolbarTitle("Scan your card")
            .setSaveCard(false)
            .setBottomHint("Please kindly place your card in the box above!")
            .setMainColor(R.color.colorPrimary)
            .build()
        startActivityIntent.launch(intent)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.card_number -> launchActivity(
                    CardProcessor().getCardProcessorActivityIntent(this),
                    finish = false
                )

                R.id.card_ocr -> {
                    scanCard()
                }
            }
        }
    }
}

