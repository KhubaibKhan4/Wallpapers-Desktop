package data.model.Video


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class VideoPicture(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nr")
    val nr: Int?,
    @SerializedName("picture")
    val picture: String?
)