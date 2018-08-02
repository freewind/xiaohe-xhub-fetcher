package github.freewind.xiaohe.xhub

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class XHubFetcherTest {

    @Test
    fun `parse the json string from server`() {
        val json = """
            {
                "list_dz": [
                    ["\u7ee5\uff1a\u3000\u3000svs svsn", "\u7e9f\u3000\u30ce\u3000\u2e8d\u3000\u5973", "\u7e9f", "\u5973", "s", "n", "\u7ee5"],
                    ["\u4e09\uff1a\u3000\u3000s sja sjae", "\u4e00\u3000\u4e8c", "\u4e00", "\u4e8c", "a", "e", "\u4e09"],
                    ["\u95ee\uff1a\u3000\u3000wf wfmk*", "\u95e8\u3000\u53e3", "\u95e8", "\u53e3", "m", "k", "\u95ee"]
                ],
                "list_ci": [],
                "notInMbList": ["\u9a48"],
                "noResultCount": 1
            }
        """.trimIndent()

        val chars = parse(json)
        assertThat(chars).isEqualTo(listOf(
                CharCodeInfo('绥', listOf("svs", "svsn"), listOf(Part('纟', 's'), Part('ノ'), Part('⺍'), Part('女', 'n'))),
                CharCodeInfo('三', listOf("s", "sja", "sjae"), listOf(Part('一', 'a'), Part('二', 'e'))),
                CharCodeInfo('问', listOf("wf", "wfmk"), listOf(Part('门', 'm'), Part('口', 'k')))
        ))
    }

    @Test
    fun `test the longestCode`() {
        val char = CharCodeInfo('绥', listOf("svs", "svsn"), listOf(Part('纟', 's'), Part('ノ'), Part('⺍'), Part('女', 'n')))
        assertThat(char.longestCode).isEqualTo("svsn")
    }

    @Test
    fun `test the real request`() {
        val result = XHubFetcher.fetch("绥驈三问".toList())
        assertThat(result).hasSize(3)
        assertThat(result.map { it.longestCode }).isEqualTo(listOf("svsn", "sjae", "wfmk"))
    }

}