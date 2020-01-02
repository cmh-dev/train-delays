import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.Test
import uk.me.cmh.webappdemo.FakeRecentTrainTimesServerApp


class FakeRecentTrainTimesServerTest {

    @Test
    fun `a call to status should return okay`() {
        val response: Response = FakeRecentTrainTimesServerApp()(Request(Method.GET, "/status"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

    @Test
    fun `the search should return the search page`() {
        val response  = FakeRecentTrainTimesServerApp()(Request(Method.GET, "/Home/Search"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), containsSubstring("Search Results"))
    }

}
