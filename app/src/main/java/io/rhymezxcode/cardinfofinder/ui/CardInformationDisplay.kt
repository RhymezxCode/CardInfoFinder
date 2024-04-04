package io.rhymezxcode.cardinfofinder.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.rhymezxcode.cardinfofinder.R
import io.rhymezxcode.cardinfofinder.databinding.ActivityCardInformationDisplayBinding
import io.rhymezxcode.cardinfofinder.util.configureBackPress
import io.rhymezxcode.cardinfofinder.util.launchActivity

@AndroidEntryPoint
class CardInformationDisplay : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityCardInformationDisplayBinding? = null

    fun getCardInformationDisplayActivityIntent(context: Context?): Intent {
        return Intent(context, CardInformationDisplay::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardInformationDisplayBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.back?.setOnClickListener {
            finish()
        }

        val bundle = intent.extras

        if (bundle != null) {
            binding?.cardBrand?.text = bundle.getString("brand")
            binding?.cardType?.text = bundle.getString("type")
            binding?.bank?.text = bundle.getString("bank_name")
            binding?.country?.text = bundle.getString("country_name")
        }

        binding?.menu?.setOnClickListener(this)

        configureBackPress()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.menu -> {
                    launchActivity(
                        intent = CardOptionSelection()
                            .getCardOptionSelectionActivityIntent(this),
                        finish = false
                    )
                }
            }
        }
    }


}
