
import java.io.*;
import java.util.*;
import java.util.regex.*;
public class NewSpellChecker {


	public final HashMap<String, Long> nWords = new HashMap<String, Long>();

	
	public NewSpellChecker(String file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		
		for(String temp = ""; temp != null; temp = in.readLine()){
			String a[]=temp.split("\t");
			if(a.length>1)
			nWords.put(a[0], Long.parseLong(a[1]));
		}
		in.close();
	}

	private final ArrayList<String> edits(String word) {
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
		for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
		for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
		for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
		return result;
	}

	public final ArrayList<String> getCandidates(String word)
	{
		return null;
		
	}
	HashMap<Long, String> candidates=null;
	public final String correct(String word) {
		String word1=word.toLowerCase();			//*
		if(nWords.containsKey(word1)) return word;			//*
		ArrayList<String> list = edits(word1);
		candidates = new HashMap<Long, String>();
		candidates.put((long) 0, word);
		for(String s : list) if(nWords.containsKey(s)) candidates.put(nWords.get(s),s);
		if(candidates.size() > 0)
		{
			System.out.println(word+" : "+candidates);			//*
			return candidates.get(Collections.max(candidates.keySet()));
		}
		for(String s : list) for(String w : edits(s)) if(nWords.containsKey(w))
		{
			candidates.put(nWords.get(w),w);
			System.out.println(word+" : "+candidates);
		}
		return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
	}

	public static void main(String args[]) throws IOException {
		
		NewSpellChecker obj=new NewSpellChecker("count_big.txt");
		System.out.println("Enter the string:");
		
		BufferedReader BR=new BufferedReader(new InputStreamReader(System.in));
		String stmt=BR.readLine();
		String seg[]=stmt.split(" ");
		for(String s:seg)
		System.out.println((s+" : ")+obj.correct(s));
	}

}