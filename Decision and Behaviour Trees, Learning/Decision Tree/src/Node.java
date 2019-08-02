public class Node
	{
		int ID; 					//nodeInteger
		Edge parent; 				//Edge to retrace path
		int csf; 					//costSoFar
		int etc; 					//estimatedTotalCost
		int category; 				// -1 for closed, 0 for unvisited, 1 for open


		Node(int ident)
		{
			this.ID = ident;
			this.parent = new Edge();
			this.csf = 0;
			this.etc = 0;
			this.category = 0;

		}

		int getID()
		{
			return this.ID;
		}

		Edge getParent()
		{
			return this.parent;
		}

		void setParent(Edge parent)
		{
			this.parent = parent;
		}

		int getCSF()
		{
			return this.csf;
		}

		void setCSF(int c)
		{
			this.csf = c; 
		}

		int getETC()
		{
			return this.etc;
		}

		void setETC(int e)
		{
			this.etc = e;
		}

		int getCategory()
		{
			return this.category;
		}

		void setCategory(int category)
		{
			this.category = category;
		}


	}