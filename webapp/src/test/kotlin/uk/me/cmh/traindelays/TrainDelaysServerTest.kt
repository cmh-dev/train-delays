package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.Test

class TrainDelaysServerTest :TrainDelaysServerBaseTest() {

    @Test
    fun `a call to the recent train times site should return the correct data`() {
        val trainServiceInfo: TrainServiceInfo = requestTrainDataFromRecentTrainTimes(
            "Haslemere (HSL)",
            "London Waterloo (WAT)"
        )
        assertThat(trainServiceInfo.startStation, equalTo("HSL"))
        assertThat(trainServiceInfo.endStation, equalTo("WAT"))
        assertThat(trainServiceInfo.trainServices, hasSize(equalTo(40)))
    }

}