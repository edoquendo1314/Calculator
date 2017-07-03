package calculator;

import java.util.HashMap;

public class Operator {

	private int precedence;
	private int numArgs;
	private String name;
	
	public Operator(String name, int numArgs, int precedence) {
		this.name = name;
		this.numArgs = numArgs;
		this.precedence = precedence;
	}

	public int getPrecedence() {
		return precedence;
	}

	public int getNumArgs() {
		return numArgs;
	}

	public String getName() {
		return name;
	}

}
