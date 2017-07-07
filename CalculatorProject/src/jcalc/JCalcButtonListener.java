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

		if (!button.getText().equals("=")) {
			// if something other than "=" was pressed

			if (lastButton != null && lastButton.getText().equals("=") && StrManip.isNumber(button.getText())) {
				// if the last button pressed was an equal sign
				// display area is displaying the result of some expression evaluation
				// if the next button we press is a number, we are starting a new expression
				// and overwriting previous results
				
				if (button.getText().equals("x")) {
					displayedText = "*";
				}else{
					displayedText = button.getText();
				}

			} else {
				// either the last button was not an equal sign,
				// or the button we are pressing now is not a number
				// or both
				// in any case, we are appending the expression or number in display area
				
				if (displayedText.equals(" ")) {
					displayedText = button.getText();
				} else if (button.getText().equals("x")) {
					displayedText += "*";
				} else {
					displayedText += button.getText();
				}
			}
		} else {
			// equal was pressed, evaluate current expression

			if (lastButton != null && !lastButton.getText().equals("=")) {
				// makes sure we aren't double pressing the "=" button
				String str = displayedText.trim();
				str = Calculator.calculate(str) + "";
				displayedText = str;
			}
		}

		lastButton = button;

		displayArea.setText(displayedText);

	}

}
