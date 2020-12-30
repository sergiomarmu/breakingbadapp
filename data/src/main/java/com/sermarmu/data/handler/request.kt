@file:Suppress("RemoveExplicitTypeArguments") // Required because there are some weird ide/compiler errors

package com.sermarmu.data.handler

import retrofit2.Response
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

@Throws(
    DataException.Network::class,
    DataException.Unexpected::class,
    DataException.Unparseable::class
)
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
        throw e
    } catch (e: IOException) {
        throw when (e) {
            is UnknownHostException -> DataException.Network(
                message = "Unknown Host Error"
            )
            is SocketException -> DataException.Network(
                message = "Socket Error"
            )
            is SocketTimeoutException -> DataException.Network(
                message = "Socket Timeout Error"
            )
            else -> DataException.Unexpected(
                message = "Unexpected Error"
            )
        }
    } catch (e: IllegalStateException) {
        throw DataException.Unparseable(
            message = "Body Unparseable Error"
        )
    }  catch (e: Exception) {
        throw DataException.Unexpected(
            message = "Unexpected Error"
        )
    }