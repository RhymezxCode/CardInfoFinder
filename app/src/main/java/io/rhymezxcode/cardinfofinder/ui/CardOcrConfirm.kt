package io.rhymezxcode.cardinfofinder.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.rhymezxcode.networkstateobserver.network.CheckConnectivity
import io.rhymezxcode.cardinfofinder.R
import io.rhymezxcode.cardinfofinder.databinding.ActivityOcrConfirmBinding
import io.rhymezxcode.cardinfofinder.ui.base.BaseActivity
import io.rhymezxcode.cardinfofinder.ui.viewModel.FetchCardInformationViewModel
import io.rhymezxcode.cardinfofinder.util.Constants
import io.rhymezxcode.cardinfofinder.util.Constants.CARD_DIGITS_REQUIRED_MESSAGE
import io.rhymezxcode.cardinfofinder.util.Constants.NO_INTERNET_CONNECTION_MESSAGE
import io.rhymezxcode.cardinfofinder.util.Resource
import io.rhymezxcode.cardinfofinder.util.dismissLoader
import io.rhymezxcode.cardinfofinder.util.launchActivity
import io.rhymezxcode.cardinfofinder.util.showLoader
import io.rhymezxcode.cardinfofinder.util.showSnack
import io.rhymezxcode.cardinfofinder.util.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardOcrConfirm : BaseActivity<ActivityOcrConfirmBinding>(), View.OnClickListener {

    //bundle
    private var bundle: Bundle = Bundle()

    //card number
    private var cardNumber: String? = null

    //viewModel
    private val fetchCardInformationViewModel: FetchCardInformationViewModel by viewModels()

    fun getOcrConfirmActivityIntent(context: Context?) = Intent(context, CardOcrConfirm::class.java)

    override fun getViewBinding() = ActivityOcrConfirmBinding.inflate(layoutInflater)

    override fun setViews() {
        val bundle = intent.extras

        if (bundle != null)
            binding?.card?.setText(bundle.getString("cardNumber"))

        cardNumber = binding?.card?.text.toString()
            .replace(" ", "").trim()

        binding?.back?.setOnClickListener(this)
        binding?.proceed?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.proceed -> {
                    when {
                        CheckConnectivity.isNetworkAvailable(this) -> {
                            validate()
                        }

                        else -> {
                            this.showSnack(NO_INTERNET_CONNECTION_MESSAGE)
                        }
                    }
                }

                R.id.back -> finish()
            }
        }
    }

    private fun validate() {
        when {
            cardNumber?.isNotEmpty() == true -> {
                if ((cardNumber?.length ?: 0) < 8) {
                    this.showSnack(CARD_DIGITS_REQUIRED_MESSAGE)
                } else {
                    cardNumber?.substring(0, 7)?.let {
                        getInformation(it)
                    }
                }
            }

            else -> {
                this.showSnack(CARD_DIGITS_REQUIRED_MESSAGE)
            }
        }
    }

    private fun getInformation(cardNumber: String) {
        // Ensure that you're accessing the lifecycleScope from the Fragment or Activity
        lifecycleScope.launch {
            fetchCardInformationViewModel.fetchInfoNow(
                cardNumber = cardNumber
            )
        }

        // Collect the Flow inside the lifecycleScope
        lifecycleScope.launch {
            fetchCardInformationViewModel.getResponse.collect { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            dismissLoader()

                            response.data?.let {
                                if (!it.bank?.name.isNullOrEmpty()) {
                                    bundle.putString("brand", it.brand)
                                    bundle.putString("type", it.type)
                                    bundle.putString("bank_name", it.bank?.name)
                                    bundle.putString("country_name", it.country?.name)

                                    launchActivity(
                                        CardInformationDisplay()
                                            .getCardInformationDisplayActivityIntent(
                                                this@CardOcrConfirm
                                            )
                                            .putExtras(bundle), finish = false
                                    )
                                } else {
                                    this@CardOcrConfirm.showSnack(Constants.NO_AVAILABLE_MESSAGE)
                                }
                            }
                        }

                        is Resource.Error -> {
                            dismissLoader()

                            response.message?.let { message ->
                                showToast(message)
                            }
                        }

                        is Resource.Loading -> {
                            showLoader()
                        }

                        is Resource.Empty -> {}
                    }
                }
            }
        }
    }

}


