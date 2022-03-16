import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class first_follow {
	
	public static void input(String input, String[] cfgRaw, Map<Character, ArrayList<String>> cfg, ArrayList<Character> cfgVariables){
		String noSpaceStr = input.replaceAll("\\s", "");
		cfgRaw = noSpaceStr.split(";"); 
		for(int i2 =0;i2<cfgRaw.length;i2++) {
			String[] cfgPart = cfgRaw[i2].split(",");
			String[] temp = Arrays.copyOfRange(cfgPart, 1, cfgPart.length);
			ArrayList<String> cfgLHS = new ArrayList<String>();
			Collections.addAll(cfgLHS, temp);
			cfg.put(cfgPart[0].charAt(0), cfgLHS);
			cfgVariables.add(cfgPart[0].charAt(0));
		}
	}
	
	public static ArrayList<Character>  epsilonComplete(Map<Character, ArrayList<String>> cfg, ArrayList<Character> cfgVariables){
		ArrayList<Character> epsilonComplete = new ArrayList<Character>();
		boolean change = false;
		for(Character lhs : cfgVariables) {
			ArrayList<String> rhs = cfg.get(lhs);
			if(rhs.contains("e")) {
				epsilonComplete.add(lhs);
				change = true;
				
			}
		}
		while(change) {
			change = false;
			for(Character lhs : cfgVariables) {
				ArrayList<String> rhs = cfg.get(lhs);
				for(String str: rhs) {
					boolean allEpsilon = true;
					for(Character ch: str.toCharArray()) {
						if(!(epsilonComplete.contains(ch))) {
							allEpsilon = false;
							break;
						}
					}
					if(allEpsilon) {
						if(!(epsilonComplete.contains(lhs))) {
							epsilonComplete.add(lhs);
							change = true;
							allEpsilon = false;
							break;
						}
					}
				}
			}
		}
			
		return epsilonComplete;
	}
	
	public static void epsilons(Map<Character, ArrayList<String>> cfg, ArrayList<Character> cfgVariables, ArrayList<Character> epsilon){
		boolean change = false;
		for(Character lhs : cfgVariables) {
			ArrayList<String> rhs = cfg.get(lhs);
			if(rhs.contains("e")) {
				epsilon.add(lhs);
				change = true;
			}
		}
		while(change) {
			for(Character lhs : cfgVariables) {
				ArrayList<String> rhs = cfg.get(lhs);
					for(String rule : rhs) {
						if(!(rule.charAt(0) == lhs)) {
							if(epsilon.contains(rule.charAt(0))) {
								if(!(epsilon.contains(lhs))) {
									epsilon.add(lhs);
									change = true;
									break;
								}
								else {
									change = false;
								}
							}
							else {
								change = false;
							}
						}
						else {
							change = false;
						}
					}
					if(change) {
						break;
					}
			}
		}
		
	}
	
	public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
        ArrayList<T> newList = new ArrayList<T>();

        for (T element : list) {
            if (!newList.contains(element)) {
  
                newList.add(element);
            }
        }
        return newList;
    }
	
	public static String output(ArrayList<Character> cfgVariables, Map<Character, ArrayList<Character>> firsts) {
		String output = "";
		for(int i = 0;i<cfgVariables.size();i++) {
			Character key = cfgVariables.get(i);
			output = output + key + ",";
			ArrayList<Character> rhs = firsts.get(key);
			for(Character chr: rhs) {
				output = output + chr;
			}
			if(i<cfgVariables.size()-1) {
				output = output + ";";
			}
		}
		System.out.println(output);
		return output;
	}
	
	public static boolean upperExists(Map<Character, ArrayList<Character>> follows, ArrayList<Character> cfgVariables) {
		boolean upperExists = false;
		for(Character var: cfgVariables) {
			ArrayList<Character> array = follows.get(var);
			for(Character chr: array) {
				if(Character.isUpperCase(chr)) {
					upperExists = true;
					return upperExists;
				}
			}
		}
		return upperExists;
	}

	public static String first(String input){
		Map<Character, ArrayList<String>> cfg = new HashMap<Character, ArrayList<String>>();
		String[] cfgRaw = null; 
		ArrayList<Character> epsilon = new ArrayList<Character>(); 
		ArrayList<Character> cfgVariables = new ArrayList<Character>(); 
		Map<Character, ArrayList<Character>> firsts = new HashMap<Character, ArrayList<Character>>();
		ArrayList<Character> epsilonComplete = new ArrayList<Character>();
		int i = 0;
		
		input(input, cfgRaw, cfg, cfgVariables);
		epsilons(cfg, cfgVariables, epsilon);
		
		for(Character lhs: cfgVariables) {
			ArrayList<Character> temp = new ArrayList<Character>(); 
			ArrayList<String> rhs = cfg.get(lhs);
			if(epsilon.contains(lhs)) {
				temp.add('e');
			}
			for(String rule: rhs) {
				for (char chr: rule.toCharArray()) {
					if(!(chr == lhs)) {
						if(!(temp.contains(chr))) {
							temp.add(chr);
						}
					}
					if(!(epsilon.contains(chr))) {
						break;
					}
				}
			}
			if(!(i==0)) {
				ArrayList<Character> behindMe = new ArrayList<Character>(cfgVariables.subList(0, i));
				ArrayList<Character> tempTemp = new ArrayList<Character>(); 
				for(Character ch: temp) {
					for(int l = 0;l<behindMe.size();l++) {
						if(behindMe.get(l) == ch) {
							ArrayList<Character> rhs2 = firsts.get(behindMe.get(l));
							tempTemp.addAll(rhs2);
						}
						else {
							if(!(behindMe.contains(ch))) {
								tempTemp.add(ch);
							}
						}
					}
				}
				tempTemp = removeDuplicates(tempTemp);
				temp = tempTemp;
			}
			Collections.sort(temp);  
			ArrayList<Character> copytemp = (ArrayList<Character>) temp.clone();
			firsts.put(lhs, copytemp);
			for(int j = 0;j<i;j++) {
				ArrayList<Character> rhs2 = firsts.get(cfgVariables.get(j));
				if(rhs2.contains(lhs)) {
					Object o = lhs;
					rhs2.remove(o);
					rhs2.addAll(copytemp);
					rhs2 = removeDuplicates(rhs2);
				}
				Collections.sort(rhs2);   
				ArrayList<Character> copyrhs2 = (ArrayList<Character>) rhs2.clone();
				firsts.remove(cfgVariables.get(j));
				firsts.put(cfgVariables.get(j), copyrhs2);
			}		
			i++;
		}
		
		epsilonComplete = epsilonComplete(cfg, cfgVariables);
		for(Character f: cfgVariables) {
			Object ep = 'e';
			ArrayList<Character> last = firsts.get(f);
			if(!(epsilonComplete.contains(f))) {
				last.remove(ep);
				ArrayList<Character> copyf = (ArrayList<Character>) last.clone();
				firsts.remove(f);
				firsts.put(f, copyf);
			}
			//new
			ArrayList<Character> xTemp = (ArrayList<Character>) last.clone();
			for(Character x: last) {
				if(Character.isUpperCase(x)) {
					xTemp.remove(x);
				}
				ArrayList<Character> copyx = (ArrayList<Character>) xTemp.clone();
				firsts.remove(f);
				firsts.put(f, copyx);
			}
			//new
		}
		
		
		String output = output(cfgVariables, firsts);
		return output;
		
	}
	
	
	public static ArrayList<Character> singleFirst(String input, Character nonTerminal){
		Map<Character, ArrayList<String>> cfg = new HashMap<Character, ArrayList<String>>();
		String[] cfgRaw = null; 
		ArrayList<Character> epsilon = new ArrayList<Character>(); 
		ArrayList<Character> cfgVariables = new ArrayList<Character>(); 
		Map<Character, ArrayList<Character>> firsts = new HashMap<Character, ArrayList<Character>>();
		ArrayList<Character> epsilonComplete = new ArrayList<Character>();
		int i = 0;
		
		input(input, cfgRaw, cfg, cfgVariables);
		epsilons(cfg, cfgVariables, epsilon);
		
		for(Character lhs: cfgVariables) {
			ArrayList<Character> temp = new ArrayList<Character>(); 
			ArrayList<String> rhs = cfg.get(lhs);
			if(epsilon.contains(lhs)) {
				temp.add('e');
			}
			for(String rule: rhs) {
				for (char chr: rule.toCharArray()) {
					if(!(chr == lhs)) {
						if(!(temp.contains(chr))) {
							temp.add(chr);
						}
					}
					if(!(epsilon.contains(chr))) {
						break;
					}
				}
			}
			if(!(i==0)) {
				ArrayList<Character> behindMe = new ArrayList<Character>(cfgVariables.subList(0, i));
				ArrayList<Character> tempTemp = new ArrayList<Character>(); 
				for(Character ch: temp) {
					for(int l = 0;l<behindMe.size();l++) {
						if(behindMe.get(l) == ch) {
							ArrayList<Character> rhs2 = firsts.get(behindMe.get(l));
							tempTemp.addAll(rhs2);
						}
						else {
							if(!(behindMe.contains(ch))) {
								tempTemp.add(ch);
							}
						}
					}
				}
				tempTemp = removeDuplicates(tempTemp);
				temp = tempTemp;
			}
			Collections.sort(temp);  
			ArrayList<Character> copytemp = (ArrayList<Character>) temp.clone();
			firsts.put(lhs, copytemp);
			for(int j = 0;j<i;j++) {
				ArrayList<Character> rhs2 = firsts.get(cfgVariables.get(j));
				if(rhs2.contains(lhs)) {
					Object o = lhs;
					rhs2.remove(o);
					rhs2.addAll(copytemp);
					rhs2 = removeDuplicates(rhs2);
				}
				Collections.sort(rhs2);   
				ArrayList<Character> copyrhs2 = (ArrayList<Character>) rhs2.clone();
				firsts.remove(cfgVariables.get(j));
				firsts.put(cfgVariables.get(j), copyrhs2);
			}		
			i++;
		}
		
		epsilonComplete = epsilonComplete(cfg, cfgVariables);
		for(Character f: cfgVariables) {
			Object ep = 'e';
			ArrayList<Character> last = firsts.get(f);
			if(!(epsilonComplete.contains(f))) {
				last.remove(ep);
				firsts.remove(f);
				firsts.put(f, last);
			}
			//new
			ArrayList<Character> xTemp = (ArrayList<Character>) last.clone();
			for(Character x: last) {
				if(Character.isUpperCase(x)) {
					xTemp.remove(x);
				}
				ArrayList<Character> copyx = (ArrayList<Character>) xTemp.clone();
				firsts.remove(f);
				firsts.put(f, copyx);
			}
			//new
		}
		
		
		ArrayList<Character> output = firsts.get(nonTerminal);
		return output;
		
	}
	
	
	public static String follow(String input){
		Map<Character, ArrayList<String>> cfg = new HashMap<Character, ArrayList<String>>();
		String[] cfgRaw = null; 
		ArrayList<Character> epsilon = new ArrayList<Character>(); 
		ArrayList<Character> cfgVariables = new ArrayList<Character>(); 
		Map<Character, ArrayList<Character>> follows = new HashMap<Character, ArrayList<Character>>();
		
		input(input, cfgRaw, cfg, cfgVariables);
		epsilons(cfg, cfgVariables, epsilon);
		
		for(Character var: cfgVariables) {
			ArrayList<Character> temp = new ArrayList<Character>(); 
			if(var == 'S') {
				temp.add('$');
			}
			for(int i = 0;i<cfgVariables.size();i++) {
				ArrayList<String> rhs = cfg.get(cfgVariables.get(i));
				for(String rule: rhs) {
					char[] characters = rule.toCharArray();
					for (int j = 0;j<characters.length;j++) {
						boolean epsilonFound = false;
						if(characters[j] == var) {
							if((j+1) == (characters.length)) {
								if(!((cfgVariables.get(i)) == var)) {
									temp.add(cfgVariables.get(i));
								}
							}
							else {
								if(Character.isLowerCase(characters[j+1])) {
									temp.add(characters[j+1]);
								}
								else {
									ArrayList<Character> first = singleFirst(input, characters[j+1]);
									if(first.contains('e')) {
										epsilonFound = true;
										Character removing = 'e';
										first.remove(removing);
										temp.addAll(first);
									}
									else {
										temp.addAll(first);
									}
								}
							}
							//new
							int epsiloncounter = j+2;
							//new

							while(epsilonFound) {
								if((epsiloncounter) >= (characters.length)) {
									temp.add(cfgVariables.get(i));
									epsilonFound = false;
									break;
								}
								else {
									if(Character.isLowerCase(characters[epsiloncounter])) {
										temp.add(characters[epsiloncounter]);
										epsilonFound = false;
										break;
									}
									else {
										ArrayList<Character> first = singleFirst(input, characters[epsiloncounter]);
										if(first.contains('e')) {
											epsilonFound = true;
											Character removing = 'e';
											first.remove(removing);
											temp.addAll(first);
											epsiloncounter++;
										}
										else {
											epsilonFound = false;
											temp.addAll(first);
											break;
										}
									}
								}
							}
						}
					}
				}
			}
			temp = removeDuplicates(temp);
			Collections.sort(temp);  
			ArrayList<Character> copytemp = (ArrayList<Character>) temp.clone();
			follows.put(var, copytemp);
		}
		
		boolean change = upperExists(follows, cfgVariables);
		int z = 0;
		while(change && z<50) {
			//System.out.println(follows.toString());
			for(Character chr: cfgVariables) {
				ArrayList<Character> rhs = follows.get(chr);
				ArrayList<Character> tempTemp = new ArrayList<Character>(); 
				for(Character term: rhs) {
					if(Character.isUpperCase(term)) {
						ArrayList<Character> rhs2 = follows.get(term);
						tempTemp.addAll(rhs2);
					}
					else {
						tempTemp.add(term);
					}
				}
				tempTemp = removeDuplicates(tempTemp);
				if(tempTemp.contains(chr)) {
					tempTemp.remove(chr);
				}
				Collections.sort(tempTemp);  
				ArrayList<Character> copytempTemp = (ArrayList<Character>) tempTemp.clone();
				follows.remove(chr);
				follows.put(chr, copytempTemp);
			}
			change = upperExists(follows, cfgVariables);
			z++;
		}
		
		for(Character chr: cfgVariables) {
			ArrayList<Character> rhs = follows.get(chr);
			if(rhs.contains('$')) {
				Object dollar = '$';
				rhs.remove(dollar);
				rhs.add('$');
			}
		}
		
		for(Character f: cfgVariables) {
			Object ep = 'e';
			ArrayList<Character> last = follows.get(f);
			//new
			ArrayList<Character> xTemp = (ArrayList<Character>) last.clone();
			for(Character x: last) {
				if(Character.isUpperCase(x)) {
					xTemp.remove(x);
				}
				ArrayList<Character> copyx = (ArrayList<Character>) xTemp.clone();
				follows.remove(f);
				follows.put(f, copyx);
			}
			//new
		}
		
		String output = output(cfgVariables, follows);
		return output;
	}
	
	
	
	public static void main(String[] srgs){
		String main = "S,xS,car,Jr;J,rSr,goal,e";
		first(main);
		follow(main);
		String main2 = "S,EvvE,EwES,sw,e;Z,ZSs,bis,d;E,EZ,so,e";
		first(main2);
		follow(main2);
		String main3 = "S,SHgH,SLld,llg,e;L,Lz,sLsg,cc;B,ab,e;H,Bw,rHaS,e";
		first(main3);
		follow(main3);
		String main4 = "S,kSkd,ayL,kLdL,s;L,LkdL,kyaL,kH,e;H,kya";
		first(main4);
		follow(main4);
		String main5 = "S,Sz,Szgs,gZ;Z,s,aSsr,e";
		first(main5);
		follow(main5);
		//String truth = "S,a;B,c;A,be;D,efg;E,eg;F,ef";
		//System.out.println(test.equals(truth));
		//String truth2 = "S,$;B,fgh;A,fgh;D,h;E,fh;F,h";
		//System.out.println(test2.equals(truth2));
		}
}
