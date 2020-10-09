import java.util.ArrayList;

import static java.lang.Character.isDigit;
import static java.lang.Double.NaN;
import static java.lang.Double.parseDouble;

// This class contains code for the core parsing algorithm
public class Parser
{
	Utilities util = new Utilities();

	// Core parsing function.  Accepts the equation string as an input and outputs the answer
	// in the form of a double.
	public double parse(String equation)
	{
		try
		{
			String lastFunction = null;
			equation = util.prepareEquation(equation);
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

						if(util.isNumberOrPeriod(equation.charAt(pStart - 1)))
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

				if (util.isNumberOrPeriod(equation.charAt(i)))
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

	private void completeFunctions(ArrayList<EquationPart> equationParts)
	{
		boolean done = false;

		while (!done)
		{
			for (EquationPart equationPart : equationParts)
			{
				done = equationPart.getFunction().length() <= 1;

				if(!done)
				{
					String lastChar = equationPart.getFunction().charAt(equationPart.getFunction().length() - 1) + "";

					equationPart.setFunction(equationPart.getFunction().substring(0, equationPart.getFunction().length() - 1));
					equationPart.setNumber(util.completeFunction(equationPart.getNumber(), lastChar));
				}
			}
		}

	}

	private double compileEquation(ArrayList<EquationPart> equationParts)
	{
		completeFunctions(equationParts);
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

				double newNumber = util.compileFunction(part.getNumber(), nextPart.getNumber(), nextPart.getFunction());
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

				double newNumber = util.compileFunction(part.getNumber(), nextPart.getNumber(), nextPart.getFunction());
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

			double newNumber = util.compileFunction(part.getNumber(), nextPart.getNumber(), nextPart.getFunction());
			EquationPart newPart = new EquationPart(newNumber, part.getFunction());

			equationParts.remove(part);
			equationParts.remove(nextPart);
			equationParts.add(index, newPart);
		}

		return equationParts.get(0).getNumber();
	}
}