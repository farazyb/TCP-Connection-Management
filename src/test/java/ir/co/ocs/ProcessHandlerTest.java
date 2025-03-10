package ir.co.ocs;

import ir.co.ocs.connections.DataInformation;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class ProcessHandlerTest {

    // Concrete implementation for testing
    private static class TestProcessHandler extends ProcessHandler {
        @Override
        public ProcessHandler getInstance() {
            return this;
        }

        @Override
        public CompletableFuture<DataInformation> doProcess(DataInformation receivedDataInformation) {
            return CompletableFuture.completedFuture(receivedDataInformation);
        }
    }

    @Test
    void testGetInstance() {
        TestProcessHandler handler = new TestProcessHandler();
        ProcessHandler instance = handler.getInstance();
        assertNotNull(instance);
        assertTrue(instance instanceof TestProcessHandler);
    }

    @Test
    void testDoProcess() {
        TestProcessHandler handler = new TestProcessHandler();
        DataInformation testData = new DataInformation(1L, "test".getBytes());
        CompletableFuture<DataInformation> future = handler.doProcess(testData);
        
        assertNotNull(future);
        assertTrue(future.isDone());
        
        DataInformation result = future.join();
        assertNotNull(result);
        assertEquals(testData.getSessionId(), result.getSessionId());
        assertArrayEquals(testData.getMessage(), result.getMessage());
    }

    @Test
    void testDoProcessAsync() {
        TestProcessHandler handler = new TestProcessHandler();
        DataInformation testData = new DataInformation(1L, "test".getBytes());
        
        CompletableFuture<DataInformation> future = handler.doProcess(testData);
        
        // Verify the future completes successfully
        assertDoesNotThrow(() -> future.get());
        
        // Verify the result matches the input
        DataInformation result = future.join();
        assertEquals(testData.getSessionId(), result.getSessionId());
        assertArrayEquals(testData.getMessage(), result.getMessage());
    }
} 