package uk.me.cmh.traindelays

import java.time.Duration
import java.time.LocalTime

data class TrainService(
    val date: String,
    val scheduledStart: LocalTime,
    val scheduledEnd: LocalTime,
    val actualEnd: LocalTime?
) {

    fun delay() = actualEnd?.let { Duration.between(scheduledEnd, actualEnd).toMinutes() }

    fun isCancelled() = actualEnd == null

}
