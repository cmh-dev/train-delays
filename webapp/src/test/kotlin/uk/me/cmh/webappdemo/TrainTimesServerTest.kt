package uk.me.cmh.webappdemo

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.webdriver.Http4kWebDriver
import org.junit.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver


class TrainTimesServerTest : TrainTimesServerBaseTest() {

    private val trainTimesServerApp = TrainTimesServerApp()
    private val driver = Http4kWebDriver(trainTimesServerApp)

    @Test
    fun `the main page return the correct content`() {
        driver.navigate().to("/")
        checkMainContent(driver)
    }

    @Test
    fun `a call to status should return okay`() {
        checkStatusResponse(trainTimesServerApp(Request(Method.GET, "/status")))
    }

}