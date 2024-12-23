package ch.app.chibaplayer.domain
/**
 * Représente une catégorie de contenu, associée à un drapeau de pays.
 *
 * @property category Le nom de la catégorie (ex. "Actualités").
 * @property countryFlagUrl L'URL de l'image du drapeau pour cette catégorie.
 */
data class Category(
    val category: String,
    val countryFlagUrl: String
)