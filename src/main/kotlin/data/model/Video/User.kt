package data.model.Video


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)