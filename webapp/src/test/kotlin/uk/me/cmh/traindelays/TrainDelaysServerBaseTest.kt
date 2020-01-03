package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.After
import org.junit.Before
import org.openqa.selenium.WebDriver
import uk.me.cmh.traindelays.FakeRecentTrainTimesServer

abstract class TrainDelaysServerBaseTest {

    protected val fakeRecentTrainTimesServer = FakeRecentTrainTimesServer(8081)

    @Before
    fun startFakeRecentTrainTimesServer() {
        println("Starting fake recent train times server")
        fakeRecentTrainTimesServer.start()
    }

    @After
    fun stopFakeRecentTrainTimesServer() {
        println("Stopping fake recent train times server")
        fakeRecentTrainTimesServer.stop()
    }

    fun checkStatusResponse(response: Response) {
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

    fun checkMainContent(driver: WebDriver) {
        assertThat(driver.title, equalTo("Train Times"))
    }

}