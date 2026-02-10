package co.za.pokie.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class PokemonDetailsDto(
    @SerialName("abilities")
    val abilities: List<AbilityDto> = listOf(),
    @SerialName("base_experience")
    val baseExperience: Int = 0,
    @SerialName("cries")
    val cries: CriesDto = CriesDto(),
    @SerialName("forms")
    val forms: List<FormDto> = listOf(),
    @SerialName("game_indices")
    val gameIndices: List<GameIndiceDto> = listOf(),
    @SerialName("height")
    val height: Int = 0,
    @SerialName("held_items")
    val heldItems: List<JsonElement?> = listOf(),
    @SerialName("id")
    val id: Int = 0,
    @SerialName("is_default")
    val isDefault: Boolean = false,
    @SerialName("location_area_encounters")
    val locationAreaEncounters: String = "",
    @SerialName("moves")
    val moves: List<MoveDto> = listOf(),
    @SerialName("name")
    val name: String = "",
    @SerialName("order")
    val order: Int = 0,
    @SerialName("past_abilities")
    val pastAbilities: List<PastAbilityDto> = listOf(),
    @SerialName("past_stats")
    val pastStats: List<PastStatDto> = listOf(),
    @SerialName("past_types")
    val pastTypes: List<JsonElement?> = listOf(),
    @SerialName("species")
    val species: SpeciesDto = SpeciesDto(),
    @SerialName("sprites")
    val sprites: SpritesDto = SpritesDto(),
    @SerialName("stats")
    val stats: List<StatDtoXX> = listOf(),
    @SerialName("types")
    val types: List<TypeDto> = listOf(),
    @SerialName("weight")
    val weight: Int = 0,
)

@Serializable
data class AbilityDto(
    @SerialName("ability")
    val ability: AbilityDtoX = AbilityDtoX(),
    @SerialName("is_hidden")
    val isHidden: Boolean = false,
    @SerialName("slot")
    val slot: Int = 0,
)

@Serializable
data class CriesDto(
    @SerialName("latest")
    val latest: String = "",
    @SerialName("legacy")
    val legacy: String = "",
)

@Serializable
data class FormDto(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)

@Serializable
data class GameIndiceDto(
    @SerialName("game_index")
    val gameIndex: Int = 0,
    @SerialName("version")
    val version: VersionDto = VersionDto(),
)

@Serializable
data class MoveDto(
    @SerialName("move")
    val move: MoveDtoX = MoveDtoX(),
    @SerialName("version_group_details")
    val versionGroupDetails: List<VersionGroupDetailDto> = listOf(),
)

@Serializable
data class PastAbilityDto(
    @SerialName("abilities")
    val abilities: List<AbilityDtoXX> = listOf(),
    @SerialName("generation")
    val generation: GenerationDto = GenerationDto(),
)

@Serializable
data class PastStatDto(
    @SerialName("generation")
    val generation: GenerationDto = GenerationDto(),
    @SerialName("stats")
    val stats: List<StatDtoXX> = listOf(),
)

@Serializable
data class SpeciesDto(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)

@Serializable
data class SpritesDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_female")
    val backFemale: JsonElement? = null,
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("back_shiny_female")
    val backShinyFemale: JsonElement? = null,
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
    @SerialName("other")
    val other: OtherDto = OtherDto(),
    @SerialName("versions")
    val versions: VersionsDto = VersionsDto(),
)

@Serializable
data class StatDtoXX(
    @SerialName("base_stat")
    val baseStat: Int = 0,
    @SerialName("effort")
    val effort: Int = 0,
    @SerialName("stat")
    val stat: StatDtoX = StatDtoX(),
)

@Serializable
data class TypeDto(
    @SerialName("slot")
    val slot: Int = 0,
    @SerialName("type")
    val type: TypeDtoX = TypeDtoX(),
)

@Serializable
data class AbilityDtoX(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)

@Serializable
data class VersionDto(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)

@Serializable
data class MoveDtoX(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)

