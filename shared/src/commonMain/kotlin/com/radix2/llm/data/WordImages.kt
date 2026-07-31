package com.radix2.llm.data

import com.radix2.llm.domain.Word
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Resolves a runtime photograph URL for a word.
 * Prefers [Word.imageUrl]; otherwise looks up a Wikipedia page thumbnail
 * (cached in memory for the session). Nothing is bundled in the APK.
 */
object WordImages {
    private val cache = mutableMapOf<String, String?>()
    private val mutex = Mutex()

    suspend fun urlFor(word: Word): String? {
        word.imageUrl?.let { return it }
        mutex.withLock {
            if (cache.containsKey(word.id)) return cache[word.id]
        }
        val title = word.wikiTitle ?: titleOverrides[word.id] ?: word.name.replace(' ', '_')
        val resolved = fetchWikipediaThumbnailUrl(title)
        mutex.withLock { cache[word.id] = resolved }
        return resolved
    }

    /** Disambiguation titles for pages where the bare name is ambiguous on Wikipedia. */
    private val titleOverrides: Map<String, String> = mapOf(
        "fruit_orange" to "Orange_(fruit)",
        "fruit_date" to "Date_palm",
        "fruit_fig" to "Common_fig",
        "fruit_kiwi" to "Kiwifruit",
        "fruit_dragonfruit" to "Pitaya",
        "fruit_mulberry" to "Morus_(plant)",
        "fruit_chickoo" to "Manilkara_zapota",
        "fruit_amla" to "Phyllanthus_emblica",
        "fruit_jamun" to "Syzygium_cumini",
        "fruit_ber" to "Jujube",
        "fruit_mosambi" to "Sweet_lemon",
        "fruit_custardapple" to "Custard_apple",
        "vegetable_corn" to "Maize",
        "vegetable_beans" to "Bean",
        "vegetable_peas" to "Pea",
        "vegetable_chili" to "Chili_pepper",
        "vegetable_yam" to "Yam_(vegetable)",
        "vegetable_capsicum" to "Bell_pepper",
        "vegetable_drumstick" to "Moringa_oleifera",
        "vegetable_brinjal" to "Eggplant",
        "vegetable_ashgourd" to "Winter_melon",
        "vegetable_elephantyam" to "Amorphophallus_paeoniifolius",
        "vegetable_yuca" to "Cassava",
        "vegetable_fenugreek" to "Fenugreek",
        "animal_cow" to "Cattle",
        "animal_lamb" to "Sheep",
        "animal_panda" to "Giant_panda",
        "animal_hippo" to "Hippopotamus",
        "animal_rhino" to "Rhinoceros",
        "animal_seal" to "Earless_seal",
        "animal_buffalo" to "Water_buffalo",
        "animal_nilgai" to "Nilgai",
        "bird_peacock" to "Peafowl",
        "bird_pigeon" to "Rock_dove",
        "bird_seagull" to "Gull",
        "bird_canary" to "Domestic_canary",
        "bird_nightingale" to "Common_nightingale",
        "bird_raven" to "Common_raven",
        "bird_saruscrane" to "Sarus_crane",
        "bird_roller" to "Indian_roller",
        "bird_koel" to "Asian_koel",
        "bird_myna" to "Common_myna",
        "flower_lotus" to "Nelumbo_nucifera",
        "flower_marigold" to "Tagetes",
        "flower_sunflower" to "Common_sunflower",
        "flower_daisy" to "Bellis_perennis",
        "flower_lavender" to "Lavandula",
        "flower_lily" to "Lilium",
        "flower_violet" to "Viola_(plant)",
        "flower_aster" to "Aster_(genus)",
        "flower_bluebell" to "Hyacinthoides_non-scripta",
        "flower_carnation" to "Dianthus_caryophyllus",
        "flower_iris" to "Iris_(plant)",
        "flower_narcissus" to "Narcissus_(plant)",
        "flower_daffodil" to "Narcissus_(plant)",
        "flower_cosmos" to "Cosmos_(plant)",
        "flower_morningglory" to "Ipomoea",
        "flower_oleander" to "Nerium",
        "flower_pansy" to "Pansy",
        "flower_periwinkle" to "Catharanthus_roseus",
        "flower_gulmohar" to "Delonix_regia",
        "flower_champa" to "Plumeria",
        "flower_mogra" to "Jasminum_sambac",
        "flower_parijat" to "Nyctanthes_arbor-tristis",
        "flower_sampige" to "Magnolia_champaca",
        "city_newyork" to "New_York_City",
        "city_bengaluru" to "Bangalore",
        "city_mysuru" to "Mysore",
    )
}

/** Platform HTTP call to Wikipedia's page summary API → thumbnail source URL. */
expect suspend fun fetchWikipediaThumbnailUrl(title: String): String?
