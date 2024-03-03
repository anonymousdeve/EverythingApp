package test.app.everything.core.event

import kotlinx.coroutines.flow.Flow

interface EventQueue<out T> {

    fun getFor(consumerId: String): Flow<T>
}