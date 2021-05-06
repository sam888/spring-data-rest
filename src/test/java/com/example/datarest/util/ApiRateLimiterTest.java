package com.example.datarest.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiRateLimiterTest {

    private final int API_LIMIT = 20;
    private final int TIME_FRAME = 3; // 5 sec

    private String clientId = "007";


    @Test
    void test_canCallApi_returnTrue_withinTimeFrameAndApiLimit() {
        ApiRateLimiter apiRateLimiter = new ApiRateLimiter(API_LIMIT, TIME_FRAME);

        for (int i = 0; i < API_LIMIT; i++) {
            boolean canCallApi = apiRateLimiter.canCallApi( clientId );
            assertTrue( canCallApi  );
            apiRateLimiter.incrementApiCall( clientId );
        }
    }

    @Test
    void test_canCallApi_returnFalse_withinTimeFrameButExceedApiLimit() {
        ApiRateLimiter apiRateLimiter = new ApiRateLimiter(API_LIMIT, TIME_FRAME);
        for (int i = 0; i < API_LIMIT; i++) {
            apiRateLimiter.incrementApiCall( clientId );
        }
        apiRateLimiter.incrementApiCall( clientId ); // This will exceed API call limit
        boolean canCallApi = apiRateLimiter.canCallApi( clientId );
        assertFalse( canCallApi  );
    }

    @Test
    void test_isWithinTimeframe_returnTrue_as_expected() throws InterruptedException {
        ApiRateLimiter apiRateLimiter = new ApiRateLimiter(API_LIMIT, TIME_FRAME);
        LocalDateTime now = LocalDateTime.now();

        Thread.sleep( 1000 );
        assertTrue( apiRateLimiter.isWithinTimeframe( now ) );
    }

    @Test
    void test_isWithinTimeframe_returnFalseByExceedingTimeframe_as_expected() throws InterruptedException {
        ApiRateLimiter apiRateLimiter = new ApiRateLimiter(API_LIMIT, TIME_FRAME);
        LocalDateTime now = LocalDateTime.now();

        Thread.sleep(  (TIME_FRAME+1) * 1000 );
        assertFalse( apiRateLimiter.isWithinTimeframe( now ) );
    }

}
