import java.util.*;
import java.io.*;
class Set{
	int N;
	int[] parent;int rank[];
	Set(int N){
		this.N=N;
		parent=new int[N];
		rank=new int[N];
		for(int i=0;i<N;i++){
			parent[i]=i;rank[i]=0;
		}
	}
	int find_set(int node){
		if(parent[node]!=node)
			parent[node]=find_set(parent[node]);
		return parent[node];
	}
	void union(int a,int b){
		int pa=find_set(a);
		int pb=find_set(b);
		if(pa==pb) return;
		if(rank[pa]>rank[pb]){
			parent[pa]=pb;
			rank[pb]++;
		}
		else{
			parent[pb]=pa;
			rank[pa]++;
		}
	}
	void reset(){
		for(int i=0;i<N;i++){
			parent[i]=i;
			rank[i]=0;
		}
	}
}
		
class Node{
	int next,weight;boolean ispresent;
	Node(int x,int y,boolean p){next=x;weight=y;ispresent=p;}
	Node(int x,int y){next=x;weight=y;ispresent=true;}
}

class Edge implements Comparable<Edge>{
	int u,v,weight;
	Edge(int x,int y,int w){u=x;v=y;weight=w;}
	public int compareTo(Edge obj){
		return this.weight-obj.weight;
	}
}

class Distance implements Comparable<Distance>{
	int vertex,distance;
	Distance(int v,int d){vertex=v;distance=d;}
	public int compareTo(Distance obj){
		return this.distance-obj.distance;
	}
}
@SuppressWarnings("unchecked")
class Graph{
	int N;
	ArrayList<Node> adj[];
	ArrayList<Edge> edgeList;
	Graph(int n){
		N=n+1;
		adj=new ArrayList[n+1];
		for(int i=0;i<=n;adj[i++]=new ArrayList<Node>());
		edgeList=new ArrayList<Edge>();
	}
	void push_edge(int x,int y,int w){
		adj[x].add(0,new Node(y,w));
		//adj[y].add(0,new Node(x,w));
		edgeList.add(new Edge(x,y,w));
	}
	void dfsCall(int v,boolean visited[]){
		visited[v]=true;
		for(int i=0;i<adj[v].size();i++)
			if(!visited[adj[v].get(i).next])
				dfsCall(adj[v].get(i).next,visited);
	}
	void dfs(int src){
		boolean visited[]=new boolean[N];
		dfsCall(src,visited);
	}
	int bfs(int src,int dest){
		HashMap<Integer,Integer> hm=new HashMap<Integer,Integer>();
		Queue<Integer> queue=new LinkedList<Integer>();
		boolean visited[]=new boolean[N];
		queue.offer(src);
		visited[src]=true;
		hm.put(src,-1);
		while(!queue.isEmpty()){
			int cur=queue.poll();
			//if(visited[cur]) continue;
			visited[cur]=true;
			for(int i=0;i<adj[cur].size();i++){
				int next=adj[cur].get(i).next;
				if(!visited[next]){
					queue.offer(next);
					visited[next]=true;
					hm.put(next,cur);
				}
			}
		}
		if(!visited[dest]) return -1;
		ArrayList<Integer> path=new ArrayList<Integer>();
		int key=dest;
		do{
			path.add(key);
			key=hm.get(key);
		}while(key!=src);
		path.add(src);
		return path.size()-1;
	}
	void tSort(){
	
	}
	boolean isConnected(int src,int dest){
		boolean visited[]=new boolean[N];
		dfsCall(src,visited);
		return (visited[src] && visited[dest]);
	}
	void SCC(){
		}
	int MST(ArrayList<Edge> mst){
		Set sets=new Set(N);
		Collections.sort(edgeList);
		int mstWeight=0;
		for(Edge e:edgeList){
			int a=e.u;int b=e.v;
			if(sets.find_set(a)!=sets.find_set(b)){
				sets.union(a,b);
				mst.add(e);
				mstWeight+=e.weight;
			}
		}
		sets.reset();
		return mstWeight;
	}
	int secondMST(){
		ArrayList<Edge> mst=new ArrayList<Edge>();
		int mstWeight=MST(mst);
		int secondMST=Integer.MAX_VALUE;
		for(Edge e:mst){
			edgeList.remove(e);
			ArrayList<Edge> arr=new ArrayList<Edge>();
			int mst2=MST(arr);
			System.out.println("mst2:"+mst2);
			if(mst2<secondMST)
				secondMST=mst2;
			edgeList.add(e);
		}
		return secondMST;
	}
	void bellmanFord(int src,int result[]){
		HashMap<Integer,Integer> pathCost=new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> parent=new HashMap<Integer,Integer>();
		pathCost.put(src,0);
		parent.put(src,-1);
		for(int i=0;i<N;i++)
			if(i!=src)
				pathCost.put(i,Integer.MAX_VALUE);
		for(int i=1;i<=N;i++){
			for(Edge e:edgeList){
				int u=e.u;int v=e.v;int weight=e.weight;
				if(pathCost.get(v)>pathCost.get(u)+weight && pathCost.get(u)!=Integer.MAX_VALUE){
					pathCost.put(v,pathCost.get(u)+weight);
					parent.put(v,u);
				}
			}
		}
		for(int i=1;i<N;i++)
			result[i]=pathCost.get(i);
		                                /*CYCLE DETECTION*/
		for(Edge e:edgeList){
			if(pathCost.get(e.v)>pathCost.get(e.u)+e.weight && pathCost.get(e.u)!=Integer.MAX_VALUE){
				System.out.println("Not Possible");
				System.exit(0);
			}
		}
	}
	void floydWarshalls(){
		int inf=99999999;
		int shortestpath[][]=new int[N][N];
		int thepath[][]=new int[N][N];
		for(int[] row:shortestpath) Arrays.fill(row,inf);
		for(Edge e:edgeList)
			shortestpath[e.u][e.v]=shortestpath[e.v][e.u]=e.weight;
		
		for(int i=1;i<N;i++)
			for(int j=1;j<N;j++)
				if(i!=j && shortestpath[i][j]!=inf)   //check
					thepath[i][j]=i;
				else
					thepath[i][j]=-1;
				
		for(int k=1;k<N;k++)
			for(int i=1;i<N;i++)
				for(int j=1;j<N;j++)
					if(shortestpath[i][j]>shortestpath[i][k]+shortestpath[k][j]){
						shortestpath[i][j]=shortestpath[i][k]+shortestpath[k][j];
						thepath[i][j]=thepath[k][j];
					}
		ArrayList<Integer> path=new ArrayList<Integer>();
		int src=1,dest=N-1;
		while(path[src][dest]!=src){
			path.add(0,dest);
			dest=path[src][dest];
		}
		path.add(0,src);
	}
	void dijkstra(int src,int pathCost[]){
		boolean visited[]=new boolean[N];
		PriorityQueue<Distance> pq=new PriorityQueue<Distance>();
		pq.offer(new Distance(src,0));
		//for(int i=0;i<N;i++)
			//if(i!=src)
				//pq.offer(new Distance(i,99999999));
		while(!pq.isEmpty()){
			Distance top=pq.poll();
			int cur=top.vertex;int dist=top.distance;
			if(visited[cur]) continue;
			pathCost[cur]=dist;visited[cur]=true;
			for(int i=0;i<adj[cur].size();i++){
				Node nd=adj[cur].get(i);
				if(!visited[nd.next])   //	MAY RAISE TLE !!!!!!!
					pq.offer(new Distance(nd.next,nd.weight+pathCost[cur]));
			}
		}
	}
	void print(){
		for(int i=0;i<N;i++){
			System.out.print(i+":");
			for(Node nd:adj[i]){
				System.out.print("("+nd.next+","+nd.weight+")");
			}
			System.out.println();
		}
	}
	void johnson(int result[][]){
		for(int i=0;i<N-1;i++)
			edgeList.add(0,new Edge(N-1,i,0));
		int arr[]=new int[N];
		bellmanFord(N-1,arr);
		for(Integer x:arr)
			System.out.print(x+"--");
		System.out.println();
		for(int i=0;i<N;i++)             
			for(int j=0;j<adj[i].size();j++)
					adj[i].get(j).weight+=arr[i]-arr[adj[i].get(j).next]; //GRAPH  REWEIGHTING
		int src=0;
		for(int[] row:result){
			dijkstra(src,row);
			src++;
		}
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				result[i][j]+=arr[j]-arr[i];     //  CALCULATING FINAL MIN DISTANCE
			}
		}
		return;
	}
}
	
	
class GraphAlgos{
	public static void main(String args[]) throws Exception{
		Reader sc=new Reader();
		int n=sc.nextInt();
		int m=sc.nextInt();
		Graph graph=new Graph(n+1);
		for(int i=0;i<m;i++)
			graph.push_edge(sc.nextInt(),sc.nextInt(),sc.nextInt());
		int result[][]=new int[n+2][n+2];
		graph.johnson(result);
		for(int i=1;i<=n;i++){
			for(int j=1;j<=n;j++)
				if(result[i][j]<9999999)
					System.out.print(result[i][j]+" ");
				else
					System.out.print("-1 ");
			System.out.println();
		}
	}
	
