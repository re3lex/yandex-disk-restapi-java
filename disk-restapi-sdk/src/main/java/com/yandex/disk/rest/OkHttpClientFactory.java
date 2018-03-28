/*
 * (C) 2015 Yandex LLC (https://yandex.com/)
 *
 * The source code of Java SDK for Yandex.Disk REST API
 * is available to use under terms of Apache License,
 * Version 2.0. See the file LICENSE for the details.
 */

package com.yandex.disk.rest;


import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class OkHttpClientFactory {

  private static final int CONNECT_TIMEOUT_MILLIS = 30 * 1000;
  private static final int READ_TIMEOUT_MILLIS = 30 * 1000;
  private static final int WRITE_TIMEOUT_MILLIS = 30 * 1000;

  public static OkHttpClient makeClient(Interceptor... interceptors) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder()
      .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
      .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
      .writeTimeout(WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
      .followSslRedirects(true)
      .followRedirects(true);

    if(interceptors != null) {
      for (Interceptor interceptor : interceptors) {
        builder.addInterceptor(interceptor);
      }

    }
    OkHttpClient client =  builder.build();
    return client;
  }
}
