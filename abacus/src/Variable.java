public class Variable
{
	private char name;
	private double value;

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

	public void setName(char name)
	{
		this.name = name;
	}
	public void setValue(double value)
	{
		this.value = value;
	}
}
