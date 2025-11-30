package com.dev_bayan_ibrahim.brc_shifting.domain.model.util

/**
 * Groups of Arabic characters that are considered interchangeable for text matching purposes.
 * Each group's first character is used as the standard replacement for all characters in that group.
 */
private val interchangeableCharGroups = listOf(
    "اأإآٱ",    // Alef variants
    "هة",       // Heh and Ta marbuta
    "يى",       // Yeh and Alif maqsura
    "ءؤئ",      // Hamza variants
    "کك",       // Arabic/Persian Kaf
    "۰٠0", "1۱١", // Eastern Arabic numerals
    "۲٢2", "3۳٣",
    "۴٤4", "۵٥5",
    "۶٦6", "۷٧7",
    "۸٨8", "۹9٩"
)

/**
 * Set of Arabic diacritics and marks that should be ignored during text matching
 */
private val ignorableDiacritics = setOf(
    'َ', 'ِ', 'ُ',   // Basic short vowels (Fatha, Kasra, Damma)
    'ْ', 'ّ',        // Sukun and Shadda
    'ً', 'ٍ', 'ٌ',   // Tanween
    'ٰ', 'ـ'         // Superscript Alef and Tatweel
)

/**
 * Checks if the Arabic string contains the query string after normalization,
 * considering interchangeable characters and ignoring diacritics.
 *
 * @param query The search string to look for
 * @return true if the normalized string contains the normalized query, false otherwise
 */
fun String.arContains(query: String): Boolean {
    return this.arNormalize().contains(query.arNormalize())
}

/**
 * Normalizes Arabic text by:
 * 1. Trimming whitespace
 * 2. Removing all diacritic marks
 * 3. Standardizing interchangeable characters
 *
 * @return The normalized version of the string
 */
fun String.arNormalize(): String {
    return trim().lowercase().mapNotNull { char ->
        when {
            // Remove ignorable diacritics
            char in ignorableDiacritics -> null
            // Replace with standardized character from interchangeable groups
            else -> interchangeableCharGroups
                .firstOrNull { group -> char in group }
                ?.first() ?: char
        }
    }.joinToString("")
}