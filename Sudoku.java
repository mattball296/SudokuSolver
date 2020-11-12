
public class Sudoku{
	
	public int[][] grid;
	
	public Sudoku(int[][] I){
		int L = I.length;
		for(int i = 0; i < L; i++){
			if(I[i].length!=L){
				throw new RuntimeException("not square");
			}
			else{
				this.grid = I;
			}
		}
	}
	
	public boolean isPossible(int r,int c,int x){
		for(int k = 0; k < 9; k++){
			if((this.grid[r][k]==x&&k!=c)||(this.grid[k][c]==x&&k!=r)){
				return false;
			}
		}
		int v = 3*(int)(Math.floor(r/3));
		int w = 3*(int)(Math.floor(c/3));
		for(int k = 0; k < 3; k++){
			for(int l = 0; l < 3; l++){
				if(this.grid[v+k][w+l]==x&&(v+k)!=r&&(w+l)!=c){
					return false;
				}
			}
		}
		return true;
	}
	
	public void solve() throws sudokuException{
		// System.out.println(this);
		for(int r=0; r<9; r++){
			for(int c=0; c<9; c++){
				if(this.grid[r][c]==0){
					// System.out.println(this);
					for(int x=1; x<10; x++){
						if(isPossible(r,c,x)){
							try{
								this.grid[r][c] = x;
								this.solve();
								return;
							}
							catch(sudokuException s){
								this.grid[r][c] = 0;
							}
						}	
					}	
				throw new sudokuException("");
				}	
			}
		}
	}
	
	public boolean isValid(){
		for(int r=0; r < 9; r++){
			for(int c=0; c < 9; c++){
				if(grid[r][c]!=0&&!isPossible(r,c,grid[r][c])){
					return false;
				}
			}
		}
		return true;
	}
	
	public void Solve(){
		try{this.solve();}
		catch(sudokuException E){
		}
	}
	
	public String toString(){
		String s="-------------------------\n";
		for(int i = 0; i < 9; i++){
			s+="| ";
			for(int j = 0; j < 9; j++){
				if(this.grid[i][j]==0){
					s+="-"+" ";
				}
				else{
					s+=this.grid[i][j]+" ";	
				}
				if((j%3)==2){
					s+="| ";
				}
			}
			s+="\n";
			if((i%3)==2){
				s+="-------------------------\n";
			}
		}
		return s;
	}
	
	public static void main(String[] S_Arr){
		int[][] G = new int[][]{{0,0,0,0,0,0,0,0,9},
								{0,2,0,0,7,0,0,0,0},
								{0,0,0,6,0,0,1,0,0},
								{0,9,0,0,0,0,0,0,0},
								{0,0,0,0,3,0,0,0,0},
								{0,4,0,0,0,0,0,0,0},
								{0,0,0,0,0,0,9,2,0},
								{0,0,0,5,0,0,0,0,0},
								{4,0,0,0,9,0,0,0,8}};
		Sudoku S = new Sudoku(G);
		System.out.println(S);
		S.Solve();
		System.out.println(S);
		System.out.println("2 | 2");
	}
}