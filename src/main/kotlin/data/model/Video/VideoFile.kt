package data.model.Video


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class VideoFile(
    @SerializedName("file_type")
    val fileType: String?,
    @SerializedName("fps")
    val fps: Double?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("quality")
    val quality: String?,
    @SerializedName("width")
    val width: Int?
)