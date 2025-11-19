package org.example;

public class Main {
    public static void main(String[] args) {
        String expression = "|1 - 2| * 3";
        Solver object = new Solver(expression);
        object.solveTheExpression();
        System.out.println(object);
        System.out.println("Solving expression with sin function: ");
        expression = "sin(1) / 5";
        System.out.println(expression);
        object.changeTheExpression(expression);
        object.solveTheExpression();
        System.out.println(object);
        System.out.println("Solving expression with sin function: ");
        expression = "|-1 * ln(2)|";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        System.out.println(object);
        expression = "(4.5 + 3)^2 * 7";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        System.out.println(object);
    }
}