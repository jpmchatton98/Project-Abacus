import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.MATH_SYMBOL;
import static java.lang.Character.isDigit;
import static java.lang.Double.NaN;
import static java.lang.Double.parseDouble;

// This class contains code for the core parsing algorithm
public class Parser
{
	// Core parsing function.  Accepts the equation string as an input and outputs the answer
	// in the form of a double.
	public double parse(String equation)
	{
		try
		{
			String lastFunction = null;
			equation = prepareEquation(equation);
			ArrayList<EquationPart> equationParts = new ArrayList<>();

			// Change the equation string to an array
			String number = "";
			String function = "";
			EquationPart curr = null;

			boolean startingNegative = false;
			int mode = 0; // 0: Number, 1: Function

			for (int i = 0; i < equation.length(); i++)
			{
				if (i == 0 && equation.charAt(i) == '-') // Starting negative
				{
					startingNegative = true;
					continue;
				}

				if (isNumberOrPeriod(equation.charAt(i)))
				{
					if (mode == 1)
					{
						if (function.equals(""))
						{
							function = "+";
						}

						lastFunction = function;

						curr = new EquationPart(function);
						equationParts.add(curr);
						curr = null;

						function = "";
					}

					mode = 0;
					number += equation.charAt(i);
				}
				else
				{
					if (mode == 0)
					{
						if (equationParts.size() > 0)
						{
							function = equationParts.get(equationParts.size() - 1).getFunction();
							if(function.length() > 1)
							{
								if (function.charAt(function.length() - 1) == '-')
								{
									equationParts.get(equationParts.size() - 1).setNumber(-1 * parseDouble(number));
									equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
											.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));
								}
								else
								{
									equationParts.get(equationParts.size() - 1).setNumber(parseDouble(number));
								}
							}
							else
							{
								equationParts.get(equationParts.size() - 1).setNumber(parseDouble(number));
							}
							function = "";
						}
						else // First Element
						{
							if (startingNegative)
							{
								number = "-" + number;
							}
							equationParts.add(new EquationPart(parseDouble(number), "+"));
						}
						number = "";
					}

					mode = 1;
					function += equation.charAt(i);
				}
			}

