package ir.co.ocs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChannelInformationTest {

    @Test
    void testBuilder() {
        String channelName = "testChannel";
        boolean keepAlive = true;

        ChannelInformation channelInfo = ChannelInformation.builder()
                .channelIdentificationName(channelName)
                .keepALive(keepAlive)
                .build();

        assertNotNull(channelInfo);
        assertEquals(channelName, channelInfo.getChannelIdentificationName());
        assertEquals(keepAlive, channelInfo.isKeepALive());
    }

    @Test
    void testBuilderWithDefaultValues() {
        ChannelInformation channelInfo = ChannelInformation.builder()
                .channelIdentificationName("testChannel")
                .build();

        assertNotNull(channelInfo);
        assertEquals("testChannel", channelInfo.getChannelIdentificationName());
        assertFalse(channelInfo.isKeepALive()); // Default value for boolean is false
    }

    @Test
    void testBuilderWithNullChannelName() {
        ChannelInformation channelInfo = ChannelInformation.builder()
                .channelIdentificationName(null)
                .keepALive(true)
                .build();

        assertNotNull(channelInfo);
        assertNull(channelInfo.getChannelIdentificationName());
        assertTrue(channelInfo.isKeepALive());
    }
} 