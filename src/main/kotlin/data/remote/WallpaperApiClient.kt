package data.remote

import data.model.Photo.Wallpapers
import data.model.Video.VideoWallpaper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import utils.Constant.AUTHORIZATION
import utils.Constant.KEY

object WallpaperApiClient {

    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient(CIO) {


        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 100000
        }

        defaultRequest {
            header(
                key = AUTHORIZATION,
                value = KEY
            )
        }
    }

    suspend fun getWallpapers(page: Int, per_page: Int): Wallpapers {
        val url =
            "https://api.pexels.com/v1/curated?page=${page}&per_page=${per_page}"
        return client.get(url).body()
    }


    suspend fun getSearched(query: String, page: Int, per_page: Int): Wallpapers {
        val url = "https://api.pexels.com/v1/search?query=${query}&page=${page}&per_page=${per_page}"
        return client.get(url).body()
    }

    suspend fun getPopularVideo(page: Int, per_page: Int): VideoWallpaper {
        val url = "https://api.pexels.com/videos/popular?per_page=${per_page}&page=${page}"
        return client.get(url).body()
    }
}