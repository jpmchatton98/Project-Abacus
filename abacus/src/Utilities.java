import static java.lang.Character.isDigit;
import static java.lang.Double.NaN;
import static java.lang.Double.parseDouble;

public class Utilities
{
	// Prepares the equation for parsing by trimming out spaces and replacing multi-character
	// functions with single characters.
	public String prepareEquation(String equation)
	{
		equation = equation.replace(" ", "");
		equation = equation.toLowerCase();

		// Basic Functions (roots, logs, and absolute value)
		equation = equation.replace("sqrt", "√");
		equation = equation.replace("cbrt", "∛");
		equation = equation.replace("logn", "Ł");
		equation = equation.replace("log" , "L");
		equation = equation.replace("abs" , "A");

		// Trig Functions
		equation = equation.replace("asin", "š");
		equation = equation.replace("sinh", "Š");
		equation = equation.replace("sin" , "S");

		equation = equation.replace("acos", "č");
		equation = equation.replace("cosh", "Č");
		equation = equation.replace("cos" , "C");

		equation = equation.replace("atan", "ť");
		equation = equation.replace("tanh", "Ť");
		equation = equation.replace("tan" , "T");

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
		equation = equation.replace("e"   , "€");

		equation = "0+" + equation;

		System.out.println(equation);

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

			case "A": // Absolute Value
			{
				return Math.abs(num);
			}

			case "L": // Log base 10
			{
				return Math.log10(num);
			}
			case "Ł": // Natural Log
			{
				return Math.log(num);
			}

			case "S": // Sine
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

			case "C": // Cosine
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

			case "T": // Tangent
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

			default:
			{
				System.out.println("Invalid function.");
				return NaN;
			}
		}
	}

	public double parseNum(String input)
	{
		if(input.contains("€") || input.contains("π"))
		{
			return parseConstants(input);
		}
		else
		{
			return parseDouble(input);
		}
	}

	public double parseConstants(String input)
	{
		//TODO: Create constant parsing algorithm
		return NaN;
	}

	// Returns true if the functions finds a digit, a period, a euro symbol (euler's number), or a lowercase pi (pi)
	public boolean isNumberOrPeriod(char input)
	{
		return isDigit(input) || input == '.' || input == '€' || input == 'π';
	}
}