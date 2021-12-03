package com.mms.oms.config.retry

class RetryExhaustedException(message: String, exception: Exception?) : Throwable(message, exception)
