package data.model.Photo


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Wallpapers(
    @SerializedName("next_page")
    val nextPage: String?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("per_page")
    val perPage: Int?,
    @SerializedName("photos")
    val photos: List<Photo>?,
    @SerializedName("total_results")
    val totalResults: Int?
)