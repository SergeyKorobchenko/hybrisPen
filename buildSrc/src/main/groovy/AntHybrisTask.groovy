package main.groovy

import org.gradle.api.internal.ConventionTask;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.process.ExecResult;
import org.gradle.process.BaseExecSpec;
import org.gradle.process.ProcessForkOptions;
import org.gradle.process.internal.DefaultExecAction;
import org.gradle.process.internal.ExecAction;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

class AntHybrisTask extends ConventionTask implements BaseExecSpec {

    private String antBin = "${project.hybris_bin}/platform/apache-ant-${project.ant_version}/bin"
    private String buildFile = "${project.hybris_bin}/platform/build.xml"
    private Map antEnvironmentVariables = project.ant_environment_variables
    private ExecAction execAction;
    private ExecResult execResult;

    List targets = []
    List additionalParameters = []

    public AntHybrisTask() {
        execAction = new DefaultExecAction(getServices().get(FileResolver.class));
    }

    @TaskAction
    void exec() {
        checkHybrisPermission()
        prepareHybrisAnt()
        execResult = execAction.execute()
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
        execAction.commandLine(params)
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

    /**
     * Returns the ExecResult object for the command run by this task. Returns null if the task has not been executed yet.
     */
    public ExecResult getExecResult() {
        return execResult;
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask commandLine(Object... arguments) {
        execAction.commandLine(arguments);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseExecSpec commandLine(Iterable<?> args) {
        execAction.commandLine(args);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask args(Object... args) {
        execAction.args(args);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseExecSpec args(Iterable<?> args) {
        execAction.args(args);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask setArgs(Iterable<?> arguments) {
        execAction.setArgs(arguments);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getArgs() {
        return execAction.getArgs();
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getCommandLine() {
        return execAction.getCommandLine();
    }

    /**
     * {@inheritDoc}
     */
    public String getExecutable() {
        return execAction.getExecutable();
    }

    /**
     * {@inheritDoc}
     */
    public void setExecutable(Object executable) {
        execAction.setExecutable(executable);
    }

    /**
     * {@inheritDoc}
     */
    public void setExecutable(String executable) {
        execAction.setExecutable(executable);
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask executable(Object executable) {
        execAction.executable(executable);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public File getWorkingDir() {
        return execAction.getWorkingDir();
    }

    /**
     * {@inheritDoc}
     */
    public void setWorkingDir(Object dir) {
        execAction.setWorkingDir(dir);
    }

    /**
     * {@inheritDoc}
     */
    public void setWorkingDir(File dir) {
        execAction.setWorkingDir(dir);
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask workingDir(Object dir) {
        execAction.workingDir(dir);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getEnvironment() {
        return execAction.getEnvironment();
    }

    /**
     * {@inheritDoc}
     */
    public void setEnvironment(Map<String, ?> environmentVariables) {
        execAction.setEnvironment(environmentVariables);
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask environment(String name, Object value) {
        execAction.environment(name, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask environment(Map<String, ?> environmentVariables) {
        execAction.environment(environmentVariables);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask copyTo(ProcessForkOptions target) {
        execAction.copyTo(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask setStandardInput(InputStream inputStream) {
        execAction.setStandardInput(inputStream);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getStandardInput() {
        return execAction.getStandardInput();
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask setStandardOutput(OutputStream outputStream) {
        execAction.setStandardOutput(outputStream);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public OutputStream getStandardOutput() {
        return execAction.getStandardOutput();
    }

    /**
     * {@inheritDoc}
     */
    public AntHybrisTask setErrorOutput(OutputStream outputStream) {
        execAction.setErrorOutput(outputStream);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public OutputStream getErrorOutput() {
        return execAction.getErrorOutput();
    }

    /**
     * {@inheritDoc}
     */
    public BaseExecSpec setIgnoreExitValue(boolean ignoreExitValue) {
        execAction.setIgnoreExitValue(ignoreExitValue);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isIgnoreExitValue() {
        return execAction.isIgnoreExitValue();
    }

    void setExecAction(ExecAction execAction) {
        this.execAction = execAction;
    }
}

