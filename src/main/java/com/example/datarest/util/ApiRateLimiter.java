package com.example.datarest.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Each instance of this class is intended for use in Service/Controller layer to control rate limit of a single API.
 * Service layer can use a static map of Map<{API_ENUM}, ApiRateLimiter> to access all Api Rate limiter where
 * {API_ENUM} is an Enum of different API
 *
 */
public class ApiRateLimiter {

    @Getter
    private final Map<String, ApiRateTracker> customerIdToTrackerMap = new ConcurrentHashMap<>();

    @Getter @Setter
    private int apiRateLimit; // API call limit within timeframe

    @Getter @Setter
    private int timeFrame; // Timeframe in seconds

    @Getter @Setter
    private String apiId; // Swap with Enum in Prod code

    public ApiRateLimiter(int apiRateLimit, int timeFrame) {
        this.apiRateLimit = apiRateLimit;
        this.timeFrame = timeFrame;
    }

    public boolean canCallApi(String clientId) {
        ApiRateTracker apiRateTracker = customerIdToTrackerMap.get( clientId );
        if ( apiRateTracker == null ) {
            return true;
        }
        int apiCalls = apiRateTracker.getNumberOfCalls();
        boolean isWithinTimeframe = isWithinTimeframe( apiRateTracker.getApiCallTime() );

        if ( !isWithinTimeframe ) return true;
        return  (apiCalls <= apiRateLimit);
    }

    /**
     * Call this right before calling actual API only if canCallApi(..) returns true.
     *
     * @param clientId
     */
    public void incrementApiCall(String clientId) {
        ApiRateTracker apiRateTracker = customerIdToTrackerMap.get( clientId );
        if ( apiRateTracker == null ) {
            apiRateTracker = new ApiRateTracker();
            apiRateTracker.setNumberOfCalls( 1 );
            apiRateTracker.setApiCallTime( LocalDateTime.now()  );
        } else if ( isWithinTimeframe( apiRateTracker.getApiCallTime() ) ) {
            int apiCalls = apiRateTracker.getNumberOfCalls();
            apiRateTracker.setNumberOfCalls( apiCalls + 1 );
        } else {
            // Over timeframe so reset API counter & call time
            apiRateTracker.setNumberOfCalls( 1 );
            apiRateTracker.setApiCallTime( LocalDateTime.now()  );
        }
        customerIdToTrackerMap.put( clientId, apiRateTracker);
    }

    public boolean isWithinTimeframe(LocalDateTime timeToCompare) {
        LocalDateTime now = LocalDateTime.now();
        long diff = ChronoUnit.SECONDS.between(timeToCompare, now);
        return (diff < timeFrame);
    }

    @Data
    private class ApiRateTracker {
        private LocalDateTime apiCallTime;
        private Integer numberOfCalls;
    }
}
