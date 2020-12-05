import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Double.NaN;

// This class contains code for the core parsing algorithm
public class Parser
{
	Utilities util = new Utilities();
	final String[] functions = new String[]{"√", "∛", "Ł", "Ä", "š", "Š", "Ŝ", "č", "Č", "Ċ", "ť", "Ť", "Ŧ", "ç", "ƈ", "Ç", "ţ", "ƭ", "Ţ", "ş", "ʂ", "Ş", "Ȼ", "Ƒ", "Ř"};
	final String[] constants = {"π", "€", "ϕ", "ζ", "δ", "α", "γ", "λ", "Ќ", "Æ"};

	private ArrayList<Variable> variables;

	// Core parsing function.  Accepts the equation string as an input and outputs the answer
	// in the form of a double.
	public double parse(String equation, ArrayList<Variable> var)
	{
		variables = var;

		try
		{
			String lastFunction = null;
			equation = util.prepareEquation(equation);
			ArrayList<EquationPart> equationParts = new ArrayList<>();

			String number = "";
			String function = "";
			EquationPart curr;

			boolean startingNegative = false;
			boolean startingComplement = false;
			int mode = 0; // 0: Number, 1: Function

			for (int i = 0; i < equation.length(); i++)
			{
				if (i == 0 && equation.charAt(i) == '-') // Starting negative
				{
					startingNegative = true;
					continue;
				}
				if (i == 0 && equation.charAt(i) == '~') // Starting Complement
				{
					startingComplement = true;
					continue;
				}

				if (equation.charAt(i) == '(') // If equation contains an opening parenthesis
				{
					int pTally = 0; // Tally of parenthetic blocks, so that we don't match them up incorrectly
					int pStart = i; // Starting index of the parentheses
					int pEnd = 0; // Ending index of the parentheses

					for (i = i + 1; i < equation.length(); i++)
					{
						if (equation.charAt(i) == '(')
						{
							pTally++;
						}
						else if (equation.charAt(i) == ')')
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

					if (pTally != 0)
					{
						throw new NumberFormatException();
					}
					else
					{
						String p = equation.substring(pStart, pEnd + 1);
						String pTrim = p.substring(1, p.length() - 1);
						equation = equation.replace(p, parse(pTrim, variables) + "");

						if (util.isNumberOrPeriod(equation.charAt(pStart - 1), variables))
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

				if (util.isNumberOrPeriod(equation.charAt(i), variables))
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
							if (function.length() > 1)
							{
								if (function.charAt(function.length() - 1) == '-')
								{
									equationParts.get(equationParts.size() - 1).setNumber("-" + number);
									equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
											.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));
									if (equationParts.get(equationParts.size() - 1).getFunction().charAt(equationParts.get(equationParts.size() - 1).getFunction().length() - 1) == '~')
									{
										equationParts.get(equationParts.size() - 1).setNumber(~Integer.parseInt(equationParts.get(equationParts.size() - 1).getNumberString()) + "");
										equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
												.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));
									}
								}
								else if (function.charAt(function.length() - 1) == '~')
								{
									equationParts.get(equationParts.size() - 1).setNumber(~Integer.parseInt(number) + "");
									equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
											.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));
									if (equationParts.get(equationParts.size() - 1).getFunction().charAt(equationParts.get(equationParts.size() - 1).getFunction().length() - 1) == '-')
									{
										equationParts.get(equationParts.size() - 1).setNumber("-" + equationParts.get(equationParts.size() - 1).getNumberString());
										equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
												.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));
									}
								}
								else
								{
									equationParts.get(equationParts.size() - 1).setNumber(number);
								}
							}
							else
							{
								equationParts.get(equationParts.size() - 1).setNumber(number);
							}
							function = "";
						}
						else // First Element
						{
							if (startingNegative)
							{
								number = "-" + number;
								equationParts.add(new EquationPart(number, "+"));
							}
							else if (startingComplement)
							{
								equationParts.add(new EquationPart(~Integer.parseInt(number) + "", "+"));
							}
							else
							{
								equationParts.add(new EquationPart(number, "+"));
							}
						}
						number = "";
					}

					mode = 1;
					function += equation.charAt(i);
				}
			}

			if(lastFunction != null)
			{
				if (lastFunction.length() > 1)
				{
					if (lastFunction.charAt(lastFunction.length() - 1) == '-')
					{
						equationParts.get(equationParts.size() - 1).setNumber("-" + number);
						equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
								.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));
						if (equationParts.get(equationParts.size() - 1).getFunction().charAt(equationParts.get(equationParts.size() - 1).getFunction().length() - 1) == '~')
						{
							equationParts.get(equationParts.size() - 1).setNumber(~Integer.parseInt(equationParts.get(equationParts.size() - 1).getNumberString()) + "");
							equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
									.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));
						}
					}
					else if (lastFunction.charAt(lastFunction.length() - 1) == '~')
					{
						equationParts.get(equationParts.size() - 1).setNumber(~Integer.parseInt(number) + "");
						equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
								.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));

						if (equationParts.get(equationParts.size() - 1).getFunction().charAt(equationParts.get(equationParts.size() - 1).getFunction().length() - 1) == '-')
						{
							equationParts.get(equationParts.size() - 1).setNumber("-" + equationParts.get(equationParts.size() - 1).getNumberString());
							equationParts.get(equationParts.size() - 1).setFunction(equationParts.get(equationParts.size() - 1).getFunction()
									.substring(0, equationParts.get(equationParts.size() - 1).getFunction().length() - 1));
						}
					}
					else
					{
						equationParts.get(equationParts.size() - 1).setNumber(number);
					}
				}
				else
				{
					equationParts.get(equationParts.size() - 1).setNumber(number);
				}

				return compileEquation(equationParts);
			}
			else
			{
				throw new NumberFormatException();
			}
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

				if (!done)
				{
					String lastChar = equationPart.getFunction().charAt(equationPart.getFunction().length() - 1) + "";

					equationPart.setFunction(equationPart.getFunction().substring(0, equationPart.getFunction().length() - 1));
					equationPart.setNumber(util.completeFunction(equationPart.getNumber(), lastChar) + "");
				}
				else
				{
					if(Arrays.toString(functions).contains(equationPart.getFunction())) //Checks to see if the equationpart function is 1 character, but that character is a recognized function
					{
						equationPart.setNumber(util.completeFunction(equationPart.getNumber(), equationPart.getFunction()) + "");
						equationPart.setFunction("*");
					}
				}
			}
		}
	}

	private void parseConstants(ArrayList<EquationPart> equationParts)
	{
		boolean done = false;

		for(EquationPart equationPart : equationParts)
		{
			if(Arrays.toString(constants).contains(equationPart.getNumberString()))
			{
				switch (equationPart.getNumberString())
				{
					case "π": // Pi
					{
						equationPart.setNumber(Math.PI + "");
						break;
					}
					case "€": // Euler's Number
					{
						equationPart.setNumber(Math.exp(1) + "");
						break;
					}
					case "ϕ": // Golden Ratio
					{
						equationPart.setNumber(2 * Math.sin((54 * Math.PI) / 180) + "");
						break;
					}
					case "ζ": // Apery's Constant
					{
						equationPart.setNumber("1.202056903159594");
						break;
					}
					case "α": // First Feigenbaum Constant
					{
						equationPart.setNumber("2.502907875095893");
						break;
					}
					case "δ": // Second Feigenbaum Constant
					{
						equationPart.setNumber("4.669201609102990");
						break;
					}
					case "γ": // Euler–Mascheroni Constant
					{
						equationPart.setNumber("0.577215664901533");
						break;
					}
					case "λ": // Conway's Constant
					{
						equationPart.setNumber("1.303577269034296");
						break;
					}
					case "Ќ": // Khinchin’s Constant
					{
						equationPart.setNumber("2.6854520010");
						break;
					}
					case "Æ": // Glaisher–Kinkelin Constant
					{
						equationPart.setNumber("1.2824271291");
						break;
					}
					default:
					{
						System.out.println("Invalid constant");
						break;
					}
				}
			}
			else if(util.isVariable(equationPart.getNumberString().charAt(0), variables))
			{
				String val = "";
				for (Variable variable : variables)
				{
					if (variable.getName() == equationPart.getNumberString().charAt(0))
					{
						val = variable.getValue() + "";
						break;
					}
				}
				equationPart.setNumber(val);
			}
		}

		while (!done)
		{
			for (int i = 0; i < equationParts.size(); i++)
			{
				if(equationParts.get(i).getNumberString().length() > 1)
				{
					done = !(Arrays.toString(constants).contains(equationParts.get(i).getNumberString().charAt(equationParts.get(i).getNumberString().length() - 1) + "") || util.contVariable(equationParts.get(i).getNumberString(), variables));
					System.out.println("test");
					if (!done)
					{
						String constant = equationParts.get(i).getNumberString().charAt(equationParts.get(i).getNumberString().length() - 1) + "";

						switch(constant)
						{
							case "π": // Pi
							{
								constant = Math.PI + "";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							case "€": // Euler's Number
							{
								constant = Math.exp(1) + "";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							case "ϕ": // Golden Ratio
							{
								constant = 2 * Math.sin((54 * Math.PI) / 180) + "";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							case "ζ": // Apery's Constant
							{
								constant = "1.202056903159594";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							case "α": // First Feigenbaum Constant
							{
								constant = "2.502907875095893";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							case "δ": // Second Feigenbaum Constant
							{
								constant = "4.669201609102990";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							case "γ": // Euler–Mascheroni Constant
							{
								constant = "0.577215664901533";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							case "λ": // Conway's Constant
							{
								constant = "1.303577269034296";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							case "Ќ": // Khinchin’s Constant
							{
								constant = "2.6854520010";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							case "Æ": // Glaisher–Kinkelin Constant
							{
								constant = "1.2824271291";
								equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));

								EquationPart newPart = new EquationPart(constant, "*");
								equationParts.add(i + 1, newPart);
								break;
							}
							default:
							{
								break;
							}
						}

						String val = "";
						if(util.contVariable(equationParts.get(i).getNumberString(), variables))
						{
							for (Variable variable : variables)
							{
								if (variable.getName() == equationParts.get(i).getNumberString().charAt(equationParts.get(i).getNumberString().length() - 1))
								{
									equationParts.get(i).setNumber(equationParts.get(i).getNumberString().substring(0, equationParts.get(i).getNumberString().length() - 1));
									val = variable.getValue() + "";
									break;
								}
							}

							EquationPart newPart = new EquationPart(val, "*");
							equationParts.add(i + 1, newPart);
						}
					}
				}
				else
				{
					done = true;
				}
			}
		}
	}

	private double compileEquation(ArrayList<EquationPart> equationParts)
	{
		parseConstants(equationParts);
		completeFunctions(equationParts);

		int index = equationParts.size() - 1;
		while(equationParts.size() > 1)
		{
			if(equationParts.get(index - 1).getFunction().equals("^"))
			{
				EquationPart part = equationParts.get(index);
				EquationPart nextPart = equationParts.get(index - 1);

				double newNumber = util.compileFunction(part.getNumber(), nextPart.getNumber(), nextPart.getFunction());
				EquationPart newPart = new EquationPart(newNumber + "", part.getFunction());

				equationParts.remove(part);
				equationParts.remove(nextPart);
				equationParts.add(index - 1, newPart);
			}

			index--;
			if(index <= 0)
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
				EquationPart newPart = new EquationPart(newNumber + "", part.getFunction());

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
			EquationPart newPart = new EquationPart(newNumber + "", part.getFunction());

			equationParts.remove(part);
			equationParts.remove(nextPart);
			equationParts.add(index, newPart);
		}

		return equationParts.get(0).getNumber();
	}
}