package test.app.everything.features.search

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import kotlinx.coroutines.flow.MutableStateFlow

class SearchViewModel : BaseObservable() {
    val onDescriptionObservable = ObservableField<CharSequence>()
    val onQueryObservable       = ObservableField<CharSequence>()
    val onTitleObservable       = ObservableField<CharSequence>()
    val onContentObservable     = ObservableField<CharSequence>()
    val onFromObservable        = ObservableField<CharSequence>()
    val onToObservable          = ObservableField<CharSequence>()

    val values = MutableStateFlow(mapOf<String, String>())
    val mapOrQuery = mutableMapOf<String, String>()

    val error = ObservableField<String?>()
    val showError = ObservableBoolean(false)
    fun submit() {
        val description = onDescriptionObservable.get()
        val query = onQueryObservable.get()
        val content = onContentObservable.get()
        val from = onFromObservable.get()
        val to = onToObservable.get()
        val title = onTitleObservable.get()
        error.set(null)
        showError.set(false)
        notifyChange()
        if (title == null && description == null && content == null && query == null) {
            error.set("required")
            showError.set(true)
            notifyChange()
            return
        }
        title?.let { mapOrQuery["qInTitle"] = it.toString() }
        description?.let { mapOrQuery["description"] = it.toString() }
        content?.let { mapOrQuery["content"] = it.toString() }
        query?.let { mapOrQuery["q"] = it.toString() }
        from?.let { mapOrQuery["from"] = it.toString() }
        to?.let { mapOrQuery["to"] = it.toString() }
        mapOrQuery["page"]="1"
        values.tryEmit(mapOrQuery)
    }

}