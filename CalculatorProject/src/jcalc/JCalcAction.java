package jcalc;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextField;

import calculator.Calculator;
import calculator.StrManip;

public class JCalcAction extends AbstractAction{

	JTextField displayArea;
	String input;
	public static String lastInput;
	
	public JCalcAction(JTextField displayArea, String input) {
		// TODO Auto-generated constructor stub
		
		super();
		this.displayArea = displayArea;
		this.input = input;
		lastInput = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// System.out.println(button.getText());
		String displayedText = displayArea.getText();		
		
		if (!input.equals("ENTER")) {
			// if something other than "ENTER" was pressed

			if (lastInput != null && lastInput.equals("ENTER") && (StrManip.isNumber(input) || StrManip.is1ArgFunction(input))) {
				// if the last button pressed was an equal sign
				// display area is displaying the result of some expression evaluation
				// if the next button we press is a number, we are starting a new expression
				// and overwriting previous results
				
				if (input.equals("x")) {
					displayedText = "*";
				}else if(StrManip.is1ArgFunction(input)){
					displayedText = input + "(";
				}else{
					displayedText = input;
				}

			} else {
				// either the last button was not an equal sign,
				// or the button we are pressing now is not a number
				// or both
				// in any case, we are appending the expression or number in display area
				
				if (displayedText.equals(" ")) {
					if(StrManip.is1ArgFunction(input)){
						displayedText = input + "(";
					}else if(StrManip.isNumber(input)){
						displayedText = input;						
					}else if(!input.equals("-")){
						displayedText = "0" + convertInput(input);
					}else{
						displayedText = input;
					}
				} else if (input.equals("x")) {
					displayedText += "*";
				}else if(StrManip.is1ArgFunction(input)){
					displayedText += input + "(";
				} else {
					displayedText += input;
				}
			}
		} else {
			// equal was pressed, evaluate current expression

			if (lastInput != null && !lastInput.equals("ENTER")) {
				// makes sure we aren't double pressing the "=" button
				String str = displayedText.trim();
				str = Calculator.calculate(str) + "";

				System.out.println(displayArea.getText());
				displayedText = str;
			}
		}

		lastInput = input;
		displayArea.setText(displayedText);		
		
	}
	
	public static String convertInput(String input){
		if(input.equals("x")){
			input = "*";
		}
		return input;
	}
	
}