			if (equationParts.size() > 0)
			{
				if(lastFunction.length() > 1)
				{
					if (lastFunction.charAt(lastFunction.length() - 1) == '-')
					{
						equationParts.get(equationParts.size() - 1).setNumber(-1 * parseDouble(number));
						equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
								.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));
					}
					else
					{
						equationParts.get(equationParts.size() - 1).setNumber(parseDouble(number));
					}
				}
				else
				{
					equationParts.get(equationParts.size() - 1).setNumber(parseDouble(number));
				}
			}
			else
			{
				if (startingNegative)
				{
					number = "-" + number;
				}
				return parseDouble(number);
			}

			return compileEquation(equationParts);
		}
		catch(NumberFormatException e)
		{
			return NaN;
		}
	}

	// Prepares the equation for parsing by trimming out spaces and replacing multi-character
	// functions with single characters.
	private String prepareEquation(String equation)
	{
		//Trim out all of the spaces
		equation = equation.replace(" ", "");

		return equation;
	}

	private ArrayList<EquationPart> completeFunctions(ArrayList<EquationPart> equationParts)
	{
		boolean done = false;

		final String[] fourChars = {"sqrt", "cbrt", "logn", "sinh", "asin", "cosh", "acos", "tanh", "atan", "sech", "asec", "csch", "acsc", "coth", "acot"};
		final String[] threeChars = {"sin", "cos", "tan", "sec", "csc", "cot"};

		while(!done)
		{
			for (EquationPart equationPart : equationParts)
			{
				done = equationPart.getFunction().length() <= 1;

				if(!done)
				{
					String lastFour = equationPart.getFunction().substring(equationPart.getFunction().length() - 4);
					if (Arrays.toString(fourChars).contains(lastFour))
					{
						equationPart.setFunction(equationPart.getFunction().substring(0, equationPart.getFunction().length() - 4));
						equationPart.setNumber(completeFunction(equationPart.getNumber(), lastFour));
					}
					else
					{
						String lastThree = equationPart.getFunction().substring(equationPart.getFunction().length() - 3);
						if (Arrays.toString(threeChars).contains(lastThree))
						{
							equationPart.setFunction(equationPart.getFunction().substring(0, equationPart.getFunction().length() - 3));
							equationPart.setNumber(completeFunction(equationPart.getNumber(), lastThree));
						}
					}
				}
			}
		}

		return equationParts;
	}
	private double completeFunction(double num, String function)
	{
		switch(function)
		{
			case "sqrt": // Square Root
			{
				return Math.sqrt(num);
			}
			case "cbrt": // Cube Root
			{
				return Math.cbrt(num);
			}

			case "log": // Log base 10
			{
				return Math.log10(num);
			}
			case "logn": // Natural Log
			{
				return Math.log(num);
			}

			case "sin": // Sine
			{
				return Math.sin(num);
			}
			case "sinh": // Hyperbolic Sine
			{
				return Math.sinh(num);
			}
			case "asin": // Arcsine
			{
				return Math.asin(num);
			}

			case "cos": // Cosine
			{
				return Math.cos(num);
			}
			case "cosh": // Hyperbolic Cosine
			{
				return Math.cosh(num);
			}
			case "acos": // Arccosine
			{
				return Math.acos(num);
			}

			case "tan": // Tangent
			{
				return Math.tan(num);
			}
			case "tanh": // Hyperbolic Tangent
			{
				return Math.tanh(num);
			}
			case "atan": // Arctangent
			{
				return Math.atan(num);
			}

			case "sec": // Secant
			{
				return 1 / Math.cos(num);
			}
			case "sech": // Hyperbolic Secant
			{
				return 1 / Math.cosh(num);
			}
			case "asec": // Arcsecant
			{
				return 1 / Math.acos(num);
			}

			case "csc": // Cosecant
			{
				return 1 / Math.sin(num);
			}
			case "csch": // Hyperbolic Cosecant
			{
				return 1 / Math.sinh(num);
			}
			case "acsc": // Arccosecant
			{
				return 1 / Math.asin(num);
			}

			case "cot": // Cotangent
			{
				return 1 / Math.tan(num);
			}
			case "coth": // Hyperbolic Cotangent
			{
				return 1 / Math.tanh(num);
			}
			case "acot": // Arccotangent
			{
				return 1 / Math.atan(num);
			}

			default:
			{
				System.out.println("Invalid function.");
				return NaN;
			}
		}
	}

	private double compileEquation(ArrayList<EquationPart> equationParts)
	{
		equationParts = completeFunctions(equationParts);

		int index = 0;
		while(equationParts.size() > 1)
		{
			if(equationParts.get(index + 1).getFunction().equals("^"))
			{
				EquationPart part = equationParts.get(index);
				EquationPart nextPart = equationParts.get(index + 1);

				double newNumber = compileFunction(part.getNumber(), nextPart.getNumber(), nextPart.getFunction());
				EquationPart newPart = new EquationPart(newNumber, part.getFunction());

				equationParts.remove(part);
				equationParts.remove(nextPart);
				equationParts.add(index, newPart);
			}
			else
			{
				index++;
			}


			if(index >= equationParts.size() - 1)
			{
				break;
			}
		}

		index = 0;
		while(equationParts.size() > 1)
		{
			if(equationParts.get(index + 1).getFunction().equals("*") || equationParts.get(index + 1).getFunction().equals("/"))
			{
				EquationPart part = equationParts.get(index);
				EquationPart nextPart = equationParts.get(index + 1);

				double newNumber = compileFunction(part.getNumber(), nextPart.getNumber(), nextPart.getFunction());
				EquationPart newPart = new EquationPart(newNumber, part.getFunction());

				equationParts.remove(part);
				equationParts.remove(nextPart);
				equationParts.add(index, newPart);
			}
			else
			{
				index++;
			}

			if(index >= equationParts.size() - 1)
			{
				break;
			}
		}

		index = 0;
		while(equationParts.size() > 1)
		{
			EquationPart part = equationParts.get(index);
			EquationPart nextPart = equationParts.get(index + 1);

			double newNumber = compileFunction(part.getNumber(), nextPart.getNumber(), nextPart.getFunction());
			EquationPart newPart = new EquationPart(newNumber, part.getFunction());

			equationParts.remove(part);
			equationParts.remove(nextPart);
			equationParts.add(index, newPart);
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