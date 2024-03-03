package test.app.everything.network


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import test.app.everything.features.list.data.remote.response.ArticleDataDto

@Keep
data class NewsResponse(
    @SerializedName("articles")
    @Expose
    val articles: List<ArticleDataDto>,
    @SerializedName("status")
    @Expose
    val status: String, // ok
    @SerializedName("totalResults")
    @Expose
    val totalResults: Int // 13852
) {

}