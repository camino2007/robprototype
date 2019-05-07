package com.rxdroid.api

import io.reactivex.*
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class RetryApiCallBehavior {

    private val errorEventSubject = PublishSubject.create<Throwable>()
    private val retrySubject = PublishSubject.create<Boolean>()

    init {
        disableRetry()
    }

    fun getRetryErrorEvent(): PublishSubject<Throwable> = errorEventSubject

    fun retryLastCall() {
        retrySubject.onNext(true)
    }

    fun disableRetry() {
        retrySubject.onNext(false)
    }

    fun <Data> observableTransformer(): ObservableTransformer<Data, Data> {
        return ObservableTransformer { upstream ->
            upstream
                    .doOnError { throwable ->
                        errorEventSubject.onNext(throwable)
                    }
                    .retryWhen { errors ->
                        errors.zipWith(retrySubject,
                                BiFunction<Throwable, Boolean, Observable<Throwable>> { throwable: Throwable, isRetryEnabled: Boolean ->
                                    if (isRetryEnabled) {
                                        Observable.just(throwable)
                                    } else {
                                        Observable.error(throwable)
                                    }

                                })
                    }
        }
    }

    fun <Data> flowableTransformer():FlowableTransformer<Data, Data>{
        return FlowableTransformer {upstream ->
            upstream
                    .doOnError { throwable ->
                        errorEventSubject.onNext(throwable)
                    }
                    .retryWhen { errors ->
                        errors.zipWith(retrySubject.toFlowable(BackpressureStrategy.LATEST),
                                BiFunction<Throwable, Boolean, Flowable<Throwable>> { throwable: Throwable, isRetryEnabled: Boolean ->
                                    if (isRetryEnabled) {
                                        Flowable.just(throwable)
                                    } else {
                                        Flowable.error(throwable)
                                    }
                                })
                    }
        }
    }

    fun <Data> singleTransformer(): SingleTransformer<Data, Data> {
        return SingleTransformer { upstream ->
            upstream
                    .doOnError { throwable ->
                        errorEventSubject.onNext(throwable)
                    }
                    .retryWhen { errors ->
                        errors.zipWith(retrySubject.toFlowable(BackpressureStrategy.LATEST),
                                BiFunction<Throwable, Boolean, Flowable<Throwable>> { throwable: Throwable, isRetryEnabled: Boolean ->
                                    if (isRetryEnabled) {
                                        Flowable.just(throwable)
                                    } else {
                                        Flowable.error(throwable)
                                    }
                                })
                    }
        }
    }

}
