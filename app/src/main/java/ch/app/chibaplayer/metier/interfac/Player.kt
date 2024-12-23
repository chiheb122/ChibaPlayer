package ch.app.chibaplayer.metier.interfac

import ch.app.chibaplayer.domain.Channel

interface Player {
    fun playChannel(channel: Channel)
    fun releasePlayer()
}