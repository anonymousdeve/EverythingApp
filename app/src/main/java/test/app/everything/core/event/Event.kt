package test.app.everything.core.event

interface Event<out T> {
    val content: T
    fun getContent(asker: String): T?
}