package com.di.publisher;

import com.refinitiv.ema.access.*;
import com.refinitiv.ema.rdm.EmaRdm;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmaPublisher {

    private OmmProvider provider;
    private final AppClient appClient;
    private final String serviceName = "DIRECT_FEED";
    private static final Logger LOG = LoggerFactory.getLogger(EmaPublisher.class);

    public EmaPublisher(AppClient appClient) {
        this.appClient = appClient;
    }

    @PostConstruct
    public void init() {
        OmmIProviderConfig config = EmaFactory.createOmmIProviderConfig();
        provider = EmaFactory.createOmmProvider(config, appClient);
        appClient.setProvider(provider);
    }

    public void publishTrade(String ric, double price, long size) {
        Long handle = appClient.getHandle(ric);
        if (handle == null) {

            LOG.debug("🔁 Waiting for stream: " + ric);
            if (ric.matches("\\d{2}:\\d{2}:\\d{2}\\.\\d+")) {
                LOG.warn("Suspected timestamp passed as RIC: " + ric + " — check handler source");
            }
            return;
        }

        FieldList fields = EmaFactory.createFieldList();
        fields.add(EmaFactory.createFieldEntry().ascii(3422, ric));
        fields.add(EmaFactory.createFieldEntry().realFromDouble(3427, price,OmmReal.MagnitudeType.EXPONENT_NEG_2));
        fields.add(EmaFactory.createFieldEntry().realFromDouble(3429, size));
        Map map = EmaFactory.createMap();
        map.summaryData(fields);
        UpdateMsg updateMsg = EmaFactory.createUpdateMsg()
                .domainType(EmaRdm.MMT_MARKET_BY_ORDER)
                .serviceName(serviceName)
                .name(ric)
                .payload(fields);

        try {
            provider.submit(updateMsg, handle);
        }catch (Exception ex){
            System.out.println("Consumer might have closed");
            appClient.removeHandle(ric);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (provider != null) {
            provider.uninitialize();
        }
    }
}