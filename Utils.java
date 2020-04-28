package src;

import java.util.Random;

public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}
	
	public static int getRandom(int min, int max)
	{
		Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
	}

}
