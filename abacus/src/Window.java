import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.lang.Double.NaN;
import static java.lang.Double.parseDouble;

// This class handles all of the UI functionality and contains the main() function
public class Window
{
	private static final int HISTORY_LEN = 10; // Constant for length of the visible history

	private static JTextField equationInput; // Equation input text field

	private static JLabel[] historyLabels; // Array of all history labels
	private static JLabel[] answerLabels; // Array of all answer labels

	private static JTextField[] variableNameInputs; // Array of all variable name input boxes
	private static JTextField[] variableValueInputs; // Array of all variable value input boxes

	public static ArrayList<String> history; // ArrayList containing raw historical equations in string form
	public static ArrayList<Variable> variables;

	private static Parser parser;

	public static void main(String[] args)
	{
		parser = new Parser();
		history = new ArrayList<>(); // Initialize history ArrayList
		variables = new ArrayList<>();

		// Initialize label arrays
		historyLabels = new JLabel[HISTORY_LEN];
		answerLabels = new JLabel[HISTORY_LEN];

		// Initialize variable input arrays
		variableNameInputs = new JTextField[HISTORY_LEN];
		variableValueInputs = new JTextField[HISTORY_LEN];

		JPanel window = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// First row
		c.weighty = 1;
		c.weightx = 1;
		c.gridy = 0;
		c.fill = 1;

		c.gridx = 0;
		JLabel varName = new JLabel("Variable Names");
		window.add(varName, c);

		c.gridx = 1;
		JLabel varVal = new JLabel("Variable Values");
		window.add(varVal, c);

		historyLabels[0] = new JLabel("", SwingConstants.LEFT);
		historyLabels[0].addMouseListener(new historyFill());

		c.gridx = 2;
		window.add(historyLabels[0], c);
		c.weighty = 0;

		answerLabels[0] = new JLabel();
		c.gridx = 3;
		c.weightx = 0;
		window.add(answerLabels[0], c);

		// Initialize all history and answer labels and print them to the screen
		for(int i = 1; i < HISTORY_LEN; i++)
		{
			c.weighty = 1;
			c.weightx = 1;
			c.gridy = i;
			c.fill = 1;

			c.gridx = 0;
			variableNameInputs[i] = new JTextField();
			window.add(variableNameInputs[i], c);

			c.gridx = 1;
			variableValueInputs[i] = new JTextField();
			window.add(variableValueInputs[i], c);

			historyLabels[i] = new JLabel("", SwingConstants.LEFT);
			historyLabels[i].addMouseListener(new historyFill());

			c.gridx = 2;
			window.add(historyLabels[i], c);
			c.weighty = 0;

			answerLabels[i] = new JLabel();
			c.gridx = 3;
			c.weightx = 0;
			window.add(answerLabels[i], c);
		}

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
		submit.setBackground(new Color(16, 161, 0));
		c.gridx = 3;
		c.fill = 0;
		c.weightx = 0;
		window.add(submit, c);

		JFrame frame = new JFrame("Project Abacus");
		frame.setContentPane(window);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(750, 500);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	private static class equationListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String equation = equationInput.getText();
			variables.clear();

			for(int i = 1; i < HISTORY_LEN; i++)
			{
				if(variableNameInputs[i].getText() != null)
				{
					if (variableNameInputs[i].getText().trim().length() != 0)
					{
						variables.add(new Variable(variableNameInputs[i].getText().toLowerCase().charAt(0), Double.parseDouble(variableValueInputs[i].getText())));
					}
				}
			}

			if(!Double.isNaN(parser.parse(equation, variables)))
			{
				history.add(equation);
				if(history.size() > HISTORY_LEN)
				{
					history.remove(0);
				}

				equationInput.setBackground(Color.WHITE);
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
			else
			{
				equationInput.setBackground(new Color(255, 138, 138));
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