package bg.bbanchev.challenges.tset.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends MongoRepository<Release, String> {
	Release findBySystemVersionNumber(Integer systemVersionNumber);
}