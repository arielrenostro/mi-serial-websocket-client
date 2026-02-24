package com.masterinjection.serialwebsocketclient.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<T>(state: T) {

    val state = MutableStateFlow(state)

    protected open fun updateDefaultFieldsState(): T {
        return state.value
    }

    protected open fun onStateChange(old: T, new: T) {

    }

    protected fun updateState() {
        updateState(null)
    }

    protected fun updateState(
        reducer: (T.() -> T)?
    ) {
        val oldState = state.value
        reducer?.let {
            state.value = it.invoke(state.value)
        }
        state.value = updateDefaultFieldsState()
        onStateChange(oldState, state.value)
    }
}