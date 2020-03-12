package uk.me.cmh.traindelays

import com.natpryce.konfig.*
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.http4k.template.ThymeleafTemplates
import org.http4k.template.ViewModel


data class TrainServiceInfoModelView(val trainServiceInfoList: List<TrainServiceInfo>) : ViewModel {
    override fun template(): String {
        return "templates/main.html"
    }
}

val portKey = Key("port", intType)
val recentTrainTimesSiteUrlKey = Key("recent.train.times.url", stringType)
val config = EnvironmentVariables() overriding ConfigurationProperties.fromResource("defaults.properties")

fun trainTimesServerApp(): HttpHandler {

    val renderer = ThymeleafTemplates().CachingClasspath()
    return routes(
        "/static" bind static(Classpath("/static")),
        "/status" bind Method.GET to { Response(Status.OK).body("okay") },
        "/" bind Method.GET to {
            Response(Status.OK)
                .body(
                    renderer.invoke(
                        TrainServiceInfoModelView(
                            listOf(
                                requestTrainDataFromRecentTrainTimes(
                                    "Haslemere (HSL)",
                                    "London Waterloo (WAT)", "6a"
                                ),
                                requestTrainDataFromRecentTrainTimes(
                                    "London Waterloo (WAT)",
                                    "Haslemere (HSL)",
                                    "4p"
                                )
                            )
                        )
                    )
                )
                .header("Content-Type", ContentType.TEXT_HTML.toHeaderValue())
        }
    )

}

/*
https://www.recenttraintimes.co.uk/Home/Search?
Op=Srch
&Fr=Haslemere+%28HSL%29
&To=London+Waterloo+%28WAT%29
&TimTyp=D
&TimDay=6a
&Days=Wk
&TimPer=4w
&dtFr=
&dtTo=
&ShwTim=AvAr
&MxArCl=5
&TOC=All
&ArrSta=5
&MetAvg=Mea
&MetSpr=RT
&MxScDu=
&MxSvAg=10
&MnScCt=2
 */

fun requestTrainDataFromRecentTrainTimes(start: String, end: String, timeOfDay: String): TrainServiceInfo {
    val client = OkHttp()
    val request = Request(Method.GET, "${config[recentTrainTimesSiteUrlKey]}/Home/Search")
        .query("Op", "Srch")
        .query("Fr", start)
        .query("To", end)
        .query("TimTyp", "D")
        .query("TimDay", timeOfDay)
        .query("Days", "Wk")
        .query("TimPer", "4w")
        .query("dtFr", "")
        .query("dtTo", "")
        .query("ShwTim", "AvAr")
        .query("MxArCl", "5")
        .query("ArrSta", "5")
        .query("TOC", "All")
        .query("MetAvg", "Mea")
        .query("MetSpr", "RT")
        .query("MxScDu", "")
        .query("MxSvAg", "10")
        .query("MnScCt", "2")
    val response = client(request)
    return parseTrainData(response.bodyString()).filterByDelayRepayEligable()
}

fun trainTimesServer(): Http4kServer {
    val serverPort = config[portKey]
    println("Stating server using port $serverPort")
    return trainTimesServerApp().asServer(Jetty(serverPort))
}

fun main() {
    trainTimesServer().start()
}


