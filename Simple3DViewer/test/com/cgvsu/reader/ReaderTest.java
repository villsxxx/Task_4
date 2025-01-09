package com.cgvsu.reader;

import com.cgvsu.math.Vector3f;
import com.cgvsu.objreader.ObjReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ReaderTest {

    @Test
    public void testLineWithXYZ1() {
        String line = "0x 0y 0z";
        String[] result = Reader.readNumbersInLineWithXYZ(line, "0");
        String[] expectedResult = new String[]{"0", "0", "0"};
        Assertions.assertEquals(result[0], expectedResult[0]);
        Assertions.assertEquals(result[1], expectedResult[1]);
        Assertions.assertEquals(result[2], expectedResult[2]);
    }
    @Test
    public void testLineWithXYZ2() {
        String line = "10x 5y 0z";
        String[] result = Reader.readNumbersInLineWithXYZ(line, "0");
        String[] expectedResult = new String[]{"10", "5", "0"};
        Assertions.assertEquals(result[0], expectedResult[0]);
        Assertions.assertEquals(result[1], expectedResult[1]);
        Assertions.assertEquals(result[2], expectedResult[2]);
    }
    @Test
    public void testLineWithXYZ3() {
        String line = "10y 5x 33z";
        String[] result = Reader.readNumbersInLineWithXYZ(line, "0");
        String[] expectedResult = new String[]{"5", "10", "33"};
        Assertions.assertEquals(result[0], expectedResult[0]);
        Assertions.assertEquals(result[1], expectedResult[1]);
        Assertions.assertEquals(result[2], expectedResult[2]);
    }
    @Test
    public void testLineWithXYZ4() {
        String line = "10x 5x 0z";
        String[] result = Reader.readNumbersInLineWithXYZ(line, "0");
        String[] expectedResult = new String[]{"5", "0", "0"};
        Assertions.assertEquals(result[0], expectedResult[0]);
        Assertions.assertEquals(result[1], expectedResult[1]);
        Assertions.assertEquals(result[2], expectedResult[2]);
    }
}
