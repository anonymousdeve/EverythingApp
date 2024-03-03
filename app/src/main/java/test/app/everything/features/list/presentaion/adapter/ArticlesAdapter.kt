package test.app.everything.features.list.presentaion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import test.app.everything.databinding.ItemSingleArticleBinding
import test.app.everything.features.list.domain.ArticleData


class ArticlesAdapter(
    private val onCompetitionClickListener: (ArticleData) -> Unit
) :
    ListAdapter<ArticleData, ArticlesAdapter.ArticlesViewHolder>(MyDiffUtil) {


    companion object MyDiffUtil : DiffUtil.ItemCallback<ArticleData>() {
        override fun areItemsTheSame(oldItem: ArticleData, newItem: ArticleData): Boolean {
            return oldItem == newItem
        }

        /**
         * Checks if the contents of two items are the same.
         *
         * @param oldItem The old article item.
         * @param newItem The new article item.
         * @return True if contents are the same, false otherwise.
         */
        override fun areContentsTheSame(oldItem: ArticleData, newItem: ArticleData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * ViewHolder class for holding the views associated with a single article item.
     *
     * @param binding The ViewBinding object for the item article layout.
     */
    inner class ArticlesViewHolder(private val binding: ItemSingleArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the views within the ViewHolder.
         *
         * @param position The position of the item within the adapter.
         * @param articleData The article object to bind data from.
         */
        fun bind(position: Int, articleData: ArticleData) = with(binding) {
            viewModel = SingleArticleViewModel(articleData, onCompetitionClickListener)
                .apply {
                    source.backgroundTintList = this@apply.loadRandomColor()
                }
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        // Inflate the item article layout
        val binding =
            ItemSingleArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticlesViewHolder(binding)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item
     *               at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        // Bind data to the ViewHolder
        getItem(position)?.let {
            holder.bind(position, it)
        }
    }
}
