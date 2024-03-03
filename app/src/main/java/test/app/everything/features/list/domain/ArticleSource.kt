package test.app.everything.features.list.domain

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ArticleSource(
    @SerializedName("id")
    @Expose
    val id: String?, // wired
    @SerializedName("name")
    @Expose
    val name: String // Wired
): Parcelable