package calculator;

public class StrManip {

	/**
	 * String manipulation functions
	 **/
	
	/**
	 * 
	 * @param original
	 * @param toInsert
	 * @param location
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
	
	/**
	 * @params equation
	 * @return String
	 * Inserts multiplication symbol into equation that has implicit multiplications.
	 * Eg: (a)(b) becomes (a)*(b)
	 * 
	 * Equation must have gone through the insertWhiteSpaces function before being passed to this function
	 **/
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
	
	/**
	 * 
	 * @param equation
	 * @return
	 * 
	 * Inserts white spaces before and after each operator/function/ or special character in the equation.
	 * This is necessary for easier tokenization of the equation. 
	 * Multiple white spaces are not a problem as the String.split method tokenizes correctly
	 * regardless of the number of white spaces
	 **/
	public static String insertWhiteSpaces(String equation){
		
		int i = 0;
		String prevToken = null;
		while(i < equation.length()){
			String currentString = equation.substring(i, i+1);
			if(isOperator(currentString) || isSpecialChar(equation.charAt(i))){
				equation = equation.substring(0, i) + " " 
						   + equation.substring(i, i+1) + " " 
						   + equation.substring(i+1, equation.length());
				
				// inserting a space before and after operator
				// equation.charAt(i) is now a blank space
				// equation.charAt(i+1) is the operator
				// equation.charAt(i+2) is the blank space after operator
				// so we increment i by 2 in here, then by 1 again outside the if
				// to get to the next number in the equation
				
				if((prevToken == null || isOperator(prevToken)) && currentString.equals("-")){
					// i+1 is the minus sign
					i++;
					equation = equation.substring(0, i) + "( 0 "
							   + equation.substring(i, equation.length());
					i += 4;
					// updated i to still be pointing to minus sign
					
					int j = i+1;
					// move to next number
					while(j < equation.length() && equation.charAt(j) == ' '){
						j++;
						i++;
//						System.out.println("moving to next number");
					}
					// move past next number
					while(j < equation.length() && isNumber(equation.substring(j, j+1))){
						j++;
						i++;
//						System.out.println("moving past next number");
					}
					equation = equation.substring(0, j) + " ) " + equation.substring(j, equation.length());
					
					//System.out.println("got a negative number boys!");
				}
				
				i += 2;
			}			
			// insert white spaces around functions (ie: log, sin, cos, etc)
			else if(!isOperator(currentString) && !isSpecialChar(currentString) && !currentString.equals(" ") && !isNumber(currentString) && !currentString.equals(".")){
				String token = "";
				int j = i;
				
				String tmpString = equation.substring(j, j+1);
				while(!isOperator(tmpString) && !isSpecialChar(tmpString) && !tmpString.equals(" ") && j < equation.length()){
					token += tmpString;
					j++;
					tmpString = equation.substring(j, j+1);
				}
				
				equation = insertString(equation, " ", i);
				equation = insertString(equation, " ", i + 1 + token.length());
				
				i += 1 + token.length();
			}
			
			prevToken = currentString;
			i++;
		}
		
		return equation;
	}	
	
	
	/**
	 * 
	 * @param equation
	 * @param location
	 * @return
	 * 
	 * Helper function that returns the (int) location of the
	 * next token in the equation
	 * given the location right after the current token
	 */
	public static int nextTokenLocation(String equation, int location){
		while(location < equation.length() && equation.charAt(location) == ' '){
			location++;
		}
		return location;
	}

	/**
	 * 
	 * @param equation
	 * @param location
	 * @return 
	 * 
	 * Helper function that returns the (string) next token
	 * given the location immediately after the current location
	 */	
	public static String nextToken(String equation, int location){
		String token = "";
		
		int i = nextTokenLocation(equation, location);
		
		while(i < equation.length() && equation.charAt(i) != ' '){
			token += equation.charAt(i);
			i++;
		}
		
		return token;
	}
	
	/**
	 * 
	 * @param equation
	 * @param currentLocation
	 * @return
	 * 
	 * Helper function that returns the (String) current token,
	 * given the location of the first character of the token
	 */
	public static String currentToken(String equation, int currentLocation){
		String token = "";
		
		while(currentLocation < equation.length() && equation.charAt(currentLocation) != ' '){
			token += equation.charAt(currentLocation);
			currentLocation++;
		}
		
		return token;		
	}	
	

	/*
	 * Functions for checking string type (numbers, operators, brackets, functions)
	 */
	
	public static boolean isNumber(String string){
		return string.matches("\\d+(\\.\\d+)?");
	}
	
	public static boolean isSpecialChar(String string){
		return string.equals("(") || string.equals(")") || string.equals(",") || string.equals("x");
	}

	/**
	 * 
	 * @param c
	 * @return
	 * 
	 * Returns whether or not the given character is one of the following:
	 * 
	 * parentheses, comma, or the letter x
	 */
	public static boolean isSpecialChar(char c){
		return c == '(' || c == ')' || c == ',' || c == 'x';
	}	
	
	public static boolean isOperator(String string){
		boolean bool = string.matches("\\+|-|\\*|/|\\^");
		
		return bool;
	}

	public static boolean isOperator(char c){
		boolean bool = c == '+' || 
				       c == '-' ||
				       c == '*' ||
				       c == '/' ||
				       c == '^';
		
		return bool;
	}	
	
	public static boolean is1ArgFunction(String string){
		if(Calculator.operatorMap.containsKey(string.toLowerCase())){
			return (Calculator.operatorMap.get(string.toLowerCase()).getNumArgs() == 1);
		}
		
		return false;
	}
	
	public static boolean isFunction(String string){
		switch(string.toLowerCase()){
		
		case "+":
		case "-":
		case "*":
		case "/":
		case "^": return false;
		default: return true;
		}
	}
	
	/*
	 * Misc functions
	 */
	
	/**
	 * 
	 * @param array
	 * @return
	 * 
	 * Returns number of parentheses and commas
	 */
	public static int countSpecialChars(String[] array){
		int count = 0;
		for(String s: array){
			if(s.equals("(") || s.equals(")") || s.equals(",")){
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * 	
	 * @param equation
	 * @return
	 * 
	 * Turns the equation string into an array of tokens.
	 * Every part of the equation is turned into an element of the array,
	 * including commas and parentheses.
	 */
	public static String[] tokenize(String equation){
		equation = insertWhiteSpaces(equation);
		equation = equation.trim();
		
		String[] array = equation.split("\\s+");
		return array;
	}
	
	public StrManip() {
		// TODO Auto-generated constructor stub
	}
	
}
