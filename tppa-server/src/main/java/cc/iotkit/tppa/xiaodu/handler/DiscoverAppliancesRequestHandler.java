package cc.iotkit.tppa.xiaodu.handler;

import cc.iotkit.tppa.xiaodu.request.DiscoverAppliancesRequest;
import cc.iotkit.tppa.xiaodu.request.Header;
import cc.iotkit.tppa.xiaodu.response.DiscoverAppliancesResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class DiscoverAppliancesRequestHandler implements IRequestHandler<DiscoverAppliancesRequest, DiscoverAppliancesResponse> {

    @Override
    public String getName() {
        return "DiscoverAppliancesRequest";
    }

    @Override
    public Class getRequestType() {
        return DiscoverAppliancesRequest.class;
    }

    @Override
    public DiscoverAppliancesResponse handle(DiscoverAppliancesRequest request) {
        DiscoverAppliancesResponse response = new DiscoverAppliancesResponse();
        response.setHeader(new Header("DuerOS.ConnectedHome.Discovery",
                "DiscoverAppliancesResponse",
                request.getHeader().getMessageId(),
                "1"));
        DiscoverAppliancesResponse.Payload payload = new DiscoverAppliancesResponse.Payload();
        response.setPayload(payload);

        List<DiscoverAppliancesResponse.DiscoveredAppliance> discoveredAppliances = new ArrayList<>();
        discoveredAppliances.add(DiscoverAppliancesResponse.DiscoveredAppliance.builder()
                .applianceId("11223344")
                .applianceTypes("SWITCH")
                .friendlyName("开关")
                .isReachable(true)
                .friendlyDescription("")
                .manufacturerName("奇特")
                .modelName("M01")
                .version("1.0")
                .actions(Arrays.asList("turnOn", "turnOff"))
                .attributes(Arrays.asList(new DiscoverAppliancesResponse.Attribute(
                        "powerstate",
                        "1",
                        "",
                        System.currentTimeMillis() / 1000,
                        1000L
                )))
                .build());
        payload.setDiscoveredAppliances(discoveredAppliances);

        List<DiscoverAppliancesResponse.DiscoveredGroup> discoveredGroups = new ArrayList<>();
        payload.setDiscoveredGroups(discoveredGroups);
        discoveredGroups.add(new DiscoverAppliancesResponse.DiscoveredGroup("客厅",
                Arrays.asList("11223344"),
                "",
                new ArrayList()
        ));

        return response;
    }

}
