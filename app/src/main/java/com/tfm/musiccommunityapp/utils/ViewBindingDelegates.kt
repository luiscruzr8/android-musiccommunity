package com.tfm.musiccommunityapp.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityViewBindingDelegate<V : ViewBinding>(
    private val factory: (LayoutInflater) -> V
) : ReadOnlyProperty<Activity, V> {

    private var binding: V? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): V {
        return binding ?: factory(thisRef.layoutInflater).also {
            thisRef.setContentView(it.root)
            binding = it
        }
    }
}

fun <V : ViewBinding> Activity.viewBinding(factory: (LayoutInflater) -> V) =
    ActivityViewBindingDelegate(factory)


class FragmentViewBindingDelegate<V : ViewBinding>(
    private val fragment: Fragment,
    private val factory: (View) -> V
) : ReadOnlyProperty<Fragment, V> {

    private var binding: V? = null

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    init {
        fragment.lifecycle.addObserver(object : LifecycleObserver {
            val observer = Observer<LifecycleOwner?> {
                it ?: return@Observer
                it.lifecycle.addObserver(object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onDestroy() {
                        handler.post { binding = null }
                    }
                })
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                fragment.viewLifecycleOwnerLiveData.observeForever(observer)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                fragment.viewLifecycleOwnerLiveData.removeObserver(observer)
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): V {
        return binding ?: factory(thisRef.requireView()).also { binding = it }
    }
}

fun <V : ViewBinding> Fragment.viewBinding(factory: (View) -> V) =
    FragmentViewBindingDelegate(this, factory)