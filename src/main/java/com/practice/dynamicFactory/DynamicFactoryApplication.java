package com.practice.dynamicFactory;

import org.slf4j.Logger;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.amqp.dsl.Amqp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Properties;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.Transformers;

@SpringBootApplication
public class DynamicFactoryApplication implements CommandLineRunner {
	@Autowired
	private ApplicationContext applicationContext;

	static Logger log = LoggerFactory.getLogger(DynamicFactoryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DynamicFactoryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String dir = "newfilelocation";
		Path path = FileSystems.getDefault().getPath(dir);
		try(WatchService watchService = FileSystems.getDefault().newWatchService()){
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
			//not a huge fan of this polling technique but after reading this article
			//https://www.baeldung.com/java-watchservice-vs-apache-commons-io-monitor-library
			//no wasteful cpu cycle mention steered my away from apache-commons
			//not a fan of this
			while(true){
				WatchKey watchKey = watchService.take();
				for (final WatchEvent<?> event : watchKey.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)){
						//this should happen only once in the run of the jvm ???
						Path filePath = ((Path) event.context());
						File file = new File(dir+ "/" +filePath.toFile().toString());
						try(InputStream stream = new FileInputStream(file)){
							Properties properties = new Properties();
							properties.load(stream);
							String host = (String) properties.get("host");
							Integer port = (Integer) properties.get("port");
//							org.springframework.amqp.rabbit.connection.ConnectionFactory factory = this.applicationContext.getBean(
//									org.springframework.amqp.rabbit.connection.ConnectionFactory.class
//							);
							org.springframework.amqp.rabbit.connection.ConnectionFactory factory = new CachingConnectionFactory(host, port);

							Queue queue = null;
							StandardIntegrationFlow flow = IntegrationFlows.from(
									Amqp.inboundAdapter(factory, queue)
							.configureContainer(c -> c.defaultRequeueRejected(false)))
									.transform(Transformers.fromJson(Pojo.class))
									.handle("handler", "handleEvent")
									.channel("data")
									.get();
						}
						catch (FileNotFoundException e){
							e.printStackTrace();
//							log.error("problem: " + e.printStackTrace());
						}
					}

				}
			}
		}

	}
}
