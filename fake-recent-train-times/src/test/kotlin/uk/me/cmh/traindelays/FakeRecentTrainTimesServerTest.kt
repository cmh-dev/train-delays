package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.Test


class FakeRecentTrainTimesServerTest {

    @Test
    fun `a call to status should return okay`() {
        val response: Response = fakeRecentTrainTimesServerApp()(Request(Method.GET, "/status"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), equalTo("okay"))
    }

    @Test
    fun `the search should return the search page`() {
        val response = fakeRecentTrainTimesServerApp()(Request(Method.GET, "/Home/Search"))
        assertThat(response.status, equalTo(Status.OK))
        assertThat(response.bodyString(), containsSubstring("Search Results"))
    }

}
