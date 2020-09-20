package com.example.rxbasics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var TAG = "RX-Response"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Example 1  - /// VERY simple example, convert String to Observable
        // Example 2  - /// And Observable that use the on Next method for every item String to Observable
        // Example 3  - /// Same as 2 but with a error generated to test the onError method
        // Example 4, 5  - /// from* There are a few ways you can use from, and some of them are listed below:
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // Example 6, 7  - /// create* This way you can create an Observable from the ground up. the 6 example use a String Array hit no errors, number 7 returns error
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // Example 8, 9  - /// interval* on example 8 the function will create an finite sequence of ticks, starting at number 10, separated by 1 second, counting only 5 times, whit no initial delay.
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // Example 10, 11  - /// backpressure* on example 10 The example code might result in OutOfMemoryException if the device is not top notch. on the 11 exmaple
        // in order to handle the backpressure in this situation, we will convert it to Flowable.
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // Example 12, 13, 14, 15 - /// emiters*
        // 12 Floable - It works exactly like an Observable but it supports Backpressure.
        // 13 Maybe - return a single optional value. If there is an emitted value, it calls onSuccess , if there’s no value, it calls onComplete or if there’s an error, it calls onError.
        // 14 Single - If we use this class and there is a value emitted, onSuccess will be called. If there’s no value, onError will be called.
        // 15 Completable - The same of Single, but a completable won’t emit any data.
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // Example 16 - /// transform*
        // With a transformer we can avoid repeating some code by applying the most commonly used chains among your Observable,
        // we’ll be chaining subscribeOn and observeOn to a couple of Observable below.
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // operators*
        // Example 17 - map() Transforms values emitted by an Observable stream into a single value.
        runExamples(17)
    }

    private fun runExamples(num: Int){
        when(num){
            1 -> {
                Observable.just("Hello Reactive World")
                    .subscribe { value -> Log.d(TAG, "Ejemplo "+num +" - "+ value) }
            }
            2 -> {
                Observable.just("Apple", "Banana", "Pera")
                    .subscribe(
                        { value -> Log.d(TAG, "Ejemplo "+num +" - Recived: $value") }, //onNext method
                        { error -> Log.d(TAG, "Ejemplo "+num +" - Error: $error")}, // onError method
                        { Log.d(TAG, "Ejemplo "+num +" - COMPLETADO")}) //onComplete method
            }
            3 -> {
                Observable.just("Apple", "Banana", "Pera")
                    .map( { input -> throw RuntimeException() } )
                    .subscribe(
                            { value -> Log.d(TAG, "Ejemplo "+num +" - Recived: $value") }, //onNext method
                            { error -> Log.d(TAG, "Ejemplo "+num +" - Error: $error")}, // onError method
                            { Log.d(TAG, "Ejemplo "+num +" - COMPLETADO")}) //onComplete method
            }
            4 -> {
                Observable.fromArray("Truck", "Car", "Bike")
                    .subscribe { Log.d(TAG, "Ejemplo "+num +" - From: $it") }
            }
            5 -> {
                Observable.fromIterable(listOf("Cat", "Dog", "Duck"))
                        .subscribe(
                                { value -> Log.d(TAG, "Ejemplo "+num +" - From Iterable: $value") },
                                { error -> Log.d(TAG, "Ejemplo "+num +" - From Iterable: $error") },
                                { Log.d(TAG, "Ejemplo "+num +" - From Iterable: COMPLETED")})
            }
            6 -> {
                creatObservableFromList(listOf("Truck", "Car", "Bike","Cat", "Duck", "Apple", "Banana", "Pera"))
                        .subscribe(
                                { value -> Log.d(TAG, "Ejemplo "+num +" - From Create: $value") },
                                { error -> Log.d(TAG, "Ejemplo "+num +" - From Create: $error") },
                                { Log.d(TAG, "Ejemplo "+num +" - From Create: COMPLETED")})
            }
            7 -> {
                creatObservableFromList(listOf("Truck", "Car", "Bike","Cat", "", "Duck"))
                        .subscribe(
                                { value -> Log.d(TAG, "Ejemplo "+num +" - From Create: $value") },
                                { error -> Log.d(TAG, "Ejemplo "+num +" - From Create: $error") },
                                { Log.d(TAG, "Ejemplo "+num +" - From Create: COMPLETED")})
            }
            8 -> {
                Observable.intervalRange(10L, 5L, 0L, 1L, TimeUnit.SECONDS)
                        .subscribe{ Log.d(TAG, "Ejemplo "+num +" - Interval: $it") }
            }
            9 -> {
                Observable.interval(1000,    TimeUnit.MILLISECONDS)
                        .subscribe{ Log.d(TAG, "Ejemplo "+num +" - Interval: $it") }
            }
            10 -> {
                val observable = PublishSubject.create<Int>()
                observable
                        .observeOn(Schedulers.computation())
                        .subscribe (
                                { Log.d(TAG, "Ejemplo "+num +" - Backpressure: $it") },
                                { t -> Log.d(TAG, "Ejemplo "+num +" - Backpressure: $t") }
                        )
                for (i in 0..1000000){
                    observable.onNext(i)
                }
            }
            11 -> {
                Log.d(TAG, "Ejemplo "+num +" - START")
                val observable = PublishSubject.create<Int>()
                observable
                        .toFlowable(BackpressureStrategy.DROP)
                        .observeOn(Schedulers.computation())
                        .subscribe (
                                { Log.d(TAG, "Ejemplo "+num +" - Backpressure: $it") },
                                { t -> Log.d(TAG, "Ejemplo "+num +" - Backpressure: $t") }
                        )
                for (i in 0..1000000){
                    observable.onNext(i)
                }
            }
            12 -> {
                Flowable.just("Hello Reactive World")
                    .subscribe(
                        { value -> Log.d(TAG, "Ejemplo "+num +" - Flowable: $value") }, //onNext method
                        { error -> Log.d(TAG, "Ejemplo "+num +" - Flowable Error: $error")}, // onError method
                        { Log.d(TAG, "Ejemplo "+num +" - Flowable COMPLETADO")})
            }
            13 -> {
                Maybe.just("Hello Reactive World")
                    .subscribe(
                        { value -> Log.d(TAG, "Ejemplo "+num +" - Maybe: $value") }, //onNext method
                        { error -> Log.d(TAG, "Ejemplo "+num +" - Maybe Error: $error")}, // onError method
                        { Log.d(TAG, "Ejemplo "+num +" - Maybe COMPLETADO")})
            }
            14 -> {
                Single.just("Hello Reactive World")
                    .subscribe(
                        { value -> Log.d(TAG, "Ejemplo "+num +" - Single: $value") }, //onNext method
                        { error -> Log.d(TAG, "Ejemplo "+num +" - Single Error: $error") }) // onError method
            }
            15 -> {
                val completable = Completable.create { emitter -> run {
                    emitter.onComplete()
                    //emitter.onError(throw RuntimeException())
                }}
                completable.subscribe(
                    { Log.d(TAG, "Ejemplo "+num +" - Completable COMPLETADO") },
                    { error -> Log.d(TAG, "Ejemplo "+num +" - Completable Error: $error") }
                )
            }
            16 -> {
                Observable.just("Apple", "Orange", "Banana")
                    .compose(applyObservableAsync())
                    .subscribe { value -> Log.d(TAG, "Ejemplo "+num +" - Primer Obs: $value") }

                Observable.just("Water", "Fire", "Wood")
                    .compose(applyObservableAsync())
                    .subscribe { value -> Log.d(TAG, "Ejemplo "+num +" - Segundo Obs: $value") }
            }
            17 -> {
                Observable.just("Apple", "Banana", "Pera")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { m -> m + " 2" }
                    .subscribe(
                        { value -> Log.d(TAG, "Ejemplo "+num +" - Recived: $value") }, //onNext method
                        { error -> Log.d(TAG, "Ejemplo "+num +" - Error: $error")}, // onError method
                        { Log.d(TAG, "Ejemplo "+num +" - COMPLETADO")})
            }
        }
    }

    private fun creatObservableFromList(myList: List<String>): Observable<String>{
        return Observable.create<String> { emitter ->
            myList.forEach { kind ->
                if (kind == ""){
                    emitter.onError(Exception("String is empty"))
                }
                emitter.onNext(kind)
            }
            emitter.onComplete()
        }
    }

    fun <T> applyObservableAsync(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}