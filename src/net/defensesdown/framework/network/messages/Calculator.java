package net.defensesdown.framework.network.messages;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 18:43
 */
public class Calculator {


    public void main(String[] args) {
        int result;

        result = multiply(2, 2); //получится 4
        result = divide(8, 2); //получится 4
    }

    int multiply(int a, int b) {
        return a * b;
    }

    int divide(int a, int b) {
        return a / b;
    }

    int sum(int a, int b) {
        return a + b;
    }

    int dif(int a, int b) {
        return a - b;
    }

}
