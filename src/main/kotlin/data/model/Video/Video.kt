package data.model.Video


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    @SerializedName("avg_color")
    val avgColor: String?,
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("full_res")
    val fullRes: String?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("tags")
    val tags: List<String?>?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("video_files")
    val videoFiles: List<VideoFile?>?,
    @SerializedName("video_pictures")
    val videoPictures: List<VideoPicture?>?,
    @SerializedName("width")
    val width: Int?
)