import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PDA {

	
	private	Map<Character, ArrayList<String>> cfg = new HashMap<Character, ArrayList<String>>();
	private String[] cfgRaw = null;
	private String[] firstRaw = null;
	private String[] followRaw = null;
	private ArrayList<Character> cfgVariables = new ArrayList<Character>(); 
	private Map<Character, ArrayList<String>> firsts = new HashMap<Character, ArrayList<String>>();
	private Map<Character, ArrayList<String>> follows = new HashMap<Character, ArrayList<String>>();
	private Map<Character, Map<Character,String>> LL1 = new HashMap<Character, Map<Character,String>>();
	private String input = "";
	private String output = "";
	private Stack<Character> PDA = new Stack<Character>();
	
	
	public PDA(String input) {
		this.input = input;
	}

	public void input(){
		String noSpaceStr = this.input.replaceAll("\\s", "");
		String[] hashSplit = noSpaceStr.split("#");
		this.cfgRaw = hashSplit[0].split(";"); 
		for(int i2 =0;i2<this.cfgRaw.length;i2++) {
			String[] cfgPart = this.cfgRaw[i2].split(",");
			String[] temp = Arrays.copyOfRange(cfgPart, 1, cfgPart.length);
			ArrayList<String> cfgRHS = new ArrayList<String>();
			Collections.addAll(cfgRHS, temp);
			this.cfg.put(cfgPart[0].charAt(0), cfgRHS);
			this.cfgVariables.add(cfgPart[0].charAt(0));
		}
		this.firstRaw = hashSplit[1].split(";");
		for(int i =0;i<this.firstRaw.length;i++) {
			String[] firstPart = this.firstRaw[i].split(",");
			String[] temp = Arrays.copyOfRange(firstPart, 1, firstPart.length);
			ArrayList<String> firstRHS = new ArrayList<String>();
			Collections.addAll(firstRHS, temp);
			this.firsts.put(firstPart[0].charAt(0), firstRHS);
		}
		this.followRaw = hashSplit[2].split(";");
		for(int i3 =0;i3<this.followRaw.length;i3++) {
			String[] followPart = this.followRaw[i3].split(",");
			String[] temp = Arrays.copyOfRange(followPart, 1, followPart.length);
			ArrayList<String> followRHS = new ArrayList<String>();
			Collections.addAll(followRHS, temp);
			this.follows.put(followPart[0].charAt(0), followRHS);
		}
		
		//System.out.println("var:" + cfgVariables.toString());
		//System.out.println("cfg:" + cfg.toString());
		//System.out.println("first:" + firsts.toString());
		//System.out.println("follow:" + follows.toString());
	}
	
	public void LL1() {
		for(Character var: cfgVariables) {
			Map <Character, String> inputSymbol= new HashMap <Character, String>();
			ArrayList<String> cfgRHS = cfg.get(var);
			ArrayList<String> firstRHS = firsts.get(var);
			String followRHS = follows.get(var).get(0);
			for(int i = 0;i<firstRHS.size();i++) {
				for(Character chrFirst: firstRHS.get(i).toCharArray()) {
					if(chrFirst == 'e') {
						for(Character chr: followRHS.toCharArray()) {
							inputSymbol.put(chr, "e");
						}
					}
					else {
						inputSymbol.put(chrFirst, cfgRHS.get(i));
					}
				}
			}
			LL1.put(var, inputSymbol);
		}
		
		//System.out.println(LL1.toString());
	}
	
	public String reverseString(String rev) {
        byte[] strAsByteArray = rev.getBytes();
 
        byte[] result = new byte[strAsByteArray.length];
 
        // Store result in reverse order into the
        // result byte[]
        for (int i = 0; i < strAsByteArray.length; i++)
            result[i] = strAsByteArray[strAsByteArray.length - i - 1];
 
        return (new String(result));
	}
	
	public String PDA(String txt) {
		cfg = new HashMap<Character, ArrayList<String>>();
		cfgRaw = null;
		firstRaw = null;
		followRaw = null;
		cfgVariables = new ArrayList<Character>(); 
		firsts = new HashMap<Character, ArrayList<String>>();
		follows = new HashMap<Character, ArrayList<String>>();
		LL1 = new HashMap<Character, Map<Character,String>>();
		output = "";
		PDA = new Stack<Character>();
		
		this.input();
		this.LL1();
		String ogTxt = txt;
		txt = txt + "$";
		PDA.add('S');
		output = output + "S, ";
		String preOutput = "S";
		char[] inputArray = txt.toCharArray();
		for(int i = 0;i<inputArray.length;i++) {
			if(PDA.isEmpty() && (inputArray[i] != '$')) {
				output = output + "ERROR  ";
				break;
			}
			if(PDA.isEmpty()) {
				break;
			}
			if(inputArray[i] == PDA.peek()) {
				PDA.pop();
			}
			else {
				Character stackTop = PDA.pop();
				if(Character.isLowerCase(stackTop)) {
					output = output + "ERROR  ";
					break;
				}
				
				String pushString = LL1.get(stackTop).get(inputArray[i]);
				if(pushString == null) {
					output = output + "ERROR  ";
					break;
				}
				String temp = "";
				boolean done = false;
				for(Character chr: preOutput.toCharArray()) {
					if(Character.isUpperCase(chr) && !done) {
						if(!(pushString == "e")) {
							temp = temp + pushString;
							done = true;
						}
						else {
							done = true;
						}
					}
					else {
						temp = temp + chr;
					}
				}
				output = output + temp + ", ";
				preOutput = temp;
				pushString = reverseString(pushString);
				for(Character ch: pushString.toCharArray()) {
					if(!(ch == 'e')) {
						PDA.push(ch);
					}
				}
				i--;
			}
		}
		
		output = output.substring(0, output.length()-2);
		String error = "ERROR";
		
		if(!PDA.isEmpty() && (preOutput != ogTxt)) {
			if(!((output.substring(output.length()-5,output.length())).equals(error))) {
				output = output + "ERROR  ";
			}
		}
		
		System.out.println(output);
		return output;
		
	}
	
	public static void main(String[] srgs){
		PDA x = new PDA("S,AB;A,iA,n;B,CA;C,zC,o#S,in;A,i,n;B,oz;C,z,o#S,$;A,oz$;B,$;C,in");
		//String truth = "S,bLc,bSNc,baNc,ERROR";
		x.PDA("noin");
		x.PDA("inon");
		x.PDA("inzon");
		x.PDA("noiin");
		x.PDA("iizi");
				
		
		//String test = x.PDA("ba");
		
		PDA y = new PDA("S,aY;Y,SXY,e;X,p,m#S,a;Y,a,e;X,p,m#S,mp$;Y,mp$;X,amp$");
		y.PDA("aam");
		y.PDA("aap");
		y.PDA("aapap");
		y.PDA("aapap");
		y.PDA("aa");
		
		
		
		
		
		
		
		//test = test.replaceAll("\\s", "");
		//System.out.println(test.equals(truth));

	}
}