@Serializable
data class VersionGroupDetailDto(
    @SerialName("level_learned_at")
    val levelLearnedAt: Int = 0,
    @SerialName("move_learn_method")
    val moveLearnMethod: MoveLearnMethodDto = MoveLearnMethodDto(),
    @SerialName("order")
    val order: Int? = null,
    @SerialName("version_group")
    val versionGroup: VersionGroupDto = VersionGroupDto(),
)

@Serializable
data class MoveLearnMethodDto(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)

@Serializable
data class VersionGroupDto(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)

@Serializable
data class AbilityDtoXX(
    @SerialName("ability")
    val ability: JsonElement? = null,
    @SerialName("is_hidden")
    val isHidden: Boolean = false,
    @SerialName("slot")
    val slot: Int = 0,
)

@Serializable
data class GenerationDto(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)

@Serializable
data class StatDtoX(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)

@Serializable
data class OtherDto(
    @SerialName("dream_world")
    val dreamWorld: DreamWorldDto = DreamWorldDto(),
    @SerialName("home")
    val home: HomeDto = HomeDto(),
    @SerialName("official-artwork")
    val officialArtwork: OfficialArtworkDto = OfficialArtworkDto(),
    @SerialName("showdown")
    val showdown: ShowdownDto = ShowdownDto(),
)

@Serializable
data class VersionsDto(
    @SerialName("generation-i")
    val generationI: GenerationIDto = GenerationIDto(),
    @SerialName("generation-ii")
    val generationIi: GenerationIiDto = GenerationIiDto(),
    @SerialName("generation-iii")
    val generationIii: GenerationIiiDto = GenerationIiiDto(),
    @SerialName("generation-iv")
    val generationIv: GenerationIvDto = GenerationIvDto(),
    @SerialName("generation-ix")
    val generationIx: GenerationIxDto = GenerationIxDto(),
    @SerialName("generation-v")
    val generationV: GenerationVDto = GenerationVDto(),
    @SerialName("generation-vi")
    val generationVi: GenerationViDto = GenerationViDto(),
    @SerialName("generation-vii")
    val generationVii: GenerationViiDto = GenerationViiDto(),
    @SerialName("generation-viii")
    val generationViii: GenerationViiiDto = GenerationViiiDto(),
)

@Serializable
data class DreamWorldDto(
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
)

@Serializable
data class HomeDto(
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class OfficialArtworkDto(
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_shiny")
    val frontShiny: String = "",
)

@Serializable
data class ShowdownDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_female")
    val backFemale: JsonElement? = null,
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("back_shiny_female")
    val backShinyFemale: JsonElement? = null,
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class GenerationIDto(
    @SerialName("red-blue")
    val redBlue: RedBlueDto = RedBlueDto(),
    @SerialName("yellow")
    val yellow: YellowDto = YellowDto(),
)

@Serializable
data class GenerationIiDto(
    @SerialName("crystal")
    val crystal: CrystalDto = CrystalDto(),
    @SerialName("gold")
    val gold: GoldDto = GoldDto(),
    @SerialName("silver")
    val silver: SilverDto = SilverDto(),
)

@Serializable
data class GenerationIiiDto(
    @SerialName("emerald")
    val emerald: EmeraldDto = EmeraldDto(),
    @SerialName("firered-leafgreen")
    val fireredLeafgreen: FireredLeafgreenDto = FireredLeafgreenDto(),
    @SerialName("ruby-sapphire")
    val rubySapphire: RubySapphireDto = RubySapphireDto(),
)

@Serializable
data class GenerationIvDto(
    @SerialName("diamond-pearl")
    val diamondPearl: DiamondPearlDto = DiamondPearlDto(),
    @SerialName("heartgold-soulsilver")
    val heartgoldSoulsilver: HeartgoldSoulsilverDto = HeartgoldSoulsilverDto(),
    @SerialName("platinum")
    val platinum: PlatinumDto = PlatinumDto(),
)

@Serializable
data class GenerationIxDto(
    @SerialName("scarlet-violet")
    val scarletViolet: ScarletVioletDto = ScarletVioletDto(),
)

@Serializable
data class GenerationVDto(
    @SerialName("black-white")
    val blackWhite: BlackWhiteDto = BlackWhiteDto(),
)

@Serializable
data class GenerationViDto(
    @SerialName("omegaruby-alphasapphire")
    val omegarubyAlphasapphire: OmegarubyAlphasapphireDto = OmegarubyAlphasapphireDto(),
    @SerialName("x-y")
    val xY: XYDto = XYDto(),
)

@Serializable
data class GenerationViiDto(
    @SerialName("icons")
    val icons: IconsDto = IconsDto(),
    @SerialName("ultra-sun-ultra-moon")
    val ultraSunUltraMoon: UltraSunUltraMoonDto = UltraSunUltraMoonDto(),
)

@Serializable
data class GenerationViiiDto(
    @SerialName("brilliant-diamond-shining-pearl")
    val brilliantDiamondShiningPearl: BrilliantDiamondShiningPearlDto = BrilliantDiamondShiningPearlDto(),
    @SerialName("icons")
    val icons: IconsDto = IconsDto(),
)

@Serializable
data class RedBlueDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_gray")
    val backGray: String = "",
    @SerialName("back_transparent")
    val backTransparent: String = "",
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_gray")
    val frontGray: String = "",
    @SerialName("front_transparent")
    val frontTransparent: String = "",
)

