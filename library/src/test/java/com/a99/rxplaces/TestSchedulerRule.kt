package com.a99.rxplaces

import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class TestSchedulerRule : TestRule {
  val testScheduler = TestScheduler()

  fun logger(tag: String, message: String): Unit {
    val formattedDate = Date(testScheduler.now(TimeUnit.SECONDS)).format()
    System.out.println("$tag @ $formattedDate: $message")
  }

  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
//        RxJavaHooks.setOnIOScheduler { testScheduler }
//        RxJavaHooks.setOnComputationScheduler { testScheduler }
//        RxJavaHooks.setOnNewThreadScheduler { testScheduler }
//
//        try {
//          base.evaluate()
//        } finally {
//          RxJavaHooks.reset()
//        }
      }
    }
  }

  fun Date.format(): String {
    return SimpleDateFormat("HH:mm:ss.SSS", Locale.US).format(this)
  }
}