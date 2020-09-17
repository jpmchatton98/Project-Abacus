import java.util.PriorityQueue;

// This class contains code for the core parsing algorithm
public class Parser
{
	// Core parsing function.  Accepts the equation string as an input and outputs a queue of
	// EquationPart objects that can be compiled into the answer.
	public PriorityQueue<EquationPart> parse(String equation)
	{
		// Replace all mutli-character strings with single characters

		equation = equation.replace("sqrt", "√");
		equation = equation.replace("cbrt", "∛");
		equation = equation.replace("pi", "π");

		// Trigonometric function logic:
		//      - Basic functions (sin, cos, tan) are lowercase
		//      - Complex functions (sec, csc, cot) are uppercase
		//      - Arc functions have cedillas
		//      - Hyperbolic functions have carons

		equation = equation.replace("sin", "s");
		equation = equation.replace("cos", "c");
		equation = equation.replace("tan", "t");
		equation = equation.replace("sec", "S");
		equation = equation.replace("csc", "C");
		equation = equation.replace("cot", "T");

		equation = equation.replace("asin", "ç");
		equation = equation.replace("acos", "ş");
		equation = equation.replace("atan", "ţ");
		equation = equation.replace("asec", "Ş");
		equation = equation.replace("acsc", "Ç");
		equation = equation.replace("acot", "Ţ");

		equation = equation.replace("sinh", "š");
		equation = equation.replace("cosh", "č");
		equation = equation.replace("tanh", "ť");
		equation = equation.replace("sech", "Š");
		equation = equation.replace("csch", "Č");
		equation = equation.replace("coth", "Ť");

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