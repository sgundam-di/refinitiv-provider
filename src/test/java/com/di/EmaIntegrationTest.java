package com.di;

import com.di.publisher.EmaPublisher;
import com.refinitiv.ema.access.*;
import com.refinitiv.ema.rdm.EmaRdm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmaIntegrationTest {

    private static final String SERVICE_NAME = "DIRECT_FEED";
    private static final String RIC = "SPY";

    static class TestClient implements OmmConsumerClient {
        final AtomicBoolean refreshReceived = new AtomicBoolean(false);
        final AtomicBoolean updateReceived = new AtomicBoolean(false);

        @Override
        public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event) {
            System.out.println("Refresh Received: " + refreshMsg.name());
            if(refreshMsg.domainType() == EmaRdm.MMT_MARKET_BY_ORDER) {
                refreshReceived.set(true);
            }
        }

        @Override public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) {
            System.out.println("Update Received: " + updateMsg.name());
            updateReceived.set(true);
        }

        @Override public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) {
            System.out.println("Status: " + statusMsg);
        }

        @Override public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent event) {}
        @Override public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent event) {}

        @Override
        public void onAllMsg(Msg msg, OmmConsumerEvent ommConsumerEvent) {
            System.out.println("All msgs received: " + msg);
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
        // Wait up to 10 seconds for both refresh and update
        for (int i = 0; i < 50 && !client.updateReceived.get(); i++) {
            consumer.dispatch(1000);
            TimeUnit.MILLISECONDS.sleep(200);
        }

        assertTrue(client.refreshReceived.get(), "Refresh was not received");
        assertTrue(client.updateReceived.get(), "Update was not received");
        }

    @Test
    void testLateSubscriptionReceivesData() throws InterruptedException {
        System.out.println("testLateSubscriptionReceivesData");

        Thread.sleep(3000); // simulate late joiner

        TestClient client = new TestClient();
        OmmConsumer consumer = EmaFactory.createOmmConsumer(getOmmConsumerConfig(), client);

        consumer.registerClient(EmaFactory.createReqMsg()
                .serviceName(SERVICE_NAME)
                .name(RIC)
                .domainType(EmaRdm.MMT_MARKET_BY_ORDER), client);

        waitFor(consumer, client);

        assertTrue(client.refreshReceived.get(), "Refresh not received (late subscriber)");
        assertTrue(client.updateReceived.get(), "Update not received (late subscriber)");
    }

    @Test
    void testInvalidServiceName() throws InterruptedException {
        System.out.println("testInvalidServiceName");

        TestClient client = new TestClient();
        OmmConsumer consumer = EmaFactory.createOmmConsumer(getOmmConsumerConfig(), client);

        consumer.registerClient(EmaFactory.createReqMsg()
                .serviceName("BAD_SERVICE")
                .name("SPY")
                .domainType(EmaRdm.MMT_MARKET_BY_ORDER), client);

        Thread.sleep(3000); // Wait a bit

        assertFalse(client.refreshReceived.get(), "Should not receive refresh for invalid service");
        assertFalse(client.updateReceived.get(), "Should not receive update for invalid service");
    }

    @Test
    void testMultipleRics() throws InterruptedException {
        System.out.println("testMultipleRics");

        List<String> rics = Arrays.asList("SPY", "WMT", "AAPL");
        Map<String, AtomicBoolean> ricStatus = new HashMap<>();

        OmmConsumerClient client = new OmmConsumerClient() {
            @Override
            public void onRefreshMsg(RefreshMsg msg, OmmConsumerEvent event) {
                if (msg.domainType() == EmaRdm.MMT_MARKET_BY_ORDER) {
                    System.out.println("Refresh for " + msg.name());
                    ricStatus.computeIfAbsent(msg.name(), r -> new AtomicBoolean()).set(true);
                }
            }

            @Override public void onUpdateMsg(UpdateMsg msg, OmmConsumerEvent event) {}
            @Override public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) {}
            @Override public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent event) {}
            @Override public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent event) {}
            @Override public void onAllMsg(Msg msg, OmmConsumerEvent event) {}
        };

        OmmConsumer consumer = EmaFactory.createOmmConsumer(getOmmConsumerConfig(), client);

        for (String ric : rics) {
            consumer.registerClient(EmaFactory.createReqMsg()
                    .serviceName(SERVICE_NAME)
                    .name(ric)
                    .domainType(EmaRdm.MMT_MARKET_BY_ORDER), client);
        }

        for (int i = 0; i < 50; i++) {
            consumer.dispatch(1000);
            Thread.sleep(100);
        }

        for (String ric : rics) {
            assertTrue(ricStatus.getOrDefault(ric, new AtomicBoolean(false)).get(), "No refresh for: " + ric);
        }
    }

    private static OmmConsumerConfig getOmmConsumerConfig() {
        OmmConsumerConfig consumerConfig = EmaFactory.createOmmConsumerConfig()
                .host("localhost:14002")
                .username("test-user")
                .operationModel(OmmConsumerConfig.OperationModel.USER_DISPATCH);
        return consumerConfig;
    }
    private void waitFor(OmmConsumer consumer, TestClient client) throws InterruptedException {
        for (int i = 0; i < 50 && !client.updateReceived.get(); i++) {
            consumer.dispatch(1000);
            Thread.sleep(200);
        }
    }

}


