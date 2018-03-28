/*
 * (C) 2015 Yandex LLC (https://yandex.com/)
 *
 * The source code of Java SDK for Yandex.Disk REST API
 * is available to use under terms of Apache License,
 * Version 2.0. See the file LICENSE for the details.
 */

package com.yandex.disk.rest.example;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.yandex.disk.rest.RestClient;

public class RestClientUtil {

  public static RestClient getInstance(final Credentials credentials) {
    return new RestClient(credentials, new StethoInterceptor());
  }
}
