package org.example;

import java.util.*;

public class Solver {
    private String inputString;
    private List<String> transformedString;
    private Stack<String> parsingStack;
    private Stack<Double> calculatingStack;
    private static final Map<String, Integer> operationsPrecedence = Map.of("(", 1, "|", 1,
            "+", 2, "-", 2, "*", 3, "/", 3, "^", 4, "!", 5);
    private static final List<String> functions = List.of("sin", "cos", "tg", "ctg", "exp", "ln");
    private double result;

    public Solver(String expression) {
        inputString = expression;
        transformedString = new ArrayList<>();
        parsingStack = new Stack<>();
        calculatingStack = new Stack<>();
        result = 0;
    }

    private boolean isANumber(String object) {
        for (char symbol : object.toCharArray()) {
            if (!Character.isDigit(symbol) && symbol != '.') {
                if (!(symbol == '-' && object.length() > 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private double factorial(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("Factorial operation is not defined for negative numbers");
        }
        if (number % 1 != 0) {
            throw new IllegalArgumentException("Factorial operation is not defined for floating-point numbers");
        }
        double result = 1;
        int numberToInteger = (int) number;
        for (int i = 2; i <= numberToInteger; i++) {
            result *= i;
        }
        return result;
    }

    private void transformString() {
        String previousContent = "";
        char currentSymbol;
        String currentNumber = "";
        String currentFunction = "";
        boolean foundContent = false;
        boolean enteringTheNumber = false;
        boolean enteringTheLastSymbol = false;
        for (int i = 0; i < inputString.length(); i++) {
            currentSymbol = inputString.charAt(i);
            if (i == inputString.length() - 1) {
                enteringTheLastSymbol = true;
            }
            if (Character.isDigit(currentSymbol) || currentSymbol == '.') {
                enteringTheNumber = true;
                if (currentSymbol == '.' && currentNumber.contains(".")) {
                    throw new IllegalArgumentException("Multiple dots in the expression");
                }
                currentNumber += currentSymbol;
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
            else if (Character.isAlphabetic(currentSymbol)) {
                if (currentFunction.isEmpty()) {
                    currentFunction += currentSymbol;
                    previousContent = currentFunction;
                }
                else {
                    if (currentFunction.equals("s") && currentSymbol == 'i') {
                        currentFunction += currentSymbol;
                        previousContent = currentFunction;
                    }
                    else if (currentFunction.equals("si") && currentSymbol == 'n') {
                        currentFunction += currentSymbol;
                        previousContent = currentFunction;
                        parsingStack.add(currentFunction);
                        currentFunction = "";
                    }
                    else if (currentFunction.equals("c") && (currentSymbol == 'o' || currentSymbol == 't')) {
                        currentFunction += currentSymbol;
                        previousContent = currentFunction;
                    }
                    else if (currentFunction.equals("co") && currentSymbol == 's' ||
                            currentFunction.equals("ct") && currentSymbol == 'g') {
                        currentFunction += currentSymbol;
                        previousContent = currentFunction;
                        parsingStack.add(currentFunction);
                        currentFunction = "";
                    }
                    else if (currentFunction.equals("t") && currentSymbol == 'g') {
                        currentFunction += currentSymbol;
                        previousContent = currentFunction;
                        parsingStack.add(currentFunction);
                        currentFunction = "";
                    }
                    else if (currentFunction.equals("e") && currentSymbol == 'x') {
                        currentFunction += currentSymbol;
                        previousContent = currentFunction;
                    }
                    else if (currentFunction.equals("ex") && currentSymbol == 'p') {
                        currentFunction += currentSymbol;
                        previousContent = currentFunction;
                        parsingStack.add(currentFunction);
                        currentFunction = "";
                    }
                    else if (currentFunction.equals("l") && currentSymbol == 'n') {
                        currentFunction += currentSymbol;
                        previousContent = currentFunction;
                        parsingStack.add(currentFunction);
                        currentFunction = "";
                    }
                    else {
                        throw new IllegalArgumentException("Invalid function name");
                    }
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
                if (currentFunction.length() == 1) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter the value for " + currentSymbol + ':');
                    currentNumber = scanner.nextLine();
                    if (!isANumber(currentNumber)) {
                        throw new IllegalArgumentException("Entered data is not a number");
                    }
                    transformedString.add(currentNumber);
                    previousContent = currentNumber;
                    currentNumber = "";
                    currentFunction = "";
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
                else if (!currentFunction.isEmpty()) {
                    if (currentFunction.length() == 1) {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter the value for " + currentFunction + ':');
                        currentNumber = scanner.nextLine();
                        if (!isANumber(currentNumber)) {
                            throw new IllegalArgumentException("Entered data is not a number");
                        }
                        transformedString.add(currentNumber);
                        previousContent = currentNumber;
                        currentNumber = "";
                        currentFunction = "";
                    }
                    else {
                        throw new IllegalArgumentException("Invalid form of the function");
                    }
                }
                if (parsingStack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid placement of the brackets in the expression");
                }
                String currentStackElem = parsingStack.pop();
                while (!currentStackElem.equals("(")) {
                    foundContent = true;
                    transformedString.add(currentStackElem);
                    currentStackElem = parsingStack.pop();
                }
                if (!parsingStack.isEmpty()) {
                    currentStackElem = parsingStack.pop();
                    if (!functions.contains(currentStackElem)) {
                        if (!foundContent && !isANumber(previousContent)) {
                            throw new IllegalArgumentException("No content inside the brackets");
                        }
                        parsingStack.add(currentStackElem);
                    }
                    else {
                        transformedString.add(currentStackElem);
                    }
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
                else if (isANumber(previousContent) || !currentFunction.isEmpty() || enteringTheNumber || previousContent.equals(")") || previousContent.equals("closingModule")) {
                    if (enteringTheNumber) {
                        if (currentNumber.charAt(currentNumber.length() - 1) == '.') {
                            throw new  IllegalArgumentException("No digits after decimal sign in the number");
                        }
                        transformedString.add(currentNumber);
                        previousContent = currentNumber;
                        currentNumber = "";
                        enteringTheNumber = false;
                    }
                    else if (!currentFunction.isEmpty()) {
                        if (currentFunction.length() == 1) {
                            Scanner scanner = new Scanner(System.in);
                            System.out.println("Enter the value for " + currentFunction + ':');
                            currentNumber = scanner.nextLine();
                            if (!isANumber(currentNumber)) {
                                throw new IllegalArgumentException("Entered data is not a number");
                            }
                            transformedString.add(currentNumber);
                            previousContent = currentNumber;
                            currentNumber = "";
                            currentFunction = "";
                        }
                    }
                    String currentStackElem = parsingStack.pop();
                    while (!currentStackElem.equals("|")) {
                        foundContent = true;
                        transformedString.add(currentStackElem);
                        currentStackElem = parsingStack.pop();
                    }
                    if (!functions.contains(currentStackElem)) {
                        if (!foundContent && !isANumber(previousContent)) {
                            throw new IllegalArgumentException("No content inside the module");
                        }
                    }
                    transformedString.add("|");
                    foundContent = false;
                    previousContent = "closingModule";
                }
            }
            else if (currentSymbol == '!') {
                if (enteringTheNumber) {
                    if (currentNumber.charAt(currentNumber.length() - 1) == '.') {
                        throw new  IllegalArgumentException("No digits after decimal sign in the number");
                    }
                    transformedString.add(currentNumber);
                    previousContent = currentNumber;
                    currentNumber = "";
                    enteringTheNumber = false;
                }
                if (previousContent.equals(")") || previousContent.equals("closingModule") || isANumber(previousContent)) {
                    parsingStack.add(String.valueOf(currentSymbol));
                }
                else {
                    throw new IllegalArgumentException("Invalid usage of factorial operation");
                }
            }
            else if (currentSymbol == '+' || currentSymbol == '-' || currentSymbol == '*' || currentSymbol == '/' ||
                    currentSymbol == '^') {
                if (enteringTheLastSymbol) {
                    throw new IllegalArgumentException(
                            "Operation sign without a variable at the end of the expression");
                }
                if (currentSymbol == '^') {
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
                if (currentSymbol == '-') {
                    if (Character.isDigit(inputString.charAt(i + 1))) {
                        currentNumber += currentSymbol;
                        enteringTheNumber = true;
                    }
                }
                if (!enteringTheNumber) {
                    if (parsingStack.isEmpty()) {
                        parsingStack.add(String.valueOf(currentSymbol));
                    }
                    else {
                        String currentStackElem = parsingStack.pop();
                        while (operationsPrecedence.getOrDefault(currentStackElem, -1) >=
                                operationsPrecedence.getOrDefault(String.valueOf(currentSymbol), -1) &&
                                !parsingStack.isEmpty()) {
                            transformedString.add(currentStackElem);
                            currentStackElem = parsingStack.pop();
                        }
                        if (operationsPrecedence.getOrDefault(currentStackElem, -1) >=
                                operationsPrecedence.getOrDefault(String.valueOf(currentSymbol), -1)) {
                            transformedString.add(currentStackElem);
                            parsingStack.add(String.valueOf(currentSymbol));
                        }
                        else {
                            parsingStack.add(currentStackElem);
                            parsingStack.add(String.valueOf(currentSymbol));
                        }
                    }
                    previousContent = String.valueOf(currentSymbol);
                }
                else {
                    previousContent = currentNumber;
                }
            }
            else {
                throw new IllegalArgumentException("Invalid form of the expression");
            }
        }
        while (!parsingStack.isEmpty()) {
            transformedString.add(parsingStack.pop());
        }
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
                        if (calculatingStack.isEmpty()) {
                            throw new IllegalArgumentException("Invalid form of the expression");
                        }
                        calculatingStack.add(calculatingStack.pop() + currentNumber);
                    }
                    case "-" -> {
                        currentNumber = calculatingStack.pop();
                        if (calculatingStack.isEmpty()) {
                            throw new IllegalArgumentException("Invalid form of the expression");
                        }
                        calculatingStack.add(calculatingStack.pop() - currentNumber);
                    }
                    case "*" -> {
                        currentNumber = calculatingStack.pop();
                        if (calculatingStack.isEmpty()) {
                            throw new IllegalArgumentException("Invalid form of the expression");
                        }
                        calculatingStack.add(calculatingStack.pop() * currentNumber);
                    }
                    case "/" -> {
                        currentNumber = calculatingStack.pop();
                        if (calculatingStack.isEmpty()) {
                            throw new IllegalArgumentException("Invalid form of the expression");
                        }
                        if (currentNumber == 0) {
                            throw new ArithmeticException("Division by zero is not allowed");
                        }
                        calculatingStack.add(calculatingStack.pop() / currentNumber);
                    }
                    case "|" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(Math.abs(currentNumber));
                    }
                    case "^" -> {
                        currentNumber = calculatingStack.pop();
                        if (calculatingStack.isEmpty()) {
                            throw new IllegalArgumentException("Invalid form of the expression");
                        }
                        calculatingStack.add(Math.pow(calculatingStack.pop(), currentNumber));
                    }
                    case "!" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(factorial(currentNumber));
                    }
                    case "sin" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(Math.sin(currentNumber));
                    }
                    case "cos" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(Math.cos(currentNumber));
                    }
                    case "tg" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(Math.tan(currentNumber));
                    }
                    case "ctg" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(Math.tan(Math.toRadians(currentNumber)));
                    }
                    case "exp" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(Math.exp(currentNumber));
                    }
                    case "ln" -> {
                        currentNumber = calculatingStack.pop();
                        calculatingStack.add(Math.log(currentNumber));
                    }
                    default -> {
                        throw new IllegalArgumentException("Invalid bracket placement in the expression");
                    }
                }
            }
        }
        result = calculatingStack.pop();
    }

    public double getResult() {
        solveTheExpression();
        return result;
    }

    public void changeTheExpression(String object) {
        inputString = object;
        transformedString.clear();
        parsingStack.clear();
        calculatingStack.clear();
    }

    @Override
    public String toString() {
        String resultString = "";
        resultString += result;
        return resultString;
    }
}
