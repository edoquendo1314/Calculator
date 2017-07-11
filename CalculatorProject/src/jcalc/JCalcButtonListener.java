package jcalc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import calculator.*;

public class JCalcButtonListener implements ActionListener {

	JCalc jcalc;
	private JButton lastButton;

	public JCalcButtonListener(JCalc jcalc) {
		this.jcalc = jcalc;
		lastButton = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		// System.out.println(button.getText());

		JTextField displayArea = jcalc.getDisplayArea();
		String displayedText = displayArea.getText();
		String buttonText = button.getText();
		
		if(buttonText.equals("clr")){
			
			displayedText = "";
			
		} else if (!buttonText.equals("=")) {
			// if something other than "=" was pressed

			if (lastButton != null && lastButton.getText().equals("=") && (StrManip.isNumber(buttonText) || StrManip.is1ArgFunction(buttonText))) {
				// if the last button pressed was an equal sign
				// display area is displaying the result of some expression evaluation
				// if the next button we press is a number, we are starting a new expression
				// and overwriting previous results
				
				if (buttonText.equals("x")) {
					displayedText = "*";
				}else if(StrManip.is1ArgFunction(buttonText)){
					displayedText = buttonText + "(";
				}else{
					displayedText = buttonText;
				}

			} else {
				// either the last button was not an equal sign,
				// or the button we are pressing now is not a number
				// or both
				// in any case, we are appending the expression or number in display area
				
				if (displayedText.equals(" ")) {
					if(StrManip.is1ArgFunction(buttonText)){
						displayedText = buttonText + "(";
					}else if(StrManip.isNumber(buttonText)){
						displayedText = buttonText;						
					}else if(!buttonText.equals("-")){
						displayedText = "0" + buttonTextToString(buttonText);
					}else{
						displayedText = buttonTextToString(buttonText);
					}
				} else if (buttonText.equals("x")) {
					displayedText += "*";
				}else if(StrManip.is1ArgFunction(buttonText)){
					displayedText += buttonText + "(";
				} else {
					displayedText += buttonText;
				}
			}
		} else {
			// equal was pressed, evaluate current expression

			if (lastButton != null && !lastButton.getText().equals("=")) {
				// makes sure we aren't double pressing the "=" button
				String str = displayedText.trim();
				try{
					str = Calculator.calculate(str) + "";					
				}catch (Exception exception){
					
				}
				
				displayedText = str;
			}
		}

		lastButton = button;

		displayArea.setText(displayedText);

	}
	
	private String buttonTextToString(String buttonText){
		if(buttonText.equals("x")){
			return "*";
		}else{
			return buttonText;
		}
	}

}
