package io.confluent.examples.kstream.structuredjson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.CountDownLatch;

@ComponentScan(basePackages={
        "io.confluent.examples.kstream.structuredjson"
})
@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private final StreamService streamService;

    public Application(@Autowired StreamService streamService) {
        this.streamService = streamService;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Starting connector.");

        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("connector-shutdown-hook") {
            @Override
            public void run() {
                streamService.stop();
                latch.countDown();
            }
        });

        try {
            LOGGER.info("Connector started successfully.");

            streamService.start();

            latch.await();
        } catch (Throwable e) {
            LOGGER.error(String.format("Connector ended with error.\n%s", e.getMessage()));
            System.exit(1);
        }

        LOGGER.info("Connector ended.");
        System.exit(0);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
