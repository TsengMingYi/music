package com.example.myapplication4;

public class Animal {

    public String eye="eyes";
    public int age    =  0;

    public void breathe(int newAge, String msg){
            age = newAge;
            System.out.println("I am breathing. " + age);
    }

    public void test(){
        int agetest = 50;
        age = 3;
    }
//
//    public Animal(){
//
//   }

    public Animal(int age){
        this.age = age;
        System.out.println(age);
    }
}
