package com.jweir.socialgraph.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jweir.socialgraph.domain.Connection;
import com.jweir.socialgraph.domain.User;
import com.jweir.socialgraph.repository.UserRepository;
import com.jweir.socialgraph.service.ConnectionService;
import com.jweir.socialgraph.service.UserService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Middleware service to handle User nodes. This also populates users with additional connectivity information.
 *
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Integer CONNECTION_DEPTH = 3;

    private final UserRepository userRepository;

    private final ConnectionService connectionService;

    @Inject
    public UserServiceImpl(UserRepository userRepository, ConnectionService connectionService) {
        this.userRepository = userRepository;
        this.connectionService = connectionService;
    }

    /**
     * Get all users. Populate each with it's set of first degree connectivity.
     * @return a stream of all users
     */
    @Override
    public Stream<User> all() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .map(user -> {
                user.setFriends(findFriends(user));
                return user;
            });
    }

    /**
     * Lookup an individual user. Populate this user with first level connection set, as well as counts out to third degree.
     *
     * @param id user identifier
     * @return fully populated user document
     */
    @Override
    public Optional<User> get(String id) {
        User user = userRepository.findOne(id);
        if (user != null) {
            user.setFriends(findFriends(user));
            user.setConnections(findConnections(user));
        }
        return user == null ? Optional.empty() : Optional.of(user);
    }

    /**
     * Perform a general purpose BFS graph traversal from supplied root node. This may be limited by maxDepth.
     * The supplied onVisit callback will be called for each node.
     *
     * @param root starting point
     * @param maxDepth depth limiter
     * @param onVisit callback per node
     */
    private void breadthFirstTraverse(User root, int maxDepth, BiConsumer<Integer, User> onVisit) {
        Set<User> visited = Sets.newHashSet();
        Queue<User> toVisit = Lists.newLinkedList();

        toVisit.add(root);
        visited.add(root);

        int atLevel = 0;
        while (!toVisit.isEmpty()) {
            int nodesToConsider = toVisit.size();
            for (int i = 0; i < nodesToConsider; i++) {
                User user = toVisit.remove();
                onVisit.accept(atLevel, user);
                if (atLevel < maxDepth) {
                    Set<Connection> connections = connectionService.findAllForUser(user);
                    for (Connection connection : connections) {
                        User neighbor = connectionService.getNeighbor(connection, user);
                        if (!visited.contains(neighbor)) {
                            toVisit.add(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }
            }
            atLevel += 1;
        }
    }

    private User.Connections findConnections(User user) {
        User.Connections connections = new User.Connections();
        breadthFirstTraverse(user, CONNECTION_DEPTH, (level, connection) -> connections.set(level, connections.get(level)+1));
        return connections;
    }

    private User.Friends findFriends(User user) {
        return connectionService.findAllForUser(user).stream()
            .map(connection -> connectionService.getNeighbor(connection, user))
            .collect(Collectors.toCollection(User.Friends::new));
    }

}
