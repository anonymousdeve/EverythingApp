package test.app.everything.core.mapper

interface Mapper<in I, out O> {
    fun map(model: I): O
}