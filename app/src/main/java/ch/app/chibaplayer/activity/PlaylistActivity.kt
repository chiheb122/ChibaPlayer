package ch.app.chibaplayer.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import ch.app.chibaplayer.R
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.media3.ui.PlayerView
import ch.app.chibaplayer.adapter.PlaylistAdapter
import ch.app.chibaplayer.domain.Channel
import ch.app.chibaplayer.metier.ManagePlayer
import ch.app.chibaplayer.metier.ManagePlaylist
import org.videolan.libvlc.util.VLCVideoLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaylistActivity : ComponentActivity()  {

    private lateinit var recyclerView: RecyclerView
    private lateinit var playerView: PlayerView
    private lateinit var vlcVideoLayout: VLCVideoLayout
    private lateinit var drawerLayout: LinearLayout
    private lateinit var playlistAdapter: PlaylistAdapter
    // Banner channel layout
    private lateinit var channelLayoutBanner: LinearLayout
    private lateinit var channelNameTextView: TextView
    private lateinit var localTimeTextView: TextView


    // ManagePlayer instance déja initialisé dans la classe connectActivity
    private lateinit var managePlayer: ManagePlayer

    // obtenir la liste des channels depuis la classe ManagePlaylist
    private val channels = mutableListOf<Channel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        // ajouter un flag pour empecher la mise en veille
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        playerView = findViewById(R.id.player_view)
        vlcVideoLayout = findViewById(R.id.vlc_video_layout)
        recyclerView = findViewById(R.id.recyclerViewPlaylist)
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.visibility = View.VISIBLE // Initialement, le drawer est afficher
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        // Gerer le banner aprés avoir selectionné un channel en dexième plan
        channelLayoutBanner = findViewById(R.id.channel_layout_Banner)
        channelNameTextView = findViewById(R.id.textViewBanner_channelName)
        localTimeTextView = findViewById(R.id.textViewBanner_time)
        channelLayoutBanner.visibility = View.GONE // Initialement, le banner est caché

        // Ajouter un gestionnaire d'événements pour le bouton Back
        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // clear channels list
        channels.clear()
        // Charger les channels depuis la classe ManagePlaylist
        val managePlaylist = ManagePlaylist.getInstance()
        val all_channels = managePlaylist.getChannels()
        // Charger les catégories depuis la classe ManagePlaylist
        val categoryName = intent.getStringExtra("category_name")

        // changer le titre de la playlist en fonction de la catégorie sélectionnée
        val textViewPlaylist = findViewById<TextView>(R.id.textViewcat)
        textViewPlaylist.text = categoryName

        // Filtrer les channels en fonction de la catégorie sélectionnée
        val filteredChannels = all_channels.filter { it.category == categoryName }
        channels.addAll(filteredChannels)
//        managePlayer = ManagePlayer(this, playerView, channels) { channel: Channel ->
//            playChannel(channel)
//        }
        managePlayer = ManagePlayer(this, playerView, vlcVideoLayout)


        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        playlistAdapter = PlaylistAdapter(channels, drawerLayout) { channel: Channel ->
            managePlayer.playChannel(channel)
            updateBanner(channel)
            // Masquer le drawer après avoir sélectionné un channel
            drawerLayout.visibility = View.GONE
            // Afficher le banner pendant 2 secondes
            channelLayoutBanner.visibility = View.VISIBLE
            channelLayoutBanner.postDelayed({
                channelLayoutBanner.visibility = View.GONE
            }, 2000)
        }
        recyclerView.adapter = playlistAdapter

        // Set OnKeyListener for the recyclerView and playerView
//        recyclerView.setOnKeyListener(this)
//        playerView.setOnKeyListener(this)

//        // Show drawer when screen is tapped or when clicking at ok button in tv plateform
        playerView.setOnClickListener {
            toggleDrawer()
        }

        // Show drawer when screen is tapped or when clicking at ok button in tv plateform
        vlcVideoLayout.setOnClickListener {
            toggleDrawer()
        }


    }
    private fun toggleDrawer() {
        drawerLayout.visibility = if (drawerLayout.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE

        }
    }


//    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
//        event?.let {
//            if (it.action == KeyEvent.ACTION_DOWN && drawerLayout.visibility == View.GONE) {
//                when (keyCode) {
//                    KeyEvent.KEYCODE_DPAD_UP -> {
//                        moveSelection(1)
//                        return true
//                    }
//                    KeyEvent.KEYCODE_DPAD_DOWN -> {
//                        moveSelection(-1)
//                        return true
//                    }
//                    KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
//                       // afficher la liste des channels
//                        toggleDrawer()
//                        return true
//                    }
//                }
//            }
//
//        }
//        return false
//    }
    /**
     * Code qui permet de controler les gestes de la telecommande sur le drawer
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                toggleDrawer()
                true
            }
            KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN -> {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        //assurer un smooth scrolling vers le bas de l'élément sélectionné sans dépasser la limite inférieure
                        moveSelection(-1)
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        //assurer un smooth scrolling vers l'élément sélectionné
                        moveSelection(1)
                    }
                true


            }
            else -> super.onKeyDown(keyCode, event)
        }
    }


    private fun moveSelection(direction: Int) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val currentPosition = playlistAdapter.getSelectedPosition()
        val newPosition = (currentPosition + direction).coerceIn(0, channels.size - 1)
        recyclerView.smoothScrollToPosition(newPosition)
        playlistAdapter.setSelectedPosition(newPosition)

//        if (newPosition != currentPosition) {
//            // Scroll to the new position
//            recyclerView.smoothScrollToPosition(newPosition)
////            // Play the channel at the new position
////            playChannel(channels[newPosition])
////            // Update the banner with the new channel information
////            updateBanner(channels[newPosition])
//            // Update the selected position in the adapter
//            playlistAdapter.setSelectedPosition(newPosition)
//        }

//    }

//    private fun playChannel(channel: Channel) {
//        managePlayer.playChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        managePlayer.releasePlayer()
    }

    private fun updateBanner(channel: Channel) {
        channelNameTextView.text = channel.name
        val currentTime = SimpleDateFormat("EEEE 'le' d MMMM yyyy", Locale.FRENCH).format(Date())
        localTimeTextView.text = currentTime
        // afficher le banner
        channelLayoutBanner.visibility = View.VISIBLE
        channelLayoutBanner.postDelayed({
            channelLayoutBanner.visibility = View.GONE
        }, 5000)
    }


}
