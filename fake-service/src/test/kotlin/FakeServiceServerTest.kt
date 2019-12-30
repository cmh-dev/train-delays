import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.Test
import uk.me.cmh.webappdemo.FakeServiceServerApp


class FakeServiceServerTest {

    @Test
    fun `a call to status should return okay`() {
        val response: Response = FakeServiceServerApp()(Request(Method.GET, "/status"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

}
