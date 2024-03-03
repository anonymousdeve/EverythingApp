package test.app.everything.features.list.data

import test.app.everything.features.list.data.remote.ApiService
import test.app.everything.network.NetworkRouter
import test.app.everything.network.NewsResponse
import test.app.everything.network.State
import javax.inject.Inject

/**
 * Repository class responsible for handling articles data.
 *
 * @param api The API service interface for fetching articles data from the remote server.
 */

open class ArticleRepository @Inject constructor(
    private val api: ApiService,
) {

    /**
     * Method to fetch articles data from the remote server.
     *
     * @return A list of articles fetched from the remote server.
     */
    suspend fun fetchArticles(map: Map<String, String>): State<NewsResponse> {
        val matToSend = mutableMapOf<String, String>()
        matToSend.putAll(map)
        if (!matToSend.containsKey("page")) {
            matToSend["page"] = "1"
        }
        if (!matToSend.containsKey("pageSize")) {
            matToSend["pageSize"] = "15"
        }
        return NetworkRouter.invokeCall { api.fetchArticles(matToSend) }
    }


}

