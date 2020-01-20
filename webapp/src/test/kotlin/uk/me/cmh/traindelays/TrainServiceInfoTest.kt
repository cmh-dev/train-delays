package uk.me.cmh.traindelays

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.Test
import java.time.LocalTime

class TrainServiceInfoTest {

    @Test
    fun `a filtered service info should only contain those services eligable for delay repay`() {

        val trainServices = listOf(
            TrainService(
                "Mon 01",
                LocalTime.of(7, 30),
                LocalTime.of(8, 30),
                LocalTime.of(8, 33)
            ),
            TrainService(
                "Tue 02",
                LocalTime.of(7, 30),
                LocalTime.of(8, 30),
                LocalTime.of(8, 45)
            ),
            TrainService(
                "Wed 03",
                LocalTime.of(7, 30),
                LocalTime.of(8, 30),
                null
            )
        )

        val trainServiceInfo = TrainServiceInfo("HSL", "WAT", trainServices)
        val trainServiceInfoFiltered = trainServiceInfo.filterByDelayRepayEligable()

        assertThat(trainServiceInfo !== trainServiceInfoFiltered, equalTo(true))
        assertThat(trainServiceInfo.trainServices !== trainServiceInfoFiltered.trainServices, equalTo(true))
        assertThat(trainServiceInfoFiltered.trainServices, hasSize(equalTo(2)))
    }

}