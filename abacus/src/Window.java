package jpmchatton98;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class Window
{
	static JFrame window;

	protected static JTextField equationInput;
	protected static Label[] historyLabels;

	protected static ArrayList<String> history;

	public static void main(String[] args)
	{
		history = new ArrayList<String>();

		window = new JFrame();
		window.setLayout(new GridLayout(10, 1));

		equationInput = new JTextField();
		equationInput.addActionListener(new equationListener());

		historyLabels = new Label[9];
		for(int i = 0; i < 9; i++)
		{
			historyLabels[i] = new Label();
			historyLabels[i].addMouseListener(new historyFill());
			window.add(historyLabels[i]);
		}

		window.add(equationInput);

		window.setTitle("Project Abacus");
		window.setSize(500, 700);
		window.setVisible(true);
		window.setResizable(false);
	}

	private static class equationListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String equation = equationInput.getText();

			history.add(equation);
			System.out.println(history);
			if(history.size() > 9)
			{
				history.remove(0);
			}

			for(int i = 0, j = 9 - history.size(); j < 9; i++, j++)
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
			Label source = (Label) e.getSource();

			equationInput.setText(source.getText());
		}

		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
	}
}