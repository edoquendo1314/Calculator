package jcalc;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JCalc extends JFrame {

	JButton[] numButtonArray;
	JCalcButtonListener jcbl;
	JTextField displayArea;
	JPanel masterButtonPanel;
	JPanel numButtonPanel;
	JPanel operatorPanel;
	
	private final int NUM_OPERATORS = 5;
	
	public JCalc(){
		super("JCalc");
		jcbl = new JCalcButtonListener(this);
	}
	
	public void createAndDisplayGUI(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		displayArea = new JTextField();
		displayArea.setSize(this.getWidth(), 20);
		displayArea.setEditable(false);
		displayArea.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
		displayArea.setText(" ");
		displayArea.setHorizontalAlignment(SwingConstants.RIGHT);		
		
		masterButtonPanel = new JPanel();
		
		numButtonPanel = new JPanel();
		
		initNumButtons(numButtonArray, numButtonPanel);
		
		operatorPanel = new JPanel();
		
		JButton[] operatorButtonArray = null;
		initOperatorButtons(operatorButtonArray, operatorPanel);
		
		setMasterButtonPanelLayout();		
		
		contentPane.add(displayArea);
		contentPane.add(masterButtonPanel);
		setContentPane(contentPane);
		setLocationByPlatform(true);
		pack();
		setVisible(true);
	}
	
	public void setMasterButtonPanelLayout(){
		GridBagLayout layout = new GridBagLayout();
		
		masterButtonPanel.setLayout(layout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 0;
		masterButtonPanel.add(numButtonPanel, gbc);
		
		
		gbc.gridwidth = 1;
		gbc.gridx = gbc.RELATIVE;
		gbc.gridy = 0;
		gbc.insets = new Insets(0,10,10,0);
		masterButtonPanel.add(operatorPanel, gbc);
		
		masterButtonPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
	}
	
	public void initNumButtons(JButton[] buttonArray, JPanel buttonPanel){
		buttonArray = new JButton[10];
		
		buttonPanel.setLayout(new GridLayout(4, 3, 5, 5));
		
		for(int i = buttonArray.length - 1; i >= 0; i--){
			if(i == 0){
				JButton invisButton = new JButton();
				invisButton.setVisible(false);
				buttonPanel.add(invisButton);
			}
			buttonArray[i] = new JButton(Integer.toString(i));
			buttonPanel.add(buttonArray[i]);
			
			buttonArray[i].addActionListener(jcbl);

		}
		
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
	}
	
	public void initOperatorButtons(JButton[] buttonArray, JPanel buttonPanel){
		buttonArray = new JButton[NUM_OPERATORS];
		
		buttonArray[0] = new JButton("+");
		buttonArray[1] = new JButton("-");
		buttonArray[2] = new JButton("x");
		buttonArray[3] = new JButton("/");
		buttonArray[4] = new JButton("=");
		
		buttonPanel.setLayout(new GridLayout(4, 1, 5, 5));
		
		for(int i = 0; i < NUM_OPERATORS; i++){
			buttonArray[i].addActionListener(jcbl);
			buttonPanel.add(buttonArray[i]);
		}
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
	}
	
	public JTextField getDisplayArea(){
		return displayArea;
	}
	
	public static void main(String[] args){
		
		JCalc jcalc = new JCalc();
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				try{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}catch(Exception e){
					
				}
				
				jcalc.createAndDisplayGUI();
			}			
			
		});
		
	}
}
