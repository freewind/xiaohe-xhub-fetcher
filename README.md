读取并解析Xhub返回的小鹤音形编码
==================

<http://www.xhup.club>网站上提供了对汉字的小鹤音形码的解析。通过本项目提供的函数可以对它进行读取。

注意：如果向网站提交的速度过快，有可能会被屏蔽ip。如果大量请求，请使用合理的速度（如5秒左右一次请求，一个请求中可以查询多个字）

说明
---

假如向网站上查询这几个字：`绥驈三问`，则http客户端会使用如下请求：

```
POST
http://www.xhup.club/index.php/Xhup/Search/searchCode
search_word=绥驈三问
（使用的是Form提交）
```

返回如下：

```json
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
```

其中含有很多Unicode字符，如果我们手动将其解析，则内容如下：

```json
{
    "list_dz": [
        ["绥：　　svs svsn", "纟　ノ　⺍　女", "纟", "纟", "s", "n", "绥"],
        ["三：　　s sja sjae", "一　二", "一", "二", "a", "e", "三"],
        ["问：　　wf wfmk*", "门　口", "门", "口", "m", "k", "问"]
    ],
    "list_ci": [],
    "notInMbList": ["驈"],
    "noResultCount": 1
}
```

通过本库提供的函数，可以把它解析为更合理的数据结构以便使用：

```
CharCodeInfo('绥', listOf("svs", "svsn"), listOf(Part('纟', 's'), Part('ノ'), Part('⺍'), Part('女', 'n')))
CharCodeInfo('三', listOf("s", "sja", "sjae"), listOf(Part('一', 'a'), Part('二', 'e')))
CharCodeInfo('问', listOf("wf", "wfmk"), listOf(Part('门', 'm'), Part('口', 'k')))
```

使用
---

```gradle
repositories {
    maven { url 'https://dl.bintray.com/freewind/maven/' }
}

dependencies {
    compile 'github.freewind:xiaohe-xhub-fetcher:0.2.1'
}
```

```kotlin
import github.freewind.xiaohe.xhub.XHubFetcher

XHubFetcher.fetch("绥驈三问".toList())
```