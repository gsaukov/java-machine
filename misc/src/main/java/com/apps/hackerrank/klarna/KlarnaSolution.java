package com.apps.hackerrank.klarna;

import java.util.Stack;

public class KlarnaSolution {

    public static String maskify(String creditCardNumber){

        char[] res = creditCardNumber.toCharArray();
        for (int i = creditCardNumber.length() - 5; i > 0 ; i--) {
            if(Character.isDigit(creditCardNumber.charAt(i))){
                res[i]='#';
            }
        }
        return String.valueOf(res);

    }

//https://medium.com/@alonso.delarte/reverse-polish-notation-is-easy-with-test-driven-development-670dfc7bb47c
    public static void main(String[] args)  {
        System.out.println(evaluate("1 3 -"));
    }

    public static String numberToOrdinal( Integer i ) {
        int ord100 = i % 100;
        int ord10 = i % 10;
        if(i==0){
            return Integer.toString(i);
        }else if (ord10 == 1 && ord100 != 11){
            return i + "st";
        } else if (ord10 == 2 && ord100 != 12){
            return i + "nd";
        } else if (ord10 == 3 && ord100 != 13){
            return i + "rd";
        } else {
            return i + "th";
        }
    }


    public static double evaluate(String expr) {
        //Ok it is not really my solution and idea behind, i took it originally from link below, However i polished it till it works without issues.
        //https://medium.com/@alonso.delarte/reverse-polish-notation-is-easy-with-test-driven-development-670dfc7bb47c
        if(expr.length() == 0){
            return 0;
        }

        Stack<Double> stack = new Stack();
        String[] elems = expr.split(" ");
        boolean numParseFlag;
        char currChar;
        double operand1, operand2, parsedNum;
        double currVal = 0.0;
        for (String elem : elems) {
            numParseFlag = true;
            if (elem.length() == 1) {
                numParseFlag = false;
                currChar = elem.charAt(0);
                switch (currChar) {
                    case '+':
                        operand1 = stack.pop();
                        operand2 = stack.pop();
                        currVal = operand2 + operand1;
                        stack.push(currVal);
                        break;
                    case '-':
                        operand1 = stack.pop();
                        operand2 = stack.pop();
                        currVal = operand2 - operand1;
                        stack.push(currVal);
                        break;
                    case '*':
                        operand1 = stack.pop();
                        operand2 = stack.pop();
                        currVal = operand2 * operand1;
                        stack.push(currVal);
                        break;
                    case '/':
                        operand1 = stack.pop();
                        operand2 = stack.pop();
                        currVal = operand2 / operand1;
                        stack.push(currVal);
                        break;
                    default:
                        numParseFlag = true;
                }
            }
            if (numParseFlag) {
                try {
                    parsedNum = Double.parseDouble(elem);
                    stack.push(parsedNum);
                } catch (NumberFormatException nfe) {
                    throw nfe;
                }
            }
        }
        if (currVal == 0){
            return stack.pop();
        } else {
            return currVal;
        }
    }
}