@Serializable
data class YellowDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_gray")
    val backGray: String = "",
    @SerialName("back_transparent")
    val backTransparent: String = "",
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_gray")
    val frontGray: String = "",
    @SerialName("front_transparent")
    val frontTransparent: String = "",
)

@Serializable
data class CrystalDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("back_shiny_transparent")
    val backShinyTransparent: String = "",
    @SerialName("back_transparent")
    val backTransparent: String = "",
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_transparent")
    val frontShinyTransparent: String = "",
    @SerialName("front_transparent")
    val frontTransparent: String = "",
)

@Serializable
data class GoldDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_transparent")
    val frontTransparent: String = "",
)

@Serializable
data class SilverDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_transparent")
    val frontTransparent: String = "",
)

@Serializable
data class EmeraldDto(
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_shiny")
    val frontShiny: String = "",
)

@Serializable
data class FireredLeafgreenDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_shiny")
    val frontShiny: String = "",
)

@Serializable
data class RubySapphireDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_shiny")
    val frontShiny: String = "",
)

@Serializable
data class DiamondPearlDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_female")
    val backFemale: JsonElement? = null,
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("back_shiny_female")
    val backShinyFemale: JsonElement? = null,
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class HeartgoldSoulsilverDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_female")
    val backFemale: JsonElement? = null,
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("back_shiny_female")
    val backShinyFemale: JsonElement? = null,
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class PlatinumDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_female")
    val backFemale: JsonElement? = null,
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("back_shiny_female")
    val backShinyFemale: JsonElement? = null,
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class ScarletVioletDto(
    @SerialName("front_default")
    val frontDefault: JsonElement? = null,
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
)

@Serializable
data class BlackWhiteDto(
    @SerialName("animated")
    val animated: AnimatedDto = AnimatedDto(),
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_female")
    val backFemale: JsonElement? = null,
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("back_shiny_female")
    val backShinyFemale: JsonElement? = null,
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class AnimatedDto(
    @SerialName("back_default")
    val backDefault: String = "",
    @SerialName("back_female")
    val backFemale: JsonElement? = null,
    @SerialName("back_shiny")
    val backShiny: String = "",
    @SerialName("back_shiny_female")
    val backShinyFemale: JsonElement? = null,
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class OmegarubyAlphasapphireDto(
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class XYDto(
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class IconsDto(
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
)

@Serializable
data class UltraSunUltraMoonDto(
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
    @SerialName("front_shiny")
    val frontShiny: String = "",
    @SerialName("front_shiny_female")
    val frontShinyFemale: JsonElement? = null,
)

@Serializable
data class BrilliantDiamondShiningPearlDto(
    @SerialName("front_default")
    val frontDefault: String = "",
    @SerialName("front_female")
    val frontFemale: JsonElement? = null,
)

@Serializable
data class TypeDtoX(
    @SerialName("name")
    val name: String = "",
    @SerialName("url")
    val url: String = "",
)
