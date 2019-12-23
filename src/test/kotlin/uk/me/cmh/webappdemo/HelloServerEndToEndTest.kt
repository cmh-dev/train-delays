package uk.me.cmh.webappdemo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.After
import org.junit.Before
import org.junit.Test

class HelloServerEndToEndTest {

    private val server = HelloServer(8080)

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
        val response  = client(Request(Method.GET, "http://localhost:8080/status"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

}