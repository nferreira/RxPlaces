package com.a99.rxplaces

import com.a99.rxplaces.options.AutocompleteOptions
import io.reactivex.Maybe

interface PlacesAutocompleteRepository {
  fun query(input: String, options: AutocompleteOptions) : Maybe<List<Prediction>>
}