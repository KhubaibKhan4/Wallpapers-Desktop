package data.model.Photo


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Src(
    @SerializedName("landscape")
    val landscape: String?,
    @SerializedName("large")
    val large: String?,
    @SerializedName("large2x")
    val large2x: String?,
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("original")
    val original: String?,
    @SerializedName("portrait")
    val portrait: String?,
    @SerializedName("small")
    val small: String?,
    @SerializedName("tiny")
    val tiny: String?
)