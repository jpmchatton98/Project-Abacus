public class EquationPart
{
	private double number;   // The number to perform the function on (e.g. 3, 5, 7, etc)
	private char   function; // The function to perform on the number (e.g. +, -, *, etc)

	EquationPart(double number)
	{
		this.number = number;
	}
	EquationPart(char function)
	{
		this.function = function;
	}

	public double getNumber()
	{
		return number;
	}
	public void   setNumber(double number)
	{
		this.number = number;
	}

	public char   getFunction()
	{
		return function;
	}
	public void   setFunction(char function)
	{
		this.function = function;
	}
}
