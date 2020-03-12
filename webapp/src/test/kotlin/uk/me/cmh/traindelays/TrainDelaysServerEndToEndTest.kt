package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.contains
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class TrainTimesServerEndToEndTest : TrainDelaysServerBaseTest() {

    private val trainTimesServer = trainTimesServer()

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
        val response = client(Request(Method.GET, "http://localhost:8080/status"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

    @Test
    fun `the main page should return the correct content`() {
        val driver = HtmlUnitDriver()
        driver.navigate().to("http://localhost:8080/")
        assertThat(driver.title, equalTo("Train Delays"))
    }

    @Test
    fun `the main page returns the train data results table`() {
        val driver = HtmlUnitDriver()
        driver.navigate().to("http://localhost:8080/")
        checkMainContentForResultsTable(driver)
    }

    @Test
    fun `the main page has journey details for a results table`() {
        val driver = HtmlUnitDriver()
        driver.navigate().to("http://localhost:8080/")
        val heading = driver.findElement(By.tagName("h4"))
        assertThat(heading.text, equalTo("HSL to WAT"))
    }

    @Test
    fun `the main page should return two results tables`() {
        val driver = HtmlUnitDriver()
        driver.navigate().to("http://localhost:8080")
        val tables = driver.findElementsByTagName("table")
        assertThat(tables, hasSize(equalTo(2)))
    }

    @Test
    fun `css should be able to be loaded from the static path`() {
    val client = OkHttp()
        val response = client(Request(Method.GET, "http://localhost:8080/static/css/materialize.min.css"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), containsSubstring(".materialize-red{"))
    }

    @Test
    fun `js should be able to be loaded from the static path`() {
    val client = OkHttp()
        val response = client(Request(Method.GET, "http://localhost:8080/static/js/materialize.min.js"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), containsSubstring("var _get=function"))
    }

    private fun checkMainContentForResultsTable(driver: WebDriver) {

        val resultsTable = driver.findElement(By.tagName("table"))
        val rows = resultsTable.findElements(By.tagName("tr"))

        assertThat(rows, hasSize(equalTo(7)))

        val dataColumns = rows.first().findElements(By.tagName("td"))
        assertThat(dataColumns, hasSize(equalTo(5)))
        assertThat(dataColumns[4].text, equalTo("16"))

    }

}