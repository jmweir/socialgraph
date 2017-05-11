package com.jweir.socialgraph.repository;

import com.jweir.socialgraph.domain.Connection;
import com.jweir.socialgraph.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

public interface ConnectionRepository extends MongoRepository<Connection, String> {

    Set<Connection> findAllByFirstOrSecond(User first, User second);

}
