package com.company;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleTest {

    @Test
    public void MathCheck() {
        int a = 81;
        int b = (int) Math.sqrt(a);
        assertEquals(a, b * b);
    }
}
