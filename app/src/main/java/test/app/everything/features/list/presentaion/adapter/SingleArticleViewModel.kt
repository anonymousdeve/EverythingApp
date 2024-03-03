package test.app.everything.features.list.presentaion.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import test.app.everything.features.list.domain.ArticleData


/**
 * ViewModel class for a single article item.
 *
 * This class encapsulates the data and behavior related to a single article item displayed in the UI.
 *
 * @param item The ArticleData representing the article item.
 * @param itemClick An instance of OnItemClicks for handling item click events.
 */

class SingleArticleViewModel(private val item: ArticleData, private val itemClick: (ArticleData) -> Unit) :
    BaseObservable() {
    /**
     * Shows the details of the article item when clicked.
     */
    fun showItem() {
        itemClick(item)
    }

    val authorName = ObservableField(item.author)
    val title = ObservableField(item.title)
    val image = ObservableField(item.urlToImage)
    val sourceName = ObservableField(item.source.name.substring(0..1))
    val date = ObservableField(item.publishedAt)
    val description = ObservableField(item.description)


    /**
     * Generates and returns a ColorStateList with a randomly generated color for use as a background color.
     *
     * This function generates random values for red, green, and blue components within the range [0, 150]
     * to create a color with limited intensity. The resulting ColorStateList is suitable for setting
     * the background tint, ensuring that text or other elements on top of it remain legible.
     *
     * @return The ColorStateList with the randomly generated color in the RGB format.
     *
     * @see ColorStateList
     */
    fun loadRandomColor(): ColorStateList {
        val red = (0..150).random()
        val green = (0..150).random()
        val blue = (0..150).random()
        return ColorStateList.valueOf(
            Color.rgb(
                red,
                green,
                blue,
            )
        )
    }

}