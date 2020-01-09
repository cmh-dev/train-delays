package uk.me.cmh.traindelays

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.webdriver.Http4kWebDriver
import org.junit.Test


class TrainDelaysServerTest : TrainDelaysServerBaseTest() {

    private val trainTimesServerApp = TrainTimesServerApp()
    private val driver = Http4kWebDriver(trainTimesServerApp)

    @Test
    fun `the main page returns the title`() {
        driver.navigate().to("/")
        checkMainContent(driver)
    }

    /*
    @Test
    fun `the main page returns the train data results table`() {
        driver.navigate().to("/")
        checkMainContentForResultsTable(driver)
    }
     */

    @Test
    fun `a call to status should return okay`() {
        checkStatusResponse(trainTimesServerApp(Request(Method.GET, "/status")))
    }

}