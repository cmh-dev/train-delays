package uk.me.cmh.webappdemo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.Test
import java.time.LocalTime

class TrainDataTableParserTest {

    val html = javaClass.getResource("/test-html/recent-train-times.html").readText()

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
            .filter {
                    trainService ->  trainService.date.equals("Fri 20")
                    && trainService.scheduledStart.equals(LocalTime.parse("08:01"))
            }
            .first()
        assertThat(expectedCancelledService.actualEnd == null, equalTo(true))
    }

    @Test
    fun `parsed data should contain a correct run service`() {
        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        val expectedRunService = trainServiceInfo.trainServices
            .filter {
                    trainService ->  trainService.date.equals("Fri 20")
                    && trainService.scheduledStart.equals(LocalTime.parse("07:13"))
            }
            .first()
        assertThat(expectedRunService.actualEnd == null, equalTo(false))
    }

    @Test
    fun `correct delay should be able to be calculate from parsed data`() {
        val trainServiceInfo: TrainServiceInfo = parseTrainData(html)
        val expectedRunService = trainServiceInfo.trainServices
            .filter {
                    trainService ->  trainService.date.equals("Fri 20")
                    && trainService.scheduledStart.equals(LocalTime.parse("07:13"))
            }
            .first()
        assertThat(expectedRunService.delay(), equalTo(22L))
    }

}