package ch.app.chibaplayer.metier

import android.content.Context
import android.net.Uri
import android.util.Log
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import ch.app.chibaplayer.domain.Channel
import ch.app.chibaplayer.metier.interfac.Player
import org.videolan.libvlc.util.VLCVideoLayout

/**
 * Classe VLCPlayerManager qui implémente l'interface Player pour gérer le joueur VLC.
 */
class VLCPlayerManager(
    context: Context,
    private val vlcVideoLayout: VLCVideoLayout
) : Player {

    private val libVLC: LibVLC = LibVLC(context)
    private val mediaPlayer: MediaPlayer = MediaPlayer(libVLC)

    init {
        mediaPlayer.attachViews(vlcVideoLayout, null, false, false)
    }

    override fun playChannel(channel: Channel) {
        try {
            val media = Media(libVLC, Uri.parse(channel.channelUrl))
            // Ajout d'options pour la stabilité des flux réseau
            media.addOption(":network-caching=1500")
            media.addOption(":clock-jitter=0")
            media.addOption(":clock-synchro=0")
            media.addOption(":rtp-timeout=60")
            media.addOption(":rtsp-tcp") // Utiliser TCP pour les flux RTP si nécessaire
            mediaPlayer.media = media
            mediaPlayer.play()
        } catch (e: Exception) {
            Log.e("VLCPlayerManager", "Error playing channel: ${e.message}")
        }
    }

    override fun releasePlayer() {
//        mediaPlayer.stop()
//        mediaPlayer.detachViews()
        mediaPlayer.release()
        libVLC.release()
    }
}
