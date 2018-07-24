package com.sepherot.fcs;

import static org.junit.Assert.*;

import com.sepherot.fcs.data.SearchConfiguration;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.nio.file.Paths;

public class CmdParserTests {
    @Test
    public void parse_search_oneTerm_returnOne() {
        // Arrange
        String[] args = {"Egon Olsen"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            for(String arg: args)
                assertTrue(result.getSearchTerms().contains(arg));
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_search_multipleTerms_returnMultiple() {
        // Arrange
        String[] args = {"Egon Olsen", "Benny Frandsen", "Kjeld Jensen"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            for(String arg: args)
                assertTrue(result.getSearchTerms().contains(arg));
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_notSetDir_setToCurrentDir() {
        // Arrange
        String[] args = {"Egon Olsen"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertEquals(".", result.getRootDirectory());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_dir_shortForm_returnValue() {
        // Arrange
        String[] args = {"Egon Olsen", "-d", Paths.get("C:", "My", "Dir").toString()};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertEquals(result.getRootDirectory(), args[2]);
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_dir_longForm_returnValue() {
        // Arrange
        String[] args = {"Egon Olsen", "--dir", Paths.get("C:", "My", "Dir").toString()};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertEquals(result.getRootDirectory(), args[2]);
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_notSetExtensions_setToNull() {
        // Arrange
        String[] args = {"Egon Olsen"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertNull(result.getExtensions());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_extensions_shortForm_returnValue() {
        // Arrange
        String[] args = {"Egon Olsen", "-e", ".java"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertTrue(result.getExtensions().contains(args[2].substring(1)));
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_extensions_longForm_returnValue() {
        // Arrange
        String[] args = {"Egon Olsen", "--extensions", ".java"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertTrue(result.getExtensions().contains(args[2].substring(1)));
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_extensions_multipleValues_returnMultipleValues() {
        // Arrange
        String[] args = {"Egon Olsen", "--extensions", ".java", "txt"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertTrue(result.getExtensions().contains(args[2].substring(1)));
            assertTrue(result.getExtensions().contains(args[3]));
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_notSetLines_setToFalse() {
        // Arrange
        String[] args = {"Egon Olsen"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertFalse(result.isShowLines());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_lines_shortForm_setToTrue() {
        // Arrange
        String[] args = {"Egon Olsen", "-l"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertTrue(result.isShowLines());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_lines_longForm_setToTrue() {
        // Arrange
        String[] args = {"Egon Olsen", "--lines"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertTrue(result.isShowLines());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_notSetCaseSensitive_setToFalse() {
        // Arrange
        String[] args = {"Egon Olsen"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertFalse(result.isCaseSensitive());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_caseSensitive_shortForm_setToTrue() {
        // Arrange
        String[] args = {"Egon Olsen", "-cs"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertTrue(result.isCaseSensitive());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_caseSensitive_longForm_setToTrue() {
        // Arrange
        String[] args = {"Egon Olsen", "--case-sensitive"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertTrue(result.isCaseSensitive());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_notSetLong_setToFalse() {
        // Arrange
        String[] args = {"Egon Olsen"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertFalse(result.isLongPaths());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_long_shortForm_setToTrue() {
        // Arrange
        String[] args = {"Egon Olsen", "-long"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertTrue(result.isLongPaths());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_notSetNoSubdirectories_setToFalse() {
        // Arrange
        String[] args = {"Egon Olsen"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertTrue(result.isRecursive());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_noSubdirectories_shortForm_setToTrue() {
        // Arrange
        String[] args = {"Egon Olsen", "-ns"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertFalse(result.isRecursive());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_noSubdirectories_longForm_setToTrue() {
        // Arrange
        String[] args = {"Egon Olsen", "--no-subdirectories"};

        try {
            // Act
            SearchConfiguration result = CmdParser.parse(args);

            // Assert
            assertFalse(result.isRecursive());
        } catch (ParseException e) {
            fail();
        }
    }
}
