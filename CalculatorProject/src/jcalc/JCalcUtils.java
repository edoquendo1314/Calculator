package jcalc;
import java.util.Comparator;
import java.util.PriorityQueue;

public class JCalcUtils {

	public static void parseString(String str){
		double ans = 0.0;
		String[] pTokens = str.split("[\\(\\)]");
		if(pTokens.length == 0){
			return;
		}
		for(int i = 0; i < pTokens.length; i++){
			System.out.println(pTokens[i]);
		}
		
		for(String s : pTokens){
			String[] mTokens = str.split("\\-");
		}
	}
	
	public static void main(String[] args){
		parseString("7+(4+3)");
	}
}
