import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InformationGain
{
	public static void main(String args[]) throws IOException
	{
		String file = "data.csv";
		String line = "";
		String[] attributes;
		
		BufferedReader br = null;
		
		double entropy[];
		double informationGain[];
		
		try
		{
			br = new BufferedReader(new FileReader(file));
			
			line = br.readLine();
			attributes = line.split(",");
			entropy = new double[(attributes.length - 2)*2 + 1];
			informationGain = new double[attributes.length - 2];
			
			ArrayList<ArrayList<Integer>> data = new ArrayList<>();
			
			int followAction = 0, chaseAction = 0, eatAction = 0;

			while((line = br.readLine()) != null)
			{
				String[] dataRow = line.split(",");
				ArrayList<Integer> a = new ArrayList<>();
				for(int i = 1; i < dataRow.length - 1; i++)
				{
					a.add(Integer.parseInt(dataRow[i]));
				}
				
				switch(dataRow[dataRow.length - 1])
				{
					case "follow":
					{
						a.add(1);
						followAction++;
						break;
					}
					case "chase":
					{
						a.add(2);
						chaseAction++;
						break;
					}
					case "eat":
					{
						a.add(3);
						eatAction++;
						break;
					}
				}
				
				data.add(a);
			}
			
			System.out.println("follow: " + followAction + " chase: " + chaseAction + " eat: " + eatAction);
			double followFraction = ((double)followAction/data.size()); 
			double chaseFraction = ((double)chaseAction/data.size());
			double eatFraction = ((double)eatAction/data.size());
			entropy[0] = -followFraction * Math.log(followFraction)/Math.log(2);
			entropy[0] -= chaseFraction * Math.log(chaseFraction)/Math.log(2);
			entropy[0] -= eatFraction * Math.log(eatFraction)/Math.log(2);
			
			System.out.println("Entropy of Set of Actions: " + entropy[0] + '\n');
			
			int t0 = 0, t1 = 0, t2 = 0, t3 = 0;
			int followAction0 = 0, followAction1 = 0, chaseAction0 = 0, chaseAction1 = 0, eatAction0 = 0, eatAction1 = 0;
			int followAction2 = 0, followAction3 = 0, chaseAction2 = 0, chaseAction3 = 0, eatAction2 = 0, eatAction3 = 0;
			for(int i = 0; i < data.size(); i++)
			{
				ArrayList<Integer> a = data.get(i);
				if(a.get(0) == 1)
				{
					t0++;
					if(a.get(2) == 1)	
						followAction0++;
					else if(a.get(2) == 2)	
						chaseAction0++;
					else	
						eatAction0++;
				}
				else
				{
					t1++;
					if(a.get(2) == 1)	
						followAction1++;
					else if(a.get(2) == 2)	
						chaseAction1++;
					else	
						eatAction1++;
				}
				
				
				if(a.get(1) == 1)
				{
					t2++;
					if(a.get(2) == 1)	
						followAction2++;
					else if(a.get(2) == 2)	
						chaseAction2++;
					else	
						eatAction2++;
				}
				else
				{
					t3++;
					if(a.get(2) == 1)	
						followAction3++;
					else if(a.get(2) == 2)	
						chaseAction3++;
					else	
						eatAction3++;
				}
				
			}
			
			double followFraction0 = ((double)followAction0/t0); 
			double chaseFraction0 = ((double)chaseAction0/t0);
			double eatFraction0 = ((double)eatAction0/t0);
			
			double followFraction1 = ((double)followAction1/t1); 
			double chaseFraction1 = ((double)chaseAction1/t1);
			double eatFraction1 = ((double)eatAction1/t1);
			
			double followFraction2 = ((double)followAction2/t2); 
			double chaseFraction2 = ((double)chaseAction2/t2);
			double eatFraction2 = ((double)eatAction2/t2);
			
			double followFraction3 = ((double)followAction3/t3); 
			double chaseFraction3 = ((double)chaseAction3/t3);
			double eatFraction3 = ((double)eatAction3/t3);
			
			if(followFraction0 != 0)
				entropy[1] = -followFraction0 * Math.log(followFraction0)/Math.log(2);
			if(chaseFraction0 != 0)
				entropy[1] -= chaseFraction0 * Math.log(chaseFraction0)/Math.log(2);
			if(eatFraction0 != 0)	
				entropy[1] -= eatFraction0 * Math.log(eatFraction0)/Math.log(2);
			
			if(followFraction1 != 0)
				entropy[2] = -followFraction1 * Math.log(followFraction1)/Math.log(2);
			if(chaseFraction1 != 0)	
				entropy[2] -= chaseFraction1 * Math.log(chaseFraction1)/Math.log(2);
			if(eatFraction1 != 0)	
				entropy[2] -= eatFraction1 * Math.log(eatFraction1)/Math.log(2);
			
			System.out.println("E-inRange: " + entropy[1] + " E-outOfRange: " + entropy[2]);
			
			if(followFraction2 != 0)
				entropy[3] = -followFraction2 * Math.log(followFraction2)/Math.log(2);
			if(chaseFraction0 != 0)
				entropy[3] -= chaseFraction2 * Math.log(chaseFraction2)/Math.log(2);
			if(eatFraction0 != 0)	
				entropy[3] -= eatFraction2 * Math.log(eatFraction2)/Math.log(2);
			
			if(followFraction3 != 0)
				entropy[4] = -followFraction3 * Math.log(followFraction3)/Math.log(2);
			if(chaseFraction1 != 0)	
				entropy[4] -= chaseFraction3 * Math.log(chaseFraction3)/Math.log(2);
			if(eatFraction1 != 0)	
				entropy[4] -= eatFraction3 * Math.log(eatFraction3)/Math.log(2);
			
			System.out.println("E-WithinReach: " + entropy[3] + " E-notWithinReach: " + entropy[4] + '\n');
			
			informationGain[0] = entropy[0] - (double)(t0/data.size()) * entropy[1] - (double)(t1/data.size()) * entropy[2];
			informationGain[1] = entropy[0] - (double)(t2/data.size()) * entropy[3] - (double)(t3/data.size()) * entropy[4];
			
			System.out.println("IG for rangeCheck (range distance): " + informationGain[0]);
			System.out.println("IG for withinReach (eating distance): " + informationGain[1] + '\n');
			System.out.println("//choosing WithinReach (eating distance) as Root" + '\n');
			
			
			for(int i = 0; i < data.size(); i++)
			{
				ArrayList<Integer> a = data.get(i);
				if(a.get(1) == 0)
					if(a.get(0) == 1)
					{
						t0++;
						if(a.get(2) == 1)	
							followAction0++;
						else if(a.get(2) == 2)	
							chaseAction0++;
						else	
							eatAction0++;
					}
					else
					{
						t1++;
						if(a.get(2) == 1)	
							followAction1++;
						else if(a.get(2) == 2)	
							chaseAction1++;
						else	
							eatAction1++;
					}
				
			}
			
			followFraction0 = ((double)followAction0/t0); 
			chaseFraction0 = ((double)chaseAction0/t0);
			eatFraction0 = ((double)eatAction0/t0);
			
			followFraction1 = ((double)followAction1/t1); 
			chaseFraction1 = ((double)chaseAction1/t1);
			eatFraction1 = ((double)eatAction1/t1);
			
			if(followFraction0 != 0)
				entropy[1] = -followFraction0 * Math.log(followFraction0)/Math.log(2);
			if(chaseFraction0 != 0)
				entropy[1] -= chaseFraction0 * Math.log(chaseFraction0)/Math.log(2);
			if(eatFraction0 != 0)	
				entropy[1] -= eatFraction0 * Math.log(eatFraction0)/Math.log(2);
			
			if(followFraction1 != 0)
				entropy[2] = -followFraction1 * Math.log(followFraction1)/Math.log(2);
			if(chaseFraction1 != 0)	
				entropy[2] -= chaseFraction1 * Math.log(chaseFraction1)/Math.log(2);
			if(eatFraction1 != 0)	
				entropy[2] -= eatFraction1 * Math.log(eatFraction1)/Math.log(2);
			
			System.out.println("E-inRange: " + entropy[1] + " E-outOfRange: " + entropy[2]);
			informationGain[0] = entropy[0] - (double)(t0/data.size()) * entropy[1] - (double)(t1/data.size()) * entropy[2];
			System.out.println('\n' + "New Information Gain for range distance: " + informationGain[0]);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			br.close();
		}
	}
}