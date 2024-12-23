package ch.app.chibaplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ch.app.chibaplayer.R
import ch.app.chibaplayer.domain.Channel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Adaptateur pour afficher une liste de chaînes dans un RecyclerView.
 *
 * @property channels Liste des chaînes à afficher.
 * @property drawerLayout Layout utilisé pour gérer les interactions avec les chaînes.
 * @property onChannelClick Callback pour gérer les clics sur une chaîne.
 */
class PlaylistAdapter(
    private val channels: List<Channel>,
    private val drawerLayout: LinearLayout,
    private val onChannelClick: (Channel) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    // Position de l'élément actuellement sélectionné
    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view, onChannelClick)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(channels[position], isSelected)

        // Mettre le premier item en focus quand on ouvre l'activité
        if (position == 0) {
            holder.itemView.requestFocus()
        }
    }

    override fun getItemCount(): Int = channels.size

    /**
     * Met à jour la position sélectionnée et notifie les changements.
     */
    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    fun getSelectedPosition(): Int {
        return selectedPosition

    }

    // ViewHolder pour chaque élément de la liste
    inner class PlaylistViewHolder(
        itemView: View,
        private val onChannelClick: (Channel) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val textViewPlaylist: TextView = itemView.findViewById(R.id.textViewPlaylist)
        private val imageViewLogo: ImageView = itemView.findViewById(R.id.imageViewLogo)
        private val playlistItemLayout: ConstraintLayout = itemView.findViewById(R.id.playlistItemLayout)

        /**
         * Lie les données de la chaîne à la vue.
         *
         * @param channel La chaîne à afficher.
         * @param isSelected Indique si cet élément est actuellement sélectionné.
         */
        fun bind(channel: Channel, isSelected: Boolean) {
            // Configure le texte et l'image de la chaîne
            textViewPlaylist.text = channel.name
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.default_category) // Image par défaut
                .error(R.drawable.error) // Image en cas d'erreur

            Glide.with(itemView.context)
                .setDefaultRequestOptions(requestOptions)
                .load(channel.logoUrl.takeIf { it.isNotEmpty() })
                .into(imageViewLogo)

            // Mettre en évidence l'élément sélectionné
            itemView.isSelected = isSelected
            playlistItemLayout.isSelected = isSelected

            // Gérer le clic sur l'élément
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition

                // Notifie les changements pour mettre à jour la vue
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                // Appelle le callback avec la chaîne sélectionnée
                onChannelClick(channel)
            }

            // Gère le focus de l'élément
            itemView.setOnFocusChangeListener { _, hasFocus ->
                playlistItemLayout.isSelected = hasFocus
            }

            // Focus automatique sur le premier élément au démarrage
            if (bindingAdapterPosition == 0) {
                itemView.requestFocus()
            }
        }
    }
}
