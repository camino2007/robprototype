package fup.prototype.robprototype.view.base.adapters

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class ObserverAdapter<T> : Observer<T> {

    override fun onComplete() {
        //nothing
    }

    override fun onSubscribe(d: Disposable) {
        //nothing
    }

    override fun onNext(t: T) {
        //nothing
    }

    override fun onError(e: Throwable) {
        //nothing
    }
}