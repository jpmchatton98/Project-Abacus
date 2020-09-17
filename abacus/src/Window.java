import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Window
{
	private static final int HISTORY_LEN = 10;

	private static JTextField equationInput;
	private static JButton submit;
	private static JPanel window;

	public static JLabel[] historyLabels;
	public static JLabel[] answerLabels;

	public static ArrayList<String> history;

	public static void main(String[] args)
	{
		history = new ArrayList<>();

		historyLabels = new JLabel[HISTORY_LEN];
		answerLabels = new JLabel[HISTORY_LEN];

		window = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		for(int i = 0; i < HISTORY_LEN; i++)
		{
			historyLabels[i] = new JLabel("history" + i, SwingConstants.LEFT);
			historyLabels[i].addMouseListener(new historyFill());
			c.weighty = 1;
			c.weightx = 1;
			c.gridy = i;
			c.gridx = 0;
			c.fill = 1;
			window.add(historyLabels[i], c);
			c.weighty = 0;

			answerLabels[i] = new JLabel("answer" + i);
			c.fill = 0;
			c.gridx = 1;
			c.weightx = 0;
			window.add(answerLabels[i], c);
		}

		equationInput = new JTextField();
		equationInput.addActionListener(new equationListener());
		c.gridx = 0;
		c.gridy = HISTORY_LEN;
		c.weightx = 1;
		c.fill = 1;
		window.add(equationInput, c);

		submit = new JButton("˄");
		submit.addActionListener(new equationListener());
		c.gridx = 1;
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