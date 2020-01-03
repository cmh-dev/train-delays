package uk.me.cmh.webappdemo

import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
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

fun TrainTimesServerApp() :HttpHandler {

    val renderer = HandlebarsTemplates().CachingClasspath()
    return routes(
        "/status" bind Method.GET to { Response(Status.OK).body("okay") },
        "/" bind Method.GET to {
            Response(Status.OK)
                .body(renderer.invoke(NoModelView("templates/main")))
                .header("Content-Type", ContentType.TEXT_HTML.toHeaderValue())
        }
    )

}

fun TrainTimesServer(port :Int) = TrainTimesServerApp().asServer(Jetty(port))

fun main() {
    val port :Int = System.getenv("PORT")?.toInt() ?: 8080
    TrainTimesServer(port).start()
}


