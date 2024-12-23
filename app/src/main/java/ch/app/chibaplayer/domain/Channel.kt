package ch.app.chibaplayer.domain

/**
 * Représente une chaîne multimédia avec ses informations associées.
 *
 * @property name Le nom de la chaîne.
 * @property channelUrl L'URL du flux ou de la page associée à la chaîne.
 * @property logoUrl L'URL du logo de la chaîne.
 * @property category La catégorie à laquelle appartient la chaîne (ex. "Sports").
 */
data class Channel(
    val name: String,
    val channelUrl: String,
    val logoUrl: String,
    val category: String
)