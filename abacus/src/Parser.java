import java.util.ArrayList;

import static java.lang.Character.isDigit;
import static java.lang.Double.NaN;
import static java.lang.Double.parseDouble;

// This class contains code for the core parsing algorithm
public class Parser
{
	// Core parsing function.  Accepts the equation string as an input and outputs a queue of
	// EquationPart objects that can be compiled into the answer.
	public double parse(String equation)
	{
		//Trim out all of the spaces
		equation = equation.replace(" ", "");

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
		//      - Do arc and hyperbolic functions first so that we don't replace parts of them
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
		String function = "";

		if(equationArray[0] != '-')
		{
			// Core parsing loop
			for (int i = 0; i < equationArray.length; i++)
			{
				if (isNumberOrPeriod(equationArray[i]))
				{
					curr += equationArray[i];
				}
				else
				{
					if (equationParts.size() > 0)
					{
						equationParts.add(new EquationPart(parseDouble(curr), function, equationParts.get(equationParts.size() - 1)));
						equationParts.get(equationParts.size() - 2).setNext(equationParts.get(equationParts.size() - 1));
					}
					else
					{
						equationParts.add(new EquationPart(parseDouble(curr), "+", null));
					}
					curr = "";
					function = equationArray[i] + "";
				}
			}
		}
		else
		{
			// Core parsing loop
			for (int i = 1; i < equationArray.length; i++)
			{
				if (isNumberOrPeriod(equationArray[i]))
				{
					curr += equationArray[i];
				}
				else
				{
					if (equationParts.size() > 0)
					{
						equationParts.add(new EquationPart(parseDouble(curr), function, equationParts.get(equationParts.size() - 1)));
						equationParts.get(equationParts.size() - 2).setNext(equationParts.get(equationParts.size() - 1));
					}
					else
					{
						equationParts.add(new EquationPart(parseDouble(curr), "-", null));
					}
					curr = "";
					function = equationArray[i] + "";
				}
			}
		}

		if(equationParts.size() < 1)
		{
			equationParts.add(new EquationPart(parseDouble(curr), function, equationParts.get(equationParts.size() - 1)));
			equationParts.get(equationParts.size() - 2).setNext(equationParts.get(equationParts.size() - 1));

			return compileEquation(equationParts);
		}
		else
		{
			return parseDouble(curr);
		}


	}

	private double compileEquation(ArrayList<EquationPart> equationParts)
	{
		EquationPart part = equationParts.get(0);

		while (part != null && !(part.next() == null && part.prev() == null)) // Search for multiplication and division
		{
			if (part.next() != null)
			{
				if (part.next().getFunction().equals("^") || part.next().getFunction().contains("√"))
				{
					EquationPart newNext = part.next().next();
					EquationPart newPrev = part.prev();

					String newFunction = part.getFunction();
					double newNumber = compileFunction(part.getNumber(), part.next().getNumber(), part.next().getFunction());

					EquationPart newPart = new EquationPart(newNumber, newFunction, newPrev, newNext);
					equationParts.add(newPart);

					if (newNext != null)
					{
						newNext.setPrev(newPart);
					}

					equationParts.remove(part);
					equationParts.remove(part.next());

					part = newPart;
				}

				if (part.next() != null)
				{
					part = part.next();
				}
				else
				{
					break;
				}
			}
			else
			{
				break;
			}
		}

		// Rewind
		while (part.prev() != null)
		{
			part = part.prev();
		}

		while (part != null && !(part.next() == null && part.prev() == null)) // Search for multiplication and division
		{
			if (part.next() != null)
			{
				if (part.next().getFunction().equals("*") || part.next().getFunction().equals("/") || part.getFunction().equals("%"))
				{
					EquationPart newNext = part.next().next();
					EquationPart newPrev = part.prev();

					String newFunction = part.getFunction();
					double newNumber = compileFunction(part.getNumber(), part.next().getNumber(), part.next().getFunction());

					EquationPart newPart = new EquationPart(newNumber, newFunction, newPrev, newNext);
					equationParts.add(newPart);

					if (newNext != null)
					{
						newNext.setPrev(newPart);
					}

					if (newPart.prev() != null)
					{
						newPart.prev().setNext(newPart);
					}

					equationParts.remove(part);
					equationParts.remove(part.next());

					part = newPart;
				}

				if (part.next() != null)
				{
					part = part.next();
				}
				else
				{
					break;
				}
			}
			else
			{
				break;
			}
		}

		// Rewind
		while (part != null && part.prev() != null)
		{
			part = part.prev();
		}

		while (part != null && !(part.next() == null && part.prev() == null)) // Search for everything else
		{
			if (part.next() != null)
			{
				EquationPart newNext = part.next().next();
				EquationPart newPrev = part.prev();

				String newFunction = part.getFunction();
				double newNumber = compileFunction(part.getNumber(), part.next().getNumber(), part.next().getFunction());

				EquationPart newPart = new EquationPart(newNumber, newFunction, newPrev, newNext);
				equationParts.add(newPart);

				if (newNext != null)
				{
					newNext.setPrev(newPart);
				}

				equationParts.remove(part);
				equationParts.remove(part.next());

				part = newPart;

				part = part.next();
			}
			else
			{
				// Rewind
				while (part.prev() != null)
				{
					part = part.prev();
				}
			}
		}

		return equationParts.get(0).getNumber();
	}

	private double compileFunction(double num1, double num2, String function)
	{
		switch(function)
		{
			case "+":
			{
				return num1 + num2;
			}
			case "-":
			{
				return num1 - num2;
			}
			case "*":
			{
				return num1 * num2;
			}
			case "/":
			{
				return num1 / num2;
			}
			case "^":
			{
				return Math.pow(num1, num2);
			}
			case "%":
			{
				return num1 % num2;
			}
			case "√":
			case "*√":
			{
				return num1 * Math.sqrt(num2);
			}
			case "+√":
			{
				return num1 + Math.sqrt(num2);
			}
			case "-√":
			{
				return num1 - Math.sqrt(num2);
			}
			case "/√":
			{
				return num1 / Math.sqrt(num2);
			}
			default:
			{
				System.out.println("Invalid function");
				return NaN;
			}
		}
	}

	private boolean isNumberOrPeriod(char input)
	{
		return isDigit(input) || input == '.';
	}
}