import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

// Main class that drives the function
public class Driver
{
	static final int HISTORY_LEN = 10;

	static JFrame window; // GUI

	protected static JTextField equationInput; // Text field for inputting equations
	protected static Label[] historyLabels; // Text labels for history

	protected static ArrayList<String> history; // ArrayList of historical equations

	public static void main(String[] args)
	{
		history = new ArrayList<String>();

		window = new JFrame();
		window.setLayout(new GridLayout(HISTORY_LEN + 1, 1));

		equationInput = new JTextField();
		equationInput.addActionListener(new equationListener());

		// Initialize ten history labels
		historyLabels = new Label[HISTORY_LEN];
		for(int i = 0; i < HISTORY_LEN; i++)
		{
			historyLabels[i] = new Label();
			historyLabels[i].addMouseListener(new historyFill()); // Mouse listener if the label is clicked
			window.add(historyLabels[i]);
		}

		window.add(equationInput);

		window.setTitle("Project Abacus");
		window.setSize(1000, 700);
		window.setVisible(true);
		window.setResizable(false);
	}

	// Performs equation submission functionality
	private static class equationListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String equation = equationInput.getText();

			history.add(equation);
			System.out.println(history);
			if(history.size() > HISTORY_LEN)
			{
				history.remove(0);
			}

			for(int i = 0, j = HISTORY_LEN - history.size(); j < HISTORY_LEN; i++, j++)
			{
				if(i < history.size())
				{
					historyLabels[j].setText(history.get(i));
				}
				else
				{
					historyLabels[j].setText("");
				}
			}
		}
	}

	// If a history label is clicked, make the current equation that historical equation
	private static class historyFill implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			Label source = (Label) e.getSource();

			equationInput.setText(source.getText());
		}

		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
	}
}