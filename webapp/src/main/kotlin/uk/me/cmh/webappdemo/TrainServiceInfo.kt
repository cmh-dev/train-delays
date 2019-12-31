package uk.me.cmh.webappdemo

data class TrainServiceInfo(val startStation: String,
                            val endStation: String,
                            val trainServices: List<TrainService>)