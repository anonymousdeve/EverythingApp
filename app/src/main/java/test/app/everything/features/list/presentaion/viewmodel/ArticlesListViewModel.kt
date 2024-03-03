package test.app.everything.features.list.presentaion.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import test.app.everything.MyApplication
import test.app.everything.core.base.BaseCoroutineDispatchers
import test.app.everything.core.base.BaseViewModel
import test.app.everything.features.list.data.ArticleRepository
import test.app.everything.features.list.data.ArticlesMapper
import test.app.everything.features.list.presentaion.event.ArticleListEvent
import test.app.everything.network.mapList
import test.app.everything.network.process
import javax.inject.Inject
import kotlin.math.log


/**
 * ViewModel class responsible for managing articles list data and handling related business logic.
 *
 * @param articleRepository The repository responsible for fetching articles data from
 *                               remote or local data sources.
 * @param dispatchers The coroutine dispatchers used for performing background tasks.
 */
@HiltViewModel
class ArticlesListViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    dispatchers: BaseCoroutineDispatchers,
    private val articlesMapper: ArticlesMapper

) : BaseViewModel<ArticleListEvent>(dispatchers) {

    /**
     * LiveData object for monitoring network connectivity changes.
     */
    private val connectivityLiveData = ReactiveNetwork.observeNetworkConnectivity(MyApplication.instance.applicationContext)

    /**
     * Method to load initial data when the ViewModel is initialized.
     */
    override fun loadInitialData() {
        fetchArticles(queryMap)
    }

    /**
     * Method to fetch articles data based on network connectivity status.
     */
    @SuppressLint("CheckResult")
    private fun fetchArticles(map: Map<String, String>) {
        connectivityLiveData.subscribe { connectivity ->
            val isConnected = connectivity.available()
            if (isConnected) {
                fetchDataFromRemote(map)
            }
        }
    }


    val listGeneralKeys = listOf(
        "qInTitle",
        "q",
        "content",
        "description",
    )
    private var totalPageCount = 0
    private var currentPage = 1

    fun canLoadMore() = currentPage <= totalPageCount

    var isLoadingMore = false
    var queryMap = mapOf("q" to "android")


    fun loadMore() {
        isLoadingMore = true
        val mapToUpdate = queryMap.toMutableMap()
        queryMap["page"]?.let {
            try {
                var page = it.toInt()
                page += 1
                currentPage = page
                mapToUpdate["page"] = "$page"
            } catch (e: Throwable) {
                Log.e(javaClass.name, "loadMore: ${e.message.toString()}")
            }
        } ?: let {
            currentPage++
            mapToUpdate["page"] = "$currentPage"
        }
        fetchDataFromRemote(mapToUpdate)
    }

    /**
     * Method to fetch articles data from the remote data source.
     */
    fun fetchDataFromRemote(map: Map<String, String>) {
        queryMap = map
        viewModelScope.launch {
            articleRepository.fetchArticles(map)
                .process(onError = {
                    viewModelScope.launch(Dispatchers.IO) {
                        pushSingle(ArticleListEvent.ShowMessage(it?.message))
                    }
                }) {
                    isLoadingMore = false
                    // Handle successful data retrieval from remote source
                    totalPageCount = (it.totalResults / 15).toInt()
                    pushSingle(ArticleListEvent.FetchArticles(it.articles.mapList(articlesMapper)))
                }
        }
    }


}
