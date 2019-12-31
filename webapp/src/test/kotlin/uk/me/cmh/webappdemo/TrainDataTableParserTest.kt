package uk.me.cmh.webappdemo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.jsoup.Jsoup
import org.junit.Test
import java.time.LocalTime

data class TrainService(val date: String,
                        val scheduledStart: LocalTime,
                        val scheduledEnd: LocalTime,
                        val actualEnd: LocalTime?)

data class TrainServiceInfo(val startStation: String,
                            val endStation: String,
                            val trainServices: List<TrainService>)

private fun parseTrainData(html: String): TrainServiceInfo {

    val trainDataTableBody = Jsoup.parse(html)
        .select("table")[1]
        .select("tbody")

    val startAndEndStation = trainDataTableBody
        .select("tr")[1]
        .select("th")
        .filterIndexed{ index, td -> index == 1 || index == 2 }
        .map { td -> td.text().split(" ").last() }
        .zipWithNext()
        .first()

    val serviceDates = trainDataTableBody
        .select("tr")[1]
        .select("th")
        .filterIndexed{ index, td -> index >= 6 }
        .map { td -> td.text() ?: "" }

    val serviceData = trainDataTableBody
        .select("tr")
        .filterIndexed {index, tr -> index > 1}
        .map { tr -> tr.select("td")
                .filterIndexed { index, td -> index == 1 || index ==2 || index >= 6 }
                .mapIndexed { index, td -> when (index) {
                        0 -> td.text() ?: ""
                        1 -> td.text() ?: ""
                        else -> td.select("a").text() ?: ""
                    }
                }
        }

    val trainServices = mutableListOf<TrainService>()

    serviceData.forEach {
        val scheduledStartTime = LocalTime.parse(it[0])
        val scheduledEndTime = LocalTime.parse(it[1])
        it
            .takeLast(it.size - 2)
            .forEachIndexed { index, actualEndTimeStr ->
                val actualEndTime = when(actualEndTimeStr) {
                    "CANC/NR" -> null
                    else -> LocalTime.parse(actualEndTimeStr.split(" ")[0])
                }
                val trainService = TrainService(serviceDates[index],
                    scheduledStartTime,
                    scheduledEndTime,
                    actualEndTime)
                trainServices.add(trainService)
            }
    }

    return TrainServiceInfo(startAndEndStation.first,
        startAndEndStation.second, trainServices)

}


class TrainDataTableParserTest {

    val html = javaClass.getResource("/test-html/recent-train-times.html").readText()

    @Test
    fun `complete web page should be parsed and full service info returned`() {

        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        assertThat(trainServiceInfo.startStation, equalTo("HSL"))
        assertThat(trainServiceInfo.endStation, equalTo("WAT"))
        assertThat(trainServiceInfo.trainServices, hasSize(equalTo(40)))

    }

}