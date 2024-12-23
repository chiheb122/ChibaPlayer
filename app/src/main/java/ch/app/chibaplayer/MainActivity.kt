package ch.app.chibaplayer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.ComponentActivity
import ch.app.chibaplayer.activity.TypePlayerActivity
import com.bumptech.glide.Glide

/**
 * Classe principale de l'application qui agit comme un écran de chargement.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation de l'ImageView pour afficher le GIF de chargement
        val loadingGif: ImageView = findViewById(R.id.loading_gif)

        // Chargement du GIF à partir du dossier drawable en utilisant Glide
        Glide.with(this)
            .asGif() // Spécifie que Glide doit charger un GIF
            .load(R.drawable.loding) // Assurez-vous que le fichier 'loding.gif' existe dans le dossier res/drawable
            .into(loadingGif)

        // Définir un délai de 3 secondes avant de lancer la nouvelle activité
        val timeout = 3000L // Durée d'attente (en millisecondes)
        Handler(Looper.getMainLooper()).postDelayed({
            // Crée une intention pour démarrer TypePlayerActivity
            val intent = Intent(this, TypePlayerActivity::class.java)
            startActivity(intent) // Lance la nouvelle activité
            finish() // Termine l'activité actuelle pour éviter un retour involontaire
        }, timeout)
    }
}
