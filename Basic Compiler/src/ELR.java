import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ELR {
	
	static String[] cfgRaw; 
	static ArrayList<String> cfgOrder = new ArrayList<String>(); 
	static Map<String, ArrayList<String>> cfg = new HashMap<String, ArrayList<String>>();
	static ArrayList<String> newCfgOrder = new ArrayList<String>(); 
	static Map<String, ArrayList<String>> oldCfg = new HashMap<String, ArrayList<String>>();
	static ArrayList<String> alpha = new ArrayList<String>(); 
	static ArrayList<String> beta = new ArrayList<String>(); 
	
	public static void LRE(String input){
		String noSpaceStr = input.replaceAll("\\s", "");
		cfgRaw = noSpaceStr.split(";"); 
		for(int i2 =0;i2<cfgRaw.length;i2++) {
			String[] cfgPart = cfgRaw[i2].split(",");
			String[] temp = Arrays.copyOfRange(cfgPart, 1, cfgPart.length);
			ArrayList<String> cfgLHS = new ArrayList<String>();
			Collections.addAll(cfgLHS, temp);
			cfg.put(cfgPart[0], cfgLHS);
			oldCfg.put(cfgPart[0], cfgLHS);
			cfgOrder.add(cfgPart[0]);
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////
		for(int i = 0;i<cfgOrder.size();i++) {
			
			String currentLHS = cfgOrder.get(i);
			ArrayList<String> currentRHS = cfg.get(currentLHS);
			
			/*
			if(i==0) {
				String firstCurrentLHS = (String) cfg.keySet().toArray()[i];
				ArrayList<String> firstCurrentRHS = cfg.get(firstCurrentLHS);
				ArrayList<String> newFirstRHS = new ArrayList<String>(); 
				for(int l = 0;l<firstCurrentRHS.size();l++) {
					newFirstRHS.add(firstCurrentRHS.get(l)); 
				}
			}*/
			for(int j = 0;j<i;j++) {
				ArrayList<String> newRHS = new ArrayList<String>(); 
				String currentLHSJ = cfgOrder.get(j);
				ArrayList<String> currentRHSJ = cfg.get(cfgOrder.get(j));
				String currentLHSJO = cfgOrder.get(i);
				ArrayList<String> currentRHSJO = cfg.get(cfgOrder.get(i));
				
				for(int x = 0;x<currentRHSJO.size();x++) {
					if((currentRHSJO.get(x).charAt(0)) == (currentLHSJ.charAt(0))) {
						for(int y = 0;y<currentRHSJ.size();y++) {
							String edited = currentRHSJO.get(x).substring(1);
							edited = currentRHSJ.get(y) + edited;
							newRHS.add(edited);
						}
					}
					else {
						newRHS.add(currentRHSJO.get(x));
					}
					
				}
				
				if(!(newRHS.isEmpty())) {
					boolean change = true;
					for(int o = 0; o<cfgOrder.size();o++) {
						if(((oldCfg.get(cfgOrder.get(o))).equals(newRHS))) {
							change = false;
						}	
					}
					if(change) {
						cfg.remove(currentLHSJO);
						ArrayList<String> copyNewRHS = (ArrayList<String>) newRHS.clone();
						cfg.put(currentLHSJO, copyNewRHS);
					}
					newRHS.clear();
				}
			}
			//////////////////////////////////////////////////////////////////////////
			currentRHS = cfg.get(currentLHS);
			for(int w = 0;w<currentRHS.size();w++) {
				String proccess = "";
				if((currentRHS.get(w).charAt(0)) == (currentLHS.charAt(0))) {
					proccess = currentRHS.get(w).substring(1);
					proccess = proccess + currentLHS +"'";
					alpha.add(proccess);
				}
				else {
					//error here!!!!!!!!!!!!!!!!!
					proccess = currentRHS.get(w) + currentLHS +"'";
					beta.add(proccess);
				}
/*
				System.out.println("i: " + i);
				System.out.println("w: " + w);
				System.out.println(cfg.toString());
				System.out.println(currentRHS.toString());
				System.out.println(currentRHS.get(w).substring(1));*/
			}

			
			
			if(!(alpha.isEmpty())) {
				alpha.add("e");
				cfg.remove(currentLHS);
				ArrayList<String> copyBeta = (ArrayList<String>) beta.clone(); 
				cfg.put(currentLHS, copyBeta);
				String key = currentLHS + "'";
				ArrayList<String> copyAlpha = (ArrayList<String>) alpha.clone();
				cfg.put(key, copyAlpha);
				
				
			}
			alpha.clear();
			beta.clear();
			String proccess = "";
		}
		//System.out.println(oldCfg.toString());
		//System.out.println(cfg.toString());
		
		String output = "";
		for(int e = 0;e<cfgOrder.size();e++) {
			String key = cfgOrder.get(e);
			String keyDash = key + "'";
			output = output + key + ", ";
			output = output + cfg.get(key).toString().substring(1, cfg.get(key).toString().length()-1) + "; ";
			if(cfg.containsKey(keyDash)) {
				output = output + keyDash + ", ";
				output = output + cfg.get(keyDash).toString().substring(1, cfg.get(keyDash).toString().length()-1) + "; ";
			}
		}
		output = output.substring(0, output.length()-2);
		System.out.println(output);
		
	}
	
	public static void main(String[] srgs){
		//LRE("S,vSt,vS,n");
		//LRE("S,SbS,SaS,v");
		//LRE("S,SbT,T;T,TzW,W;W,av");
		//LRE("S,EW,Wd;E,SW,ES,n;W,SE,n");
		LRE("S,SnT,Sv,T,b;T,vSb,ivJb,i;J,SdJ,S");
		}
}
