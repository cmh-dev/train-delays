package uk.me.cmh.webappdemo

import java.time.Duration
import java.time.LocalTime

data class TrainService(val date: String,
                        val scheduledStart: LocalTime,
                        val scheduledEnd: LocalTime,
                        val actualEnd: LocalTime?) {

    fun delay() = Duration.between(scheduledEnd, actualEnd).toMinutes()

}
