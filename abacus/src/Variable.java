public class Variable
{
	private final char name;
	private final double value;

	Variable(char name, double value)
	{
		this.name = name;
		this.value = value;
	}

	public char getName()
	{
		return name;
	}

	public double getValue()
	{
		return value;
	}
}
