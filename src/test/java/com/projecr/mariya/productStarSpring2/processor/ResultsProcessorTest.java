package com.projecr.mariya.productStarSpring2.processor;

import com.projecr.mariya.productStarSpring2.model.Result;
import com.projecr.mariya.productStarSpring2.service.CsvParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultsProcessorTest {
    
    @Mock
    private CsvParserService csvParserService;
    
    @InjectMocks
    private ResultsProcessor resultsProcessor;
    
    private List<Result> mockResults;
    
    @BeforeEach
    void setUp() {
        mockResults = Arrays.asList(
            new Result("Иван Иванов", "М", "10 км", "55:20"),
            new Result("Петр Петров", "М", "10 км", "52:15"),
            new Result("Сергей Сергеев", "М", "10 км", "58:30"),
            new Result("Анна Аннова", "Ж", "10 км", "60:00"),
            new Result("Мария Мариева", "Ж", "5 км", "28:45")
        );
    }
    
    @Test
    void testLoadData_Success() throws IOException {
        when(csvParserService.parseFile("test.csv")).thenReturn(mockResults);
        
        resultsProcessor.loadData("test.csv");
        
        verify(csvParserService, times(1)).parseFile("test.csv");
        assertNotNull(resultsProcessor.getAllResults());
        assertEquals(5, resultsProcessor.getAllResults().size());
    }
    
    @Test
    void testLoadData_ThrowsIOException() throws IOException {
        when(csvParserService.parseFile(anyString())).thenThrow(new IOException("Файл не найден"));
        
        assertThrows(IOException.class, () -> resultsProcessor.loadData("test.csv"));
    }
    
    @Test
    void testGetTopResults_Men10km() throws IOException {
        when(csvParserService.parseFile(anyString())).thenReturn(mockResults);
        resultsProcessor.loadData("test.csv");
        
        List<Result> topResults = resultsProcessor.getTopResults("10 км", "М", 2);
        
        assertEquals(2, topResults.size());
        assertEquals("Петр Петров", topResults.get(0).getFullName());
        assertEquals("Иван Иванов", topResults.get(1).getFullName());
    }
    
    @Test
    void testGetTopResults_Women5km() throws IOException {
        when(csvParserService.parseFile(anyString())).thenReturn(mockResults);
        resultsProcessor.loadData("test.csv");
        
        List<Result> topResults = resultsProcessor.getTopResults("5 км", "Ж", 1);
        
        assertEquals(1, topResults.size());
        assertEquals("Мария Мариева", topResults.get(0).getFullName());
    }
    
    @Test
    void testGetTopResults_WithoutLoadingData() {
        assertThrows(IllegalStateException.class, () -> {
            resultsProcessor.getTopResults("10 км", "М", 3);
        });
    }
    
    @Test
    void testGetTopResults_WithInvalidN() throws IOException {
        when(csvParserService.parseFile(anyString())).thenReturn(mockResults);
        resultsProcessor.loadData("test.csv");
        
        assertThrows(IllegalArgumentException.class, () -> {
            resultsProcessor.getTopResults("10 км", "М", 0);
        });
    }
    
    @Test
    void testGetTopResults_WithNGreaterThanAvailable() throws IOException {
        when(csvParserService.parseFile(anyString())).thenReturn(mockResults);
        resultsProcessor.loadData("test.csv");
        
        List<Result> topResults = resultsProcessor.getTopResults("10 км", "М", 10);
        
        assertEquals(3, topResults.size());
    }
    
    @Test
    void testGetTopResults_NoMatchingResults() throws IOException {
        when(csvParserService.parseFile(anyString())).thenReturn(mockResults);
        resultsProcessor.loadData("test.csv");
        
        List<Result> topResults = resultsProcessor.getTopResults("20 км", "М", 3);
        
        assertTrue(topResults.isEmpty());
    }
    
    @Test
    void testGetTopResults_SortingOrder() throws IOException {
        List<Result> unsortedResults = Arrays.asList(
            new Result("Медленный", "М", "5 км", "30:00"),
            new Result("Средний", "М", "5 км", "25:00"),
            new Result("Быстрый", "М", "5 км", "20:00")
        );
        
        when(csvParserService.parseFile(anyString())).thenReturn(unsortedResults);
        resultsProcessor.loadData("test.csv");
        
        List<Result> topResults = resultsProcessor.getTopResults("5 км", "М", 3);
        
        assertEquals("Быстрый", topResults.get(0).getFullName());
        assertEquals("Средний", topResults.get(1).getFullName());
        assertEquals("Медленный", topResults.get(2).getFullName());
    }
    
    @Test
    void testGetAllResults() throws IOException {
        when(csvParserService.parseFile(anyString())).thenReturn(mockResults);
        resultsProcessor.loadData("test.csv");
        
        List<Result> allResults = resultsProcessor.getAllResults();
        
        assertNotNull(allResults);
        assertEquals(5, allResults.size());
    }
    
    @Test
    void testClearData() throws IOException {
        when(csvParserService.parseFile(anyString())).thenReturn(mockResults);
        resultsProcessor.loadData("test.csv");
        assertNotNull(resultsProcessor.getAllResults());
        
        resultsProcessor.clearData();
        
        assertNull(resultsProcessor.getAllResults());
    }
}