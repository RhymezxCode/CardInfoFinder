package io.rhymezxcode.cardinfofinder.ui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import io.github.rhymezxcode.networkstateobserver.network.NetworkStateObserver
import io.rhymezxcode.cardinfofinder.util.NetworkManager
import io.rhymezxcode.cardinfofinder.util.configureBackPress
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    @Inject
    lateinit var networkStateObserver: NetworkStateObserver

    private lateinit var activity: Activity

    protected var binding: VB? = null
    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding?.root)
        init(savedInstanceState)
        setViews()
        configureBackPress()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    open fun init(savedInstanceState: Bundle?) {
        activity = this@BaseActivity
    }

    open fun setViews() {}

    override fun onResume() {
        super.onResume()
        observeNetwork(networkStateObserver, this, lifecycle)
    }

    private fun observeNetwork(
        networkStateObserver: NetworkStateObserver,
        context: Context,
        lifecycle: Lifecycle
    ) {
        lifecycleScope.launch {
            NetworkManager.callNetworkConnection(networkStateObserver, context, lifecycle)
        }
    }
}

