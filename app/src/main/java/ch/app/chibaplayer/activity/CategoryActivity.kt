/**
 * This is the CategoryActivity class, which displays a list of categories
 * and allows the user to select a category.
 */
package ch.app.chibaplayer.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.app.chibaplayer.R
import ch.app.chibaplayer.adapter.CategoryAdapter
import ch.app.chibaplayer.domain.Category
import ch.app.chibaplayer.metier.ManagePlaylist

class CategoryActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categories: List<Category>
    val managePlaylist = ManagePlaylist.getInstance()

    // manageplaylist instance déja initialisé dans la classe connectActivity

    /**
     * Initializes the activity and sets up the RecyclerView for displaying categories.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Observer les changements de données une fois qu'elles sont disponibles
        observePlaylistChanges()

//        // Setup RecyclerView for categories
        recyclerView = findViewById(R.id.recyclerViewCategory)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



    }

    /**
     * Observe les changements de données dans ManagePlaylist et met à jour la liste des catégories.
     */
    private fun observePlaylistChanges() {
        // Observer les changements de données dans ManagePlaylist
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {

            categories = managePlaylist.getCategories()
            categoryAdapter = CategoryAdapter(categories) { category ->
                val intent = Intent(this, PlaylistActivity::class.java)
                intent.putExtra("category_name", category.category)

                startActivity(intent)
            }
            recyclerView.adapter = categoryAdapter
        }
        handler.postDelayed(runnable, 500) // Attente de 500 ms pour la mise à jour (peut être ajusté selon les besoins)
    }

}