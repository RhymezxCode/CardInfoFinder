package io.rhymezxcode.cardinfofinder.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
@InstallIn(SingletonComponent::class)
object AuthModule {

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
    fun provideAuth(
        @ApplicationContext context: Context,
    ) = AuthInterceptor(context)

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

    @Provides
    @Singleton
    fun provideFetchCardInformationRepository(
        api: CardInfoFinderApiList
    ): FetchCardInformationRepository = FetchCardInformationRepositoryImpl(api)
}