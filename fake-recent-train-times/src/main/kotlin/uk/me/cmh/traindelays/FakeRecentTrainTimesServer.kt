package uk.me.cmh.traindelays

import org.http4k.core.*
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import org.http4k.template.viewModel

data class NoModelView(val template: String) : ViewModel {
    override fun template(): String {
        return template
    }
}

fun FakeRecentTrainTimesServerApp() :HttpHandler {

    val renderer = HandlebarsTemplates().CachingClasspath()
    val view = Body.viewModel(renderer, TEXT_HTML).toLens()
    return routes(
        "/status" bind Method.GET to { Response(Status.OK).body("okay") },
        "/Home/Search" bind Method.GET to {
            Response(Status.OK)
                .body(renderer.invoke(NoModelView("templates/recent-train-times")))
                .header("Content-Type", TEXT_HTML.toHeaderValue())
         }
    )

}

fun FakeRecentTrainTimesServer(port :Int) = FakeRecentTrainTimesServerApp().asServer(Jetty(port))

fun main() {
    val port :Int = System.getenv("PORT")?.toInt() ?: 8080
    FakeRecentTrainTimesServer(port).start()
}