import com.google.gson.Gson

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import test.app.everything.features.list.data.ArticleRepository
import test.app.everything.features.list.data.ArticlesMapper
import test.app.everything.features.list.data.remote.ApiService
import test.app.everything.features.list.data.remote.response.ArticleDataDto
import test.app.everything.features.list.domain.ArticleData
import test.app.everything.features.list.domain.ArticleSource
import test.app.everything.network.NewsResponse

@ExperimentalCoroutinesApi
class CompetitionsRepositoryTest {

    private lateinit var mockApi: ApiService
    private lateinit var mockMapper: ArticlesMapper
    private lateinit var repository: ArticleRepository
    private val jsonData = "{\n" +
            "  \"status\": \"ok\",\n" +
            "  \"totalResults\": 1,\n" +
            "  \"articles\": [\n" +
            "    {\n" +
            "      \"source\": {\n" +
            "        \"id\": null,\n" +
            "        \"name\": \"\"\n" +
            "      },\n" +
            "      \"author\": \"\",\n" +
            "      \"title\": \"\",\n" +
            "      \"description\": \"\",\n" +
            "      \"url\": \"\",\n" +
            "      \"urlToImage\": \"\",\n" +
            "      \"publishedAt\": \"\",\n" +
            "      \"content\": \"\"\n" +
            "    }\n" +
            "  ]\n" +
            "}"

    @Before
    fun setup() {
        mockApi = mock(ApiService::class.java)
        mockMapper = ArticlesMapper()

        repository = ArticleRepository(mockApi)
    }

    @Test
    fun testFetchCompetitions() = runBlockingTest {
        // Mock data

        val newsApiResponse: NewsResponse = Gson().fromJson(jsonData, NewsResponse::class.java)
        val mappedCompetitions = listOf(
            ArticleData(
                "", "",
                "",
                "",
                "",
                ArticleSource("", ""),
                "",
                "", ""
            )
        )


        // Mock API response
        `when`(mockApi.fetchArticles(queryMap = mapOf("q" to "android "))).thenReturn(newsApiResponse)

        // Invoke fetchCompetitions
        val result = repository.fetchArticles(mapOf("q" to "android "))

        `when`(mockMapper.map(newsApiResponse.articles.first())).thenReturn(mappedCompetitions.first())

        // Verify API and mapper interactions
        verify(mockApi).fetchArticles(mapOf("q" to "android "))

        // Verify result
        assertNotNull(result)
        assertEquals(mappedCompetitions, result)
    }
}
