package com.oskarro.queue.model

enum class Stage(var value: String, var order: Int, var color: String = "#000000") {
    WYDANIE("SUROWCE DO WYDANIA", 0, "#E4E7FFCC"),
    NAWAZENIE("DO NAWAZENIA", 1, "#E4DBFFB2"),
    MIESZANIE("DO MIESZANIA", 2, "#E4C9FF8C"),
    MIESZANIE_ROZLEW("DO MIESZANIA I ROZLEWU", 3, "#E4C1FF7A"),
    ROZSYPYWANIE("ROZSYPYWANIE", 4, "#E4B7FF65"),
    SLEEVE("SLEEVE", 5, "#E4ACFF4D"),
    ZGRZEWANIE_DATOWANIE_ETYKIETOWANIE("ZGRZEWANIE, DATOWANIE, ETYKIETOWANIE", 6, "#E49DFF2E"),
    PLOMBOWANIE("PLOMBOWANIE", 7, "#E497FF20"),
    MAGAZYN("DO MAGAZYNU", 8, "#E488FF00"),
    KONIEC("KONIEC", 9, "#000000"),
    ERROR("ERROR", 10, "#E4FF9230");

    companion object {
        private val map = values().associateBy(Stage::value)
        fun fromString(value: String) = map[value] ?: ERROR
    }
}