import java.util.Arrays;

public class DFA {
	
	static String[] statesAndAccepts; 
	static String[] results; 
	static String[] states;
	static int numberOfStates;
	static int numberOfResults;
	static String[][] stateInfo;
		
	public DFA(String dfa){
		statesAndAccepts = dfa.split("#"); 
		results = statesAndAccepts[1].split(","); 
		states = statesAndAccepts[0].split(";");
		numberOfStates = states.length;
		numberOfResults = results.length;
		stateInfo = new String[numberOfStates][3];
		for(int i = 0; i<numberOfStates; i++){
			stateInfo[i] = states[i].split(",");
		}
	}
	
	public static boolean run(String sentence){
		boolean accepted = false;
		int sentenceLength = sentence.length();
		String currentState = "0";
		for(int i=0; i<sentenceLength; i++){
			char currentLetter = sentence.charAt(i);
			if(currentLetter == '0'){
				currentState = stateInfo[Integer.parseInt(currentState)][1];
			}
			if(currentLetter == '1'){
				currentState = stateInfo[Integer.parseInt(currentState)][2];
			}
		}
		for(int i=0; i<numberOfResults; i++){
			if(currentState.equals(results[i])){
				accepted = true;
			}
		}
		return accepted;
	}
	
	public static void main(String[] srgs){
		DFA dfa1 = new DFA("0,1,2;1,3,4;2,4,5;3,3,3;4,3,6;5,6,5;6,3,6#5,6");
		System.out.println("DFA 1");
		System.out.println(dfa1.run("10111"));
		System.out.println(dfa1.run("01"));
		System.out.println(dfa1.run("1"));
		System.out.println(dfa1.run("111"));
		System.out.println(dfa1.run("01011"));
		System.out.println("DFA 2");
		DFA dfa2 = new DFA("0,1,2;1,3,4;2,4,0;3,0,5;4,5,1;5,2,3#0");
		System.out.println(dfa2.run("000000"));
		System.out.println(dfa2.run("001001"));
		System.out.println(dfa2.run("1010"));
		System.out.println(dfa2.run("101010"));
		System.out.println(dfa2.run("10100"));
	}
}
