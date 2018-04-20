package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaStoreTransactionsRequest}.
 **/
public class StoreTransactionsResponse extends AbstractResponse {

    // FIXME Jura patch
    public StoreTransactionsResponse() {
    }

    /**
     * Initializes a new instance of the StoreTransactionsResponse class.
     */
    public StoreTransactionsResponse(long duration) {
        setDuration(duration);
    }
}

