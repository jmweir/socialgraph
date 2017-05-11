package com.jweir.socialgraph.config.seed;

import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.Repository;

import java.util.Map;

public class DataSeedDelegate {

    private Map<String, Repository> repositories;

    public DataSeedDelegate(ApplicationContext ctx) {
        this.repositories = ctx.getBeansOfType(Repository.class);
    }

    public Map<String, Repository> getRepositories() {
        return repositories;
    }

}
