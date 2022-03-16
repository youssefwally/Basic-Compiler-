import java.util.ArrayList;
import java.util.Stack;

public class FDFA {
	
	String[] statesAndAccepts; 
	static String[] accepts; 
	String[] states;
	int numberOfStates;
	static int numberOfAccepts;
	static String[][] stateInfo;
	static String actionString;
		
	public FDFA(String dfa){
		actionString = "";
		statesAndAccepts = dfa.split("#"); 
		accepts = statesAndAccepts[1].split(","); 
		states = statesAndAccepts[0].split(";");
		numberOfStates = states.length;
		numberOfAccepts = accepts.length;
		stateInfo = new String[numberOfStates][4];
		for(int i = 0; i<numberOfStates; i++){
			stateInfo[i] = states[i].split(",");
		}
	}
	
	public static void run(String sentence){
		boolean accepted = false;
		int sentenceLength = sentence.length();
		String currentState = "0";
		String lastState = "";
		Stack<String> passedStates = new Stack<String>();
		passedStates.push(currentState);
		int leftPointer = 0;
		int rightPointer = 0;
		int poped = 0;
		while(rightPointer != sentenceLength) {
			poped = 0;
			currentState = "0";
			for(int i=rightPointer; i<sentenceLength; i++){
				/*if(i<0) {
					i=0;
				}*/
				char currentLetter = sentence.charAt(i);
				if(currentLetter == '0'){
					currentState = stateInfo[Integer.parseInt(currentState)][1];
					passedStates.push(currentState);
					leftPointer = leftPointer +1;
				}
				if(currentLetter == '1'){
					currentState = stateInfo[Integer.parseInt(currentState)][2];
					passedStates.push(currentState);
					leftPointer = leftPointer +1;
				}
			}
			if(!(passedStates.empty())){
				lastState = passedStates.peek();
			}
			else {
				break;
			}
			while(!(passedStates.empty())) {
				currentState = passedStates.pop();
				poped +=1;
				accepted = acceptedState(currentState);
				if(accepted) {
					actionString = actionString + stateInfo[Integer.parseInt(currentState)][3];
					rightPointer = 0;
					if(passedStates.size()==1) {
						sentence = sentence.substring(passedStates.size()+1);
						sentenceLength = sentence.length();
					}
					else {
						sentence = sentence.substring(passedStates.size());
						sentenceLength = sentence.length();
					}
					passedStates.clear();
					break;
				}
			}
			if(accepted && (poped==1)) {
				rightPointer = sentenceLength;
				break;
			}
			if(!accepted) {
				actionString = actionString + stateInfo[Integer.parseInt(lastState)][3];
				break;
			}
			
		}
		System.out.println(actionString);
	}
	
	public static boolean acceptedState(String state) {
		boolean accepted = false;
		for(int i=0; i<numberOfAccepts; i++){
			if(state.equals(accepts[i])){
				accepted = true;
			}
		}
		return accepted;
		
	}
	
	public static void main(String[] srgs){
		FDFA fdfa1 = new FDFA("0,1,0,A;1,1,2,B;2,1,3,C;3,4,2,D;4,4,2,E#1,4");
		fdfa1.run("111");
	}

}
