package com.di;

import com.di.publisher.EmaPublisher;
import com.refinitiv.ema.access.*;
import com.refinitiv.ema.rdm.EmaRdm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmaIntegrationTest {

    private static final String SERVICE_NAME = "TAQ_SERVICE";
    private static final String RIC = "WOR";
    @Autowired
    private EmaPublisher emaPublisher;

    static class TestClient implements OmmConsumerClient {
        boolean received = false;

        @Override
        public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event) {
            System.out.println("Refresh Received: " + refreshMsg.name());
            received = true;
        }

        @Override public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) {
            System.out.println("Update Received: " + updateMsg.name());
            received = true;
        }

        @Override public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) {
            System.out.println("Status: " + statusMsg);
        }

        @Override public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent event) {}
        @Override public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent event) {}

        @Override
        public void onAllMsg(Msg msg, OmmConsumerEvent ommConsumerEvent) {
            System.out.println("Msg: " + msg);
        }
    }

    @Test
    void testReceiveMarketData() throws InterruptedException {
        final String SERVICE_NAME = "DIRECT_FEED";
        final String RIC = "SPY";
        TestClient client = new TestClient();

        OmmConsumerConfig consumerConfig = getOmmConsumerConfig();
        OmmConsumer consumer = EmaFactory.createOmmConsumer(consumerConfig, client);
        try {

            long handle = consumer.registerClient(
                    EmaFactory.createReqMsg()
                            .domainType(EmaRdm.MMT_MARKET_BY_ORDER)
                            .serviceName(SERVICE_NAME)
                            .name(RIC),
                    client
            );
        }catch (Exception ex) {
            System.out.println("exception occurred");
        }

            for (int i = 0; i < 10 && !client.received; i++) {
                consumer.dispatch(1000);
                Thread.sleep(200);
            }

            assertTrue(client.received, "Should have received refresh/update from provider");
            Thread.sleep(2000);
        }

    private static OmmConsumerConfig getOmmConsumerConfig() {
        OmmConsumerConfig consumerConfig = EmaFactory.createOmmConsumerConfig()
                .host("localhost:14002")
                .username("test-user")
                .operationModel(OmmConsumerConfig.OperationModel.USER_DISPATCH);
        return consumerConfig;
    }


}


