package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test

class TrainTimesServerSmokeTest {

    private val endPoint = when(System.getProperty("smoketest.endpoint")) {
        null -> "http://localhost:8080"
        "" -> "http://localhost:8080"
        else -> System.getProperty("smoketest.endpoint")
    }

    @Test
    fun `the status of the server is okay`() {
        val uri = "$endPoint/status"
        val client = OkHttp()
        val response = client(Request(Method.GET, uri))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

    @Test
    fun `the main page should return a 200 status code`() {
        val uri = "$endPoint"
        val client = OkHttp()
        val response = client(Request(Method.GET, uri))
        assertThat(response.status, equalTo(Status.OK))
    }

}