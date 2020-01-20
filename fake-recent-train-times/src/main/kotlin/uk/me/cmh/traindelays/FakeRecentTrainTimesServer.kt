package uk.me.cmh.traindelays

import com.natpryce.konfig.*
import org.http4k.core.*
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.http4k.template.ThymeleafTemplates
import org.http4k.template.ViewModel
import org.http4k.template.viewModel

data class NoModelView(val template: String) : ViewModel {
    override fun template(): String {
        return template
    }
}

val portKey = Key("port", intType)
val config = EnvironmentVariables() overriding ConfigurationProperties.fromResource("defaults.properties")

fun fakeRecentTrainTimesServerApp(): HttpHandler {

    val renderer = ThymeleafTemplates().CachingClasspath()

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

fun fakeRecentTrainTimesServer(): Http4kServer {
    val port = config[portKey]
    return fakeRecentTrainTimesServer(port)
}

fun fakeRecentTrainTimesServer(port: Int): Http4kServer {
    return fakeRecentTrainTimesServerApp().asServer(Jetty(port))
}

fun main() {
    fakeRecentTrainTimesServer().start()
}