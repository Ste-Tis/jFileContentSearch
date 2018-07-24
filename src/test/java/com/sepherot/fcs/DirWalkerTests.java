package com.sepherot.fcs;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DirWalkerTests {
    private String rootPath;
    private String sub1Path;
    private String sub2Path;
    private String fileTxtPath;
    private String filePyPath;
    private String fileLogPath;

    public DirWalkerTests() throws IOException {
        Path tempDir = Files.createTempDirectory("root");

        rootPath = tempDir.toAbsolutePath().toString();
        sub1Path = Paths.get(rootPath, "sub1").toAbsolutePath().toString();
        sub2Path = Paths.get(rootPath, "sub2").toAbsolutePath().toString();
        fileTxtPath = Paths.get(rootPath, "lorem.txt").toAbsolutePath().toString();
        filePyPath = Paths.get(sub1Path, "amon.py").toAbsolutePath().toString();
        fileLogPath = Paths.get(sub1Path, "amarth.log").toAbsolutePath().toString();

        String txtContent = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr,\n" +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,\n" +
                "sed diam voluptua.";
        String pyContent = "#! /usr/bin/env python\n" +
                "# -*- coding: utf-8 -*-\n" +
                "from dataclasses import dataclass\n" +
                "import os\n" +
                "\n\n" +
                "__author__ = 'Stephan Tischer'\n" +
                "_date__ = '2018-07-04'\n" +
                "__version__ = '0.1.0'\n" +
                "@dataclass\n" +
                "class File:\n" +
                "  name: str\n" +
                "  path: str\n" +
                "  extension: str\n" +
                "\n\n" +
                "  def __init__(self, path):\n" +
                "      self.path = path\n" +
                "      self.name = os.path.basename(path)\n" +
                "      self.extension = os.path.splitext(path)[1].replace('.', '').lower()\n";
        String logContent = "2018-07-04 21:29:53 INFO - New request\n" +
                "2018-07-04 21:30:35 INFO - New request\n" +
                "2018-07-04 21:30:45 WARN - Invalid request\n";

        removeTestDirectory();

        File rootDir = new File(rootPath);
        rootDir.mkdir();
        File sub1Dir = new File(sub1Path);
        sub1Dir.mkdir();
        File sub2Dir = new File(sub2Path);
        sub2Dir.mkdir();

        Path txtFile = Paths.get(fileTxtPath);
        Files.write(txtFile, txtContent.getBytes());
        Path pyFile = Paths.get(filePyPath);
        Files.write(pyFile, pyContent.getBytes());
        Path logFile = Paths.get(fileLogPath);
        Files.write(logFile, logContent.getBytes());
    }

    @Override
    public void finalize() throws IOException {
        removeTestDirectory();
    }

    private void removeTestDirectory() throws IOException {
        File f = new File(rootPath);
        if(f.exists())
            FileUtils.deleteDirectory(new File(rootPath));
    }

    @Test
    public void constructor_noValues() {
        // Act
        DirWalker result = new DirWalker();

        // Assert
        assertTrue(result.getAllowedExtensions().isEmpty());
        assertTrue(result.getRecursive());
    }

    @Test
    public void constructor_SetValues() {
        // Arrange
        boolean recursive = false;
        List<String> extensions = new ArrayList<>();
        extensions.add("java");
        extensions.add("txt");

        // Act
        DirWalker result = new DirWalker(extensions, recursive);

        // Assert
        for(String ext: extensions)
            assertTrue(result.getAllowedExtensions().contains(ext));
        assertFalse(result.getRecursive());
    }

    @Test
    public void allowedExtensions_GetSet() {
        // Arrange
        DirWalker target = new DirWalker();
        List<String> extensions = new ArrayList<>();
        extensions.add("java");
        extensions.add("txt");

        // Act
        target.setAllowedExtenstions(extensions);
        List<String> result = target.getAllowedExtensions();

        // Assert
        for(String ext: extensions)
            assertTrue(result.contains(ext));
    }

    @Test
    public void recursive_GetSet() {
        // Arrange
        DirWalker target = new DirWalker();
        boolean recursive = false;

        // Act
        target.setRecursive(recursive);
        boolean result = target.getRecursive();

        // Assert
        assertEquals(recursive, result);
    }

    @Test
    public void listFiles_searchRecursive_allFiles() {
        // Arrange
        DirWalker target = new DirWalker();
        target.setRecursive(true);
        List<String> expected = new ArrayList<>();
        expected.add(fileTxtPath);
        expected.add(fileLogPath);
        expected.add(filePyPath);

        // Act
        List<String> result = target.listFiles(rootPath);

        // Assert
        assertEquals(expected.size(), result.size());
        for(String exp: expected)
            assertTrue(result.contains(exp));
    }

    @Test
    public void listFiles_searchNotRecursive_onlyFilesRoot() {
        // Arrange
        DirWalker target = new DirWalker();
        target.setRecursive(false);
        List<String> expected = new ArrayList<>();
        expected.add(fileTxtPath);

        // Act
        List<String> result = target.listFiles(rootPath);

        // Assert
        assertEquals(expected.size(), result.size());
        for(String exp: expected)
            assertTrue(result.contains(exp));
    }

    @Test
    public void listFiles_searchFilterExtensions_onlyMatchingFiles() {
        // Arrange
        DirWalker target = new DirWalker();
        target.setRecursive(true);
        List<String> extensions = new ArrayList<>();
        extensions.add("txt");
        extensions.add(".py");
        target.setAllowedExtenstions(extensions);
        List<String> expected = new ArrayList<>();
        expected.add(fileTxtPath);
        expected.add(filePyPath);

        // Act
        List<String> result = target.listFiles(rootPath);

        // Assert
        assertEquals(expected.size(), result.size());
        for(String exp: expected)
            assertTrue(result.contains(exp));
    }

    @Test
    public void listFiles_nonExistingDirectory_returnEmptyList() {
        // Arrange
        DirWalker target = new DirWalker();
        String path = Paths.get(rootPath, "notFound").toAbsolutePath().toString();

        // Act
        List<String> result = target.listFiles(path);

        // Assert
        assertEquals(0, result.size());
    }
}
