package com.example.social;



import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class SimpleTest {
    @Test
    public void test(){
        int a = 10;
        int b = 10;
        Assert.assertEquals(20,a+b);
    }
}
