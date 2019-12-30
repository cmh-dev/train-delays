package uk.me.cmh.webappdemo

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

fun FakeServiceServerApp() :HttpHandler {

    val renderer = HandlebarsTemplates().CachingClasspath()
    val view = Body.viewModel(renderer, TEXT_HTML).toLens()
    return routes(
        "/status" bind Method.GET to { Response(Status.OK).body("okay") },
        "/" bind Method.GET to {
            Response(Status.OK)
                .body(renderer.invoke(NoModelView("templates/html")))
                .header("Content-Type", ContentType.TEXT_HTML.toHeaderValue())
         }
    )

}

fun FakeServiceServer(port :Int) = FakeServiceServerApp().asServer(Jetty(port))

fun main() {
    val port :Int = System.getenv("PORT")?.toInt() ?: 8080
    FakeServiceServer(port).start()
}