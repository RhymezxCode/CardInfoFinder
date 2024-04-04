package io.rhymezxcode.cardinfofinder.util

object Constants {

    //error handling and codes
    const val SUCCESS = 200
    const val NOT_FOUND = 404
    const val SERVER_ERROR = 500
    const val HTTP_UNAUTHORIZED = 401
    const val TOO_MANY_REQUEST = 429
    const val BAD_REQUEST = 400

    //request messages
    const val NO_AVAILABLE_MESSAGE = "No available data from card!"
    const val CARD_DIGITS_REQUIRED_MESSAGE = "Please enter the first 8 digits of your card!"
    const val NOT_FOUND_MESSAGE = "Not Found!"
    const val SERVER_ERROR_MESSAGE = "Server Error!"
    const val UNAUTHORISED_MESSAGE = "Unauthorised!"
    const val TOO_MANY_REQUEST_MESSAGE = "Too Many Request!"
    const val BAD_REQUEST_MESSAGE = "Bad Request!"
    const val FETCHED_MESSAGE = "Fetched Successfully!"
    const val NULL_RESPONSE = "Response body is null"
    const val SOMETHING_IS_WRONG = "Something went wrong!"

    //network messages
    const val NO_INTERNET_CONNECTION_MESSAGE = "No Internet Connection!"
    const val SERVER_OR_NETWORK_CONNECTION_MESSAGE = "Network is lost or issues with server!"
    const val NETWORK_UNAVAILABLE_MESSAGE = "Network is unavailable!"
    const val LOSING_NETWORK_MESSAGE = "You are losing your network!"
    const val NETWORK_LOST_MESSAGE = "Network is lost!"
    const val NETWORK_FAILURE = "Network Failure"
}