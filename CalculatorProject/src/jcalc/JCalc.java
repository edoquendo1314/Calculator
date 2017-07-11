package jcalc;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
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
	
	private final int NUM_OPERATORS = 8;
	
	public JCalc(){
		super("JCalc");
		jcbl = new JCalcButtonListener(this);
	}
	
	public void createAndDisplayGUI(){
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel contentPane = new JPanel();
		
		GridBagLayout layout = new GridBagLayout();
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 0;		
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
//		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.setLayout(layout);
		
		displayArea = new JTextField();
//		displayArea.setSize(this.getWidth(), 60);
		
		displayArea.setEditable(false);
		displayArea.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
		displayArea.setText(" ");
		displayArea.setHorizontalAlignment(SwingConstants.RIGHT);
		//contentPane.addKeyListener(new jDisplayAreaActionListener(displayArea));
		
		masterButtonPanel = new JPanel(); //masterButtonPanel is parent of numButtonPanel and operatorPanel
		
		numButtonPanel = new JPanel();
		operatorPanel = new JPanel();
		
		initNumButtons(numButtonArray, numButtonPanel);
		
		
		ArrayList<JButton> operatorButtonArray = new ArrayList<JButton>();
		initOperatorButtons(operatorButtonArray, operatorPanel);
		
		setMasterButtonPanelLayout();		
		
		keyBinding(contentPane);
		
		displayArea.setPreferredSize(new Dimension(this.getWidth(), 60));
		
		gbc.gridy = 0;  // adding displayArea to first row
		contentPane.add(displayArea, gbc);
		
		gbc.gridy = 1;  // adding the masterButtonPanel to second row
		contentPane.add(masterButtonPanel, gbc);
		
		setContentPane(contentPane);
		//this.getContentPane().add(contentPane);
		setLocationByPlatform(true);
		
		pack();	
		
		setVisible(true);
	}
	
	public void keyBinding(JPanel panel){
		InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = panel.getActionMap();
		
		String[] keyArray = {
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
		};

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "1");
		
		for(int i = 0; i < keyArray.length; i++){
			String s = keyArray[i];
			inputMap.put(KeyStroke.getKeyStroke(s), s);   // normal number keys
			inputMap.put(KeyStroke.getKeyStroke(i+96, 0), s); // numpad keys, 96 == numpad 0 keycode
			actionMap.put(s, newAction(s));
		}
		
		String[] operatorArray = {
				"+", "-", "/", "*", "^"
		};
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "+");
		inputMap.put(KeyStroke.getKeyStroke('=', InputEvent.SHIFT_DOWN_MASK), "+");
		actionMap.put("+", newAction("+"));
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "-");
		inputMap.put(KeyStroke.getKeyStroke('-'), "-");
		actionMap.put("-", newAction("-"));
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, 0), "*");
		inputMap.put(KeyStroke.getKeyStroke('8', InputEvent.SHIFT_DOWN_MASK), "*");
		actionMap.put("*", newAction("*"));
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DIVIDE, 0), "/");
		inputMap.put(KeyStroke.getKeyStroke('/'), "/");
		actionMap.put("/", newAction("/"));
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
		actionMap.put("ENTER", newAction("ENTER"));
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), ".");
		actionMap.put(".", newAction("."));
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL, 0), ".");
		actionMap.put(".", newAction("."));
		
	}

	
	private JCalcAction newAction(String input){

		JCalcAction action = new JCalcAction(displayArea, input);
		
		return action;
	}
	
	public void setMasterButtonPanelLayout(){
		GridBagLayout layout = new GridBagLayout();
		
		masterButtonPanel.setLayout(layout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		masterButtonPanel.add(numButtonPanel, gbc);
		
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
	
	public void initOperatorButtons(ArrayList<JButton> buttonArray, JPanel buttonPanel){
		
		buttonArray.add(new JButton("+"));
		buttonArray.add(new JButton("-"));
		buttonArray.add(new JButton("x"));
		buttonArray.add(new JButton("/"));
		buttonArray.add(new JButton("="));
		buttonArray.add(new JButton("("));
		buttonArray.add(new JButton(")"));
		buttonArray.add(new JButton("clr"));
		buttonArray.add(new JButton("sin"));
		buttonArray.add(new JButton("cos"));
		buttonArray.add(new JButton("tan"));
		buttonArray.add(new JButton("asin"));
		buttonArray.add(new JButton("acos"));
		buttonArray.add(new JButton("atan"));
		buttonArray.add(new JButton("sec"));
		buttonArray.add(new JButton("csc"));
		buttonArray.add(new JButton("cot"));
		
		buttonPanel.setLayout(new GridLayout(4, 1, 5, 5));
		
		for(int i = 0; i < buttonArray.size(); i++){
			buttonArray.get(i).addActionListener(jcbl);
			buttonPanel.add(buttonArray.get(i));
		}
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
	}
	
	public JTextField getDisplayArea(){
		return displayArea;
	}
	
	
	public void paint(Graphics g){
		super.paint(g);
		displayArea.setPreferredSize(new Dimension(this.getWidth(), (int)(this.getHeight()*0.5)));
		System.out.println(this.getWidth());
		System.out.println(displayArea.getWidth());
		System.out.println(this.getContentPane().getWidth());
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
