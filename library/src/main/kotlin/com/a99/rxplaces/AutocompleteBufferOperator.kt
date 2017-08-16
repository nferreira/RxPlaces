package com.a99.rxplaces

import io.reactivex.FlowableOperator
import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.SerializedObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import io.reactivex.subscribers.SerializedSubscriber
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

class AutocompleteBufferOperator<T>(
        scheduler: Scheduler = Schedulers.computation(),
        val timespan: Pair<Long, TimeUnit>
) : ObservableOperator<T, T> {
  private val inner = scheduler.createWorker()
  private var emitterSubscription = Disposables.empty()
  private var scheduledCompletion: (() -> Unit)? = null

  override fun apply(upstreamSubscriber: Observer<in T>): Observer<in T> {
    return AutocompleteBufferOperatorSubscriber(upstreamSubscriber)
  }

  private inner class AutocompleteBufferOperatorSubscriber<T>(
      val upstreamSubscriber: Observer<in T>
  ) : Observer<T> {
    override fun onSubscribe(d: Disposable?) {
    }

    override fun onNext(upstreamData: T) {
      if (upstreamSubscriber is DisposableObserver) {
        if (upstreamSubscriber.isDisposed) return
      }

      val serialized = SerializedObserver<T>(upstreamSubscriber)
      cancelPreviousEmission()
      scheduleNextEmission(serialized, upstreamData)
    }

    override fun onComplete() {
      if (upstreamSubscriber is DisposableObserver) {
        if (upstreamSubscriber.isDisposed) return
      }
      if (!emitterSubscription.isDisposed) {
        upstreamSubscriber.onComplete()
      } else {
        scheduledCompletion = { upstreamSubscriber.onComplete() }
      }
    }

    override fun onError(e: Throwable?) {
      if (upstreamSubscriber is DisposableObserver) {
        if (upstreamSubscriber.isDisposed) return
      }
      upstreamSubscriber.onError(e)
    }

    private fun scheduleNextEmission(serialized: SerializedObserver<T>, t: T) {
      emitterSubscription = inner.schedule(
          {
            serialized.onNext(t)
            scheduledCompletion?.invoke()
          },
          timespan.interval(), timespan.timeUnit())
    }

    private fun cancelPreviousEmission() {
      emitterSubscription.dispose()
    }
  }
}