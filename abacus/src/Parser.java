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

	// Takes in a queue of EquationPart objects and compiles them into an answer
	public double compileAnswer(PriorityQueue<EquationPart> parts)
	{
		double answer = 0;

		while(parts.peek() != null)
		{
			EquationPart part = parts.remove();

			switch(part.getFunction()) // Determine which function to do and do it
			{
				case '+': // Add
				{
					answer += part.getNumber();
					break;
				}
				case '-': // Subtract
				{
					answer -= part.getNumber();
					break;
				}
				case '*': // Multiply
				{
					answer *= part.getNumber();
					break;
				}
				case '/': // Divide
				{
					answer /= part.getNumber();
					break;
				}
				case '^': // Exponent
				{
					answer = Math.pow(answer, part.getNumber());
					break;
				}
				case '%': // Modulus
				{
					answer %= part.getNumber();
					break;
				}
				default: // Invalid
				{
					System.out.println("Invalid function");
				}
			}
		}

		return answer;
	}
}