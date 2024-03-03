package test.app.everything.features.list.data.remote.response


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import test.app.everything.features.list.domain.ArticleSource

@Keep
data class ArticleDataDto(
    @SerializedName("author")
    @Expose
    val author: String, // Joel Khalili
    @SerializedName("content")
    @Expose
    val content: String, // For eight years, Craig Wright has claimed to be the elusive Bitcoin creator Satoshi Nakamoto. On Monday, in the swelling heat of a UK courtroom, a trial began that will finally settle the question.… [+3163 chars]
    @SerializedName("description")
    @Expose
    val description: String, // A UK High Court will settle a long-running debate over whether Craig Wright really is Satoshi Nakamoto, inventor of Bitcoin. Monday’s opening arguments laid the groundwork for both sides.
    @SerializedName("publishedAt")
    @Expose
    val publishedAt: String, // 2024-02-05T21:07:04Z
    @SerializedName("source")
    @Expose
    val source: ArticleSource,
    @SerializedName("title")
    @Expose
    val title: String, // The Trial Over Bitcoin’s True Creator Is in Session
    @SerializedName("url")
    @Expose
    val url: String, // https://www.wired.com/story/craig-wright-bitcoin-satoshi-nakamoto-trial/
    @SerializedName("urlToImage")
    @Expose
    val urlToImage: String // https://media.wired.com/photos/65bd7e2524c06ba3ede91a33/191:100/w_1280,c_limit/Craig-Wright-Trial-Day-1-Business-Yellow-1494808061.jpg
)