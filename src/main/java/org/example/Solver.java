package org.example;

import java.util.*;

public class Solver {
    private String inputString;
    private List<String> transformedString;
    private Stack<String> parsingStack;
    private Stack<Double> calculatingStack;
    private static final Map<String, Integer> operationsPrecedence = Map.of("(", 1, "|", 1,
            "+", 2, "-", 2, "*", 3, "/", 3, "^", 4, "!", 5);
    private static final List<String> functions = List.of("sin", "cos", "tg", "ctg", "exp", "log");
    private Integer result;

    public Solver(String expression) {
        inputString = expression;
        transformedString = new ArrayList<>();
        parsingStack = new Stack<>();
        calculatingStack = new Stack<>();
        result = 0;
    }

    private void transformString() {
        String previousContent = "";
        char currentSymbol;
        boolean dotInTheNumber = false;
        String currentNumber = "";
        boolean foundContent = false;
        boolean previousContentIsNumber = false;
        boolean enteringTheNumber = false;
        boolean enteringTheLastSymbol = false;
        for (int i = 0; i < inputString.length(); i++) {
            currentSymbol = inputString.charAt(i);
            if (i == inputString.length() - 1) {
                enteringTheLastSymbol = true;
            }
            if (Character.isDigit(currentSymbol) || currentSymbol == '.') {
                enteringTheNumber = true;
                currentNumber += currentSymbol;
                if (currentNumber.contains(".")) {
                    throw new IllegalArgumentException("Multiple dots in the expression");
                }
                if (enteringTheLastSymbol) {
                    if (currentNumber.charAt(currentNumber.length() - 1) == '.') {
                        throw new IllegalArgumentException("No digits after decimal sign in the number");
                    }
                    transformedString.add(currentNumber);
                    previousContent = currentNumber;
                    currentNumber = "";
                    enteringTheNumber = false;
                }
            }
            else if (currentSymbol == ' ') {
                if (!currentNumber.isEmpty()) {
                    if (currentNumber.charAt(currentNumber.length() - 1) == '.') {
                        throw new  IllegalArgumentException("No digits after decimal sign in the number");
                    }
                    transformedString.add(currentNumber);
                    previousContent = currentNumber;
                    currentNumber = "";
                    enteringTheNumber = false;
                }
            }
            else if (currentSymbol == '(') {
                if (!enteringTheNumber) {
                    parsingStack.add(String.valueOf(currentSymbol));
                }
                previousContent = "(";
            }
            else if (currentSymbol == ')') {
                if (enteringTheNumber) {
                    if (currentNumber.charAt(currentNumber.length() - 1) == '.') {
                        throw new IllegalArgumentException("No digits after decimal sign in the number");
                    }
                    transformedString.add(currentNumber);
                    previousContent = currentNumber;
                    currentNumber = "";
                    enteringTheNumber = false;
                }
                String currentStackElem = parsingStack.pop();
                System.out.println(currentStackElem);
                while (!currentStackElem.equals("(")) {
                    foundContent = true;
                    transformedString.add(currentStackElem);
                    currentStackElem = parsingStack.pop();
                }
                if (!foundContent) {
                    throw new IllegalArgumentException("No content inside the brackets");
                }
                foundContent = false;
                previousContent = ")";
            }
            else if (currentSymbol == '|') {
                if (!enteringTheNumber && (previousContent.isEmpty() || previousContent.equals("+") ||
                        previousContent.equals("-") ||
                        previousContent.equals("*") || previousContent.equals("/") ||
                        previousContent.equals("(") || functions.contains(previousContent))) {
                    parsingStack.add("|");
                    previousContent = "openingModule";
                }
                else if (enteringTheNumber || previousContent.equals(")") || previousContent.equals("closingModule")) {
                    if (enteringTheNumber) {
                        if (currentNumber.charAt(currentNumber.length() - 1) == '.') {
                            throw new  IllegalArgumentException("No digits after decimal sign in the number");
                        }
                        transformedString.add(currentNumber);
                        previousContent = currentNumber;
                        currentNumber = "";
                        enteringTheNumber = false;
                    }
                    String currentStackElem = parsingStack.pop();
                    while (!currentStackElem.equals("|")) {
                        foundContent = true;
                        transformedString.add(currentStackElem);
                        currentStackElem = parsingStack.pop();
                    }
                    if (!foundContent) {
                        throw new IllegalArgumentException("No content inside the module");
                    }
                    transformedString.add("|");
                    foundContent = false;
                    previousContent = "closingModule";
                }
            }
            else if (currentSymbol == '+' || currentSymbol == '-' || currentSymbol == '*' || currentSymbol == '/' ||
                    currentSymbol == '^' || currentSymbol == '!') {
                if (enteringTheLastSymbol && currentSymbol != '!') {
                    throw new IllegalArgumentException(
                            "Operation sign without a variable at the end of the expression");
                }
                if (currentSymbol == '^' || currentSymbol == '!') {
                    if (enteringTheNumber) {
                        if (currentNumber.charAt(currentNumber.length() - 1) == '.') {
                            throw new  IllegalArgumentException("No digits after decimal sign in the number");
                        }
                        transformedString.add(currentNumber);
                        previousContent = currentNumber;
                        currentNumber = "";
                        enteringTheNumber = false;
                    }
                }
                if (parsingStack.isEmpty()) {
                    parsingStack.add(String.valueOf(currentSymbol));
                }
                else {
                    String currentStackElem = parsingStack.pop();
                    while (operationsPrecedence.getOrDefault(currentStackElem, -1) >=
                        operationsPrecedence.getOrDefault(String.valueOf(currentSymbol), -1) && !parsingStack.isEmpty()) {
                        transformedString.add(currentStackElem);
                        currentStackElem = parsingStack.pop();
                    }
                    parsingStack.add(currentStackElem);
                    parsingStack.add(String.valueOf(currentSymbol));
                    System.out.println(parsingStack);
                }
                previousContent = String.valueOf(currentSymbol);
            }
        }
        while (!parsingStack.isEmpty()) {
            transformedString.add(parsingStack.pop());
        }
        System.out.println(transformedString);
    }

    private boolean isANumber(String object) {
        for (char symbol : object.toCharArray()) {
            if (!Character.isDigit(symbol) && symbol != '.') {
                return false;
            }
        }
        return true;
    }

    public void solveTheExpression() {
        transformString();
        for (String currentElem : transformedString) {
            if (isANumber(currentElem)) {
                calculatingStack.add(Double.parseDouble(currentElem));
            }
            else {
                double currentNumber;
                switch (currentElem) {
                    case "+" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(calculatingStack.pop() + currentNumber);
                    }
                    case "-" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(calculatingStack.pop() - currentNumber);
                    }
                    case "*" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(calculatingStack.pop() * currentNumber);
                    }
                    case "/" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(calculatingStack.pop() / currentNumber);
                    }
                    case "|" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(Math.abs(currentNumber));
                    }
                    case "^" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(Math.pow(calculatingStack.pop(), currentNumber));
                    }
                }
            }
        }
        System.out.println(calculatingStack);
    }
}
