@file:Suppress("RemoveExplicitTypeArguments") // Required because there are some weird ide/compiler errors

package com.sermarmu.data.handler

import kotlinx.coroutines.CancellationException
import okio.IOException
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T> tryRequest(
    request: () -> Response<T>
): T =
    try {
        request().let {
            return@let if (it.isSuccessful)
                it.body() ?: throw DataException.Unparseable(
                    message = "Body Unparseable Error",
                    response = it.raw()
                )
            else throw DataException.Network(
                message = "Network Error",
                response = it.raw()
                    .newBuilder()
                    .body(it.errorBody())
                    .build()
            )
        }

    } catch (e: CancellationException) {
        throw DataException.Unexpected(
            message = "Unexpected Error"
        )
    } catch (e: IOException) {
        throw when (e) {
            is UnknownHostException,
            is SocketException,
            is SocketTimeoutException -> DataException.Network(
                message = "Network Error",
                cause = e
            )
            else -> DataException.Unexpected(
                message = "Unexpected Error"
            )
        }
    } catch (e: IllegalStateException) {
        throw DataException.Unparseable(
            message = "Body Unparseable Error"
        )
    } catch (e: Exception) {
        throw DataException.Unexpected(
            message = "Unexpected Error"
        )
    }