package com.test.technical.movies.util

import io.reactivex.Scheduler

interface SchedulersWrapper {
  val io: Scheduler
  val main: Scheduler
}
