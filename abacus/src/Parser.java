import java.util.ArrayList;

import static java.lang.Character.isDigit;
import static java.lang.Double.parseDouble;

// This class contains code for the core parsing algorithm
public class Parser
{
	// Core parsing function.  Accepts the equation string as an input and outputs a queue of
	// EquationPart objects that can be compiled into the answer.
	public double parse(String equation)
	{
		// Replace all multi-character strings with single characters

		equation = equation.replace("sqrt", "√");
		equation = equation.replace("cbrt", "∛");
		equation = equation.replace("pi", "π");
		equation = equation.replace("log", "L");
		equation = equation.replace("logn", "Ł");

		// Trigonometric function logic:
		//      - Basic functions (sin, cos, tan) are lowercase
		//      - Complex functions (sec, csc, cot) are uppercase
		//      - Arc functions have cedillas
		//      - Hyperbolic functions have carons

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

		equation = equation.replace("sin", "s");
		equation = equation.replace("cos", "c");
		equation = equation.replace("tan", "t");
		equation = equation.replace("sec", "S");
		equation = equation.replace("csc", "C");
		equation = equation.replace("cot", "T");

		ArrayList<EquationPart> equationParts = new ArrayList<>();

		// Change the equation string to an array
		char[] equationArray = equation.toCharArray();
		String curr = "";
		char finalFunction = ' ';

		// Core parsing loop
		for (char c : equationArray)
		{
			if (isNumberOrPeriod(c))
			{
				curr += c;
			}
			else
			{
				finalFunction = c;
				if (equationParts.size() > 0)
				{
					equationParts.add(new EquationPart(parseDouble(curr), c, equationParts.get(equationParts.size() - 1)));
					equationParts.get(equationParts.size() - 2).setNext(equationParts.get(equationParts.size() - 1));
				}
				else
				{
					equationParts.add(new EquationPart(parseDouble(curr), '+', null));
				}
				curr = "";
			}
		}

		equationParts.add(new EquationPart(parseDouble(curr), finalFunction, equationParts.get(equationParts.size() - 1)));
		equationParts.get(equationParts.size() - 2).setNext(equationParts.get(equationParts.size() - 1));

		return compileEquation(equationParts);
	}

	private double compileEquation(ArrayList<EquationPart> equationParts)
	{
		double result = 0;

		EquationPart part = equationParts.get(0);
		while(part != null)
		{
			switch(part.getFunction())
			{
				case '+':
				{
					result += part.getNumber();
					break;
				}
				case '-':
				{
					result -= part.getNumber();
					break;
				}
				case '*':
				{
					result *= part.getNumber();
					break;
				}
				case '/':
				{
					result /= part.getNumber();
					break;
				}
				case '^':
				{
					result = Math.pow(result, part.getNumber());
					break;
				}
				case '%':
				{
					result %= part.getNumber();
					break;
				}
				default:
				{
					System.out.println("Invalid function");
				}
			}

			part = part.next();
		}

		return result;
	}

	private boolean isNumberOrPeriod(char input)
	{
		return isDigit(input) || input == '.';
	}
}