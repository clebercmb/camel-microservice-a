package com.example.camelmicroservicea.route.a;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
public class MyFirstTimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    private SimpleLoggingProcessingComponent loggingComponent;

    @Override
    public void configure() throws Exception {
        // queue, timer
        // transformation
        // database, log
        from("timer:first-timer")   // Timer endpoint. It could be a queue channel
            .log("${body}")
            .transform().constant("My constant message")
            .log("2) ${body}")
            //.transform().constant("My constant message 2")
            //.transform().constant("Local time is " + LocalDateTime.now())
            .bean(getCurrentTimeBean)
            .log("3) ${body}")
            .bean(loggingComponent)
            .log("4) ${body}")
            .process(new SimpleLoggingProcessor())
            .to("log:first-timer"); // It could be a database channel
    }
}

@Component
class GetCurrentTimeBean {
    public String getCurrentTime() {
        return "Local time is " + LocalDateTime.now();
    }
}

@Component
class SimpleLoggingProcessingComponent {
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

    public void process(String message) {
        logger.info("SimpleLoggingProcessingComponent {}", message);
    }
}

class SimpleLoggingProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("SimpleLoggingProcessor {}", exchange.getMessage().getBody());
    }
}