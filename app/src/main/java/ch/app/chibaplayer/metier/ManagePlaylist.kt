package ch.app.chibaplayer.metier

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import ch.app.chibaplayer.domain.Category
import ch.app.chibaplayer.domain.Channel
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream

/**
 * Classe de gestion de la liste des chaînes.
 */
class ManagePlaylist private constructor() {

    private val client = OkHttpClient()
    private val categories = mutableListOf<Category>()
    private  val channels = mutableListOf<Channel>()

    companion object {
        @Volatile private var instance: ManagePlaylist? = null

        fun getInstance(): ManagePlaylist {
            return instance ?: synchronized(this) {
                instance ?: ManagePlaylist().also { instance = it }
            }
        }
    }

    fun fetchChannelList(playlistUrl: String) {
        categories.clear()
        val request = Request.Builder().url(playlistUrl).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val playlist = response.body?.string()
                val parsedChannels = parsePlaylist(playlist)
                Handler(Looper.getMainLooper()).post {
                    channels.clear()
                    channels.addAll(parsedChannels)
                }
            }
        })
    }
    fun fetchChannelListFromLocalFile1(context: Context, uri: String) {
        try {
            categories.clear()
            val inputStream = context.contentResolver.openInputStream(Uri.parse(uri))
            val playlist = inputStream?.bufferedReader()?.use(BufferedReader::readText)
            val parsedChannels = parsePlaylist(playlist)
            channels.clear()
            channels.addAll(parsedChannels)
            Handler(Looper.getMainLooper()).post {
                if (channels.isNotEmpty()) {
                    Toast.makeText(context, "Chaînes chargées depuis le fichier", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Aucune chaîne disponible", Toast.LENGTH_SHORT).show()
                    (context as Activity).finish()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Erreur lors de la lecture du fichier", Toast.LENGTH_SHORT).show()
        }
    }
    fun fetchChannelListFromLocalFile(context: Context, fileName: String) {
        categories.clear()
        val inputStream: InputStream = context.assets.open(fileName)
        val playlist = inputStream.bufferedReader().use { it.readText() }
        val parsedChannels = parsePlaylist(playlist)
        channels.clear()
        channels.addAll(parsedChannels)
        Handler(Looper.getMainLooper()).post {
            if (channels.isNotEmpty()) {
                Toast.makeText(context, "Chaînes chargées depuis le fichier", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Aucune chaîne disponible", Toast.LENGTH_SHORT).show()
                (context as Activity).finish()
            }
        }
    }

    private fun parsePlaylist(playlist: String?): List<Channel> {
        val channels = mutableListOf<Channel>()
        val category = mutableListOf<Category>()
        playlist?.let {
            val lines = it.lines()
            var channelName = ""
            var logoUrl = ""
            var streamUrl = ""
            var currentCategory = ""
            var currentCountryFlagUrl = ""

            for (line in lines) {
                if (line.startsWith("#EXTINF:")) {
                    channelName = line.substringAfter("tvg-name=\"").substringBefore("\"")
                    logoUrl = line.substringAfter("tvg-logo=\"").substringBefore("\"")
                    currentCategory = extractValue(line, "group-title")
                    currentCountryFlagUrl = extractValue(line, "tvg-country")
                    val existingCategory = category.find { it.category == currentCategory }
                    if (existingCategory == null) {
                        category.add(Category(currentCategory, currentCountryFlagUrl))
                    }
                } else if (line.startsWith("http") || line.startsWith("rtp") || line.startsWith("rtsp") || line.startsWith("udp")) {
                    streamUrl = line
                    val channel = Channel(channelName, streamUrl, logoUrl, currentCategory)
                    channels.add(channel)
                }
            }
        }
        categories.addAll(category)
        return channels
    }

    private fun extractValue(line: String, attributeName: String): String {
        val attributeStart = line.indexOf("$attributeName=\"") + attributeName.length + 2
        val attributeEnd = line.indexOf("\"", attributeStart)
        return line.substring(attributeStart, attributeEnd)
    }

    fun getCategories(): List<Category> {
        return categories
    }

    fun getChannels(): List<Channel> {
        return channels
    }
}
