package com.mms.oms.config.retry

class RetryExemptedException(s: String, e: Exception) : Throwable(s, e)
