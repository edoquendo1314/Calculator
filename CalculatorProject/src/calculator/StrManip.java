package calculator;

public class StrManip {

	/**
	 * String manipulation functions
	 **/
	
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
	
	public StrManip() {
		// TODO Auto-generated constructor stub
	}

}
