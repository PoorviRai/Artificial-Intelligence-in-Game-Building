class Edge
	{
		int weight;
		int source;
		int destination;

		Edge()
		{
			weight = 0;
			source = -1;
			destination = -1;
		}

		int getWeight()
		{
			return this.weight;
		}

		int getSource()
		{
			return this.source;
		}

		int getDestination()
		{
			return this.destination;
		}

		void setWeight(int wt)
		{
			this.weight = wt;
		}

		void setSource(int src)
		{
			this.source = src;
		}

		void setDestination(int dst)
		{
			this.destination = dst;
		}
	}