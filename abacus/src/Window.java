import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

public class Window
{
	private static final int HISTORY_LEN = 10;

	private JTextField equationInput;
	private JButton submit;
	private JPanel window;

	public JLabel history0;
	public JLabel history1;
	public JLabel history2;
	public JLabel history3;
	public JLabel history4;
	public JLabel history5;
	public JLabel history6;
	public JLabel history7;
	public JLabel history8;
	public JLabel history9;
	public JLabel answer0;
	public JLabel answer1;
	public JLabel answer2;
	public JLabel answer3;
	public JLabel answer4;
	public JLabel answer5;
	public JLabel answer6;
	public JLabel answer7;
	public JLabel answer8;
	public JLabel answer9;

	public JLabel[] history;

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("testForm");
		frame.setContentPane(new Window().window);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(500, 500);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private class equationListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String equation = equationInput.getText();

			for(int i = 0; i < 10; i++)
			{
				try
				{
					Object instance = getClass().getDeclaredField("history" + i).get(this);
					Method m = instance.getClass().getMethod("split", String.class);

					Object returnValue = m.invoke(instance, ";");
					if(returnValue instanceof JLabel)
					{
						System.out.println((JLabel)returnValue);
					}
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
				}
			}
		}
	}

	/*// If a history label is clicked, make the current equation that historical equation
	private class historyFill implements MouseListener
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
	}*/
}
