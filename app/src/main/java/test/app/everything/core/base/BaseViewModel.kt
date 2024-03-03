package test.app.everything.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import test.app.everything.core.event.EventQueue
import test.app.everything.core.event.MutableEventQueue


abstract class BaseViewModel<E>(
    private val dispatchers: BaseCoroutineDispatchers = BaseCoroutineDispatchers()
) : ViewModel() {

    private val _eventQueue = MutableEventQueue<E>(dispatchers)
    val eventQueue: EventQueue<E> = _eventQueue

    open fun loadInitialData() {}

    fun push(state: E) {
        viewModelScope.launch { _eventQueue.push(state) }
    }

    fun pushSingle(state: E) {
        viewModelScope.launch { _eventQueue.pushSingle(state) }
    }

    suspend fun CoroutineScope.push(state: E) {
        _eventQueue.push(state)
    }

    suspend fun CoroutineScope.pushSingle(state: E) {
        _eventQueue.pushSingle(state)
    }

}