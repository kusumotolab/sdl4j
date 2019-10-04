package com.github.kusumotolab.sdl4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class CommandLine {

  public CommandLineResult forceExecute(final String... commands) {
    return forceExecute(new File("."), commands);
  }

  public CommandLineResult forceExecute(final File dir, final String... commands) {
    try {
      return execute(dir, commands);
    } catch (final IOException | InterruptedException e) {
      e.printStackTrace();
      return null;
    }
  }

  public CommandLineResult execute(final String... commands)
      throws IOException, InterruptedException {
    return execute(new File("."), commands);
  }

  public CommandLineResult execute(final File dir, final String... commands)
      throws IOException, InterruptedException {
    final ProcessBuilder processBuilder = new ProcessBuilder().redirectErrorStream(true)
        .directory(dir)
        .command(commands);

    final Process process = processBuilder.start();
    final int exitCode = process.waitFor();

    final InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
    final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    final List<String> outputLines = bufferedReader.lines()
        .collect(Collectors.toList());

    return new CommandLineResult(outputLines, exitCode);
  }

  public class CommandLineResult {

    private final List<String> outputLines;
    private final int exitCode;

    CommandLineResult(final List<String> outputLines, final int exitCode) {
      this.outputLines = outputLines;
      this.exitCode = exitCode;
    }

    public List<String> getOutputLines() {
      return outputLines;
    }

    public int getExitCode() {
      return exitCode;
    }

    public boolean isSuccess() {
      return exitCode == 0;
    }
  }
}
