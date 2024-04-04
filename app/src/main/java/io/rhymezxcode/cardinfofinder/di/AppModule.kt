package io.rhymezxcode.cardinfofinder.di

import android.app.Activity
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.rhymezxcode.networkstateobserver.network.NetworkStateObserver
import io.rhymezxcode.cardinfofinder.data.providers.Url
import io.rhymezxcode.cardinfofinder.data.providers.remote.AuthInterceptor
import io.rhymezxcode.cardinfofinder.data.providers.remote.CardInfoFinderApiList
import io.rhymezxcode.cardinfofinder.data.repository.FetchCardInformationRepository
import io.rhymezxcode.cardinfofinder.data.repository.FetchCardInformationRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Provides
    @Singleton
    fun provideGsonConverter() = GsonConverterFactory
        .create(
            GsonBuilder()
                .setLenient()
                .disableHtmlEscaping()
                .create()
        )

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: Provider<HttpLoggingInterceptor>,
        authInterceptor: Provider<AuthInterceptor>
    ) = OkHttpClient.Builder()
        .addInterceptor(authInterceptor.get())
        .addInterceptor(httpLoggingInterceptor.get())
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideCardInfoFinderApiService(
        gsonConverter: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): CardInfoFinderApiList = Retrofit.Builder()
        .baseUrl(Url.BASE_URL)
        .addConverterFactory(gsonConverter)
        .client(okHttpClient)
        .build()
        .create(CardInfoFinderApiList::class.java)

    //TODO: Observe the network state
    @Provides
    fun provideNetworkStateObserver(
        activity: Activity
    ): NetworkStateObserver {
        return NetworkStateObserver.Builder()
            .activity(activity = activity)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        activity: Activity
    ) = AuthInterceptor(activity)

    @Provides
    @Singleton
    fun provideFetchCardInformationRepository(
        api: CardInfoFinderApiList
    ): FetchCardInformationRepository = FetchCardInformationRepositoryImpl(api)
}