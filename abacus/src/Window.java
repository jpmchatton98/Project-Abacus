import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.lang.Character.toUpperCase;

// This class handles all of the UI functionality and contains the main() function
public class Window
{
	private static final int HISTORY_LEN = 10; // Constant for length of the visible history

	private static JTextField equationInput; // Equation input text field

	private static JLabel[] historyLabels; // Array of all history labels
	private static JLabel[] answerLabels; // Array of all answer labels

	public static ArrayList<String> history; // ArrayList containing raw historical equations in string form

	private static JTextField[] variableNames;
	private static JTextField[] variableValues;
	private static String[][] variables;

	private static Parser parser;

	public static void main(String[] args)
	{
		parser = new Parser();
		history = new ArrayList<>(); // Initialize history ArrayList
		variables = new String[HISTORY_LEN + 1][2];

		// Initialize label arrays
		historyLabels = new JLabel[HISTORY_LEN];
		answerLabels = new JLabel[HISTORY_LEN];

		// Initialize variable arrays
		variableNames = new JTextField[HISTORY_LEN + 1];
		variableValues = new JTextField[HISTORY_LEN + 1];

		JPanel window = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Initialize all history and answer labels and print them to the screen
		for(int i = 0; i < HISTORY_LEN; i++)
		{
			variableNames[i] = new JTextField(1);
			variableValues[i] = new JTextField();

			historyLabels[i] = new JLabel("", SwingConstants.LEFT);
			historyLabels[i].addMouseListener(new historyFill());
			c.weighty = 1;
			c.weightx = 0.2;
			c.gridy = i;
			c.gridx = 0;
			c.fill = 1;
			window.add(variableNames[i], c);
			c.gridx = 1;
			window.add(variableValues[i], c);

			c.gridx = 2;
			c.weightx = 1;
			window.add(historyLabels[i], c);
			c.weighty = 0;

			answerLabels[i] = new JLabel();
			c.fill = 0;
			c.gridx = 3;
			c.weightx = 0;
			window.add(answerLabels[i], c);
		}

		variableNames[HISTORY_LEN] = new JTextField(1);
		variableValues[HISTORY_LEN] = new JTextField();
		c.weighty = 1;
		c.weightx = 0.2;
		c.gridy = HISTORY_LEN;
		c.gridx = 0;
		c.fill = 1;
		window.add(variableNames[HISTORY_LEN], c);
		c.gridx = 1;
		window.add(variableValues[HISTORY_LEN], c);

		Font f = new Font("SansSerif", Font.PLAIN, 24);

		// Initialize equationInput and print it to the screen
		equationInput = new JTextField();
		equationInput.setFont(f);
		equationInput.addActionListener(new equationListener());
		c.gridx = 2;
		c.gridy = HISTORY_LEN;
		c.weightx = 1;
		c.fill = 1;
		window.add(equationInput, c);

		// Initialize submit button and print it to the screen
		// Submit button with a ˄ as its text
		JButton submit = new JButton("˄");
		submit.addActionListener(new equationListener());
		c.gridx = 3;
		c.fill = 0;
		c.weightx = 0;
		window.add(submit, c);

		JFrame frame = new JFrame("Project Abacus");
		frame.setContentPane(window);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	private static class equationListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			for(int i = 0; i < variableNames.length; i++)
			{
				if(variableNames[i].getText().length() != 0)
				{
					variables[i][0] = toUpperCase(variableNames[i].getText().charAt(0)) + "";
					variables[i][1] = variableValues[i].getText();
				}
			}

			String equation = equationInput.getText();

			history.add(equation);
			if(history.size() > HISTORY_LEN)
			{
				history.remove(0);
			}

			for(int i = 0, j = HISTORY_LEN - history.size(); j < HISTORY_LEN; i++, j++)
			{
				if(i < history.size())
				{
					historyLabels[j].setText(history.get(i));
					answerLabels[j].setText(parser.parse(historyLabels[j].getText(), variables) + "");
				}
				else
				{
					historyLabels[j].setText("");
					answerLabels[j].setText("");
				}
			}
		}
	}

	private static class historyFill implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			JLabel source = (JLabel) e.getSource();

			equationInput.setText(source.getText());
		}

		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
	}
}