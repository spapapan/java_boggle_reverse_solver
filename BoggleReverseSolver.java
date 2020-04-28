package src;

import java.awt.Point;
import java.util.ArrayList;

public class BoggleReverseSolver {

	    private ArrayList<String> wordsList; 
	    private String[][] lettersMatrix = new String[Main.matrixSize][Main.matrixSize];
	    

	    public BoggleReverseSolver(ArrayList<String> wordsList)
	    {
	        this.wordsList=wordsList; 
	    }
	    
	    public void findBoggler()
	    {
	    	boolean success=false;
	    	int tries=0;
	    	while (tries<Main.TOTAL_TRIES && !success)
	    	{
	    		init(); 
	    		success = solve();
	    		tries++;
	    	}
	    	
	    	
	    	if (success) 
	    		printMatrix(); 
	    	System.out.println("Total tries: " + tries);
	    }

	    public boolean solve()
	    {
	    	boolean success=true; 
	    	int index=0;
	    	ArrayList<ArrayList<String>> lettersWordsList = getLettersWordsList();
	    	breakpoint:
	    	for (ArrayList<String> letters : lettersWordsList)
	    	{
	    		ArrayList<WordPositionItem> letterPositionList = new ArrayList<>();
	    		//System.out.println("Word: " + index);
		    	Point startingPosition = new Point();   
		        startingPosition = getStartingPoint(letters.get(0));
		        
		        if (startingPosition==null)
		        {
		        	success=false;
	        		//System.out.println("startingPosition null");
	        		break breakpoint;
		        }
		        
		        //Set first letter 
		        setWordToPosition(startingPosition,letters.get(0)); 
		        letterPositionList.add(new WordPositionItem(startingPosition,letters.get(0)));
		        
		        ArrayList<Point> neighbours = getNeighbours(startingPosition,letters.get(0),letterPositionList);
		        letters.remove(0);
		        
		        for (String letter : letters)
		        {
		        	if (neighbours.size()==0) {
		        		success=false;
		        		//System.out.println("neighbours zero");
		        		break breakpoint;
		        	}
		        		
		        	Point nextPosition = getRandomDirection(neighbours);
		        	//System.out.println(letter + ": " + nextPosition.x + " , " + nextPosition.y);
		        	setWordToPosition(nextPosition,letter);
		        	letterPositionList.add(new WordPositionItem(nextPosition,letter));  
		        	neighbours = getNeighbours(nextPosition,letter,letterPositionList);
		        } 
		        index++;
	    	}
	    	
	    	if (success) {
	    		fillEmptySlots();
	    	}
	    	
	    	return success;
 
	    }
	    
	    private void setWordToPosition(Point pos,String letter)
	    {
	    	lettersMatrix[pos.x][pos.y] = letter; 
	    }
	    
	    private void fillEmptySlots()
	    {
	    	for (int i=0; i<Main.matrixSize; i++)
	    	{
	    		for (int j=0; j<Main.matrixSize; j++)
		    	{
	    			if (lettersMatrix[i][j].equals(""))
	    			{
	    				lettersMatrix[i][j] = getRandomLetter();
	    			}
		    	}
	    	}
	    }
	    
	    private String getRandomLetter()
	    {
	    	String[] abcList = Main.abc.split(",");
	    	return abcList[Utils.getRandom(0, abcList.length-1)];
	    }
	    
	    private Point getStartingPoint(String letter)
	    {
	    	Point start = new Point();
	    	ArrayList<Point> availablePos = new ArrayList<>();
	    	boolean foundLetter=false;
	    	for (int i=0; i<Main.matrixSize; i++)
	    	{
	    		for (int j=0; j<Main.matrixSize; j++)
		    	{
		    		if (letter.equals(lettersMatrix[i][j]))
		    		{
		    			start.x=i;
		    			start.y=j;
		    			foundLetter=true;
		    			break;
		    		}
		    		else if(lettersMatrix[i][j].equals("")) {
		    			availablePos.add(new Point(i,j));
		    		}
		    	}
	    	}
	    	
	    	if (!foundLetter) { 
	    		int rand=0;
	    		if (availablePos.size()>1)
	    			rand = Utils.getRandom(0, availablePos.size()-1); 
	    		else if (availablePos.size()==0)
	    			return null;
	    			
	    		start = availablePos.get(rand); 
	    	}
	    	
	    	return start;
	    }
	     
	    private Point getRandomDirection(ArrayList<Point> neighbours)
	    { 
	        Point point = new Point();
	        int random = Utils.getRandom(0,neighbours.size()-1);

	        return neighbours.get(random);
	    }

