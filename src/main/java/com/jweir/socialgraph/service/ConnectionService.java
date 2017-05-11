package com.jweir.socialgraph.service;

import com.jweir.socialgraph.domain.Connection;
import com.jweir.socialgraph.domain.User;

import java.util.Set;

public interface ConnectionService {

    Set<Connection> findAllForUser(User user);

    User getNeighbor(Connection connection, User source);

}
