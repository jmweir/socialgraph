package com.jweir.socialgraph.service.impl;

import com.jweir.socialgraph.domain.Connection;
import com.jweir.socialgraph.domain.User;
import com.jweir.socialgraph.repository.ConnectionRepository;
import com.jweir.socialgraph.service.ConnectionService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Set;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;

    @Inject
    public ConnectionServiceImpl(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    @Override
    public Set<Connection> findAllForUser(User user) {
        return connectionRepository.findAllByFirstOrSecond(user, user);
    }

    @Override
    public User getNeighbor(Connection connection, User source) {
        return connection.getFirst().getId().equals(source.getId())
            ? connection.getSecond()
            : connection.getFirst();
    }
}
