import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

public class ContextRec {
	int sentenceCount;
	JTextPane textarea;
	ArrayList<Character> separator=null;
	JPopupMenu popupMenu;
	String letter[]={"to","from","dear","sir","thank","you","yours","faithfully","sincerely"};
	String code[]={"int","float"};
	String essay[]={"the","of"};
	String poem[]={"",""};
	String story[]={"",""};
	String tagArray[][]={letter,code,essay,poem,story};			
	ArrayList<String> tags[]=new ArrayList[10];					//a list of all tags as an array of arraylists
	int n=5;
	int hitCount[]=new int[n];
	
	public ContextRec(JTextPane area)			//  constructor to initialize all variables
	{
		char sep[]={' ',',','\n',';','.'};
		separator=new ArrayList<Character>(); for(char c:sep)separator.add(c);
		sentenceCount=0;
		textarea=area;
		popupMenu = new JPopupMenu();
		for(int i=0;i<n;i++)					//  add the tags to the arrayList array.
		{
			tags[i]=new ArrayList<String>();
			for(String s:tagArray[i])
			{
				tags[i].add(s);
				hitCount[i]=0;
			}
		}
	}

	public void hidePopup()
	{
		popupMenu.setVisible(false);
	}
	public void createPopup(JLabel jLab)			//  the contents of jlabel is shown as popup
	{
        popupMenu.removeAll();
        popupMenu.setOpaque(false);
        popupMenu.setBorder(null);
        popupMenu.add(jLab, BorderLayout.CENTER);
        popupMenu.show(textarea, 450, 500);							//  Right bottom of screen
        textarea.requestFocus();
	}
	public int getSentenceCount()		//  returns the number of sentences typed
	{
		String typedText=textarea.getText();
		sentenceCount=0;
		for(int i=0;i<typedText.length();i++)
			if(typedText.charAt(i)=='.')//||typedText.charAt(i)=='\n')&&typedText.charAt(i+1)==' ')
				sentenceCount++;
		return sentenceCount;
	}
	public int getMax()					//   return the index of the tag with the maximum hits
	{
		int i,max=hitCount[0],index=0;
		boolean hitsExist=false;
		for(i=0;i<n;i++)
		{
			if(hitCount[i]>1)				// o or 1 or 2 ----- CHECK THIS
			{
				hitsExist=true;
				break;
			}
		}
		if(!hitsExist)
			return -1;
		for(i=1;i<n;i++)
		{
			if(hitCount[i]>max)							//------------------------- NEED TO TAKE CARE OF TIES
			{
				max=hitCount[i];
				index=i;
			}
		}
		return index;
	}
	public void recognizer() throws BadLocationException		//   the main concept of recognizing the context
	{
		hidePopup();
		System.out.println("Sentence count= "+sentenceCount);
		//textarea.getDocument().insertString(caretPos, "!SENT", null);
		
        String textTyped=textarea.getText().toLowerCase();
        String words[]=textTyped.split("(,| |;|\n)");
        /*
         ArrayList<String> allWords=new ArrayList<>();
        for(String s:words) allWords.add(s);
        System.out.println("WORDS TYPED:"+allWords);
        */
        String word="";
        int i;
        for(i=0;i<n;i++) hitCount[i]=0;
        for(i=0;i<textTyped.length();i++)
        {
        	char c=textTyped.charAt(i);
        	if(separator.contains(c))
        	{
        		for(int j=0;j<n;j++)
        		{
        			if(tags[j].contains(word))
        				hitCount[j]++;
        		}
        		word="";
        	}
        	else word=word+c;
        }
        int index=getMax();
        System.out.println("index selected="+index);
        switch(index)
        {
        	case -1:System.out.println("No hits");
        			hidePopup();
        			return;
        	case 0: letterTheme();        			
        			break;
        			
        	/*case 1: letterTheme();        			
					break;
        	case 2: letterTheme();        			
					break;
        	case 3: letterTheme();        			
					break;
        	case 4: letterTheme();        			
					break;
        	case 5: letterTheme();        			
					break;*/
        	default: hidePopup();
        }
	}
	public void letterTheme()
	{
		JLabel jLab;
		String s="Are you typing a letter?";
		jLab=new JLabel(s);
		jLab.setFont(new Font("Segoe UI", 0, 14));
		createPopup(jLab);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
