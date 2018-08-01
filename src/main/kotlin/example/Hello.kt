package example

import com.beust.klaxon.JsonArray
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.Fuel

fun main(args: Array<String>) {
    val word = "小鹤双拼维护".toList()
    val encode = fetchFromXPub(word)
    word.zip(encode).forEach { (char, code) ->
        println("$char: $code")
    }
}

fun fetchFromXPub(chars: List<Char>): List<String> {
    val result = Fuel.post("http://www.xhup.club/index.php/Xhup/Search/searchCode", listOf("search_word" to chars.joinToString(""))).responseString().third.get()
    println(result)
    val json = Klaxon().parseJsonObject(result.reader())
    return json.array<JsonArray<String>>("list_dz")!!.map {
        it.first().substringAfterLast("\u3000")
                .trim()
                .split("""\s+""".toRegex())
                .last()
                .replace("*", "")
    }
}