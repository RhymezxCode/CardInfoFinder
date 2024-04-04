package io.rhymezxcode.cardinfofinder.data.providers.remote

import android.app.Activity
import io.rhymezxcode.cardinfofinder.util.Constants
import io.rhymezxcode.cardinfofinder.util.Constants.BAD_REQUEST
import io.rhymezxcode.cardinfofinder.util.Constants.HTTP_UNAUTHORIZED
import io.rhymezxcode.cardinfofinder.util.Constants.NOT_FOUND
import io.rhymezxcode.cardinfofinder.util.Constants.SERVER_ERROR
import io.rhymezxcode.cardinfofinder.util.Constants.SUCCESS
import io.rhymezxcode.cardinfofinder.util.Constants.TOO_MANY_REQUEST
import io.rhymezxcode.cardinfofinder.util.showSnack
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val activity: Activity
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
        val request = requestBuilder.newBuilder()
            .header("Content-Type", "application/json")
            .build()

        val response: Response = chain.proceed(request)

        when {
            response.isNotFound() -> activity.showSnack(Constants.NOT_FOUND_MESSAGE)
            response.isServerError() -> activity.showSnack(Constants.SERVER_ERROR_MESSAGE)
            response.isUnAuthorised() -> activity.showSnack(Constants.UNAUTHORISED_MESSAGE)
            response.isTooManyRequest() -> activity.showSnack(Constants.TOO_MANY_REQUEST_MESSAGE)
            response.isBadRequest() -> activity.showSnack(Constants.BAD_REQUEST_MESSAGE)
            response.isSuccessful() -> activity.showSnack(Constants.FETCHED_MESSAGE)
            else -> activity.showSnack()
        }

        return response

    }

    private fun Response.isSuccessful() = this.code == SUCCESS
    private fun Response.isNotFound() = this.code == NOT_FOUND
    private fun Response.isServerError() = this.code == SERVER_ERROR
    private fun Response.isUnAuthorised() = this.code == HTTP_UNAUTHORIZED
    private fun Response.isTooManyRequest() = this.code == TOO_MANY_REQUEST
    private fun Response.isBadRequest() = this.code == BAD_REQUEST

}