public class EquationPart
{
	private double number;   // The number to perform the function on (e.g. 3, 5, 7, etc)
	private String function; // The function to perform on the number (e.g. +, -, *, etc)

	EquationPart(String function)
	{
		this.function = function;
	}
	EquationPart(double number, String function)
	{
		this.number = number;
		this.function = function;
	}

	public double getNumber()
	{
		return number;
	}
	public void setNumber(double number)
	{
		this.number = number;
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
