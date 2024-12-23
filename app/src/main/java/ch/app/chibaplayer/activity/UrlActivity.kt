package ch.app.chibaplayer.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import ch.app.chibaplayer.R
import ch.app.chibaplayer.metier.ManagePlaylist

/**
 * `UrlActivity` permet à l'utilisateur de saisir une URL pour charger une playlist.
 */
class UrlActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url)

        // Références aux vues dans le layout
        val btnSend: Button = findViewById(R.id.btn_send) // Bouton pour soumettre l'URL
        val inputText: EditText = findViewById(R.id.input_text) // Champ de saisie pour l'URL

        // Listener pour le bouton "Envoyer"
        btnSend.setOnClickListener {
            // Récupérer le texte saisi dans le champ d'entrée
            val playlistUrl = inputText.text.toString()

            // Gestionnaire de playlist (singleton)
            val managePlaylist = ManagePlaylist.getInstance()

            // Charger la liste des chaînes depuis l'URL saisie
            managePlaylist.fetchChannelList(playlistUrl)

            // Passer à l'activité `CategoryActivity`
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
    }
}
