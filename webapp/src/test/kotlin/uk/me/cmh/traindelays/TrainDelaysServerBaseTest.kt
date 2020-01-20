package uk.me.cmh.traindelays

import org.junit.After
import org.junit.Before

abstract class TrainDelaysServerBaseTest {

    protected val fakeRecentTrainTimesServer = fakeRecentTrainTimesServer(8081)

    @Before
    fun startFakeRecentTrainTimesServer() {
        println("Starting fake recent train times server")
        fakeRecentTrainTimesServer.start()
    }

    @After
    fun stopFakeRecentTrainTimesServer() {
        println("Stopping fake recent train times server")
        fakeRecentTrainTimesServer.stop()
    }

}