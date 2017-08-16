package com.a99.rxplaces

import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.MainThreadDisposable
import io.reactivex.android.MainThreadDisposable.verifyMainThread

internal class RxTextView {

  companion object {
    fun textChanges(textView: TextView): Observable<CharSequence> {
      val observable = Observable.create<CharSequence> { subscriber ->
        try {
          verifyMainThread()

          val watcher = TextWatchers.from(subscriber)

          textView.addTextChangedListener(watcher)

          subscriber.onUnsubscribe { textView.addTextChangedListener(watcher) }
        } catch (t: Throwable) {
          subscriber.onError(t)
        }
      }

      return observable
    }

    infix fun <T> ObservableEmitter<T>.onUnsubscribe(function: () -> Unit) {
//      add(object : MainThreadDisposable() {
//        override fun onDispose() {
//          function()
//        }
//      })
    }
  }
}