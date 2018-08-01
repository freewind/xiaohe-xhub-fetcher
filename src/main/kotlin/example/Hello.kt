package example

import com.beust.klaxon.JsonArray
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.Fuel
import org.apache.commons.lang3.StringEscapeUtils

fun main(args: Array<String>) {
    // 驈 is not supported by service
    val word = "驈三".toList()
    val charCodes = fetchFromXPub(word)
    charCodes.forEach { (char, code) ->
        println("$char: $code")
    }
}

fun fetchFromXPub(chars: List<Char>): List<CharCode> {
    val result = Fuel.post("http://www.xhup.club/index.php/Xhup/Search/searchCode", listOf("search_word" to chars.joinToString(""))).responseString().third.get()
    println(result)
    val json = Klaxon().parseJsonObject(result.reader())
    return json.array<JsonArray<String>>("list_dz")!!.map {
        it.first().let { unicodeStr ->
            val str = StringEscapeUtils.unescapeJava(unicodeStr)
            val char = str.substringBefore("：").single()
            val code = str
                    .substringAfterLast("　")
                    .trim()
                    .split("""\s+""".toRegex())
                    .last()
                    .replace("*", "")
            CharCode(char, code)
        }

    }
}

data class CharCode(val name: Char, val code: String)