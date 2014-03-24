import java.io.*;

import static java.util.Arrays.asList;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;
class TrigramDistribution
{
	Map<List<String>,Long> TrigramDict=new HashMap<List<String>,Long>();//contains the trigrams as a list of strings and its frequency from w3_.txt
	TrigramDistribution (String file)throws IOException
	{
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		for(String temp = ""; temp != null; temp = in.readLine())
		{
			String a[]=temp.split("\t");
			if(a.length>1)
			{
				//because of retarded error.Try without this line sometime.
				TrigramDict.put(asList(a[1],a[2],a[3]),Long.parseLong(a[0]));
				
			}
				
		}
	
	
	}
	public void getTrigramCount(List<String> l,unigramDistribution u)//displays the count of a given trigram, 'not found' if not found
	{
		List<String>l1=new ArrayList<String>();
		List<String>l2=new ArrayList<String>();
		List<String>l3=new ArrayList<String>();
		Map<List<String>,Long> candidateTrigramDict=new HashMap<List<String>,Long>();
		
		if(TrigramDict.containsKey(l))
		{
			System.out.println(l+"="+TrigramDict.get(l));return;
		}
		else
		{//System.out.println(l+"="+"not found");
			l1=u.correct(l.get(0));
			l2=u.correct(l.get(1));
			l3=u.correct(l.get(2));
			for(String i:l1)
			{
				for(String j:l2)
				{
					for(String k:l3)
					{
						if(TrigramDict.containsKey(asList(i,j,k)))
								{
									candidateTrigramDict.put(asList(i,j,k), TrigramDict.get(asList(i,j,k)));
								}
						
					}
						
				}
			}
			Long maxValueInMap=(Collections.max(candidateTrigramDict.values()));  // This will return max value in the Hashmap
	        for (Entry<List<String>, Long> entry : candidateTrigramDict.entrySet()) {  // Itrate through hashmap
	            if (entry.getValue()==maxValueInMap) {
	                System.out.println(entry.getKey()+"="+entry.getValue());     // Print the key with max value
	            }
		}
		
		
	}
	
	
}
}
class unigramDistribution
{
	public Map<String,Long> uniDict=new HashMap<String,Long>();//dictionary of words and their counts in count_big.txt
	public unigramDistribution(String file) throws IOException
	{
		BufferedReader in=new BufferedReader(new FileReader(file));
		for(String temp="";temp!=null;temp=in.readLine())
		{
			String a[]=temp.split("\t");
			if(a.length>1)
				uniDict.put(a[0], Long.parseLong(a[1]));
		}
		
		
	}
	
	private final ArrayList<String> edits(String word)//returns a list of all words that are 1 edit distance away from the argument word
	{
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));//deletion
		for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));//transposition
		for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));//substitution
		for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));//insertion
		return result;
	}
	HashMap<Long, String> candidates=null;//dictionary to keep track of frequency of words in result(see above)
	public final ArrayList<String> correct(String word) {
		String word1=word.toLowerCase();			//*
		//if(uniDict.containsKey(word1)) return word;			//*
		ArrayList<String> list = edits(word1);
		ArrayList<String> validCandidates=new ArrayList<String>();
		candidates = new HashMap<Long, String>();
		candidates.put((long) 0, word);//candidates contains list of words in dictionary that are edit distance of 1 away 
		for(String s : list) if(uniDict.containsKey(s)) candidates.put(uniDict.get(s),s);
		/*if(candidates.size() > 0)
		{
			//System.out.println(word+" : "+candidates);			//*
			return candidates.get(Collections.max(candidates.keySet()));//return the word in candidates(edit distance 1 as of now) with highest frequency
		}*/
		/*for(String s : list) for(String w : edits(s)) if(uniDict.containsKey(w))//check if the dictionary contains words of edit distance 2
		{
			candidates.put(uniDict.get(w),w);//if so, add it to candidates
			//System.out.println(word+" : "+candidates);
		}*/
		//return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;//return the candidate with highest frequency, if no word in candidate is in the dictionary the  original word is returned
		for(Map.Entry<Long, String> entry:candidates.entrySet())
		{
			validCandidates.add(entry.getValue());
		}
		return validCandidates;
	}

}
public class Trigram {
	
	static ArrayList<List<String>> trigrams_set=new ArrayList<List<String>>();
	
	public static void computeTrigramsSet(String input)
	{
		int count=input.split("\\s+").length;//count now has no of words in input
		//System.out.println("no of words="+count);
		String words[]=input.split("\\s+");
		
		for(int i=0;i<count-2;i++)//any sentence of length n has n-2 trigrams
		{
			List<String> temp = new ArrayList<String>();
			temp.clear();
			for(int j=0;j<3;j++)
			{		temp.add(words[i+j]);//temp is each trigram
					//System.out.println("temp="+temp);
					
			}
			
			trigrams_set.add(temp);//temp is added to the list of trigrams
			//System.out.println("trigrams_set="+trigrams_set);
			
		}
		
		
	}
	public static void main(String[] args) throws IOException
	{
		unigramDistribution u=new unigramDistribution("count_big.txt");
		TrigramDistribution o=new TrigramDistribution("w3_.txt");
		String input="asd the king is here";
		computeTrigramsSet(input);
		for(List<String> s:trigrams_set)
		{	//System.out.println(s);
			o.getTrigramCount(s,u);
		}
		
		/*for(Map.Entry<String, Long> entry:o.TrigramDict.entrySet())
		{
			System.out.println(entry.getKey()+" "+entry.getValue());
		}*/
		//for(String s:trigrams_set)
			
		
	}

}
