package io.rhymezxcode.cardinfofinder.data.providers.remote

import android.content.Context
import com.nibbssdk.util.CoroutinesHelper.coMain
import io.rhymezxcode.cardinfofinder.util.Constants
import io.rhymezxcode.cardinfofinder.util.Constants.BAD_REQUEST
import io.rhymezxcode.cardinfofinder.util.Constants.HTTP_UNAUTHORIZED
import io.rhymezxcode.cardinfofinder.util.Constants.NOT_FOUND
import io.rhymezxcode.cardinfofinder.util.Constants.SERVER_ERROR
import io.rhymezxcode.cardinfofinder.util.Constants.SUCCESS
import io.rhymezxcode.cardinfofinder.util.Constants.TOO_MANY_REQUEST
import io.rhymezxcode.cardinfofinder.util.showToast
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
        val request = requestBuilder.newBuilder()
            .header("Content-Type", "application/json")
            .build()

        val response: Response = chain.proceed(request)

        coMain {
            when {
                response.isNotFound() -> context.showToast(Constants.NOT_FOUND_MESSAGE)
                response.isServerError() -> context.showToast(Constants.SERVER_ERROR_MESSAGE)
                response.isUnAuthorised() -> context.showToast(Constants.UNAUTHORISED_MESSAGE)
                response.isTooManyRequest() -> context.showToast(Constants.TOO_MANY_REQUEST_MESSAGE)
                response.isBadRequest() -> context.showToast(Constants.BAD_REQUEST_MESSAGE)
                response.isSuccessful() -> context.showToast(Constants.FETCHED_MESSAGE)
                else -> context.showToast()
            }
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