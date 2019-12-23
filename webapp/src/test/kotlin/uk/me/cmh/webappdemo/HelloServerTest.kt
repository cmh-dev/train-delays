package uk.me.cmh.webappdemo

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.junit.Test

class HelloServerTest {

    @Test
    fun `a call to root should return hello world`() {
        val response: Response = HelloServerApp()(Request(Method.GET, "/"))
        assertThat(response.status, equalTo(OK))
        assertThat(response.bodyString(), equalTo("Hello World!"))
    }

    @Test
    fun `a call to bye should return bye bye`() {
        val response: Response = HelloServerApp()(Request(Method.GET, "/bye"))
        assertThat(response.status, equalTo(OK))
        assertThat(response.bodyString(), equalTo("Bye Bye!"))
    }

    @Test
    fun `a call to status should return okay`() {
        val response: Response = HelloServerApp()(Request(Method.GET, "/status"))
        assertThat(response.status, equalTo(OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

}