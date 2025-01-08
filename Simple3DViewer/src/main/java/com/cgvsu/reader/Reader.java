package com.cgvsu.reader;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {
    public static String[] readNumbersInLine(String s, String defaultString, int n) {
        String[] arr = s.split("[,\\s]+");
        String[] coefficient = new String[n];
        if (arr.length == n) {
            return arr;
        }
        if (arr.length < n){
            for (int i = 0; i < arr.length; i++) {
                coefficient[i] = arr[i];
            }
            for (int i = arr.length; i < n; i++) {
                coefficient[i] = defaultString;
            }
        } else {
            for (int i = 0; i < n; i++) {
                coefficient[i] = arr[i];
            }
        }
        return coefficient;
    }
    public static String[] readNumbersInLineWithXYZ(String s, String defaultString) {
        int n = 3;
        String[] arr = s.split("[,\\s]+");
        // 10x 9z 10y
        String[] coefficient = new String[n];
        Arrays.fill(coefficient, defaultString);
        for(int i = 0; i<arr.length; i++){
            if(Pattern.matches("\\d+x", arr[i])){
                coefficient[0] = arr[i].substring(0, arr[i].length()-1);
            }
            if(Pattern.matches("\\d+y", arr[i])){
                coefficient[1] = arr[i].substring(0, arr[i].length()-1);
            }
            if(Pattern.matches("\\d+z", arr[i])){
                coefficient[2] = arr[i].substring(0, arr[i].length()-1);
            }
        }
        return coefficient;
    }

}
