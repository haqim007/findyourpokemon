package dev.haqim.findyourpokemon.domain.model

data class Pokemon(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isOwned: Boolean,
    val type: List<String>,
    val move: List<String>,
    val nickname: String?
)

val dummyPokemons = listOf(
    Pokemon(
        id = "1",
        name = "bulbasaur",
        avatarUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
        isOwned = false,
        type = listOf(
            "aaaa", "bbbb", "ccc"
        ),
        move = listOf(
            "aaaa", "bbbb", "ccc","aaaa", "bbbb", "ccc","aaaa", "bbbb", "ccc"
        ),
        nickname = null
    ),
    Pokemon(
        id = "2",
        name = "ivysaur",
        avatarUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png",
        isOwned = true,
        type = listOf(
            "aaaa", "bbbb", "ccc"
        ),
        move = listOf(
            "aaaa", "bbbb", "ccc","aaaa", "bbbb", "ccc","aaaa", "bbbb", "ccc"
        ),
        nickname = null
    ),
    Pokemon(
        id = "3",
        name = "venusaur",
        avatarUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png",
        isOwned = false,
        type = listOf(
            "aaaa", "bbbb", "ccc"
        ),
        move = listOf(
            "aaaa", "bbbb", "ccc","aaaa", "bbbb", "ccc","aaaa", "bbbb", "ccc"
        ),
        nickname = "venus"
    ),
)