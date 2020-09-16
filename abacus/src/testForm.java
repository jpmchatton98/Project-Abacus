import javax.swing.*;

public class testForm
{
	private JTextField equationInput;
	private JButton submit;
	private JList historyList;
	private JList answerList;
	private JPanel window;

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("testForm");
		frame.setContentPane(new testForm().window);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
}

