package test.app.everything.features.search

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import test.app.everything.databinding.DialogSearchBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SearchDialog(context: Context) : Dialog(context) {
    private lateinit var binding: DialogSearchBinding
    private val viewModel = SearchViewModel()
    private val languages = mutableMapOf(
        "ar" to false,
        "de" to false,
        "en" to false,
        "fr" to false,
    )
    private val sortBy = mutableMapOf(
        "publishedAt" to true,
        "relevancy" to false,
        "popularity" to false,
    )

    private fun initDataView() {
        binding.query.setText(viewModel.mapOrQuery["q"])
        binding.description.setText(viewModel.mapOrQuery["description"])
        binding.title.setText(viewModel.mapOrQuery["qInTitle"])
        binding.content.setText(viewModel.mapOrQuery["content"])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(searchOldData.isNotEmpty())

        binding.viewModel = viewModel
        with(viewModel.mapOrQuery) {
            clear()
            if (searchOldData.isNotEmpty()) {
                if (searchOldData.containsKey("language")) {
                    languages[searchOldData["language"]!!] = true
                }
                if (searchOldData.containsKey("sortBy")) {
                    sortBy["publishedAt"] = false
                    sortBy[searchOldData["sortBy"]!!] = true
                }
                putAll(searchOldData)
                initDataView()
            }
        }


        binding.from.setEndIconOnClickListener {
            binding.from.editText?.let {
                showSelectText(it)
            }
        }

        binding.to.setEndIconOnClickListener {
            binding.to.editText?.let {
                showSelectText(it)
            }
        }

        initViewGroup(binding.sortBy, "sortBy", sortBy)


        CoroutineScope(Dispatchers.Main).launch {
            viewModel.values.collectLatest {
                if (it.isNotEmpty()) {
                    onSubmitAction(it)
                    dismiss()
                }
            }

        }

        initViewGroup(binding.lang, "language", languages)


    }

    private fun showSelectText(view: EditText) {
        val myCalendar: Calendar = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val format1 = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(format1, Locale("en"))
            view.setText(sdf.format(myCalendar.time))
        }
        DatePickerDialog(
            view.context,
            dateListener,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).apply {
            datePicker.maxDate = Calendar.getInstance().timeInMillis
        }.show()

    }


    private fun initViewGroup(view: RadioGroup, mainKey: String, data: MutableMap<String, Boolean>) {
        view.removeAllViews()
        data.forEach { (key, _) ->
            val viewItem = RadioButton(view.context)
            viewItem.text = key
            view.addView(viewItem)
            viewItem.setOnCheckedChangeListener { _, isChecked ->
                data.filterValues { it }.keys.forEach {
                    data[it] = false
                }
                if (isChecked) viewModel.mapOrQuery[mainKey] = key
            }
        }

        data.filterValues { it }.forEach { (key, _) ->
            view.children.forEach {
                if (it is RadioButton && it.text == key) {
                    it.isChecked = true
                }
            }
        }
    }

    companion object {
        fun intDialog(
            context: Context, oldData: Map<String, String> = emptyMap(), onSubmit: (Map<String, String>) ->
            Unit
        ) {
            onSubmitAction = onSubmit
            searchOldData = oldData
            SearchDialog(context).apply {
                show()
            }
        }

        private var onSubmitAction: (Map<String, String>) -> Unit = {}
        private var searchOldData = mapOf<String, String>()

    }
}