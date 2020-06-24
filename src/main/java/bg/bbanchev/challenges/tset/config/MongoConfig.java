package bg.bbanchev.challenges.tset.config;

import java.io.IOException;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@Configuration
@Profile("local")
public class MongoConfig {

	@Bean(destroyMethod = "stop")
	public MongodExecutable mongodExecutable() throws UnknownHostException, IOException {
		// for local testing create a persistence storage
		Storage storage = new Storage("./systemdb", null, 0);
		IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net("127.0.0.1", 27017, Network.localhostIsIPv6())).replication(storage).build();
		MongodStarter starter = MongodStarter.getDefaultInstance();
		MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
		mongodExecutable.start();
		return mongodExecutable;
	}
}