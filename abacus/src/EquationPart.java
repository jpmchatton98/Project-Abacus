public class EquationPart
{
	private double number;   // The number to perform the function on (e.g. 3, 5, 7, etc)
	private String function; // The function to perform on the number (e.g. +, -, *, etc)

	private EquationPart nextPart = null; // The next part in the equation
	private EquationPart prevPart; // The previous part in the equation

	EquationPart(double number, EquationPart prevPart)
	{
		this.number = number;
		this.prevPart = prevPart;
	}
	EquationPart(String function, EquationPart prevPart)
	{
		this.function = function;
		this.prevPart = prevPart;
	}
	EquationPart(double number, String function, EquationPart prevPart)
	{
		this.number = number;
		this.function = function;
		this.prevPart = prevPart;
	}
	EquationPart(double number, String function, EquationPart prevPart, EquationPart nextPart)
	{
		this.number = number;
		this.function = function;
		this.prevPart = prevPart;
		this.nextPart = nextPart;
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

	public EquationPart prev()
	{
		return prevPart;
	}
	public EquationPart next()
	{
		return nextPart;
	}
	public void setPrev(EquationPart part)
	{
		prevPart = part;
	}
	public void setNext(EquationPart part)
	{
		nextPart = part;
	}
}
