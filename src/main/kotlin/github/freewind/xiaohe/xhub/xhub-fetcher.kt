package github.freewind.xiaohe.xhub

import com.beust.klaxon.JsonArray
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.Fuel
import org.apache.commons.lang3.StringEscapeUtils

private val url = "http://www.xhup.club/index.php/Xhup/Search/searchCode"

object XHubFetcher {
    fun fetch(chars: List<Char>): List<CharCodeInfo> {
        val result = Fuel.post(url, listOf("search_word" to chars.joinToString(""))).responseString().third.get()
        return parse(result)
    }
}

fun parse(jsonStr: String): List<CharCodeInfo> {
    val json = Klaxon().parseJsonObject(jsonStr.reader())
    return json.array<JsonArray<String>>("list_dz")!!.map {
        val items = it.map { StringEscapeUtils.unescapeJava(it) }
        val codes = items[0]
        val allParts = items[1]
        val firstPart = items[2].single()
        val lastPart = items[3].single()
        val firstCode = items[4].single()
        val lastCode = items[5].single()
        val char = items[6].single()
        CharCodeInfo(
                char = char,
                codes = getCodes(codes),
                parts = getParts(allParts, firstPart, firstCode, lastPart, lastCode)
        )
    }
}

private fun getParts(allParts: String, firstPart: Char, firstCode: Char, lastPart: Char, lastCode: Char) =
        listOf(Part(firstPart, firstCode)) + allParts.split("　").drop(1).dropLast(1).map { Part(it.single()) } + listOf(Part(lastPart, lastCode))

fun getCodes(codes: String): List<String> {
    return codes.substringAfterLast("　")
            .trim()
            .split("""\s+""".toRegex())
            .map { it.replace("*", "") }
}

data class CharCodeInfo(val char: Char, val codes: List<String>, val parts: List<Part>) {
    val longestCode = codes.last()
}

data class Part(val name: Char, val code: Char? = null)