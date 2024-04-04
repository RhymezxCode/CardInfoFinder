package io.rhymezxcode.cardinfofinder.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.rhymezxcode.cardinfofinder.R
import io.rhymezxcode.cardinfofinder.databinding.ActivityCardOptionSelectionBinding
import io.rhymezxcode.cardinfofinder.util.configureBackPress
import io.rhymezxcode.cardinfofinder.util.launchActivity
import lens24.intent.Card
import lens24.intent.ScanCardCallback
import lens24.intent.ScanCardIntent

@AndroidEntryPoint
class CardOptionSelection : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCardOptionSelectionBinding
    private var backPass: Long? = 0
    private lateinit var snack: Snackbar

    //bundle data
    private var bundle: Bundle = Bundle()


    private var activityResultCallback = ScanCardCallback.Builder()
        .setOnSuccess { card: Card, _: Bitmap? -> setCard(card) }
        .setOnError {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Something went wrong!",
                Snackbar.LENGTH_LONG
            ).show()
        }
        .build()
    private var startActivityIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        activityResultCallback
    )

    fun getCardOptionSelectionActivityIntent(context: Context?): Intent {
        return Intent(context, CardOptionSelection::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardOptionSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardNumber.setOnClickListener(this)
        binding.cardOcr.setOnClickListener(this)

        configureBackPress()
    }

    private fun setCard(card: Card) {
        bundle.putString("cardNumber", card.cardNumber)
        launchActivity(
            intent = OcrConfirm().getOcrConfirmActivityIntent(this).putExtras(bundle),
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

