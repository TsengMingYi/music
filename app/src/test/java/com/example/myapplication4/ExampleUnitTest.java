package com.example.myapplication4;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Animal animal;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void test12345(){

       animal = new Animal(321);

       Animal animal12;
        System.out.println(animal.age  + "   "  + animal.eye);
        animal.breathe(5,"ting");

    }
}