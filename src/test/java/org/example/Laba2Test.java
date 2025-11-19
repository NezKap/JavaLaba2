package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Laba2Test {

    @Test
    void solvingBasicExpressions() {
        String expression = "25 + 6";
        Solver object = new Solver(expression);
        object.solveTheExpression();
        assertEquals("31.0", object.toString());
        expression = "37 - 6";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        assertEquals("31.0", object.toString());
        expression = "36 / 6";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        assertEquals("6.0", object.toString());
        expression = "6 * 7";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        assertEquals("42.0", object.toString());
    }

    @Test
    void correctSolvingWithFloatingPointNumbers() {
        String expression = "25.7 + 6.42";
        Solver object = new Solver(expression);
        object.solveTheExpression();
        assertEquals("32.12", object.toString());
        expression = "42.5 / 7.6";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        assertEquals(String.valueOf(42.5 / 7.6), object.toString());
    }

    @Test
    void correctSolvingWithTrigonometricFunctions() {
        String expression = "sin(5)";
        Solver object = new Solver(expression);
        object.solveTheExpression();
        assertEquals(String.valueOf(Math.sin(5)), object.toString());
        expression = "cos(25)";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        assertEquals(String.valueOf(Math.cos(25)), object.toString());
        expression = "tg(75 + sin(50))";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        assertEquals(String.valueOf(Math.tan(75 + Math.sin(50))), object.toString());
    }

    @Test
    void correctSolvingWithLnAndExp() {
        String expression = "exp(6)";
        Solver object = new Solver(expression);
        object.solveTheExpression();
        assertEquals(String.valueOf(Math.exp(6)), object.toString());
        expression = "ln(2)";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        assertEquals(String.valueOf(Math.log(2)), object.toString());
    }

    @Test
    void correctSolvingWithAlphabeticVaribales() {
        String expression = "|x + y| * 7";
        Solver object = new Solver(expression);
                /*
        object.solveTheExpression();
        assertEquals("14.0", object.toString());
         */
        expression = "xe + 7";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
    }

    @Test
    void solvingComplexExpressions() {
        String expression = "(25.6 + 72^2) / (exp(9) * 5 / 2.65)";
        Solver object = new Solver(expression);
        object.solveTheExpression();
        assertEquals(String.valueOf((25.6 + Math.pow(72, 2)) / (Math.exp(9) * 5  / 2.65)), object.toString());
    }

    @Test
    void divisioningByZero() {
        String expression = "1 / 0";
        Solver object = new Solver(expression);
        assertThrows(ArithmeticException.class, object::solveTheExpression);
        expression = "(5 + 7) / (12 - 12)";
        object.changeTheExpression(expression);
        assertThrows(ArithmeticException.class, object::solveTheExpression);
        expression = "16 / ((28 + 5^2) * 0)";
        object.changeTheExpression(expression);
        assertThrows(ArithmeticException.class, object::solveTheExpression);
    }

    @Test
    void invalidExpression() {
        String expression = "1 // 0";
        Solver object = new Solver(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = "+ 1 * 5";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = "(1 + 6";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = "(1 + 6 - 7) ( + 6))";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = "1 & 2 + 3";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = " 1 * 2 + 2.5.";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = "sin(1";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = "si) + 6";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = "sin(60) + 1 *";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = "exp(3) + 6 * 10.6 + .";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
        expression = "65) + 6";
        object.changeTheExpression(expression);
        assertThrows(IllegalArgumentException.class, object::solveTheExpression);
    }

    @Test
    void edgeCases() {
        String expression = "52";
        Solver object = new Solver(expression);
        object.solveTheExpression();
        assertEquals("52.0", object.toString());
        expression = "-96";
        object.changeTheExpression(expression);
        object.solveTheExpression();
        assertEquals("-96.0", object.toString());
    }
}
