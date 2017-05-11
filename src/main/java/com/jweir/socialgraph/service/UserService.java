package com.jweir.socialgraph.service;

import com.jweir.socialgraph.domain.User;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserService {

    Stream<User> all();

    Optional<User> get(String id);

}
