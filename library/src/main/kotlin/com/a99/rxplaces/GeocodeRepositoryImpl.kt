package com.a99.rxplaces

import com.a99.rxplaces.GoogleMapsApi.Companion.INVALID_REQUEST
import com.a99.rxplaces.GoogleMapsApi.Companion.OK
import com.a99.rxplaces.GoogleMapsApi.Companion.OVER_QUERY_LIMIT
import com.a99.rxplaces.GoogleMapsApi.Companion.REQUEST_DENIED
import com.a99.rxplaces.GoogleMapsApi.Companion.UNKNOWN_ERROR
import com.a99.rxplaces.GoogleMapsApi.Companion.ZERO_RESULTS
import io.reactivex.Flowable

internal class GeocodeRepositoryImpl (val api: GoogleMapsApi, val key: String) : GeocodeRepository {
  override fun reverseGeocode(placeId: String): Flowable<GeocodeResult> {
    return api.getReverseGeocode(key, placeId)
        .flatMapPublisher {
          when (it.status) {
            OK -> Flowable.fromIterable(it.results)
            ZERO_RESULTS -> Flowable.empty()
            OVER_QUERY_LIMIT -> Flowable.error(OverQueryLimitException(FAILURE_MESSAGE))
            REQUEST_DENIED -> Flowable.error(RequestDeniedException(FAILURE_MESSAGE))
            INVALID_REQUEST -> Flowable.error(InvalidRequestException(FAILURE_MESSAGE))
            UNKNOWN_ERROR -> Flowable.error(UnknownErrorException(FAILURE_MESSAGE))
            else -> Flowable.error(Exception(FAILURE_MESSAGE.plus(" Status: ${it.status}")))
          }
        }
  }

  companion object {
    private const val FAILURE_MESSAGE = "Can't perform reverse geocode."
  }
}