import java.util.PriorityQueue;

// This class contains code for the core parsing algorithm
public class Parser
{
	// Core parsing function.  Accepts the equation string as an input and outputs a queue of
	// EquationPart objects that can be compiled into the answer.
	public PriorityQueue<EquationPart> parse(String equation)
	{
		// Change the equation string to an array
		char[] equationArray = equation.toCharArray();

		// Core parsing loop
		for(int i = 0; i < equationArray.length; i++)
		{
			// TODO: Create parsing algorithm
		}

		return null;
	}
}