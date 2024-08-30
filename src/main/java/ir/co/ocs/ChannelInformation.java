package ir.co.ocs;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChannelInformation {
    private String channelIdentificationName;
    private boolean keepALive;

}
