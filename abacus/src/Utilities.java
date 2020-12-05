import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Character.PARAGRAPH_SEPARATOR;
import static java.lang.Character.isDigit;
import static java.lang.Double.NaN;
import static java.lang.Double.parseDouble;

public class Utilities
{
	final String[] constants = {"π", "€", "ϕ", "ζ", "δ", "α", "γ", "λ", "Ќ", "Æ"};

	// Prepares the equation for parsing by trimming out spaces and replacing multi-character
	// functions with single characters.
	public String prepareEquation(String equation)
	{
		equation = equation.replace(" ", "");
		equation = equation.toLowerCase();

		// Binary Equations
		equation = equation.replace("<<"  , "<");
		equation = equation.replace(">>"  , ">");
		equation = equation.replace("xor" , "⊕");

		// Basic Functions (roots, logs, and absolute value)
		equation = equation.replace("sqrt", "√");
		equation = equation.replace("cbrt", "∛");
		equation = equation.replace("logn", "Ł");
		equation = equation.replace("log" , "Ĺ");
		equation = equation.replace("abs" , "Ä");

		// Rounding functions
		equation = equation.replace("ceil", "Ȼ");
		equation = equation.replace("flr", "Ƒ");
		equation = equation.replace("rnd", "Ř");

		// Trig Functions
		equation = equation.replace("asin", "š");
		equation = equation.replace("sinh", "Š");
		equation = equation.replace("sin" , "Ŝ");

		equation = equation.replace("acos", "č");
		equation = equation.replace("cosh", "Č");
		equation = equation.replace("cos" , "Ċ");

		equation = equation.replace("atan", "ť");
		equation = equation.replace("tanh", "Ť");
		equation = equation.replace("tan" , "Ŧ");

		equation = equation.replace("acsc", "ç");
		equation = equation.replace("csch", "ƈ");
		equation = equation.replace("csc" , "Ç");

		equation = equation.replace("acot", "ţ");
		equation = equation.replace("coth", "ƭ");
		equation = equation.replace("cot" , "Ţ");

		equation = equation.replace("asec", "ş");
		equation = equation.replace("sech", "ʂ");
		equation = equation.replace("sec" , "Ş");

		// Constants
		equation = equation.replace("pi"  , "π");
		equation = equation.replace("en"  , "€");
		equation = equation.replace("gr"  , "ϕ");
		equation = equation.replace("ap"  , "ζ");
		equation = equation.replace("fcd" , "δ");
		equation = equation.replace("fca" , "α");
		equation = equation.replace("em"  , "γ");
		equation = equation.replace("cc"  , "λ");
		equation = equation.replace("kc"  , "Ќ");
		equation = equation.replace("gk"  , "Æ");

		equation = "0+" + equation;

		return equation;
	}

	public double compileFunction(double num1, double num2, String function)
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
			case "%": // Modulus
			{
				return num1 % num2;
			}

			case "^": // Exponent
			{
				return Math.pow(num1, num2);
			}
			case "v": // Nth Root
			{
				return Math.pow(num2, (1/num1));
			}
			case "Ĺ": // Nth Log
			{
				return Math.log(num2) / Math.log(num1);
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
			case "<": // Left Shift
			{
				return (int) num1 << (int) num2;
			}
			case ">": // Right Shift
			{
				return (int) num1 >> (int) num2;
			}

			default:
			{
				System.out.println("Invalid function");
				return NaN;
			}
		}
	}

	public double completeFunction(double num, String function)
	{
		switch(function)
		{
			case "√": // Square Root
			{
				return Math.sqrt(num);
			}
			case "∛": // Cube Root
			{
				return Math.cbrt(num);
			}

			case "Ä": // Absolute Value
			{
				return Math.abs(num);
			}

			case "Ł": // Natural Log
			{
				return Math.log(num);
			}

			case "Ŝ": // Sine
			{
				return Math.sin(num);
			}
			case "Š": // Hyperbolic Sine
			{
				return Math.sinh(num);
			}
			case "š": // Arcsine
			{
				return Math.asin(num);
			}

			case "Ċ": // Cosine
			{
				return Math.cos(num);
			}
			case "Č": // Hyperbolic Cosine
			{
				return Math.cosh(num);
			}
			case "č": // Arccosine
			{
				return Math.acos(num);
			}

			case "Ŧ": // Tangent
			{
				return Math.tan(num);
			}
			case "Ť": // Hyperbolic Tangent
			{
				return Math.tanh(num);
			}
			case "ť": // Arctangent
			{
				return Math.atan(num);
			}

			case "Ş": // Secant
			{
				return 1 / Math.cos(num);
			}
			case "ʂ": // Hyperbolic Secant
			{
				return 1 / Math.cosh(num);
			}
			case "ş": // Arcsecant
			{
				return 1 / Math.acos(num);
			}

			case "Ç": // Cosecant
			{
				return 1 / Math.sin(num);
			}
			case "ƈ": // Hyperbolic Cosecant
			{
				return 1 / Math.sinh(num);
			}
			case "ç": // Arccosecant
			{
				return 1 / Math.asin(num);
			}

			case "Ţ": // Cotangent
			{
				return 1 / Math.tan(num);
			}
			case "ƭ": // Hyperbolic Cotangent
			{
				return 1 / Math.tanh(num);
			}
			case "ţ": // Arccotangent
			{
				return 1 / Math.atan(num);
			}

			case "Ȼ": // Ceiling
			{
				return Math.ceil(num);
			}
			case "Ƒ": // Floor
			{
				return Math.floor(num);
			}
			case "Ř": // Round
			{
				return Math.round(num);
			}

			default:
			{
				System.out.println("Invalid function.");
				return NaN;
			}
		}
	}

	// Returns true if the functions finds a digit, a period, a euro symbol (euler's number), or a lowercase pi (pi)
	public boolean isNumberOrPeriod(char input, ArrayList<Variable> variables)
	{
		return isDigit(input) || input == '.' || Arrays.toString(constants).contains(input + "") || isVariable(input, variables);
	}

	public boolean isVariable(char input, ArrayList<Variable> variables)
	{
		for (Variable variable : variables)
		{
			if (input == variable.getName())
			{
				return true;
			}
		}
		return false;
	}
	public boolean contVariable(String input, ArrayList<Variable> variables)
	{
		for(int i = 0; i < input.length(); i++)
		{
			if(isVariable(input.charAt(i), variables))
			{
				return true;
			}
		}
		return false;
	}
}