package ch.app.chibaplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.app.chibaplayer.R
import ch.app.chibaplayer.domain.Category
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Adaptateur pour afficher une liste de catégories dans un RecyclerView.
 *
 * @property categories Liste des catégories à afficher.
 * @property onClick Fonction lambda à appeler lorsqu'une catégorie est cliquée.
 */
class CategoryAdapter(
    private val categories: List<Category>,
    private val onClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    // Classe interne ViewHolder pour gérer les éléments individuels du RecyclerView.
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewLogo: ImageView = view.findViewById(R.id.roundedImageCategory) // Image de la catégorie
        val textViewCategory: TextView = view.findViewById(R.id.textViewCategory) // Nom de la catégorie
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infle le layout pour chaque élément de la liste
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        // Affiche le nom de la catégorie
        holder.textViewCategory.text = category.category

        // Options pour le chargement d'image avec Glide
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.default_category) // Image affichée pendant le chargement
            .error(R.drawable.error) // Image affichée en cas d'erreur

        // Charger l'image ou utiliser une valeur par défaut si l'URL est vide
        val imageUrl = if (category.countryFlagUrl.isNullOrEmpty()) null else category.countryFlagUrl
        Glide.with(holder.imageViewLogo.context)
            .setDefaultRequestOptions(requestOptions)
            .load(imageUrl)
            .into(holder.imageViewLogo)

        // Définir un listener pour réagir au clic sur l'élément
        holder.itemView.setOnClickListener {
            onClick(category)
        }

        // Met automatiquement le focus sur le premier élément de la liste
        if (position == 0) {
            holder.itemView.requestFocus()
        }

        // Gère l'état visuel de sélection lorsqu'un élément obtient ou perd le focus
        holder.itemView.setOnFocusChangeListener { _, hasFocus ->
            holder.itemView.isSelected = hasFocus
        }
    }

    // Retourne le nombre total d'éléments dans la liste
    override fun getItemCount() = categories.size
}
