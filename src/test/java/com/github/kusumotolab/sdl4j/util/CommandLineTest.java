package com.github.kusumotolab.sdl4j.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import com.github.kusumotolab.sdl4j.util.CommandLine.CommandLineResult;

public class CommandLineTest {

  @Test
  public void testExecute() {
    final CommandLine commandLine = new CommandLine();
    final CommandLineResult result = commandLine.forceExecute("pwd");

    assertThat(result.getExitCode()).isEqualTo(0);
    assertThat(result.getOutputLines()).hasSize(1)
        .allMatch(e -> e.endsWith("sdl4j"));
  }
}