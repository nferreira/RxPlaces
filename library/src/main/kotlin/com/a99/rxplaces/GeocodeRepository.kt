package com.a99.rxplaces

import io.reactivex.Flowable

interface GeocodeRepository {
  fun reverseGeocode(placeId: String) : Flowable<GeocodeResult>

  companion object {
    fun create(key: String) : GeocodeRepository {
      return GeocodeRepositoryImpl(GoogleMapsApi.create(), key)
    }
  }
}