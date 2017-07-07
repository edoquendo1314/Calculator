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
	
	// add any new operators/functions to the operatorMap in the static block below
	// Operator constructor takes: String name, int numArguments, int precedence
	static {
		operatorMap = new HashMap<String, Operator>();
		
		// single arg functions
		operatorMap.put("sin", new Operator("sin", 1, 5));
		operatorMap.put("cos", new Operator("cos", 1, 5));
		operatorMap.put("tan", new Operator("tan", 1, 5));
		operatorMap.put("cot", new Operator("cot", 1, 5));
		operatorMap.put("sec", new Operator("sec", 1, 5));
		operatorMap.put("csc", new Operator("csc", 1, 5));
		
		operatorMap.put("sqrt", new Operator("sqrt", 1, 4));
		operatorMap.put("abs", new Operator("abs", 1, 5));
		operatorMap.put("ln", new Operator("ln", 1, 5));
				
		
		// 2 arg functions
		operatorMap.put("log", new Operator("log", 2, 5));
		
		// binary operators
		operatorMap.put("^", new Operator("^", 2, 4));
		operatorMap.put("*", new Operator("*", 2, 3));
		operatorMap.put("/", new Operator("/", 2, 3));
		operatorMap.put("+", new Operator("/", 2, 2));
		operatorMap.put("-", new Operator("/", 2, 2));
	}

	/** performs the appropriate operation
	*   eg: operate("*", "2", "3") will return 6 (as a double)
	**/
	public static double operate(String operator, String arg1, String arg2){
		
		double a = Double.parseDouble(arg1);
		double b = Double.parseDouble(arg2);
		
		operator = operator.toLowerCase();
		
		switch(operator){
			case "+": return a+b;
			case "-": return a-b;
			case "*": return a*b;
			case "/": return a/b;
			case "^": return Math.pow(a, b);
			case "log": return logN(a, b);
			default: return -1f;
		}
	}		

	/** runs the appropriate function
	 ** eg runFunction("sin", 90) will return 1.0 (as a double)
	 **/
	public static double runFunction(String func, String operand){
		double n = Double.parseDouble(operand);
		
		switch(func){
		case "sin": return roundDouble(Math.sin(Math.toRadians(n)));
		case "tan": return roundDouble(Math.tan(Math.toRadians(n)));
		case "cos": return roundDouble(Math.cos(Math.toRadians(n)));
		case "sqrt": return roundDouble(Math.sqrt(n));
		case "abs": return roundDouble(Math.abs(n));
		case "ln": return roundDouble(Math.log(n));
		case "cot": 
			if(runFunction("tan", operand) == 0){
				System.out.println("Cannot divide by 0");
				return 0;
			}
			return roundDouble(1.0/runFunction("tan", operand));
		case "sec": 
			if(runFunction("cos", operand) == 0){
				System.out.println("Cannot divide by 0");
				return 0;
			}
			return roundDouble(1.0/runFunction("cos", operand));
		case "csc": 
			if(runFunction("sin", operand) == 0){
				System.out.println("Cannot divide by 0");
				return 0;
			}
			return roundDouble(1.0/runFunction("sin", operand));
		default: return 0.0;
		}			
	}		
	
	/** returns the precedence of the operator represented by String s
	**/
	public static int getPrecedence(String s){
		s = s.toLowerCase();
		try{
			int precedence = operatorMap.get(s).getPrecedence();
			return precedence;
		}catch(NullPointerException e){
			return 0;
		}
	}	

	public static double logN(double base, double n){
		double result = Math.log(n) / Math.log(base);
		return result;
	}
	
	/** converts user entered equation into postfix notation
	 ** eg:  "3+4*6" becomes { 3, 4, 6, *, + } in array form
	 ** white spaces and multiplication symbols must be inserted before running this function
	 ** otherwise it will not work as intended
	 **/
	public static String[] infixToPostfix(String string){
		string = string.trim(); // remove leading and trailing white spaces
		//System.out.println(string);
		String[] array = string.split("\\s+");
		String[] outputArray = new String[array.length - StrManip.countSpecialChars(array)];
		LinkedList<String> fifo = new LinkedList<String>();
		Stack<String> operatorStack = new Stack<String>();
		
		int i = 0;
		
		while(i < array.length){
			if(StrManip.isNumber(array[i])){
				fifo.add(array[i]);				
			}else if(StrManip.isOperator(array[i]) || operatorMap.containsKey(array[i])){
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
	
	/**
	 * Given an equation (eg, "3+4(6)")
	 * 
	 * It will insert white spaces and multiplication symbols as necessary
	 * "3+4(6)" becomes "3 + 4 * (6)"
	 * Then it will evaluate the equation and return a double
	 * calculate("3+4(6)") will return 27.0 
	 */
	public static double calculate(String equation){
		equation = StrManip.insertWhiteSpaces(equation);
		equation = StrManip.insertMultiplication(equation);
//		System.out.println(equation);
		String[] array = infixToPostfix(equation);
		double result = evaluatePostfix(array);
		return result;
	}
	
	/**
	 * Given a string array of an equation in postfix form
	 * (such as the one from infixToPostfix function)
	 * it will return a double that evaluates the equation
	 * 
	 * (see the calculate function above for the entire process)
	 */
	public static double evaluatePostfix(String[] array){
		
		Stack<String> stack = new Stack<String>();
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
	
	/**
	 *  This function is to avoid rounding errors from floating point arithmetic
	 *  It works by adding 1 to n, rounding it to 15 places, then subtracting 1
	 *  This was primarily for trig functions like sin(180) giving weird values instead of 0
	 */
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
			//testFunction(eq);
			
			//System.out.println(StrManip.insertWhiteSpaces(eq));
			
			System.out.println(" = " + calculate(eq));
			//testFunction(eq);
			
			//System.out.println(StrManip.insertWhiteSpaces(eq));
			
			
		}
	}
	
	// using this function to print out different things and test functions
	// helpful for keeping the main function clean
	// we can delete this once we're done
	public static void testFunction(String eq){
		eq = StrManip.insertWhiteSpaces(eq);
		eq = StrManip.insertMultiplication(eq);
		
		String[] array = infixToPostfix(eq);
		
		for(String s: array){
			System.out.print(s + " ");
		}
		System.out.println();	
		//System.out.println(calculate(eq));
	}
	
}
