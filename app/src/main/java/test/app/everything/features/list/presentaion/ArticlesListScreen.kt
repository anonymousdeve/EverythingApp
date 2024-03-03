package test.app.everything.features.list.presentaion

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import test.app.everything.core.base.BaseFragment
import test.app.everything.databinding.FragmentArticleListScreenBinding
import test.app.everything.features.list.domain.ArticleData
import test.app.everything.features.list.presentaion.adapter.ArticlesAdapter
import test.app.everything.features.list.presentaion.event.ArticleListEvent
import test.app.everything.features.list.presentaion.viewmodel.ArticlesListViewModel
import test.app.everything.features.search.SearchDialog

/**
 * Fragment class responsible for displaying the list of articles in the UI.
 */
@AndroidEntryPoint
class ArticlesListScreen : BaseFragment<ArticleListEvent>() {

    /**
     * Adapter for displaying articles in a RecyclerView.
     */
    private lateinit var articlesAdapter: ArticlesAdapter

    /**
     * Tag used for logging and debugging purposes.
     */
    override val mTag = "ArticlesListScreen"

    /**
     * View binding instance for accessing views in the layout.
     */
    override val mBinding by lazy {
        FragmentArticleListScreenBinding.inflate(layoutInflater)
    }

    /**
     * ViewModel instance associated with the fragment.
     */
    override val mViewModel by viewModels<ArticlesListViewModel>()

    /**
     * Method called when the fragment view is created.
     *
     * @param view The fragment's root view.
     * @param savedInstanceState The saved instance state of the fragment.
     */
    override fun onMyViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
    }

    /**
     * Method for setting up views and initializing UI components.
     */
    override fun setUpViews() {
        // Views setup can be done here if needed
        setUploadingMore()
        setUpClick()
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        articlesAdapter = ArticlesAdapter { article ->
            // Navigate to article details screen when a article item is clicked
        }
        mBinding.rvData.adapter = articlesAdapter
    }

    /**
     * Method for rendering events received from the ViewModel.
     *
     * @param event The article list event received from the ViewModel.
     */
    override fun renderEvent(event: ArticleListEvent) {
        when (event) {
            is ArticleListEvent.FetchArticles -> displayArticles(event.articles)
            is ArticleListEvent.ShowMessage -> Toast.makeText(requireActivity(), event.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Method for displaying the list of articles in the UI.
     *
     * @param articles The list of articles to be displayed.
     */
    private fun displayArticles(articles: List<ArticleData?>) {
        val listOfArticles = articlesAdapter.currentList.toMutableList()
        listOfArticles.addAll(articles)
        articlesAdapter.submitList(listOfArticles)
        setUpTitle()
    }

    private fun setUpTitle() {
        mBinding.title.text = mViewModel.queryMap.filter { value ->
            value.key in mViewModel.listGeneralKeys && value.value.isNotEmpty()
        }.values.firstOrNull()
    }

    /**
     * Displays a search dialog and updates the view model based on the user's selection.
     *
     * This function initializes and shows a search dialog using the SearchDialog class. It allows
     * the user to input search parameters, and upon selection, it triggers a data fetch operation
     * in the view model. Additionally, it updates the title text in the binding with relevant information
     * obtained from the user's selection.
     *
     * @see SearchDialog
     * @see ViewModel.fetchDataFromRemote
     *
     * @param requireActivity The reference to the hosting activity.
     * @param queryMap The map containing search parameters used to initialize the search dialog.
     */
    private fun displaySearchDialog() {
        SearchDialog.intDialog(requireActivity(), mViewModel.queryMap) {
            mViewModel.fetchDataFromRemote(it)
            mBinding.title.text = it.filter { value ->
                value.key in mViewModel.listGeneralKeys && value.value.isNotEmpty()
            }.values.firstOrNull()
        }
    }

    private fun setUpClick() {
        mBinding.search.setOnClickListener {
            displaySearchDialog()
        }
    }

    /**
     * Initiates the loading of additional data from a remote source.
     *
     * This function triggers the view model to load more data, typically from a remote source,
     * to extend the current dataset. It is often associated with a mechanism for pagination or
     * continuous loading of content to provide a seamless user experience.
     *
     * @see ViewModel.loadMore
     */
    private fun loadMoreDataFromRemote() {
        mViewModel.loadMore()
    }


    /**
     * Sets up a listener to detect scrolling events and triggers loading more data if needed.
     *
     * This function adds an `OnScrollListener` to the RecyclerView associated with the current binding.
     * It monitors scroll events and, when the end of the list is reached, checks if more data can be loaded.
     * If conditions permit (not already loading more, and more data can be loaded), it triggers the
     * `LoadMore` event in the associated view model.
     *
     * @see RecyclerView.OnScrollListener
     * @see LinearLayoutManager
     * @see ViewModel.canLoadMore
     * @see ViewModel.isLoadingMore
     * @see ViewModel.pushSingle
     */
    private fun setUploadingMore() {
        mBinding.rvData.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager
                layoutManager?.let {
                    if (layoutManager is LinearLayoutManager) {
                        val visibleItemCount: Int = layoutManager.childCount
                        val totalItemCount: Int = layoutManager.itemCount
                        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && mViewModel.canLoadMore() && !mViewModel.isLoadingMore) {
                            loadMoreDataFromRemote()
                        }
                    }
                }
            }
        })
    }


}
