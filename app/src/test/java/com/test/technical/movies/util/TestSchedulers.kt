package com.test.technical.movies.util

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulers : SchedulersWrapper {
  override val io: Scheduler get() = Schedulers.trampoline()
  override val main: Scheduler get() = Schedulers.trampoline()
}
