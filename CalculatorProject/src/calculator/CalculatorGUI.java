package calculator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CalculatorGUI extends JFrame{

	public CalculatorGUI() {
		super("Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//this.setLocationByPlatform(true);
		
		setSize(500,500);
		setVisible(true);
		this.setLocationRelativeTo(null);
	}

	public static void main(String[] args){
		Runnable r = new Runnable(){

			@Override
			public void run() {
				
				try{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}catch(Exception e){
					
				}
				CalculatorGUI cGUI = new CalculatorGUI();
			}
			
		};
		
		SwingUtilities.invokeLater(r);
	}
	
}