	    private ArrayList<Point> getNeighbours(Point point,String letter,ArrayList<WordPositionItem> letterPositionList)
	    {
	    	int i = point.x;
	    	int j = point.y;
	    	
	        ArrayList<Point> directions = new ArrayList<>();
	        if (i>0) {
	            if (checkBox(i-1,j,letter,letterPositionList))                      //Up
	                directions.add(new Point(i-1,j));
	            if (j>0)
	                if (checkBox(i-1,j-1,letter,letterPositionList))             //Up Left
	                    directions.add(new Point(i-1,j-1));
	            if (j<Main.matrixSize-1)
	                if (checkBox(i-1,j+1,letter,letterPositionList))             //Up Right
	                    directions.add(new Point(i-1,j+1));
	        }
	        if (i<Main.matrixSize-1) {
	            if (checkBox(i+1,j,letter,letterPositionList))                      //Down
	                directions.add(new Point(i+1,j));
	            if (j>0)
	                if (checkBox(i+1,j-1,letter,letterPositionList))             //Down Left
	                    directions.add(new Point(i+1,j-1));
	            if (j<Main.matrixSize-1)
	                if (checkBox(i+1,j+1,letter,letterPositionList))             //Down Right
	                    directions.add(new Point(i+1,j+1));
	        }
	        if (j<Main.matrixSize-1)
	            if (checkBox(i,j+1,letter,letterPositionList))                      //RIGHT
	                directions.add(new Point(i,j+1));
	        if (j>0)
	            if (checkBox(i,j-1,letter,letterPositionList))                      //LEFT
	                directions.add(new Point(i,j-1));

	        return directions;
	    }

	    private boolean checkBox(int x, int y,String letter,ArrayList<WordPositionItem> letterPositionList)
	    {
	        if (lettersMatrix[x][y].equals("")) {
	            return true;
	        }
	        else if (lettersMatrix[x][y].equals(letter)) {
	        	for (WordPositionItem item : letterPositionList) { //If is the same letter but from same word
	        		if (item.position.x==x && item.position.y==y)
	        			return false;
	        	}
	        
	        	return false;
	        }
	        else
	            return false;
	    } 

	    private void init()
	    { 
	        initLettersMatrix(); 
	        checkTotalLetters();
	    }
	    
	    private  ArrayList<ArrayList<String>> getLettersWordsList()
	    {
	    	ArrayList<ArrayList<String>> lettersWordsList = new ArrayList<>();
	    	for (String word : wordsList)
	    	{
	    		String[] letters = word.split("");
	    		ArrayList<String> lettersList = new ArrayList<>();
	    		for (int i=0; i<letters.length; i++)
	            {
	                if (!letters[i].equals(""))
	                	lettersList.add(letters[i]);
	            }
	    		lettersWordsList.add(lettersList);
	    	} 
	    	
	    	return lettersWordsList;
	    }

	    private void checkTotalLetters()
	    {
	        ArrayList<String> tempLettersList = new ArrayList<>();
	        ArrayList<String> lettersList = new ArrayList<>();

	        for (String word : wordsList)
	        {
	            String[] letters = word.split("");
	            for (int i=0; i<letters.length; i++)
	            {
	                if (!letters[i].equals(""))
	                {
	                    for (int j=0; j<tempLettersList.size(); j++)
	                    {
	                        if (tempLettersList.get(j).equals(letters[i]))
	                            tempLettersList.set(j,"");
	                        break;
	                    }
	                    tempLettersList.add(letters[i]);
	                }
	            }
	        }

	        for (String letter : tempLettersList)
	        {
	            if (!letter.equals("")) {
	                lettersList.add(letter);
	                //System.out.println(letter);
	            }
	        }

	        if (lettersList.size()>Main.matrixSize*Main.matrixSize)
	        { 
	        	throw new IllegalArgumentException("Boggler Solver Error: Too many letters: "+ lettersList.size()); 
	        }
	    }

	    private void printMatrix()
	    { 
	    	System.out.println("-------------------");
	    	System.out.println("  01234"); 
	        for (int i=0; i<Main.matrixSize; i++)
	        {
	            String column="";
	            for (int j=0; j<Main.matrixSize; j++)
	            {
	            	String letter = lettersMatrix[i][j];
	            	if (letter.equals(""))
	            		letter = " ";
	            	
	            	if (j==0)
	            		column = i + " " + letter;
	            	else
	            		column += letter; 
	            }
	            System.out.println(column);
	        }
	        System.out.println("-------------------");
	    }

	    private void initLettersMatrix()
	    {
	        for (int i=0; i<Main.matrixSize; i++)
	        {
	            for (int j=0; j<Main.matrixSize; j++)
	            {
	                lettersMatrix[i][j] = "";
	            }
	        }
	    }

	    public class WordPositionItem{
	    	public Point position;
	    	public String letter;
	    	
	    	public WordPositionItem(Point position,String letter) {
	    		this.position=position;
	    		this.letter=letter;
	    	}
	    	
	    }
}
