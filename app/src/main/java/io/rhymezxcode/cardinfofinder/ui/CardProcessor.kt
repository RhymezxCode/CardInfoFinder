package io.rhymezxcode.cardinfofinder.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.braintreepayments.cardform.view.CardForm
import dagger.hilt.android.AndroidEntryPoint
import io.github.rhymezxcode.networkstateobserver.network.CheckConnectivity
import io.rhymezxcode.cardinfofinder.R
import io.rhymezxcode.cardinfofinder.databinding.ActivityCardProcessorBinding
import io.rhymezxcode.cardinfofinder.ui.base.BaseActivity
import io.rhymezxcode.cardinfofinder.ui.viewModel.FetchCardInformationViewModel
import io.rhymezxcode.cardinfofinder.util.Constants
import io.rhymezxcode.cardinfofinder.util.Resource
import io.rhymezxcode.cardinfofinder.util.dismissLoader
import io.rhymezxcode.cardinfofinder.util.launchActivity
import io.rhymezxcode.cardinfofinder.util.showLoader
import io.rhymezxcode.cardinfofinder.util.showSnack
import io.rhymezxcode.cardinfofinder.util.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardProcessor : BaseActivity<ActivityCardProcessorBinding>(), View.OnClickListener {

    //card form layout
    private var cardForm: CardForm? = null

    //bundle data
    private var bundle: Bundle = Bundle()

    //viewModel
    private val fetchCardInformationViewModel: FetchCardInformationViewModel by viewModels()

    fun getCardProcessorActivityIntent(context: Context?) =
        Intent(context, CardProcessor::class.java)

    override fun getViewBinding() = ActivityCardProcessorBinding.inflate(layoutInflater)

    override fun setViews() {
        cardForm = binding?.card
        cardForm?.cardRequired(true)?.setup(this)
        binding?.back?.setOnClickListener(this)
        binding?.proceed?.setOnClickListener(this)
    }

    private fun validate() {
        when {
            cardForm?.cardNumber?.isNotEmpty() == true -> {
                if ((cardForm?.cardNumber?.length ?: 0) < 8) {
                    this.showSnack(Constants.CARD_DIGITS_REQUIRED_MESSAGE)
                } else {
                    cardForm?.cardNumber?.substring(0, 7)?.let {
                        getInformation()
                    }
                }
            }

            else -> {
                this.showSnack(Constants.CARD_DIGITS_REQUIRED_MESSAGE)
            }
        }
    }

    private fun getInformation() {
        lifecycleScope.launch {
            fetchCardInformationViewModel.fetchInfoNow(
                cardNumber = cardForm?.cardNumber ?: ""
            )
        }

        fetchCardInformationViewModel.getResponse.observe(
            this
        ) { event ->
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
                                        .getCardInformationDisplayActivityIntent(this)
                                        .putExtras(bundle), finish = false
                                )
                            } else {
                                this.showSnack(Constants.NO_AVAILABLE_MESSAGE)
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
                }
            }
        }

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
                            this.showSnack(Constants.NO_INTERNET_CONNECTION_MESSAGE)
                        }
                    }
                }

                R.id.back -> finish()
            }
        }
    }


}


