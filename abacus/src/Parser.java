import java.util.ArrayList;
import java.util.Arrays;

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

				if(equation.charAt(i) == '(') // If equation contains an opening parenthesis
				{
					int pTally = 0; // Tally of parenthetic blocks, so that we don't match them up incorrectly
					int pStart = i; // Starting index of the parentheses
					int pEnd = 0; // Ending index of the parentheses

					for(i = i + 1; i < equation.length(); i++)
					{
						if(equation.charAt(i) == '(')
						{
							pTally++;
						}
						else if(equation.charAt(i) == ')')
						{
							if (pTally == 0)
							{
								pEnd = i;
								break;
							}
							else
							{
								pTally--;
							}
						}
					}

					if(pTally != 0)
					{
						throw new NumberFormatException();
					}
					else
					{
						String p = equation.substring(pStart, pEnd + 1);
						String pTrim = p.substring(1, p.length() - 1);
						equation = equation.replace(p, parse(pTrim) + "");

						if(isNumberOrPeriod(equation.charAt(pStart - 1)))
						{
							equation = equation.substring(0, pStart) + "*" + equation.substring(pStart);
						}

						equationParts.clear();
						number = "";
						function = "";

						i = -1;
						continue;
					}
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
			e.printStackTrace();
			return NaN;
		}
	}

	// Prepares the equation for parsing by trimming out spaces and replacing multi-character
	// functions with single characters.
	private String prepareEquation(String equation)
	{
		equation = equation.replace(" ", "");
		equation = equation.toLowerCase();

		equation = "0+" + equation;

		return equation;
	}

	private ArrayList<EquationPart> completeFunctions(ArrayList<EquationPart> equationParts)
	{
		boolean done = false;

		final String[] fourChars = {"sqrt", "cbrt", "logn", "sinh", "asin", "cosh", "acos", "tanh", "atan", "sech", "asec", "csch", "acsc", "coth", "acot"};
		final String[] threeChars = {"sin", "cos", "tan", "sec", "csc", "cot", "log", "abs"};

		boolean invalid = false;

		try
		{
			while (!done && !invalid)
			{
				for (EquationPart equationPart : equationParts)
				{
					done = equationPart.getFunction().length() <= 1;

					if (!done)
					{
						if (equationPart.getFunction().length() >= 4)
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
								else
								{
									System.out.println("Invalid function");
									invalid = true;
									break;
								}
							}
						}
						else
						{
							String lastThree = equationPart.getFunction().substring(equationPart.getFunction().length() - 3);
							if (Arrays.toString(threeChars).contains(lastThree))
							{
								equationPart.setFunction(equationPart.getFunction().substring(0, equationPart.getFunction().length() - 3));
								equationPart.setNumber(completeFunction(equationPart.getNumber(), lastThree));
							}
							else
							{
								System.out.println("Invalid function");
								invalid = true;
								break;
							}
						}

						if (equationPart.getFunction().length() == 0)
						{
							equationPart.setFunction("*");
						}
					}
				}
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
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

			case "abs": // Absolute Value
			{
				return Math.abs(num);
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
		if(equationParts == null)
		{
			return NaN;
		}

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
			if(equationParts.get(index + 1).getFunction().equals("*") || equationParts.get(index + 1).getFunction().equals("/") || equationParts.get(index + 1).getFunction().equals("%"))
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
			case "+": // Addition
			{
				return num1 + num2;
			}
			case "-": // Subtraction
			{
				return num1 - num2;
			}
			case "*": // Multiplication
			{
				return num1 * num2;
			}
			case "/": // Division
			{
				return num1 / num2;
			}

			case "^": // Exponent
			{
				return Math.pow(num1, num2);
			}
			case "%": // Modulus
			{
				return num1 % num2;
			}

			case "&": // Binary AND
			{
				return (int) num1 & (int) num2;
			}
			case "|": // Binary OR
			{
				return (int) num1 | (int) num2;
			}
			case "⊕": // Binary XOR
			{
				return (int) num1 ^ (int) num2;
			}

			default:
			{
				System.out.println("Invalid function");
				return NaN;
			}
		}
	}

	public boolean isNumberOrPeriod(char input)
	{
		return isDigit(input) || input == '.';
	}
}