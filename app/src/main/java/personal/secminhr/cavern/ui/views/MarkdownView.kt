package personal.secminhr.cavern.ui.views

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MarkdownView(text: String) {
    val markdownRenderHTML = "<!doctype html>\n" +
            "<html>\n" +
            " <head> \n" +
            "  <meta charset=\"UTF-8\"> \n" +
            "  <!-- Tocas UI：CSS 與元件 --> \n" +
            "  <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/tocas-ui/2.3.3/tocas.css\"> \n" +
            "  <!-- Fonts --> \n" +
            "  <link href=\"https://fonts.googleapis.com/css2?family=Open+Sans&amp;display=swap\" rel=\"stylesheet\"> \n" +
            "  <link href=\"https://fonts.googleapis.com/css2?family=Source+Code+Pro&amp;display=swap\" rel=\"stylesheet\"> \n" +
            "  <!-- jQuery --> \n" +
            "  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.slim.min.js\"></script> \n" +
            "  <link rel=\"stylesheet\" href=\"https://pandao.github.io/editor.md/css/editormd.css\"> \n" +
            "  <link rel=\"stylesheet\" href=\"https://stoneapp.tech/cavern/include/css/cavern.css\"> \n" +
            " </head> \n" +
            " <body>     \n" +
            "  <div class=\"ts flatted segment\" id=\"post\">  \n" +
            "   <div class=\"markdown\" style=\"display:None\">{{markdown_replace}}\n" +
            "   </div>\n" +
            "  </div>\n" +
            "  <script src=\"https://unpkg.com/load-js@1.2.0\"></script>\n" +
            "  <script src=\"https://stoneapp.tech/cavern/include/js/lib/editormd.js\"></script>\n" +
            "  <script src=\"https://stoneapp.tech/cavern/include/js/markdown.js\"></script>\n" +
            "  <script src=\"https://stoneapp.tech/cavern/include/js/post.js\"></script>\n" +
            " </body>\n" +
            "</html>"

    AndroidView({ WebView(it) }) {
        it.settings.javaScriptEnabled = true
        val markdown = markdownRenderHTML.replace("{{markdown_replace}}", text)
        it.loadDataWithBaseURL("https://stoneapp.tech/cavern", markdown, "text/html", "utf-8", "")
    }
}