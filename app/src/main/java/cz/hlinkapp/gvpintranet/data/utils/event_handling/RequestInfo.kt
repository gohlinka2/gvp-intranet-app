package cz.hlinkapp.gvpintranet.data.utils.event_handling

/**
 * A simple class representing a status and result of a request. [requestStatus] represents an ongoing state of the request,
 * that is either [RequestStatus.PROCESSING] or [RequestStatus.IDLE]. [requestResult] represents the result of completed request,
 * that can take values of [RequestResult].
 * You can use the provided helper methods for quick initialization.
 */
class RequestInfo (val requestStatus: RequestStatus, val requestResult: OneTimeEvent<RequestResult>) {

    fun isProcessing() = requestStatus == RequestStatus.PROCESSING

    companion object {
        /**
         * Use to init representation of a download process.
         */
        fun processing() : RequestInfo = RequestInfo(RequestStatus.PROCESSING, OneTimeEvent(RequestResult.NONE))

        /**
         * Use to init a representation of an operation that hasn't started yet.
         */
        fun notStarted() : RequestInfo = RequestInfo(RequestStatus.IDLE, OneTimeEvent(RequestResult.NONE))

        /**
         * Use to init a representation of a finished operation.
         * @param result The operation result.
         */
        fun done(result: RequestResult) : RequestInfo = RequestInfo(RequestStatus.IDLE,OneTimeEvent(result))
    }

    enum class RequestStatus {
        IDLE,
        PROCESSING
    }

    enum class RequestResult {
        NO_INTERNET, //the request has been canceled because of no connection
        FAILED, //the request has failed
        OK, //the request was completed successfully
        NONE //the request hasn't started executing yet
    }
}