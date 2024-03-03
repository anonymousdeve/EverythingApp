package test.app.everything.features.list.data.remote

import retrofit2.http.GET
import retrofit2.http.QueryMap
import test.app.everything.features.list.data.remote.response.ArticleDataDto
import test.app.everything.network.NewsResponse

/**
 * Interface representing the API service for fetching articles data.
 */
interface ApiService {

    /**
     * Method to fetch articles data from the remote server.
     *
     * @return An ApiResponse containing a list of CompetitionDto objects wrapped in a MutableList.
     */
    @GET("everything")
    suspend fun fetchArticles(
        @QueryMap queryMap: Map<String, String?>
    ): NewsResponse

}
