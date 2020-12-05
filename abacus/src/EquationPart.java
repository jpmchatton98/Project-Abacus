import static java.lang.Double.parseDouble;

public class EquationPart
{
	private String number;   // The number to perform the function on (e.g. 3, 5, 7, etc)
	private String function; // The function to perform on the number (e.g. +, -, *, etc), can be multiple characters

	EquationPart(String function)
	{
		this.function = function;
	}

	EquationPart(String number, String function)
	{
		this.number = number;
		this.function = function;
	}

	public double getNumber()
	{
		return parseDouble(number);
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getNumberString()
	{
		return number;
	}

	public String getFunction()
	{
		return function;
	}

	public void setFunction(String function)
	{
		this.function = function;
	}
}
