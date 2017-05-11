package com.jweir.socialgraph.repository;

import com.jweir.socialgraph.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByName(String name);

}
