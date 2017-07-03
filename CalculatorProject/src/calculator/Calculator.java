package calculator;
import java.text.DecimalFormat;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

	static HashMap<String, Operator> operatorMap;	
	
	
	/*
	 * Edit functions below to add more operators/functions to parse and evaluate
	 */
	
	static {
		operatorMap = new HashMap();
		
		// single arg functions
		operatorMap.put("sin", new Operator("sin", 1, 5));
		operatorMap.put("cos", new Operator("cos", 1, 5));
		operatorMap.put("tan", new Operator("tan", 1, 5));
		operatorMap.put("sqrt", new Operator("sqrt", 1, 4));
		operatorMap.put("abs", new Operator("abs", 1, 5));
		
		// 2 arg functions
		operatorMap.put("log", new Operator("log", 2, 5));
		
		// binary operators
		operatorMap.put("^", new Operator("^", 2, 4));
		operatorMap.put("*", new Operator("*", 2, 3));
		operatorMap.put("/", new Operator("/", 2, 3));
		operatorMap.put("+", new Operator("/", 2, 2));
		operatorMap.put("-", new Operator("/", 2, 2));
	}
	
	public static int getPrecedence(String s){
		s = s.toLowerCase();
		try{
			int precedence = operatorMap.get(s).getPrecedence();
			return precedence;
		}catch(NullPointerException e){
			return 0;
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

	public static double runFunction(String func, String operand){
		double n = Double.parseDouble(operand);

		switch(func){
		case "sin": return roundDouble(Math.sin(Math.toRadians(n)));
		case "tan": return roundDouble(Math.tan(Math.toRadians(n)));
		case "cos": return roundDouble(Math.cos(Math.toRadians(n)));
		case "sqrt": return roundDouble(Math.sqrt(n));
		case "abs": return roundDouble(Math.abs(n));
		default: return 0.0;
		}
	}	
	// white spaces and multiplication symbols must be inserted before running this function
	public static String[] infixToPostfix(String string){
		string = string.trim(); // remove leading and trailing white spaces
		String[] array = string.split("\\s+");
		String[] outputArray = new String[array.length - StrManip.countBrackets(array)];
		LinkedList<String> fifo = new LinkedList();
		Stack<String> operatorStack = new Stack();
		
		int i = 0;
		
		while(i < array.length){
			if(StrManip.isNumber(array[i])){
				fifo.add(array[i]);
			}else if(StrManip.isOperator(array[i]) || StrManip.is1ArgFunction(array[i])){
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
	
	public static double calculate(String equation){
		equation = StrManip.insertWhiteSpaces(equation);
		equation = StrManip.insertMultiplication(equation);
		String[] array = infixToPostfix(equation);
		double result = evaluatePostfix(array);
		return result;
	}
	
	public static double evaluatePostfix(String[] array){
		
		Stack<String> stack = new Stack();
		LinkedList<String> fifo = new LinkedList();
		double result = 0;
		
		int i = 0;
		
		while(i < array.length){
			if(StrManip.isNumber(array[i])){
				stack.push(array[i]);
			}else if(StrManip.is1ArgFunction(array[i])){
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
	
	public static double roundDouble(double n){
		return round15(n+1) - 1;
	}
	
	public static double round15(double x){
		DecimalFormat twoDForm = new DecimalFormat("0.##############E0");
		String str = twoDForm.format(x);
		return Double.valueOf(str);
	}
	
	
	public static void main(String[] args){
		
		Scanner keyboard = new Scanner(System.in);
		String eq = "";
		
		while(!eq.equals("exit")){
			eq = keyboard.nextLine();
			
			System.out.println(" = " + calculate(eq));	
		}
		

	}
	
}
