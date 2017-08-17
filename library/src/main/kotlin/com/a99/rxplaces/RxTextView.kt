package com.a99.rxplaces

import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.MainThreadDisposable
import io.reactivex.android.MainThreadDisposable.verifyMainThread
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

internal class RxTextView {

  companion object {
    fun textChanges(textView: TextView): Observable<CharSequence> {
      val observable = Observable.create<CharSequence> { subscriber ->
        try {
          verifyMainThread()

          val watcher = TextWatchers.from(subscriber)

          textView.addTextChangedListener(watcher)

          subscriber.setDisposable(Disposables.fromAction {
              textView.addTextChangedListener(watcher)
          })
        } catch (t: Throwable) {
          subscriber.onError(t)
        }
      }

      return observable
    }
  }
}