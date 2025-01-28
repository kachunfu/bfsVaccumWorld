package week2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BFS {
	
	static Map<Integer, Boolean> dirtLeft;
	static Map<Integer, Boolean> dirtRight;
	static Map<Integer, String> vaccumPosition;
	static Map<String, Map<Integer, Integer>> actionMaps;
	
	static Map<Integer, Integer> actionLeft;
	static Map<Integer, Integer> actionRight;
	static Map<Integer, Integer> actionSuck;

	static List<Integer> states;
	
	//from destination to initial state 
	static Map<Integer, Map<Integer,String>> actions;
	static Integer dest;
	
	boolean goalTest(int i)
	{
		return i==7 || i==8;
	}
		
	Map<Integer, Integer> BFSprev(int start)
	{
		Map<Integer, Integer> dist = new HashMap<>();
		Map<Integer, Integer> prev = new HashMap<>();
		Deque<Integer> frontier = new ArrayDeque<>();
		List<Integer> explored = new ArrayList<>();
		
		actions = new HashMap<>();
		
		for (Integer state: states)
		{
			dist.put(state, Integer.MAX_VALUE);
			prev.put(state, null);
			actions.put(state, new HashMap<Integer,String>());
		}
		
		dist.put(start, 0);
		actions.put(start, null);
		
		frontier.add(start);
		while (!frontier.isEmpty())
		{
			int state = frontier.remove();
			System.out.println("State: " + state + " is being explored.");
			explored.add(state);
			
			for(Entry<String, Map<Integer, Integer>> actionMap: actionMaps.entrySet())
			{
				int child = actionMap.getValue().get(state);
				System.out.println(state + " " + actionMap.getKey() + " " + child);
				
				if(!explored.contains(child) || frontier.contains(child))
				{
					frontier.add(child);
					dist.put(child, dist.get(state)+1);
					prev.put(child, state);
					
					actions.get(child).put(state, actionMap.getKey());
					
					if(goalTest(child))
					{
						dest = child;
						return prev;
					}
				}
			}
		}
		return null;
	}
	
	List<Integer> reconstructPath(int start, int destination, Map<Integer,Integer> prev)
	{
		List<Integer>result = new ArrayList<>();
		Integer current = destination;
		
		if(prev!=null)
		{
			while(current != null)
			{
				result.add(current);
				System.out.println("result added " + current);
			
				current = prev.get(current);
			}
		}
		return reverse(result);
	}
	
	List<Integer> reverse(List<Integer> list)
	{
		List<Integer> reversed = new ArrayList<>();
		for(int i = list.size()-1; i>= 0; i--)
			reversed.add(list.get(i));
		
		return reversed;
	}
	
	List<String> reverseAction(List<Integer> list)
	{
		List<String> actionSeqReversed = new ArrayList<>();
		List<String> actionSeq = new ArrayList<>();
		List<Integer> listReversed = reverse(list);
		
		for(int i = 1 ; i < listReversed.size() ; i++)
			actionSeqReversed.add(actions.get(listReversed.get(i-1)).get(listReversed.get(i)));
		
		for(int i = actionSeqReversed.size()-1; i>=0; i--)
			actionSeq.add(actionSeqReversed.get(i));
		return actionSeq;
	}
	
	void printResult(List<Integer> result)
	{
		if(!result.isEmpty())
		{
			
			List<String> actionSeq = reverseAction(result);
			
			for(int i = 0; i < result.size(); i++)
			{
				if(i == result.size()-1)
					System.out.println(result.get(i));
				else
					System.out.print(result.get(i) + " " + actionSeq.get(i) + "\n");
			}
		}
		else
			System.out.println("result is Empty");
	}
	
	public static void main(String[] args) {
		dirtLeft = new HashMap<>(); 
		dirtRight = new HashMap<>();
		vaccumPosition = new HashMap<>();
		actionLeft = new HashMap<>();
		actionRight = new HashMap<>();
		actionSuck = new HashMap<>();
		states = new ArrayList<>();
		actionMaps = new HashMap<>();
		
		for(int i = 1; i <= 8; i++)
		{
			switch(i)
			{
			case 1, 2 -> 
				{
					dirtLeft.put(i,true);
					dirtRight.put(i,true);
					vaccumPosition.put(i,i%2==0 ? "right":"left");
					actionLeft.put(i, 1);
					actionRight.put(i, 2);
					actionSuck.put(i, i==1? 3:6);
					
				}
			case 3,4 -> 
				{
					dirtLeft.put(i,false);
					dirtRight.put(i,true);
					vaccumPosition.put(i, i%2==0 ? "right":"left");
					actionLeft.put(i, 3);
					actionRight.put(i, 4);
					actionSuck.put(i, i==3? 3:8);
				}
			case 5,6 ->
				{
					dirtLeft.put(i, true);
					dirtRight.put(i,  false);
					vaccumPosition.put(i, i%2==0? "right":"left");
					actionLeft.put(i,  5);
					actionRight.put(i,  6);
					actionSuck.put(i, i==5? 7:6);
				}
			case 7,8 ->
				{
					dirtLeft.put(i, false);
					dirtRight.put(i, false);
					vaccumPosition.put(i,  i%2==0? "right":"left");
					actionLeft.put(i, 7);
					actionRight.put(i, 8);
					actionSuck.put(i, i==7? 7:8);
				} 
				
			}
			states.add(i);
		}
		actionMaps.put("left", actionLeft);
		actionMaps.put("right", actionRight);
		actionMaps.put("suck", actionSuck);
		BFS bfs = new BFS();
		
		Map<Integer, Integer> prev =  bfs.BFSprev(2);
		
		List<Integer> result = bfs.reconstructPath( 2, dest, prev);
		System.out.println("===========================");
		System.out.println("The result is :");
		bfs.printResult(result);
	}
}
