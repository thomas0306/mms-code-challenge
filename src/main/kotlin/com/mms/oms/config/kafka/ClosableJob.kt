package com.mms.oms.config.kafka

import java.io.Closeable

interface ClosableJob : Closeable, Runnable
