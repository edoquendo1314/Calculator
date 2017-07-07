package calculator;

public class CalculatorMain {

	
	public static void main(String[] args) {
		String s = "log(5, 4)";
		
		String[] array = StrManip.tokenize(s);
		
		System.out.println(array.length);
		
		for(String str: array){
			System.out.print(str + " ");
		}
	}
}
