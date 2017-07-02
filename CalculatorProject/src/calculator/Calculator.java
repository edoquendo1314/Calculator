package calculator;
import java.text.DecimalFormat;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

	static HashMap<String,Integer> precedenceMap;	
	
	// initialize variables here
	
	static {
		precedenceMap = new HashMap();
		precedenceMap.put("sin", 5);
		precedenceMap.put("cos", 5);
		precedenceMap.put("tan", 5);
		precedenceMap.put("sqrt", 4);
		precedenceMap.put("^", 4);
		precedenceMap.put("*", 3);
		precedenceMap.put("/", 3);
		precedenceMap.put("+", 2);
		precedenceMap.put("-", 2);
	}
	
	public static int countBrackets(String[] array){
		int count = 0;
		for(String s: array){
			if(s.equals("(") || s.equals(")")){
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * 
	 * @param String original
	 * @param String toInsert
	 * @param int location
	 * @return String
	 * 
	 * Inserts the string toInsert at location.
	 * Example: String original = "string"
	 *          int location = 0  ("s")
	 *          String toInsert = "this is a "
	 *          
	 *          insertString(original, toInsert, location) returns "this is a string"
	 */
	public static String insertString(String original, String toInsert, int location){
		String result = "";
		
		if(location >= original.length()){
			return original;
		}
		
		result = original.substring(0, location) + toInsert + original.substring(location, original.length());
		
		return result;
	}
	
	// need to insert white spaces before running this function
	public static String insertMultiplication(String equation){
		// case 1  (a)x
		// case 2   a(x) = a * (x)
		// case 3  (a)(x) = (a) * (x)
		
		int i = 0;
		while(i < equation.length()){
			
			i = nextTokenLocation(equation, i);
			if(i >= equation.length()){
				return equation;
			}
			
			String currentToken = currentToken(equation, i);
			
			// case 1 and 3
			if(equation.charAt(i) == ')'){
				if(i + 1 < equation.length()){
					int nextTokenLocation = nextTokenLocation(equation, i+1);
					
					String nextToken = nextToken(equation, i+1);
					
					if(nextTokenLocation < equation.length()){
						if(equation.charAt(nextTokenLocation) == '(' || isNumber(nextToken) || is1ArgFunction(nextToken)){
							//equation = equation.substring(0, i+1) + " *" + equation.substring(i+1, equation.length());
							equation = insertString(equation, " *", i+1);
							
							i += 2 + nextToken.length();
						}
					}
				}
				
			}else if(isNumber(currentToken) && i + currentToken.length() < equation.length()){
				String nextToken = nextToken(equation, i + currentToken.length());
				if(nextToken.equals("(") || is1ArgFunction(nextToken)){
					//equation = equation.substring(0, i+currentToken.length()) + " *" + equation.substring(i+currentToken.length(), equation.length());
					equation = insertString(equation, " *", i+currentToken.length());
					i += 2 + nextToken.length();
				}
			}
			
			i++;
			
		}
		
		return equation;
	}
	
	public static int nextTokenLocation(String equation, int location){
		while(location < equation.length() && equation.charAt(location) == ' '){
			location++;
		}
		return location;
	}
	
	public static String nextToken(String equation, int location){
		String token = "";
		
		int i = nextTokenLocation(equation, location);
		
		while(i < equation.length() && equation.charAt(i) != ' '){
			token += equation.charAt(i);
			i++;
		}
		
		return token;
	}
	
	public static String currentToken(String equation, int currentLocation){
		String token = "";
		
		while(currentLocation < equation.length() && equation.charAt(currentLocation) != ' '){
			token += equation.charAt(currentLocation);
			currentLocation++;
		}
		
		return token;		
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
			
			String currentString = equation.substring(i, i+1);
			
			if(!isOperator(currentString) && !isBracket(currentString) && !currentString.equals(" ") && !isNumber(currentString)){
				String token = "";
				int j = i;
				
				String tmpString = equation.substring(j, j+1);
				while(!isOperator(tmpString) && !isBracket(tmpString) && !tmpString.equals(" ") && j < equation.length()){
					token += tmpString;
					j++;
					tmpString = equation.substring(j, j+1);
				}
				
				equation = insertString(equation, " ", i);
				equation = insertString(equation, " ", i + 1 + token.length());
				
				i += 1 + token.length();
			}
			
			i++;
		}
		
		return equation;
	}
	
	// white spaces and multiplication symbols must be inserted before running this function
	public static String[] infixToPostfix(String string){
		string = string.trim(); // remove leading and trailing white spaces
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
	
	public static double calculate(String equation){
		equation = insertWhiteSpaces(equation);
		equation = insertMultiplication(equation);
		String[] array = infixToPostfix(equation);
		double result = evaluatePostfix(array);
		return result;
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
		s = s.toLowerCase();
		try{
			int precedence = precedenceMap.get(s);
			return precedence;
		}catch(NullPointerException e){
			return 0;
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
		case "sin": return roundDouble(Math.sin(Math.toRadians(n)));
		case "tan": return roundDouble(Math.tan(Math.toRadians(n)));
		case "cos": return roundDouble(Math.cos(Math.toRadians(n)));
		case "sqrt": return roundDouble(Math.sqrt(n));
		default: return 0.0;
		}
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
			
//			eq = insertWhiteSpaces(eq);
//			eq = insertMultiplication(eq);
//			System.out.println("Prefix: " + eq);
//			
//			String[] array = infixToPostfix(eq);
//			
//			System.out.print("Postfix: ");
//			for(String s: array){
//				System.out.print(s + " ");
//			}

			System.out.println(" = " + calculate(eq));	
		}
		

	}
	
}
