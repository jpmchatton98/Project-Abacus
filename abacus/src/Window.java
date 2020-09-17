import javax.swing.*;
import java.awt.*;

public class Window
{
	private static final int HISTORY_LEN = 10;

	private static JTextField equationInput;
	private static JButton submit;
	private static JPanel window;

	public static JLabel[] history;
	public static JLabel[] answers;

	public static void main(String[] args)
	{
		history = new JLabel[HISTORY_LEN];
		answers = new JLabel[HISTORY_LEN];

		window = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		for(int i = 0; i < HISTORY_LEN; i++)
		{
			history[i] = new JLabel("history" + i);
			c.weighty = 1;
			c.weightx = 1;
			c.gridy = i;
			c.gridx = 0;
			window.add(history[i], c);
			c.weighty = 0;

			answers[i] = new JLabel("answer" + i);
			c.gridx = 1;
			c.weightx = 0;
			window.add(answers[i], c);
		}

		equationInput = new JTextField();
		c.gridx = 0;
		c.gridy = HISTORY_LEN;
		c.weightx = 1;
		c.fill = 1;
		window.add(equationInput, c);

		submit = new JButton("˄");
		c.gridx = 1;
		c.fill = 0;
		c.weightx = 0;
		window.add(submit, c);

		JFrame frame = new JFrame("test window");
		frame.setContentPane(window);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}