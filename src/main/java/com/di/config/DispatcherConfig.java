package com.di.config;

import com.di.core.TaqDispatcher;
import com.di.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DispatcherConfig {

    @Bean
    public TaqDispatcher taqDispatcher(
            Handler100AddOrder addOrder,
            Handler101ModifyOrder modifyOrder,
            Handler102DeleteOrder deleteOrder,
            Handler103OrderExecution orderExecution,
            Handler104ReplaceOrder replaceOrder,
            Handler105Imbalance imbalance,
            Handler106AddOrderRefresh addOrderRefresh,
            Handler110Trade trade,
            Handler111CrossTrade crossTrade,
            Handler112TradeCancel cancel,
            Handler113CrossCorrection crossCorrection,
            Handler114RetailImprovement retailImprovement,
            Handler223Summary summary,
            HandlerType3 type3,
            HandlerType34 type34
    ) {
        TaqDispatcher dispatcher = new TaqDispatcher();

        dispatcher.register(3, type3);
        dispatcher.register(34, type34);
        dispatcher.register(100, addOrder);
        dispatcher.register(101, modifyOrder);
        dispatcher.register(102, deleteOrder);
        dispatcher.register(103, orderExecution);
        dispatcher.register(104, replaceOrder);
        dispatcher.register(105, imbalance);
        dispatcher.register(106, addOrderRefresh);
        dispatcher.register(110, trade);
        dispatcher.register(111, crossTrade);
        dispatcher.register(112, cancel);
        dispatcher.register(113, crossCorrection);
        dispatcher.register(114, retailImprovement);
        dispatcher.register(223, summary);

        return dispatcher;
    }
}
