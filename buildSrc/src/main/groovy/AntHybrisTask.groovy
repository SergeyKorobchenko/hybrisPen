package main.groovy

import org.gradle.api.tasks.AbstractExecTask;
import org.gradle.process.ExecResult;
import org.gradle.process.BaseExecSpec;
import org.gradle.process.ProcessForkOptions;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

class AntHybrisTask extends AbstractExecTask {

    private String antBin = "${project.hybris_bin}/platform/apache-ant-${project.ant_version}/bin"
    private String buildFile = "${project.hybris_bin}/platform/build.xml"
    private Map antEnvironmentVariables = project.ant_environment_variables

    List targets = []
    List additionalParameters = []

    AntHybrisTask() {
        super(AntHybrisTask.class)
    }

    @TaskAction
    void exec() {
        checkHybrisPermission()
        prepareHybrisAnt()
        super.exec()
    }

    protected void prepareHybrisAnt() {
        List params = []
        if (isWindows()) {
            params.add("${antBin}/ant.bat");
        } else {
            params.add("${antBin}/ant");
        }
        params.add('-f');
        params.add(buildFile);
        params.addAll(targets);
        params.addAll(additionalParameters);

        addEnvironmentVariables()
        setCommandLine(params)
    }

    /**
     * Sets environment variables for the ant process
     */
    protected void addEnvironmentVariables() {
        for (entry in antEnvironmentVariables) {
            logger.debug("Setting ant environment variable: " + entry.key + " to value " + entry.value)
            environment(entry.key, entry.value)
        }
    }

    protected void checkHybrisPermission() {
        if (!isWindows()) {
            File file = new File("${antBin}/ant");
            file.setExecutable(true);
        }
    }

    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

}

