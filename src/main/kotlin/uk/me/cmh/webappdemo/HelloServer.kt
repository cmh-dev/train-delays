package uk.me.cmh.webappdemo

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun HelloServerApp() = routes(
    "/" bind Method.GET to { Response(OK).body("Hello World!") },
    "/bye" bind Method.GET to { Response(OK).body("Bye Bye!") },
    "/status" bind Method.GET to { Response(OK).body("okay") }
)

fun HelloServer(port :Int) = HelloServerApp().asServer(Jetty(port))

fun main() {
    val port :Int = System.getenv("PORT")?.toInt() ?: 8080
    HelloServer(port).start()
}


