package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.Test
import java.time.LocalTime

class TrainDataTableParserTest {

    private val html = javaClass.getResource("/test-html/recent-train-times.html").readText()

    @Test
    fun `parsed data should contain correct start and end stations`() {
        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        assertThat(trainServiceInfo.startStation, equalTo("HSL"))
        assertThat(trainServiceInfo.endStation, equalTo("WAT"))
        assertThat(trainServiceInfo.trainServices, hasSize(equalTo(40)))
    }

    @Test
    fun `parsed data should contain correct number of services`() {
        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        assertThat(trainServiceInfo.trainServices, hasSize(equalTo(40)))
    }

    @Test
    fun `parsed data should contain a correct cancelled service`() {
        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        val expectedCancelledService = trainServiceInfo.trainServices
            .first { trainService ->
                trainService.date == "Fri 20"
                    && trainService.scheduledStart == LocalTime.parse("08:01")
            }
        assertThat(expectedCancelledService.isCancelled(), equalTo(true))
    }

    @Test
    fun `delay of a cancelled service should be null`() {
        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        val expectedCancelledService = trainServiceInfo.trainServices
            .first { trainService ->
                trainService.date == "Fri 20"
                        && trainService.scheduledStart == LocalTime.parse("08:01")
            }
        assertThat(expectedCancelledService.delay() == null, equalTo(true))
    }

    @Test
    fun `parsed data should contain a correct run service`() {
        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        val expectedRunService = trainServiceInfo.trainServices.first { trainService ->
            trainService.date == "Fri 20"
                    && trainService.scheduledStart == LocalTime.parse("07:13")
        }
        assertThat(expectedRunService.actualEnd == null, equalTo(false))
    }

    @Test
    fun `correct delay should be able to be calculate from parsed data`() {
        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        val expectedRunService = trainServiceInfo.trainServices.first { trainService ->
            trainService.date == "Fri 20"
                    && trainService.scheduledStart == LocalTime.parse("07:13")
        }
        assertThat(expectedRunService.delay(), equalTo(22L))
    }

    @Test
    fun `delayed train should not be shown as cancelled from parsed data`() {
        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        val expectedRunService = trainServiceInfo.trainServices.first { trainService ->
            trainService.date == "Fri 20"
                    && trainService.scheduledStart == LocalTime.parse("07:13")
        }
        assertThat(expectedRunService.isCancelled(), equalTo(false))
    }

    @Test
    fun `a response with blanks in should be correctly parsed with the blank ones not present`() {
        val htmlWithBlanks = javaClass.getResource("/test-html/recent-train-times-blanks.html").readText()
        val trainServiceInfo: TrainServiceInfo = parseTrainData(htmlWithBlanks)
        assertThat(trainServiceInfo.trainServices, hasSize(equalTo(89)))
    }

    @Test
    fun `a response with dots and blanks in should be correctly parsed with those not present`() {
        val htmlWithDotsAndBlanks = javaClass.getResource("/test-html/recent-train-times-dots-blanks.html").readText()
        val trainServiceInfo: TrainServiceInfo = parseTrainData(htmlWithDotsAndBlanks)
        assertThat(trainServiceInfo.trainServices, hasSize(equalTo(86)))
    }



}