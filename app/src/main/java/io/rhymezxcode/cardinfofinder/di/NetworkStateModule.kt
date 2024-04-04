package io.rhymezxcode.cardinfofinder.di

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.rhymezxcode.networkstateobserver.network.NetworkStateObserver

@Module
@InstallIn(ActivityComponent::class)
object NetworkStateModule {

    @Provides
    fun provideNetworkStateObserver(
        activity: Activity
    ): NetworkStateObserver {
        return NetworkStateObserver.Builder()
            .activity(activity = activity)
            .build()
    }
}