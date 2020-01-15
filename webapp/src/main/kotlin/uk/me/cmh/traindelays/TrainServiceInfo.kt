package uk.me.cmh.traindelays

data class TrainServiceInfo(val startStation: String,
                            val endStation: String,
                            val trainServices: List<TrainService>) {

    fun filterByDelayRepayEligable(): TrainServiceInfo {
        return this.copy(trainServices = this.trainServices.filter {
                trainService -> trainService.isCancelled() || trainService.delay()!! >= 15
        })
    }

}