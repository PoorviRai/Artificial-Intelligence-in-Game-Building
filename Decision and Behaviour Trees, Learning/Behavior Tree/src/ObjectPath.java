import java.util.ArrayList;

import processing.core.PVector;

class ObjectPath
{
	ArrayList<PVector> objectPath;
	float pathRadius;

	ObjectPath()
	{
		this.objectPath = new ArrayList<>();
		this.pathRadius = 20f;
	}

	PVector getStart()
	{
		return this.objectPath.get(0);
	}

	void add(int x, int y)
	{
		this.objectPath.add(new PVector(x, y));
	}

	PVector getEnd()
	{
		return this.objectPath.get(this.objectPath.size() - 1);
	}
}