package com.sermarmu.data.handler

import okhttp3.Response
import java.lang.Exception

sealed class DataException(
    message: String? = null,
    cause: Throwable? = null
) : Exception() {
    class Unparseable(
        message: String? = null,
        cause: Throwable? = null,
        val response: Response? = null
    ) : DataException(
        message,
        cause
    )

    class Network(
        message: String? = null,
        cause: Throwable? = null,
        val response: Response? = null
    ) : DataException(
        message,
        cause
    )

    class Unexpected(
        message: String? = null,
        cause: Throwable? = null
    ) : DataException(
        message,
        cause
    )
}