package data.model.Video


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class VideoWallpaper(
    @SerializedName("next_page")
    val nextPage: String?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("per_page")
    val perPage: Int?,
    @SerializedName("total_results")
    val totalResults: Int?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("videos")
    val videos: List<Video?>?
)