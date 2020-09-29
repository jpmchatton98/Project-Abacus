import java.util.ArrayList;
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
							equationParts.get(equationParts.size() - 1).setNumber(parseDouble(number));
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
				equationParts.get(equationParts.size() - 1).setNumber(parseDouble(number));
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

		return equation;
	}

	private double compileEquation(ArrayList<EquationPart> equationParts)
	{
		int index = 0;
		while(equationParts.size() > 1)
		{
			if(equationParts.get(index + 1).getFunction().equals("^") || equationParts.get(index + 1).getFunction().contains("√"))
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
			case "+√":
			{
				return num1 + Math.sqrt(num2);
			}
			case "-√":
			{
				return num1 - Math.sqrt(num2);
			}
			case "√":
			case "*√":
			{
				return num1 * Math.sqrt(num2);
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