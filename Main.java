package src;

import java.util.ArrayList;

public class Main {
	
	public final static int TOTAL_TRIES = 1000;
	public final static int matrixSize = 5; //5x5
	public final static String abc = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";

	public Main() {
		
		ArrayList<String> wordsList = new ArrayList<>();
        wordsList.add("school"); 
        wordsList.add("java");
        wordsList.add("android");
        wordsList.add("bazinga"); 
        
		BoggleReverseSolver bogglerReverseSolver = new BoggleReverseSolver(wordsList);
		bogglerReverseSolver.findBoggler();
	}

	
	
	public static void main(String[] args) {
		new Main();

	}

}
