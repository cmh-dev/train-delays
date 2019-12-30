package uk.me.cmh.webappdemo

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun FakeServiceServerApp() = routes(
   "/status" bind Method.GET to { Response(Status.OK).body("okay") }
)

fun FakeServiceServer(port :Int) = FakeServiceServerApp().asServer(Jetty(port))

fun main() {
    val port :Int = System.getenv("PORT")?.toInt() ?: 8080
    FakeServiceServer(port).start()
}