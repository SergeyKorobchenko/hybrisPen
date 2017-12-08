package main.groovy

import org.gradle.api.internal.project.ProjectInternal;

import org.gradle.api.logging.Logging;
import org.gradle.api.logging.Logger;

class Environment {

    private Logger logger = Logging.getLogger(Environment.class)

    protected ProjectInternal project;

    public Environment(ProjectInternal project) {
        this.project = project;
    }

    public String getHostname() {
        // ENV variable name gradle use to determine target environment to deploy.
        System.getenv()['DEPLOY_ENV']
    }

    protected hostnameFromFile() {
        if (project.hasProperty('environment_hostname_file')) {
            def fileName = project.environment_hostname_file
            if (fileName) {
                try {
                    def f = new File(fileName)
                    def hostname = f.getText()
                    return hostname?.trim()
                } catch (Exception e) {
                    logger.info("Was not able to get hostname from file ${fileName}: ${e.getMessage()}")
                    logger.debug("Was not able to get hostname from file ${fileName}", e)
                }
            }
        }
        return null
    }

    public String getEnvironmentName() {
        def environment = getHostname();
        if (project.hasProperty('env')) {
            logger.debug("Setting environment from property env");
            environment = project.env;
        }
        return environment;
    }

    public List getEnvironmentHierarchy(environment_name) {
        def result = [project.environment_base]
        def configuredEnvironments = getEnvironments(environment_name);
        if (configuredEnvironments != null) {

            //add a common one for all servers in the environment
            result.add("${project.environment_base}_${environment_name}")

            result.addAll(configuredEnvironments)
        }
        return result
    }

    /**
     * @return a list of environments which will be used for current hostname or parameter env (-Penv=value)
     */
    public List getEnvironments(environment_name) {
        logger.lifecycle("Getting environments using ${environment_name}")
        for (entry in project.environment_hostname_to_environments) {
            def key = entry.key
            if (environment_name ==~ /${key}/) {
                def value = entry.value
                logger.info("Environment ${environment_name} matched '${key}' using value ${value}")
                return value;
            } else {
                logger.debug("Environment ${environment_name} did not match '${key}'");
            }
        }
        logger.error("No matching enviroment config found for ${environment_name}")
    }

    public Object getAttribute(String name) {
        if (project.hasProperty(name)) {
            return project.property(name);
        }

        if (project.hasProperty('environment_specific_attributes')) {
            // try to resolve environment dependend property
            def activeenvironments = getEnvironmentHierarchy(getEnvironmentName())
            for (env in activeenvironments.reverse()) {
                def value = project.environment_specific_attributes?.get(env)?.get(name)
                if (value != null) {
                    return value
                }
            }
        }
        logger.error("No attribute ${name} found")
        return null
    }
}
