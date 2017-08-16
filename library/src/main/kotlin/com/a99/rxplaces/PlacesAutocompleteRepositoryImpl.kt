package com.a99.rxplaces

import com.a99.rxplaces.GoogleMapsApi.Companion.INVALID_REQUEST
import com.a99.rxplaces.GoogleMapsApi.Companion.OK
import com.a99.rxplaces.GoogleMapsApi.Companion.OVER_QUERY_LIMIT
import com.a99.rxplaces.GoogleMapsApi.Companion.REQUEST_DENIED
import com.a99.rxplaces.GoogleMapsApi.Companion.UNKNOWN_ERROR
import com.a99.rxplaces.GoogleMapsApi.Companion.ZERO_RESULTS
import com.a99.rxplaces.options.AutocompleteOptions
import io.reactivex.Maybe

internal class PlacesAutocompleteRepositoryImpl
constructor(val apiKey: String, val googleMapsApi: GoogleMapsApi) : PlacesAutocompleteRepository {

  override fun query(
      input: String,
      options: AutocompleteOptions): Maybe<List<Prediction>> {

    val single = createAutocompleteQuerySingle(input, options)
        .flatMap { flattenPredictions(it) }

    return single
  }

  private fun createAutocompleteQuerySingle(
      input: String,
      options: AutocompleteOptions): Maybe<PlaceAutocompleteResponse> {

    return googleMapsApi.getPlaceAutocomplete(
        key = apiKey,
        input = input,
        offset = options.offset,
        location = options.location?.formatWithComma(),
        radius = options.radius,
        language = options.language,
        types = options.types.toPipedString(),
        components = options.components.toPipedString(),
        strictBounds = options.strictBounds
    )
  }

  private fun flattenPredictions(response: PlaceAutocompleteResponse): Maybe<List<Prediction>> {
    return when (response.status) {
      OK -> Maybe.fromCallable { response.predictions }
      ZERO_RESULTS -> Maybe.fromCallable { emptyList<Prediction>() }
      OVER_QUERY_LIMIT -> Maybe.error(OverQueryLimitException(FAILURE_MESSAGE))
      REQUEST_DENIED -> Maybe.error(RequestDeniedException(FAILURE_MESSAGE))
      INVALID_REQUEST -> Maybe.error(InvalidRequestException(FAILURE_MESSAGE))
      UNKNOWN_ERROR -> Maybe.error(UnknownErrorException(FAILURE_MESSAGE))
      else -> Maybe.error(Exception(FAILURE_MESSAGE.plus("Status: ${response.status}")))
    }
  }

  companion object {
    private const val FAILURE_MESSAGE = "Can't get auto complete options."
  }
}
