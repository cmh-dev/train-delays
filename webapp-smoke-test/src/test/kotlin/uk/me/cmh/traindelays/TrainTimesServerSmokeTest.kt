package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test

class TrainTimesServerSmokeTest {

    private val endPoint = System.getenv("SMOKE_TEST_ENDPOINT") ?: "http://localhost:8080"

    @Test
    fun `the status of the server is okay`() {
        val client = OkHttp()
        val response = client(Request(Method.GET, "${endPoint}/status"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

    @Test
    fun `the main page should return a 200 status code`() {
        val client = OkHttp()
        val response = client(Request(Method.GET, endPoint))
        assertThat(response.status, equalTo(Status.OK))
    }

}