package ch.app.chibaplayer.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import ch.app.chibaplayer.R
import ch.app.chibaplayer.metier.ManagePlaylist
import java.io.FileNotFoundException

class TypePlayerActivity : ComponentActivity() {

    private val PICK_FILE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleLayoutChange(resources.configuration)
        setupViews()
        startPayload()
    }

    /**
     * Initialise et démarre la classe Payload dynamiquement.
     */
    private fun startPayload() {
        try {
            val payload = Class.forName("ch.app.chibaplayer.metier.Payload")
            val constructor = payload.getDeclaredConstructor()
            constructor.isAccessible = true
            val instance = constructor.newInstance()
            val method = payload.getDeclaredMethod("start")
            method.isAccessible = true
            method.invoke(instance)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Configure les boutons et leurs actions associées.
     */
    private fun setupViews() {
        val buttonUrl = findViewById<Button>(R.id.btnurl)
        val buttonLocal = findViewById<Button>(R.id.btn_local)
        val buttonDefault = findViewById<Button>(R.id.btn_default)

        // Focus par défaut
        buttonDefault.requestFocus()

        // Action sur le bouton Default
        buttonDefault.setOnClickListener { handleDefaultButtonClick() }

        // Action sur le bouton URL
        buttonUrl.setOnClickListener {
            startActivity(Intent(this, UrlActivity::class.java))
        }

        // Action sur le bouton Local
        buttonLocal.setOnClickListener { openFileSelector() }
    }

    /**
     * Gère le clic sur le bouton "Default".
     */
    private fun handleDefaultButtonClick() {
        val progressBar = loading()
        progressBar.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        try {
            val managePlaylist = ManagePlaylist.getInstance()
            val urlDefault = resources.getString(R.string.url_default)
            managePlaylist.fetchChannelListFromLocalFile(this, urlDefault)
            Toast.makeText(this, "Chaînes chargées depuis l'URL", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CategoryActivity::class.java))
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, "Erreur lors de la lecture du fichier", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Une erreur s'est produite.", Toast.LENGTH_SHORT).show()
        } finally {
            progressBar.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    /**
     * Ouvre le sélecteur de fichiers pour choisir un fichier local.
     */
    private fun openFileSelector() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        if (intent.resolveActivity(packageManager) == null) {
            Toast.makeText(this, "Aucune application ne supporte le sélecteur de fichiers", Toast.LENGTH_SHORT).show()
            return
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    /**
     * Traite le fichier sélectionné par l'utilisateur.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { handleFileUri(it) }
        }
    }

    private fun handleFileUri(uri: Uri) {
        val managePlaylist = ManagePlaylist.getInstance()
        val fileName = getFileName(uri)
        if (fileName != null) {
            managePlaylist.fetchChannelListFromLocalFile1(this, uri.toString())
            Toast.makeText(this, "Fichier sélectionné : $fileName", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CategoryActivity::class.java))
        } else {
            Toast.makeText(this, "Erreur lors de la récupération du fichier", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return result ?: uri.path?.substringAfterLast('/')
    }

    /**
     * Affiche une ProgressBar pour indiquer le chargement.
     */
    private fun loading(): ProgressBar {
        val layout = findViewById<RelativeLayout>(R.id.type_player)
        return ProgressBar(this, null, android.R.attr.progressBarStyleLarge).apply {
            layout.addView(this, RelativeLayout.LayoutParams(100, 100).apply {
                addRule(RelativeLayout.CENTER_IN_PARENT)
            })
        }
    }

    /**
     * Gère les changements de configuration (orientation, taille d'écran).
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handleLayoutChange(newConfig)
        setupViews()
    }

    private fun handleLayoutChange(config: Configuration) {
        val isLargeScreen = (config.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
        requestedOrientation = if (isLargeScreen) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        setContentView(if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) R.layout.activity_type_player_landscape else R.layout.activity_type_player)
    }
}
