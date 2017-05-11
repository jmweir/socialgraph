package com.jweir.socialgraph.service;

import com.google.common.collect.Sets;
import com.jweir.socialgraph.domain.Connection;
import com.jweir.socialgraph.domain.User;
import com.jweir.socialgraph.repository.ConnectionRepository;
import com.jweir.socialgraph.repository.UserRepository;
import com.jweir.socialgraph.service.impl.ConnectionServiceImpl;
import com.jweir.socialgraph.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Isolate the UserService. Build a graph of users and assert some traversal functionality.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    private ConnectionService connectionService;

    private UserService userService;

    // A set of test nodes.
    private User a;
    private User b;
    private User c;
    private User d;
    private User e;
    
    @Before
    public void setup() {
        connectionService = new ConnectionServiceImpl(connectionRepository);
        userService = new UserServiceImpl(userRepository, connectionService);

        // Create nodes
        a = createTestUser("a");
        b = createTestUser("b");
        c = createTestUser("c");
        d = createTestUser("d");
        e = createTestUser("e");

        addUserMocks(
            userRepository,
            a, b, c, d, e
        );

        // Connect the nodes
        addConnectionMocks(
            connectionRepository,

            createTestConnection(a, b),
            createTestConnection(a, c),
            createTestConnection(b, c),
            createTestConnection(a, d),
            createTestConnection(c, e)
        );
    }

    /**
     * Assert that the test graph reveals the correct connectivity numbers and set of first degree connections.
     */
    @Test
    public void shouldDiscoverConnections() {
        User user = userService.get(a.getId()).get();

        assertThat(user.getFriends()).hasSize(3).contains(b, c, d);
        assertThat(user.getConnections().get(1)).isEqualTo(3);
        assertThat(user.getConnections().get(2)).isEqualTo(1);
        assertThat(user.getConnections().get(3)).isEqualTo(0);

        user = userService.get(d.getId()).get();

        assertThat(user.getFriends()).hasSize(1).contains(a);
        assertThat(user.getConnections().get(1)).isEqualTo(1);
        assertThat(user.getConnections().get(2)).isEqualTo(2);
        assertThat(user.getConnections().get(3)).isEqualTo(1);
    }

    private void addUserMocks(UserRepository userRepository, User... users) {
        Map<String, User> userLookup = Arrays.stream(users).collect(Collectors.toMap(user -> user.getId(), Function.identity()));
        when(userRepository.findOne(any(String.class)))
            .then(invocation -> userLookup.get(invocation.getArgumentAt(0, String.class)));
    }

    private void addConnectionMocks(ConnectionRepository connectionRepository, Connection... connections) {
        Map<String, Connection> byIdLookup = Arrays.stream(connections).collect(
            Collectors.toMap(connection -> connection.getId(), Function.identity()));
        Map<String, Set<Connection>> byFirstLookup = Arrays.stream(connections).collect(
            Collectors.toMap(connection -> connection.getFirst().getId(), connection -> Sets.newHashSet(connection),
                (set1, set2) -> { set1.addAll(set2); return set1; }));
        Map<String, Set<Connection>> bySecondLookup = Arrays.stream(connections).collect(
            Collectors.toMap(connection -> connection.getSecond().getId(), connection -> Sets.newHashSet(connection),
                (set1, set2) -> { set1.addAll(set2); return set1; }));

        when(connectionRepository.findOne(any(String.class)))
            .then(invocation -> byIdLookup.get(invocation.getArgumentAt(0, String.class)));

        when(connectionRepository.findAllByFirstOrSecond(any(User.class), any(User.class)))
            .then(invocation -> {
                if (byFirstLookup.containsKey(invocation.getArgumentAt(0, User.class).getId())) {
                    return byFirstLookup.get(invocation.getArgumentAt(0, User.class).getId());
                } else {
                    return bySecondLookup.get(invocation.getArgumentAt(1, User.class).getId());
                }
            });
    }

    private User createTestUser(String name) {
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setName(name);
        return user;
    }

    private Connection createTestConnection(User first, User second) {
        Connection connection = new Connection(first, second);
        connection.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        return connection;
    }

}
