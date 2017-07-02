package calculator;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

	public static int countBrackets(String[] array){
		int count = 0;
		for(String s: array){
			if(s.equals("(") || s.equals(")")){
				count++;
			}
		}
		
		return count;
	}
	
	public static String insertWhiteSpaces(String equation){
		
		int i = 0;
		while(i < equation.length()){
			if(isOperator(equation.substring(i, i+1)) || isBracket(equation.substring(i, i+1))){
				equation = equation.substring(0, i) + " " 
						   + equation.substring(i, i+1) + " " 
						   + equation.substring(i+1, equation.length());
				
				// inserting a space before and after operator
				// equation.charAt(i) is now a blank space
				// equation.charAt(i+1) is the operator
				// equation.charAt(i+2) is the blank space after operator
				// so we increment i by 2 in here, then by 1 again outside the if
				// to get to the next number in the equation
				
				i += 2;
			}
			
			i++;
		}
		
		return equation;
	}
	
	public static String[] infixToPostfix(String string){
		string = insertWhiteSpaces(string);
		String[] array = string.split("\\s+");
		String[] outputArray = new String[array.length - countBrackets(array)];
		
		LinkedList<String> fifo = new LinkedList();
		Stack<String> operatorStack = new Stack();
		
		int i = 0;
		
		while(i < array.length){
			if(isNumber(array[i])){
				fifo.add(array[i]);
			}else if(isOperator(array[i]) || is1ArgFunction(array[i])){
				try{
					while(!operatorStack.isEmpty() && 
							getPrecedence(operatorStack.peek()) >= getPrecedence(array[i])){
						fifo.add(operatorStack.pop());
					}
				}catch(EmptyStackException e){
					
				}
				
				operatorStack.push(array[i]);
			}else if(array[i].equals("(")){
				operatorStack.push(array[i]);
			}else if(array[i].equals(")")){
				while(!operatorStack.isEmpty() &&
						!operatorStack.peek().equals("(")){
					fifo.add(operatorStack.pop());
				}
				if(!operatorStack.isEmpty()) operatorStack.pop();
			}
			
			i++;
		}
		
		try{
			while(!operatorStack.isEmpty()){
				fifo.add(operatorStack.pop());
			}
			
		}catch(EmptyStackException e){
			
		}
		
		for(int j = 0; j < outputArray.length; j++){
			if(!fifo.isEmpty())
				outputArray[j] = fifo.removeFirst();
		}
		
		return outputArray;
	}
	
	public static boolean isNumber(String string){
		return string.matches("\\d+(\\.\\d+)?");
	}
	
	public static boolean isBracket(String string){
		return string.equals("(") || string.equals(")");
	}
	
	public static boolean isOperator(String string){
		boolean bool = string.matches("\\+|-|\\*|/|\\^");
		
		return bool;
	}
	
	public static boolean is1ArgFunction(String string){
		
		switch(string){
		case "sin":
		case "tan":
		case "cos":
		case "sqrt": return true;
		default: return false;
		}
		
	}
	
	public static double operate(String operator, String arg1, String arg2){
		double result = 0f;
		
		double a = Double.parseDouble(arg1);
		double b = Double.parseDouble(arg2);
		
		operator = operator.toLowerCase();
		
		switch(operator){
			case "+": return a+b;
			case "-": return a-b;
			case "*": return a*b;
			case "/": return a/b;
			case "^": return Math.pow(a, b);
			default: return -1f;
		}
	}
	
	public static int getPrecedence(String s){
		switch(s){
		case "sin": return 5;
		case "^": return 4;
		case "*":
		case "/": return 3;
		case "+":
		case "-": return 2;
		default: return 0;
		}
	}
	
	public static double evaluatePostfix(String[] array){
		
		Stack<String> stack = new Stack();
		LinkedList<String> fifo = new LinkedList();
		double result = 0;
		
		int i = 0;
		
		while(i < array.length){
			if(isNumber(array[i])){
				stack.push(array[i]);
			}else if(is1ArgFunction(array[i])){
				try{
					String operand = stack.pop();
					double curResult = runFunction(array[i], operand);
					
					stack.push(""+curResult);
					result = curResult;
				}catch(Exception e){
					System.out.println("something wrong with function processing");
				}
			}
			else{
				try{
					String operand2 = stack.pop();
					String operand1 = stack.pop();
					
					double curResult = operate(array[i], operand1, operand2);
					
					stack.push(""+curResult);
					result = curResult;
					
				}catch(EmptyStackException e){
					System.out.println("something wrong with equation");
				}
			}
			
			i++;
		}
		
		return result;
	}
	
	public static double runFunction(String func, String operand){
		double n = Double.parseDouble(operand);
		
		
		switch(func){
		case "sin": return Math.sin(Math.toRadians(n));
		case "tan": return Math.tan(Math.toRadians(n));
		case "cos": return Math.cos(Math.toRadians(n));
		case "sqrt": return Math.sqrt(n);
		default: return 0.0;
		}
	}
	
	
	public Calculator() {
		// TODO Auto-generated constructor stub
	
	}

	public static void main(String[] args){
		
		Scanner keyboard = new Scanner(System.in);
		
		String eq = keyboard.nextLine();

		eq = insertWhiteSpaces(eq);
		System.out.println("Prefix: " + eq);
		
		String[] array = infixToPostfix(eq);
		
		System.out.print("Postfix: ");
		for(String s: array){
			System.out.print(s + " ");
		}
		
		System.out.println(" = " + evaluatePostfix(array));
		
	}
	
}
