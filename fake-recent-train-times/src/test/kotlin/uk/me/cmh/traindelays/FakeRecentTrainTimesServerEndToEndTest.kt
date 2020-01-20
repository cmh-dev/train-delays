package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.startsWith
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.After
import org.junit.Before
import org.junit.Test

class FakeRecentTrainTimesServerEndToEndTest {

    private val server = fakeRecentTrainTimesServer()

    @Before
    fun setUp() {
        server.start()
    }


    @After
    fun tearDown() {
        server.stop()
    }

    @Test
    fun `the status of the server is okay`() {
        val client = OkHttp()
        val response = client(Request(Method.GET, "http://localhost:8081/status"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

    @Test
    fun `any request to the search page will return the fake search results`() {
        val client = OkHttp()
        val response = client(Request(Method.GET, "http://localhost:8081/Home/Search"))
        val contentTypeHeaderValue = response.headers
            .first { header -> header.first.toLowerCase() == "content-type" }
            .second ?: ""
        assertThat(contentTypeHeaderValue, startsWith("text/html"))
        assertThat(response.bodyString(), containsSubstring("<title>Search - Recent Train Times</title>"))
        assertThat(
            response.bodyString(), containsSubstring(
                "<p>The table below lists all train services departing Haslemere " +
                        "(d HSL) and arriving in London Waterloo (a WAT) for the specified time " +
                        "period.  Also shown is the percentage of the time that each service arrived " +
                        "in London Waterloo no more than 5 minutes late, as well as the average " +
                        "arrival time of each service in London Waterloo.</p>"
            )
        )
    }

}