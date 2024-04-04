package io.rhymezxcode.cardinfofinder.ui

import android.content.Context
import android.content.Intent
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.rhymezxcode.cardinfofinder.R
import io.rhymezxcode.cardinfofinder.databinding.ActivityCardInformationDisplayBinding
import io.rhymezxcode.cardinfofinder.ui.base.BaseActivity
import io.rhymezxcode.cardinfofinder.util.launchActivity

@AndroidEntryPoint
class CardInformationDisplay : BaseActivity<ActivityCardInformationDisplayBinding>(),
    View.OnClickListener {

    override fun getViewBinding(): ActivityCardInformationDisplayBinding =
        ActivityCardInformationDisplayBinding.inflate(layoutInflater)

    fun getCardInformationDisplayActivityIntent(context: Context?) =
        Intent(context, CardInformationDisplay::class.java)


    override fun setViews() {
        val bundle = intent.extras

        binding?.back?.setOnClickListener {
            finish()
        }

        if (bundle != null) {
            binding?.cardBrand?.text = bundle.getString("brand")
            binding?.cardType?.text = bundle.getString("type")
            binding?.bank?.text = bundle.getString("bank_name")
            binding?.country?.text = bundle.getString("country_name")
        }

        binding?.menu?.setOnClickListener(this)
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
