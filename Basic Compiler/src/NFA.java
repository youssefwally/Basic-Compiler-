import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;





public class NFA {
	String[] statesAndAcceptsDFA; 
	static String[] results; 
	String[] states;
	int numberOfStates;
	static int numberOfResults;
	static String[][] stateInfo;
	String[] statesAndAccepts; 
	String[] accepted; 
	String[] transitions;
	int numberOfTransitions;
	int numberOfAccepted;
	String maxState = "0";
	String dfaString = "";
	ArrayList<ArrayList<String>> newStates = new ArrayList<ArrayList<String>>(); 
	ArrayList<ArrayList<String>> zeroTransitions = new ArrayList<ArrayList<String>>(); 
	ArrayList<ArrayList<String>> oneTransitions = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> eTransitions = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> eClosure = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> currentStates = new ArrayList<ArrayList<String>>();
	Map<String, ArrayList<ArrayList<String>>> dfa = new HashMap<String, ArrayList<ArrayList<String>>>();
	
	public NFA(String nfa){
		statesAndAccepts = nfa.split("#"); 
		accepted = statesAndAccepts[3].split(","); 
		numberOfTransitions = 3;
		numberOfAccepted = accepted.length;
		for(int i=0; i <numberOfTransitions;i++) {
			ArrayList<String> temp = new ArrayList<String>();
			int startState = 0;
			if(i==0) {
				String[] zeroTransitionsTemp = statesAndAccepts[0].split(";");
				for(int j=0; j<zeroTransitionsTemp.length;j++) {
					String[] stateTransition = zeroTransitionsTemp[j].split(",");
					if(stateTransition[0].equals(String.valueOf(startState))) {
						temp.add(stateTransition[1]);
					}
					else {
						ArrayList listCopy = new ArrayList(temp);
						zeroTransitions.add(listCopy);
						temp.clear();
						startState = startState + 1;
						j = j-1;
					}
					if(j+1 == zeroTransitionsTemp.length) {
						ArrayList listCopy = new ArrayList(temp);
						zeroTransitions.add(listCopy);	
					}
				}
			}
			if(i==1) {
				String[] oneTransitionsTemp = statesAndAccepts[1].split(";");
				for(int j=0; j<oneTransitionsTemp.length;j++) {
					String[] stateTransition = oneTransitionsTemp[j].split(",");
					if(stateTransition[0].equals(String.valueOf(startState))) {
						temp.add(stateTransition[1]);
					}
					else {
						ArrayList listCopy = new ArrayList(temp);
						oneTransitions.add(listCopy);
						temp.clear();
						startState = startState + 1;
						j = j-1;
					}
					if(j+1 == oneTransitionsTemp.length) {
						ArrayList listCopy = new ArrayList(temp);
						oneTransitions.add(listCopy);	
					}
				}
			}
			if(i==2) {
				String[] eTransitionsTemp = statesAndAccepts[2].split(";");
				for(int j=0; j<eTransitionsTemp.length;j++) {
					String[] stateTransition = eTransitionsTemp[j].split(",");
					if(stateTransition[0].equals(String.valueOf(startState))) {
						temp.add(stateTransition[1]);
					}
					else {
						ArrayList listCopy = new ArrayList(temp);
						eTransitions.add(listCopy);
						temp.clear();
						startState = startState + 1;
						j = j-1;
					}
					if(j+1 == eTransitionsTemp.length) {
						ArrayList listCopy = new ArrayList(temp);
						eTransitions.add(listCopy);	
					}
				}
			}
		}
		//getting max state*****************************************************
		for(int i=0;i<3;i++) {
			if(i==0) {
				for(int j=0;j<zeroTransitions.size();j++) {
					for(int k=0;k<zeroTransitions.get(j).size();k++) {
						if(!(zeroTransitions.get(j).isEmpty())){
							if(Integer.parseInt(zeroTransitions.get(j).get(k))>Integer.parseInt(maxState)) {
								maxState = zeroTransitions.get(j).get(k);
							}
						}
						
					}
				}
			}
			if(i==1) {
				for(int j=0;j<oneTransitions.size();j++) {
					for(int k=0;k<oneTransitions.get(j).size();k++) {
						if(!(oneTransitions.get(j).isEmpty())){
							if(Integer.parseInt(oneTransitions.get(j).get(k))>Integer.parseInt(maxState)) {
								maxState = oneTransitions.get(j).get(k);
							}
						}
						
					}
				}
			}
			if(i==2) {
				for(int j=0;j<eTransitions.size();j++) {
					for(int k=0;k<eTransitions.get(j).size();k++) {
						if(!(eTransitions.get(j).isEmpty())){
							if(Integer.parseInt(eTransitions.get(j).get(k))>Integer.parseInt(maxState)) {
								maxState = eTransitions.get(j).get(k);
							}
						}
						
					}
				}
			}
		}
		//size fix of transition arrays********************************************************
		for(int i = 0;i<3;i++) {
			ArrayList<String> emptyList = new ArrayList<String>();
			if(i==0) {
				while(zeroTransitions.size()-1 < Integer.parseInt(maxState)) {
					zeroTransitions.add(emptyList);
				}
			}
			if(i==1) {
				while(oneTransitions.size()-1 < Integer.parseInt(maxState)) {
					oneTransitions.add(emptyList);
				}
			}
			if(i==2) {
				while(eTransitions.size()-1 < Integer.parseInt(maxState)) {
					eTransitions.add(emptyList);
				}
			}
		}
		//eClosure***************************************************************
		for(int i = 0;i<eTransitions.size();i++) {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(String.valueOf(i));
			if(!(eTransitions.get(i).isEmpty())) {
				for(int j = 0;j<eTransitions.get(i).size();j++) {
					String stateInTest =  eTransitions.get(i).get(j);
					temp.add(stateInTest);
					String stateInTestLoop = stateInTest;
					if(Integer.parseInt(stateInTest) < i) {
						ArrayList pastE = new ArrayList(eClosure.get(Integer.parseInt(stateInTest)));
						temp.addAll(pastE);
						ArrayList<String> newList = new ArrayList<String>();
						for (String element : temp) {
				            if (!newList.contains(element)) {
				                newList.add(element);
				            }
				        }
						temp = newList;
					}
					
				}
			}
				ArrayList listCopy = new ArrayList(temp);
				eClosure.add(listCopy);
				temp.clear();	
		}
		//eClosure cont.************************
		for(int k = 0; k< Integer.parseInt(maxState);k++) {
			for(int i = 0; i<eClosure.size();i++) {
				for(int j = 1; j< eClosure.get(i).size();j++) {
					String startState = eClosure.get(i).get(j-1);
					eClosure.get(i).addAll(eClosure.get(Integer.parseInt(eClosure.get(i).get(j))));
					if(startState.equals(eClosure.get(i).get(j))) {
						break;
					}
					if((j-1)>-1) {
						if((eClosure.get(i).get(j-1)).equals(eClosure.get(i).get(j))) {
							break;
						}
					}
					if((j-2)>-1) {
						if((eClosure.get(i).get(j-2)).equals(eClosure.get(i).get(j))) {
							break;
						}
					}
				}
				ArrayList<String> newList = new ArrayList<String>();
				for (String element : eClosure.get(i)) {
		            if (!newList.contains(element)) {
		                newList.add(element);
		            }
		        }
				Collections.sort(newList);  
				eClosure.get(i).clear(); 
				eClosure.get(i).addAll(newList);
			}
		}
		//getting the dfa
		ArrayList<String> start = new ArrayList<String>();
		start.addAll(eClosure.get(0));
		currentStates.add(start);
		for(int s =0;s<currentStates.size();s++) {
			while((!(newStates.contains(currentStates.get(s))))){
				ArrayList<String> zeroTransitionStates = new ArrayList<String>();
				ArrayList<String> oneTransitionStates = new ArrayList<String>();
				ArrayList<String> zeroTransitionEpsilonStates = new ArrayList<String>();
				ArrayList<String> oneTransitionEpsilonStates = new ArrayList<String>();
				ArrayList<ArrayList<String>> transitions = new ArrayList<ArrayList<String>>();
				if((!(newStates.contains(currentStates.get(s))))) {
					//getting 0 transitions
					ArrayList<String> statesFromCurrentState = currentStates.get(s);
					ArrayList<String> newList0 = new ArrayList<String>();
					ArrayList<String> newList1 = new ArrayList<String>();
					ArrayList<String> newList2 = new ArrayList<String>();
					ArrayList<String> newList3 = new ArrayList<String>();
					
					for(int i=0;i<statesFromCurrentState.size();i++) {
						/*if((zeroTransitions.get(Integer.parseInt(statesFromCurrentState.get(i))).isEmpty() && i==0)) {
							zeroTransitionStates.add(statesFromCurrentState.get(i));
						}*/
						zeroTransitionStates.addAll(zeroTransitions.get(Integer.parseInt(statesFromCurrentState.get(i))));
						for (String element : zeroTransitionStates) {
				            if (!newList0.contains(element)) {
				                newList0.add(element);
				            }
				        }
					}
					for(int i=0;i<newList0.size();i++) {
							zeroTransitionEpsilonStates.addAll(eClosure.get(Integer.parseInt(newList0.get(i))));
							for (String element : zeroTransitionEpsilonStates) {
					            if (!newList2.contains(element)) {
					                newList2.add(element);
					            }
						}
					}
					//getting 1 transitions
					for(int i=0;i<statesFromCurrentState.size();i++) {
						/*if((oneTransitions.get(Integer.parseInt(statesFromCurrentState.get(i))).isEmpty()) && i==0) {
							oneTransitionStates.add(statesFromCurrentState.get(i));
						}*/
						oneTransitionStates.addAll(oneTransitions.get(Integer.parseInt(statesFromCurrentState.get(i))));
						for (String element : oneTransitionStates) {
				            if (!newList1.contains(element)) {
				                newList1.add(element);
				            }
				        }
					}
					for(int i=0;i<newList1.size();i++) {
							oneTransitionEpsilonStates.addAll(eClosure.get(Integer.parseInt(newList1.get(i))));
							for (String element : oneTransitionEpsilonStates) {
					            if (!newList3.contains(element)) {
					                newList3.add(element);
					            }
						}
					}
					
					
					ArrayList listCopy0 = new ArrayList(newList2);
					ArrayList listCopy1 = new ArrayList(newList3);
					
					if(listCopy0.size()<Integer.parseInt(maxState)+2) {
						transitions.add(listCopy0);
					}
					if(listCopy1.size()<Integer.parseInt(maxState)+2) {
						transitions.add(listCopy1);
					}
					ArrayList listCopy3 = new ArrayList(transitions);
					dfa.put(currentStates.get(s).toString(), listCopy3);
					
					if(!(currentStates.contains(listCopy0)) &&  (listCopy0.size()<Integer.parseInt(maxState)+2)) {
						currentStates.add(listCopy0);
					}
					if(!(currentStates.contains(listCopy1)) &&  (listCopy1.size()<Integer.parseInt(maxState)+2)) {
						currentStates.add(listCopy1);
					}
					if((currentStates.get(s).size()<Integer.parseInt(maxState)+2)) {
						newStates.add(currentStates.get(s));
					}
					zeroTransitionStates.clear();
					zeroTransitionEpsilonStates.clear();
					oneTransitionStates.clear();
					oneTransitionEpsilonStates.clear();
					transitions.clear();
				}	
			}
		}
		//getting new accepted states
		Set<String> keySet = dfa.keySet();
		ArrayList<String> keys = new ArrayList<String>(keySet);
		Collections.sort(keys); 
		boolean[] newAcceptedState = new boolean[keys.size()];
		Arrays.fill(newAcceptedState, false);
		for(int i = 0;i<accepted.length;i++) {
			String testingState = accepted[i];
			for(int j = 0;j<keys.size();j++) {
				if(keys.get(j).contains(testingState)) {
					newAcceptedState[j] = true;
				}
			}
			
		}
		//getting new state names
		for(int i = 0;i<keys.size();i++) {
			if(i>0) {
				dfaString = dfaString + ";";
			}
			dfaString = dfaString + i;
			dfaString = dfaString + ",";
			dfaString = dfaString + keys.indexOf(dfa.get(keys.get(i)).get(0).toString());
			dfaString = dfaString + ",";
			dfaString = dfaString + keys.indexOf(dfa.get(keys.get(i)).get(1).toString());
			if(i == keys.size()-1) {
				dfaString = dfaString + "#";
				for(int j=0;j<newAcceptedState.length;j++) {
					if((j>0) && (newAcceptedState[j-1] == true)) {
						dfaString = dfaString + ",";
					}
					if(newAcceptedState[j] == true) {
						dfaString = dfaString + j;
					}
				}
			}
			
		}
		
		System.out.println("***************");
		System.out.println("DFA String");
		System.out.println(dfaString);
		
		
		System.out.println("***************");
		System.out.println("Hashmap");
		for (String name: dfa.keySet()) {
		    String key = name.toString();
		    String value = dfa.get(name).toString();
		    System.out.println(key + " " + value);
		}
		
		System.out.println("***************");
		
		System.out.println("Tables");
		System.out.println(zeroTransitions.toString());
		System.out.println(oneTransitions.toString());
		System.out.println(eTransitions.toString());
		System.out.println(eClosure.toString());
		
		System.out.println("***************");
		
		//dfa
		statesAndAccepts = dfaString.split("#"); 
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
		/*P19_40_7641_Youssef_Wally x = new P19_40_7641_Youssef_Wally("2,3#0,1;6,7;8,9#1,2;1,4;3,6;4,5;5,6;7,8;7,10;9,8;9,10#10");
		System.out.println(x.run("11"));
		System.out.println(x.run("01010"));
		System.out.println(x.run("10111"));
		System.out.println(x.run("11111"));
		System.out.println(x.run("11011"));*/
		NFA x = new NFA("2,3#3,4;5,6;6,7#0,1;0,9;1,2;1,5;4,8;7,8;8,1;8,9#9");
		System.out.println(x.run("011101"));
		System.out.println(x.run(""));
		System.out.println(x.run("000"));
		System.out.println(x.run("1111"));
		System.out.println(x.run("111"));
	}
}
