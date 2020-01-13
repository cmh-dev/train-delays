package uk.me.cmh.traindelays

import com.natpryce.konfig.*
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.http4k.template.ThymeleafTemplates
import org.http4k.template.ViewModel


data class NoModelView(val template: String) :ViewModel {
    override fun template(): String {
        return template
    }
}

data class TrainServiceInfoModelView(val trainServiceInfo: TrainServiceInfo) :ViewModel {
    override fun template(): String {
        return "templates/main"
    }
}

val portKey = Key("port", intType)
val recentTrainTimesSiteUrlKey = Key("recent.train.times.url", stringType)
val config = EnvironmentVariables() overriding ConfigurationProperties.fromResource("defaults.properties")

fun TrainTimesServerApp() :HttpHandler {

    val renderer = ThymeleafTemplates().CachingClasspath()
    return routes(
        "/status" bind Method.GET to { Response(Status.OK).body("okay") },
        "/" bind Method.GET to {
            Response(Status.OK)
                .body(renderer.invoke(
                    TrainServiceInfoModelView(
                        requestTrainDataFromRecentTrainTimes("Haslemere (HSL)", "London Waterloo (WAT)"))
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

fun requestTrainDataFromRecentTrainTimes(start :String, end :String) :TrainServiceInfo  {
    val client = OkHttp()
    val request = Request(Method.GET, "${config[recentTrainTimesSiteUrlKey]}/Home/Search")
        .query("Op","Srch")
        .query("Fr",start)
        .query("To",end)
        .query("TimTyp","D")
        .query("TimDay","6a")
        .query("Days","Wk")
        .query("TimPer","4w")
        .query("dtFr","")
        .query("dtTo","")
        .query("ShwTim","AvAr")
        .query("MxArCl","5")
        .query("ArrSta", "5")
        .query("TOC","All")
        .query("MetAvg","Mea")
        .query("MetSpr","RT")
        .query("MxScDu","")
        .query("MxSvAg","10")
        .query("MnScCt","2")
    val response = client(request)
    return parseTrainData(response.bodyString())
}

fun TrainTimesServer() :Http4kServer {
    val serverPort = config[portKey]
    println("Stating server using port $serverPort")
    return TrainTimesServerApp().asServer(Jetty(serverPort))
}

fun main() {
    TrainTimesServer().start()
}


