package com.company;

import java.util.*;
/*
    В консоль вводится арифметическое вражение, содержащие целые числа, скобки и операции:
    *, /, +, -
    Считаю, что числа, скобки, операции отделены друг от друга пробелом. Не проверяю входную
    строку на корректность, считаю, что входная строка хорошая.
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение: ");
        String expr = in.nextLine();
        expr += " ";
        String[] symbols = expr.split(" ");   //разделяем по пробелам
        String[] to_pol = reverse_polish(symbols);
        int my_res = calculation(to_pol);
        System.out.println(my_res);
        in.close();
    }

     public static String[] reverse_polish(String[] symbols) {
        Stack<String> stack = new Stack<>();        //стек операций
        String[] ans = new String[symbols.length];  //выходной массив
        int j = 0;
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i].equals("(")) {              //если "(", то отправляем в стек
                stack.push(symbols[i]);
            } else if (symbols[i].equals(")")){        //если ")", то вынимаем из стека,
                while(!(stack.peek()).equals("(")){ //пока не встретим "("
                    ans[j] = stack.peek();          //отправляем в выходной массив
                    stack.pop();
                    j++;
                }
                stack.pop();                        //на верхушке стека - "(", удаляем
            } else if (is_oper(symbols[i])){        //если встертили опреацию (*, /, +, -)
                if (stack.empty() || oper_rank(symbols[i]) > oper_rank(stack.peek())){
                    stack.push(symbols[i]);    //если стек пуст или приоритет операции выше той, что
                } else {                       //на верхушке стека, то отправляем в стек
                    ans[j] = stack.peek();     //иначе, операцию из стека отправляем в выходной массив,
                    stack.pop();               //а текущую опреацию отправляем в стек
                    stack.push(symbols[i]);
                    j++;
                }
            }
            else{                             //иначе, попалось целое число, отправляем сразу
                ans[j] = symbols[i];          // в выходной массив. (Входная строка хорошая)
                j++;
            }
        }
        while (!stack.empty()){             //если в стеке, что то осталось переводим в выходной
            ans[j] = stack.peek();          //массив
            stack.pop();
            j++;
        }
        return ans;
    }

    public static int oper_rank(String oper){   //приоритет * и / самый высокий, у +, - меньше
        int rank = 0;                           //у ( - самый маленький
        if(oper.equals("*") || oper.equals("/")){
            rank = 2;
        } else if (oper.equals("+") || oper.equals("-")){
            rank = 1;
        }
        return rank;
    }

    public static boolean is_oper(String str){      // проверка тго, что строка - операция
        String[] oper = new String[]{"*", "/", "+", "-"};
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < oper.length; i++) {
            if (str.equals(oper[i])) return true;
        }
        return false;
    }

    public static boolean is_number(String str) {       //проверка того, что строка - целое число
        if (str == null || str.isEmpty()) return false; //правда не учитывается случаи вроде "01222".
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    public static int simple_calc(int x, int y, String oper){   //два числа и операция - вычисляем
        if (oper.equals("*")){
            return y * x;
        } else if (oper.equals("/")) {
            return y / x;
        } else if (oper.equals("+")) {
            return y + x;
        } else {
            return y - x;
        }
    }

    public static int calculation(String[] symbols){    //вычисляем значение выражения, после
        Stack<Integer> stack = new Stack<>();           //записи в польской нотации
        for(int i = 0; i < symbols.length; i++){
            if (is_oper(symbols[i])){           //если число - отправляем в стек
                int tmp = stack.pop();          //если операция, то берём из стека два первых
                int res = simple_calc(tmp, stack.pop(), symbols[i]);   //числа и считаем для них
                stack.push(res);                //отправляем опять в стек
                //System.out.println("res = " + res );
            }
            else if (is_number(symbols[i])) {
                int tmp = Integer.parseInt(symbols[i]);
                stack.push(tmp);
            }
        }
        return stack.peek();    //получили ответ
    }
}
