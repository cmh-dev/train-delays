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
import uk.me.cmh.webappdemo.FakeServiceServer

class FakeServiceServerEndToEndTest {

    private val server = FakeServiceServer(8080)

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

    @Test
    fun `the root page should display the standard welcome page`() {
        val client = OkHttp()
        val response  = client(Request(Method.GET, "http://localhost:8080/"))
        val contentTypeHeaderValue = response.headers
            .first { header -> header.first.toLowerCase() == "content-type" }
            .second ?: ""
        assertThat(contentTypeHeaderValue, startsWith("text/html"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), containsSubstring("Hello from the fake service"))
    }

}