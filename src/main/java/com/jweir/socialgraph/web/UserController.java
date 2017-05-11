package com.jweir.socialgraph.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.jweir.socialgraph.domain.User;
import com.jweir.socialgraph.error.ResourceNotFoundException;
import com.jweir.socialgraph.serialization.View;
import com.jweir.socialgraph.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Inject
    UserController(UserService userService) {
        this.userService = userService;
    }

    @JsonView(View.Summary.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    Set<User> all() {
        return userService.all()
            .collect(Collectors.toSet());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    User get(@PathVariable("id") String id) {
        return userService.get(id).orElseThrow(() -> new ResourceNotFoundException());
    }

}
