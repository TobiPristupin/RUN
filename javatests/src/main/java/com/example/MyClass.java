package com.example;

public class MyClass {

    public static void main(String[] args){
        System.out.println(fact(4));
    }

    public static int fact(int input){
        if (input == 1){
            return input;
        }

        return input * fact(input - 1);
    }


}
