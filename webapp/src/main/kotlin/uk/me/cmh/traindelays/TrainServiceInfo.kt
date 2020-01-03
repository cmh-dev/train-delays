package uk.me.cmh.traindelays

import uk.me.cmh.traindelays.TrainService

data class TrainServiceInfo(val startStation: String,
                            val endStation: String,
                            val trainServices: List<TrainService>)