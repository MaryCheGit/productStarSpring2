package com.projecr.mariya.productStarSpring2.service;

import com.projecr.mariya.productStarSpring2.model.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserServiceTest {
    
    private CsvParserService csvParserService;
    
    @BeforeEach
    void setUp() {
        csvParserService = new CsvParserService();
    }
    
    @Test
    void testParseFile_WithValidData(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("test.csv");
        String content = "Иван Иванов, М, 10 км, 55:20\n" +
                        "Мария Петрова, Ж, 5 км, 28:45\n";
        Files.writeString(csvFile, content);
        
        List<Result> results = csvParserService.parseFile(csvFile.toString());
        
        assertEquals(2, results.size());
        assertEquals("Иван Иванов", results.get(0).getFullName());
        assertEquals("М", results.get(0).getGender());
        assertEquals("10 км", results.get(0).getDistance());
        assertEquals("55:20", results.get(0).getTimeString());
    }
    
    @Test
    void testParseFile_WithEmptyFile(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("empty.csv");
        Files.writeString(csvFile, "");
        
        List<Result> results = csvParserService.parseFile(csvFile.toString());
        
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testParseFile_FileNotFound() {
        assertThrows(IOException.class, () -> {
            csvParserService.parseFile("nonexistent.csv");
        });
    }
    
    @Test
    void testParseFile_WithInvalidData(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("invalid.csv");
        String content = "Иван Иванов, М, 10 км, 55:20\n" +
                        " , Ж, 5 км, 28:45\n" +
                        "Петр Петров, X, 5 км, 22:15\n";
        Files.writeString(csvFile, content);
        
        List<Result> results = csvParserService.parseFile(csvFile.toString());
        
        assertEquals(1, results.size());
        assertEquals("Иван Иванов", results.get(0).getFullName());
    }
}