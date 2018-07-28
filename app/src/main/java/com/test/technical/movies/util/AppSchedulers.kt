package com.test.technical.movies.util

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

class AppSchedulers : SchedulersWrapper {
  override val io: Scheduler get() = Schedulers.io()
  override val main: Scheduler get() = AndroidSchedulers.mainThread()
}
