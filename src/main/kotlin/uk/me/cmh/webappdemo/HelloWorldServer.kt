package uk.me.cmh.webappdemo

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val port :Int = System.getenv("PORT")?.toInt() ?: 8080
    { request: Request -> Response(OK).body("Hello Big Old World!") }.asServer(Jetty(port)).start()
}