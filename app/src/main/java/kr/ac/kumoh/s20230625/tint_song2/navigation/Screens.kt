package kr.ac.kumoh.s20230625.tint_song2.navigation

object Screens {
    const val TINT = "tint"
    const val SONG = "song"

    const val SONG_DETAIL = "songDetail"
    const val TINT_DETAIL = "tintDetail"

    const val ID_ARG = "id"

    const val SONG_DETAIL_ROUTE = "$SONG_DETAIL/{$ID_ARG}"
    const val TINT_DETAIL_ROUTE = "$TINT_DETAIL/{$ID_ARG}"
}