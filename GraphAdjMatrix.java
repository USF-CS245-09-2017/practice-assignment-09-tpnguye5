import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
//use prims

public class GraphAdjMatrix implements Graph{
	private Vertex[][] vertices_list; 
	
	class Vertex{
		int neighbor;
		int cost;
		boolean known;
		Vertex next; 
	}
	
	public GraphAdjMatrix(int vertices){
		vertices_list = new Vertex[vertices][vertices];
		
		for(int i = 0; i < vertices; i++){
			for(int j = 0; j < vertices; j++){
				Vertex v = new Vertex();
				vertices_list[i][j] = v;
				v.cost = 0;
				v.known = false;
			}
		}
	}
	
	public void addEdge(int v1, int v2){
		vertices_list[v1][v2].cost = 1;
	}
	public void topologicalSort(){
		
		/*
		 * First, we need to find a vertex with no incident edges.
		 * 
		 * We do this by keeping a count of incident edges of all the vertices in the graph.
		 */
		
		int[] NumIncident = new int[vertices_list.length];
		Queue<Integer> q = new LinkedList<Integer>();
		//Queue for BFS
		/*
		 * Then, instantiate a queue to keep track of the vertices with no edges pointing to it. 
		 * We want the vertices with edges pointing out.
		 */
		
		//We populate the array with 0s
		for (int v = 0; v < vertices_list.length; v++){
			NumIncident[v] = 0;
		}
		//We go through the matrix and look for vertices with a cost of 1 or not 0.
		//Then, we update out NumIncident array.
		for (int v = 0; v < vertices_list.length; v++){
			for (int u = 0; u < vertices_list[v].length; u++){
				if (vertices_list[v][u].cost != 0){
					NumIncident[u]++;
				}
			}
		}
		//Go through the vertices again, we check if the NumIncident array at some vertex
		//is 0. If it is, then we add it to the queue because that vertex has edges pointing 
		//away from it. 
		for (int v = 0; v < vertices_list.length; v++){
			if (NumIncident[v] == 0){
				q.add(v);
			}
		}
		//Instantiate array of int for our sort. 
		int[] topSortArr = new int[vertices_list.length];
		int count = 0;

		while (!q.isEmpty()){
			int v = q.poll();
			//add vertex to the topological sort
			topSortArr[count++] = v;
			for (int u = 0; u < vertices_list.length; u++){
				if (vertices_list[v][u].cost != 0){
					NumIncident[u]--; 
					if (NumIncident[u]== 0){
						q.add(u);
					}
				}
			}
			
		}
	}
	
	private int listLength(int vertex){
		int e = 0;
		
		for (int j = 0; j < vertices_list.length; j++){
			if (vertices_list[vertex][j].cost != 0){
				e++;
			}
		}
		return e; 
	}
	//----------------------------------------------------------------------
	//Returns:
	//An array of vertex IDs such that each ID represents a vertex which 
	//is the destination of the edge origination from the argument.
	//----------------------------------------------------------------------
	public int[] neighbors(int vertex){
		int[] result;		
		int count=0;
		result = new int[listLength(vertex)];
		
		for (int i = 0; i < vertices_list.length; i++){
			if (vertices_list[vertex][i].cost != 0){
				result[count] = i;
				count++;
			}
		}
		
		return result;
	}
	
	// These functions have been added to P9.
	public void addEdge(int v1, int v2, int weight){
		vertices_list[v1][v2].cost = weight;
		vertices_list[v2][v1].cost = weight;
	}
	
	public int getEdge(int v1, int v2){
		int weight;
		return weight = (vertices_list[v1][v2].cost != 0)? vertices_list[v1][v2].cost : -1;
	}
	
	//helper method that counts the number of edges for each vertex
	private int numEdges(int vertex){
		int num = 0;
		for (int j = 0; j < vertices_list.length; j++){
			int v_cost = vertices_list[vertex][j].cost;
			if (v_cost != 0){
				num++;	
			}
		}
		return num;
	}
	
	//Private method to find the smallest cost of a certain vertex
	private int smallestCost(int[] cost, boolean[] visited){
		int smallest = Integer.MAX_VALUE;
		int index = -1;
		
		for (int i = 0; i < vertices_list.length; i++){
			if (visited[i] == false && cost[i] < smallest){
				smallest = cost[i];
				index = i;
			}
		}
		return index;
	}
	//creates minspan tree and returns the cheapest cost.
	public int createSpanningTree(){
		int[] minSpan = new int[vertices_list.length];
		
		int[] cost = new int[vertices_list.length];

		boolean[] visited = new boolean[vertices_list.length];
		
		for (int j = 0; j< vertices_list.length;j++){
			cost[j] = Integer.MAX_VALUE;
			visited[j] = false;
		}
		
		cost[0] = 0;
		
		minSpan[0] = -1;
		
		for (int v = 0; v < vertices_list.length-1; v++){
			int u = smallestCost(cost, visited);
			visited[u] = true;
			
			for (int count = 0; count < vertices_list.length; count++){
				if (vertices_list[u][count].cost!= 0 && visited[count]==false && vertices_list[u][count].cost < cost[count]){
					minSpan[count] = u;
					cost[count] = vertices_list[u][count].cost;
				}
			}
			
		}
		int result = 0;
		for (int c: cost){
			result += c;
		}
		return result;
	}

}
