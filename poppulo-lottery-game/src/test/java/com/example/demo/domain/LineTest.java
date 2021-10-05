package com.example.demo.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @Test
    void testCalculateResultEqualTo10() {
        List<int[]> tests = new ArrayList<int[]>(){{
            add(new int[]{0,1,1});
            add(new int[]{1,1,0});
            add(new int[]{0,0,2});
        }};
        for(int[] arr: tests) {
            Line line = new Line();
            line.setNumbers(arr);
            int got = line.calculateResult();
            assertEquals(got,10);
        }
    }

    @Test
    void testCalculateResultEqualTo5() {
        List<int[]> tests = new ArrayList<int[]>(){{
            add(new int[]{0,0,0});
            add(new int[]{1,1,1});
            add(new int[]{2,2,2});
        }};
        for(int[] arr: tests) {
            Line line = new Line();
            line.setNumbers(arr);
            int got = line.calculateResult();
            assertEquals(got,5);
        }
    }

    @Test
    void testCalculateResultEqualTo1() {
        List<int[]> tests = new ArrayList<int[]>(){{
            add(new int[]{0,1,2});
            add(new int[]{1,0,0});
            add(new int[]{2,0,1});
        }};
        for(int[] arr: tests) {
            Line line = new Line();
            line.setNumbers(arr);
            int got = line.calculateResult();
            assertEquals(got,1);
        }
    }

    @Test
    void testCalculateResultEqualTo0() {
        List<int[]> tests = new ArrayList<int[]>(){{
            add(new int[]{0,1,0});
            add(new int[]{2,0,2});
            add(new int[]{2,1,2});
            add(new int[]{1,1,2});
            add(new int[]{1,2,1});
        }};
        for(int[] arr: tests) {
            Line line = new Line();
            line.setNumbers(arr);
            int got = line.calculateResult();
            assertEquals(got,0);
        }
    }
}