package test.app.everything.features.list.presentaion.event

import test.app.everything.features.list.domain.ArticleData

/**
 * Sealed class representing events related to articles list in the UI.
 */
sealed class ArticleListEvent {

    /**
     * Event representing the action of fetching articles from a data source.
     *
     * @param articles The list of articles fetched from the data source. Can be null if
     *                      the fetch operation failed or returned an empty list.
     */
    data class FetchArticles(val articles: List<ArticleData?>) : ArticleListEvent()
    data class ShowMessage(val message: String?) : ArticleListEvent()


}
