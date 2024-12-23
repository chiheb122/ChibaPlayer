package ch.app.chibaplayer.metier

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import ch.app.chibaplayer.domain.Channel
import ch.app.chibaplayer.metier.interfac.Player

class ExoPlayerManager(
    context: Context,
    private val playerView: PlayerView
) : Player {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    init {
        playerView.player = exoPlayer
    }

    override fun playChannel(channel: Channel) {
        val mediaItem = MediaItem.fromUri(Uri.parse(channel.channelUrl))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun releasePlayer() {
        exoPlayer.release()
    }
}
