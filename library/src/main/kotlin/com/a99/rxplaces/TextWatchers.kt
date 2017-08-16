package com.a99.rxplaces

import android.text.Editable
import android.text.TextWatcher
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.Disposable

internal class TextWatchers {
  companion object {
    fun from(subscriber: ObservableEmitter<in CharSequence>) : TextWatcher {
      return object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          if (subscriber is Disposable) {
            if (!subscriber.isDisposed) {
              subscriber.onNext(s)
            }
          } else {
            subscriber.onNext(s)
          }
        }
      }
    }
  }
}