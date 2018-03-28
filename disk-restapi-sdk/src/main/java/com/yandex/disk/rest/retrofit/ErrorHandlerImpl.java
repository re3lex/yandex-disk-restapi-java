/*
 * (C) 2015 Yandex LLC (https://yandex.com/)
 *
 * The source code of Java SDK for Yandex.Disk REST API
 * is available to use under terms of Apache License,
 * Version 2.0. See the file LICENSE for the details.
 */

package com.yandex.disk.rest.retrofit;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yandex.disk.rest.exceptions.http.BadGatewayException;
import com.yandex.disk.rest.exceptions.http.BadRequestException;
import com.yandex.disk.rest.exceptions.http.ConflictException;
import com.yandex.disk.rest.exceptions.http.FileTooBigException;
import com.yandex.disk.rest.exceptions.http.ForbiddenException;
import com.yandex.disk.rest.exceptions.http.GoneException;
import com.yandex.disk.rest.exceptions.http.HttpCodeException;
import com.yandex.disk.rest.exceptions.http.InsufficientStorageException;
import com.yandex.disk.rest.exceptions.http.InternalServerException;
import com.yandex.disk.rest.exceptions.http.LockedException;
import com.yandex.disk.rest.exceptions.http.MethodNotAllowedException;
import com.yandex.disk.rest.exceptions.http.NotAcceptableException;
import com.yandex.disk.rest.exceptions.http.NotFoundException;
import com.yandex.disk.rest.exceptions.http.NotImplementedException;
import com.yandex.disk.rest.exceptions.http.PreconditionFailedException;
import com.yandex.disk.rest.exceptions.http.ServiceUnavailableException;
import com.yandex.disk.rest.exceptions.http.TooManyRequestsException;
import com.yandex.disk.rest.exceptions.http.UnauthorizedException;
import com.yandex.disk.rest.exceptions.http.UnprocessableEntityException;
import com.yandex.disk.rest.exceptions.http.UnsupportedMediaTypeException;
import com.yandex.disk.rest.json.ApiError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import retrofit2.Response;


public class ErrorHandlerImpl {

  private static final Logger logger = LoggerFactory.getLogger(ErrorHandlerImpl.class);

  /**
   * @return never return a value, just make compiler happy
   */
  @NonNull
  public static <T> T throwHttpCodeException(@NonNull Response<T> response)
    throws HttpCodeException {
    ApiError error;
    try {
      error = readApiError(response.errorBody().byteStream());
    }
    catch (Exception ex) {
      error = ApiError.UNKNOWN;
    }
    throw createHttpCodeException(response.code(), error);
  }

  @NonNull
  public static HttpCodeException createHttpCodeException(final int httpCode, @NonNull final InputStream in) {
    return createHttpCodeException(httpCode, readApiError(in));
  }

  @NonNull
  private static ApiError readApiError(@NonNull final InputStream in) {
    final Reader reader = new InputStreamReader(in);
    try {
      return new Gson().fromJson(reader, ApiError.class);
    }
    catch (JsonSyntaxException ex) {
      return ApiError.UNKNOWN;
    }
  }

  @NonNull
  private static HttpCodeException createHttpCodeException(final int httpCode, @NonNull final ApiError apiError) {
    logger.debug("getStatus=" + httpCode);
    switch (httpCode) {
      case 400:
        return new BadRequestException(httpCode, apiError);
      case 401:
        return new UnauthorizedException(httpCode, apiError);
      case 403:
        return new ForbiddenException(httpCode, apiError);
      case 404:
        return new NotFoundException(httpCode, apiError);
      case 405:
        return new MethodNotAllowedException(httpCode, apiError);
      case 406:
        return new NotAcceptableException(httpCode, apiError);
      case 409:
        return new ConflictException(httpCode, apiError);
      case 410:
        return new GoneException(httpCode, apiError);
      case 412:
        return new PreconditionFailedException(httpCode, apiError);
      case 413:
        return new FileTooBigException(httpCode, apiError);
      case 415:
        return new UnsupportedMediaTypeException(httpCode, apiError);
      case 422:
        return new UnprocessableEntityException(httpCode, apiError);
      case 423:
        return new LockedException(httpCode, apiError);
      case 429:
        return new TooManyRequestsException(httpCode, apiError);
      case 500:
        return new InternalServerException(httpCode, apiError);
      case 501:
        return new NotImplementedException(httpCode, apiError);
      case 502:
        return new BadGatewayException(httpCode, apiError);
      case 503:
        return new ServiceUnavailableException(httpCode, apiError);
      case 507:
        return new InsufficientStorageException(httpCode, apiError);
      default:
        return new HttpCodeException(httpCode, apiError);
    }
  }
}
