package bg.bbanchev.challenges.tset.config;

import java.io.IOException;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * Embedded test mongodb instance.
 */
@Configuration
@Profile("test")
public class TestMongoConfig {

	@Bean(destroyMethod = "stop")
	public MongodExecutable mongodExecutable() throws UnknownHostException, IOException {
		IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net("127.0.0.1", 27018, Network.localhostIsIPv6())).build();
		MongodStarter starter = MongodStarter.getDefaultInstance();
		MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
		mongodExecutable.start();
		return mongodExecutable;
	}

	@Bean
	public MongoClient mongo(MongodExecutable server) {
		return MongoClients.create("mongodb://localhost:27018");
	}

	@Bean
	public MongoTemplate mongoTemplate(MongodExecutable server) throws Exception {
		return new MongoTemplate(mongo(server), "test_db");
	}

}