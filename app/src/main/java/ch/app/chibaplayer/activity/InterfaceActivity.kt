package ch.app.chibaplayer.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import ch.app.chibaplayer.R

/**
 * Activité principale de l'application. Elle affiche un bouton pour lancer la playlist.
 */

class InterfaceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_interface)

        // un listener sur les boutons
        // pour lancer la playlist
        val btn_livetv : Button = findViewById(R.id.btn_livetv)
        btn_livetv.setOnClickListener {
            // Intent pour démarrer PlaylistActivity
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }
    }
}


