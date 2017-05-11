package com.jweir.socialgraph.config.seed;

import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStreamReader;

public class DataSeed implements InitializingBean, ApplicationContextAware {

    private String baseResourcesPath;

    private String profile;

    private ApplicationContext ctx;

    private GroovyShell shell;

    private DataSeedDelegate delegate;

    private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    @Override
    public void afterPropertiesSet() throws Exception {
        shell = getShell();

        for (Resource scriptResource : getScriptResources()) {
            apply(scriptResource);
        }
    }

    public void setBaseResourcesPath(String baseResourcesPath) {
        this.baseResourcesPath = baseResourcesPath;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    private GroovyShell getShell() {
        if (shell == null) {
            CompilerConfiguration configuration = new CompilerConfiguration();
            configuration.setScriptBaseClass(DelegatingScript.class.getName());
            shell = new GroovyShell(Thread.currentThread().getContextClassLoader(), configuration);
        }
        return shell;
    }

    private DataSeedDelegate getDelegate() {
        if (delegate == null) {
            delegate = new DataSeedDelegate(ctx);
        }
        return delegate;
    }

    private Resource[] getScriptResources() throws IOException {
        return resolver.getResources(String.format("%s/%s/*.groovy", baseResourcesPath, profile));
    }

    private void apply(Resource scriptResource) throws IOException {
        DelegatingScript script = (DelegatingScript) shell.parse(new InputStreamReader(scriptResource.getInputStream()));
        script.setDelegate(getDelegate());
        script.run();
    }

}