	static class Reader{
        final private int BUFFER_SIZE = 1000006;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;
 
        public Reader(){
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }
 
        public Reader(String file_name) throws IOException{
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }
 
        public String readLine() throws IOException{
            byte[] buf = new byte[100006]; // line length
            int cnt = 0, c;
            while((c=read())!=-1){
                if(c=='\n')
                    break;
                buf[cnt++]=(byte)c;
            }
            return new String(buf,0,cnt);
        }
 
        public int nextInt() throws IOException{
            int ret=0;
            byte c=read();
            while(c<=' ')
                c=read();
            boolean neg=(c=='-');
            if(neg)
                c=read();
            do{
                ret=ret*10+c-'0';
            }while((c=read())>='0' && c<='9');
            if(neg)
                return -ret;
            return ret;
        }
 
        public long nextLong() throws IOException{
            long ret=0;
            byte c=read();
            while(c<=' ')
                c=read();
            boolean neg=(c=='-');
            if(neg)
                c=read();
            do{
                ret=ret*10+c-'0';
            }
            while((c=read())>='0' && c<='9');
            if(neg)
                return -ret;
            return ret;
        }
 
        public double nextDouble() throws IOException{
            double ret=0,div = 1;
            byte c=read();
            while(c<=' ')
                c=read();
            boolean neg =(c=='-');
            if(neg)
                c=read();
            do{
                ret=ret*10+c-'0';
            }
            while((c=read())>='0' && c<='9');
			if(c=='.'){
                while ((c = read()) >= '0' && c <= '9'){
                    ret+=(c-'0')/(div*=10);
                }
            }
            if(neg)
                return -ret;
            return ret;
        }
 
        private void fillBuffer() throws IOException{
            bytesRead = din.read(buffer,bufferPointer=0,BUFFER_SIZE);
            if (bytesRead==-1)
                buffer[0]=-1;
        }
 
        private byte read() throws IOException{
            if (bufferPointer==bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }
		public void close() throws IOException{
            if (din==null)
                return;
            din.close();
        }
    }
}


