package uk.me.cmh.traindelays

import com.natpryce.konfig.*
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel

data class NoModelView(val template: String) : ViewModel {
    override fun template(): String {
        return template
    }
}

val port = Key("port", intType)
val recentTrainTimesSiteUrl = Key("recent.train.times.url", stringType)
val config = systemProperties() overriding ConfigurationProperties.fromResource("defaults.properties")

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

fun TrainTimesServer() = TrainTimesServerApp().asServer(Jetty(config[port]))


fun main() {
    TrainTimesServer().start()
}


