package com.a99.rxplaces

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subscribers.TestSubscriber
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class RxAutocompleteTest {
    @Rule @JvmField val testSchedulerRule = TestSchedulerRule()
    val testScheduler = testSchedulerRule.testScheduler
    val repository = mock<PlacesAutocompleteRepository> {
        on {
            query(any(), any())
        } doReturn Maybe.fromCallable { listOf<Prediction>() }
    }
    val rxAutocomplete = createRxAutoComplete(repository)

    @Test
    fun manyInputs_samePace() {
//        // given
//        val testSubscriber = TestSubscriber<Any>()
//        val words = listOf("avenida brasil", "rua alvorada", "avenida rio branco")
//
//        val from = simulateTyping(Observable.fromIterable(words))
//                .zipWith(Observable.interval(100, TimeUnit.MILLISECONDS), { word, _ -> word })
//
//        rxAutocomplete.observe(from)
//                .subscribe(testSubscriber)
//
//        // when
//        shiftTime(10, TimeUnit.SECONDS)
//
//        // then
//        testSubscriber.assertNoErrors()
//        testSubscriber.assertValueCount(1)
//
//        verify(repository).query(any(), any())
    }

    @Test
    fun manyInputs_differentPace_oneQuery() {
//        // given
//        val testSubscriber = TestSubscriber<Any>()
//        val testSubject = PublishSubject.create<String>(testScheduler)
//
//        rxAutocomplete.observe(testSubject)
//                .subscribe(testSubscriber)
//
//        // when
//        testSubject.onNext("aven")
//        shiftTime(300, TimeUnit.MILLISECONDS)
//        verify(repository, never()).query(any(), any())
//
//        testSubject.onNext("aveni")
//        shiftTime(500, TimeUnit.MILLISECONDS)
//        verify(repository, never()).query(any(), any())
//
//        testSubject.onNext("avenida")
//        shiftTime(600, TimeUnit.MILLISECONDS)
//        verify(repository, never()).query(any(), any())
//
//        testSubject.onNext("avenida bra")
//        shiftTime(2, TimeUnit.SECONDS)
//        verify(repository).query(any(), any())
//
//        // then
//        testSubscriber.assertNoErrors()
//        testSubscriber.assertValueCount(1)
    }

    @Test
    fun manyInputs_differentPace_manyQueries() {
//        // given
//        val testSubscriber = TestSubscriber<Any>()
//        val testSubject = PublishSubject.create<String>(testScheduler)
//
//        rxAutocomplete.observe(testSubject)
//                .subscribe(testSubscriber)
//
//        // when
//        testSubject.onNext("aven")
//        shiftTime(1, TimeUnit.SECONDS)
//        verify(repository, never()).query(any(), any())
//
//        testSubject.onNext("aveni")
//        shiftTime(700, TimeUnit.MILLISECONDS)
//        verify(repository, never()).query(any(), any())
//
//        testSubject.onNext("avenida")
//        shiftTime(3, TimeUnit.SECONDS)
//        verify(repository).query(any(), any())
//
//        testSubject.onNext("avenida bra")
//        shiftTime(2, TimeUnit.SECONDS)
//        verify(repository, times(2)).query(any(), any())
//
//        // then
//        testSubscriber.assertNoErrors()
//        testSubscriber.assertValueCount(2)
    }

    @Test
    fun inputAndErase_shouldNotQuery() {
//        // given
//        val testSubscriber = TestSubscriber<Any>()
//        val testSubject = PublishSubject.create<String>(testScheduler)
//
//        rxAutocomplete.observe(testSubject)
//                .subscribe(testSubscriber)
//
//        // when
//        testSubject.onNext("avenida paulista")
//
//        verify(repository, never()).query(any(), any())
//
//        testSubject.onNext("avenida")
//        shiftTime(1, TimeUnit.SECONDS)
//        verify(repository, never()).query(any(), any())
//
//        testSubject.onNext("")
//        shiftTime(1500, TimeUnit.MILLISECONDS)
//        verify(repository, never()).query(any(), any())
//
//        // then
//        testSubscriber.assertNoErrors()
//        testSubscriber.assertNoValues()
    }

    private fun createRxAutoComplete(repository: PlacesAutocompleteRepository) = RxAutocomplete.create(testScheduler, repository, testSchedulerRule::logger)

    private fun shiftTime(interval: Long, timeUnit: TimeUnit) {
        testSchedulerRule.testScheduler.advanceTimeBy(interval, timeUnit)
    }
}