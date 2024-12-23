package ch.app.chibaplayer.metier

import android.content.Context
import android.view.View
import androidx.media3.ui.PlayerView
import ch.app.chibaplayer.domain.Channel
import org.videolan.libvlc.util.VLCVideoLayout

class ManagePlayer(
    context: Context,
    private val playerView: PlayerView,
    private val vlcVideoLayout: VLCVideoLayout
) {

    private val exoPlayerManager: ExoPlayerManager = ExoPlayerManager(context, playerView)
    private val vlcPlayerManager: VLCPlayerManager = VLCPlayerManager(context, vlcVideoLayout)

    fun playChannel(channel: Channel) {
        if (channel.channelUrl.startsWith("http") && !channel.channelUrl.startsWith("https")) {
//            // masquer le vlcVideoLayout
            if (vlcVideoLayout.visibility == View.VISIBLE) {
                vlcVideoLayout.visibility = View.GONE
            }
//            // afficher le playerView
//            playerView.visibility = View.VISIBLE
//            // fermer le vlcVideoLayout
//            vlcPlayerManager.releasePlayer()
            exoPlayerManager.playChannel(channel)
        } else if (channel.channelUrl.startsWith("rtp") || channel.channelUrl.startsWith("rtsp") || channel.channelUrl.startsWith("https") || channel.channelUrl.startsWith("udp") ){
//            // masquer le playerView
//            playerView.visibility = View.GONE
//            // afficher le vlcVideoLayout
            vlcVideoLayout.visibility = View.VISIBLE
//            // fermer le playerView
//            exoPlayerManager.releasePlayer()
            vlcPlayerManager.playChannel(channel)
        }
    }

    fun releasePlayer() {
        exoPlayerManager.releasePlayer()
        vlcPlayerManager.releasePlayer()
    }
}
