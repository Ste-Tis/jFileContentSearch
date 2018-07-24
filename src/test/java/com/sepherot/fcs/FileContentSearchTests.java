package com.sepherot.fcs;

import static org.junit.Assert.*;

import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileContentSearchTests {
    private String rootPath;
    private String sub1Path;
    private String sub2Path;
    private String fileTxtPath;
    private String filePyPath;
    private String fileLogPath;

    public FileContentSearchTests() throws IOException {
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
                "\n\n" +
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
    public void search_termInFile_returnShortFileName() {
        try {
            // Arrange
            String[] args = new String[] {"sed", "-d", rootPath};
            FileContentSearch fcs = new FileContentSearch(args);

            // Act
            String result = fcs.search();

            // Assert
            assertTrue(result.contains(new File(fileTxtPath).getName()));
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    public void search_termInFile_returnLongFileName() {
        try {
            // Arrange
            String[] args = new String[] {"sed", "-d", rootPath, "-long"};
            FileContentSearch fcs = new FileContentSearch(args);

            // Act
            String result = fcs.search();

            // Assert
            assertTrue(result.contains(fileTxtPath));
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    public void search_MultipleTerms_returnMultipleFiles() {
        try {
            // Arrange
            String[] args = new String[] {"sed", "def", "-d", rootPath, "-long"};
            FileContentSearch fcs = new FileContentSearch(args);

            // Act
            String result = fcs.search();

            // Assert
            assertTrue(result.contains(fileTxtPath));
            assertTrue(result.contains(filePyPath));
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    public void search_termInFile_returnLineNumbers() {
        try {
            // Arrange
            String[] args = new String[] {"sed", "-d", rootPath, "-long", "-l"};
            FileContentSearch fcs = new FileContentSearch(args);

            // Act
            String result = fcs.search();

            // Assert
            assertTrue(result.contains(fileTxtPath));
            assertTrue(result.contains("2, 3"));
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    public void search_termNotInFile_returnNoHits() {
        try {
            // Arrange
            String[] args = new String[] {"notFound", "-d", rootPath};
            FileContentSearch fcs = new FileContentSearch(args);

            // Act
            String result = fcs.search();

            // Assert
            assertTrue(!result.contains(fileTxtPath));
            assertTrue(!result.contains(fileLogPath));
            assertTrue(!result.contains(filePyPath));
        } catch (ParseException e) {
            assertTrue(false);
        }
    }
}
