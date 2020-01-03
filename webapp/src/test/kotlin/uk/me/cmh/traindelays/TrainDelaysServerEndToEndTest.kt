package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class TrainTimesServerEndToEndTest : TrainDelaysServerBaseTest() {

    private val trainTimesServer = TrainTimesServer(8080)

    @Before
    fun startTrainTimesServer() {
        println("Starting train times server")
        trainTimesServer.start()
    }

    @After
    fun stopTrainTimesServer() {
        println("Stopping train times server")
        trainTimesServer.stop()
    }

    @Test
    fun `the status of the server is okay`() {
        val client = OkHttp()
        val response  = client(Request(Method.GET, "http://localhost:8080/status"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }


    @Test
    fun `the main page should return the correct content`() {
        val driver = HtmlUnitDriver()
        driver.navigate().to("http://localhost:8080/")
        checkMainContent(driver)
    }


}