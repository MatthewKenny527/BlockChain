import java.util.*;
import java.security.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date; 


public class SHA256 {
    public static String [] shaAry ;
    public static String [] sentAry;
    
    public static void main (String[] args) throws IOException{
        //randomString();
        //highestcompares();
        //dicArray();
        
        //Loopnames();
        //long start =java.time.LocalTime.getTime();
        //System.out.println(java.time.LocalTime.now());  
        hashbuilder();
        //System.out.println(java.time.LocalTime.now());
        //onebillion();
        //System.out.println(java.time.LocalTime.now().getTime()-start);
        long startTime = System.currentTimeMillis();
        onebillion();
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime/1000.00 + " seconds");
 
    }

    public static String sha256(String input){
        try{
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            byte[] salt = "CS210+".getBytes("UTF-8");
            mDigest.update(salt);
            byte[] data = mDigest.digest(input.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i=0;i<data.length;i++){
                sb.append(Integer.toString((data[i]&0xff)+0x100,16).substring(1));
            }
            return sb.toString();
        }catch(Exception e){
            return(e.toString());
        }
    }

    public static void compare(String s1,String s2,String x1,String x2) throws IOException
    {
        int count=0;
        for(int i=0;i<x1.length();i++)
        {
            if(x1.charAt(i)==x2.charAt(i))
            {
                count++;
            }
            if(i>=50 && count < 6)
            {
                break;
            }
        }
        if(count >= 21 && !s1.equals(s2))
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./hash-v1.txt", true));
            System.out.println(s1 + "\n" + s2 +"\n" + x1 + "\n" + x2 +"\n"+ count +"\n\n" );
            writer.write(s1 + "\n" + s2 +"\n" + x1 + "\n" + x2 +"\n"+ count +"\n\n" );
            writer.close();
        }

    }

    public static void onebillion() throws IOException
    {
        Dictionary dict = new Dictionary();
        Random rand = new Random();
        String sentence1="";
        String sentence2="";
        String shaOne="";
        String shatwo="";

        int counter=0;

        for(int i = 0; i< 100000; i++)
        {
            if(i%100==0)
            {
                counter++;
                System.out.println(counter);
            }
            sentence1 = dict.getWord(rand.nextInt(dict.getSize())) + " and " + dict.getWord(rand.nextInt(dict.getSize())) + " are friends.";
            shaOne = sha256(sentence1);
            for(int j =0;j<dict.getSize()*10;j++) {
                
                compare(sentence1, sentAry[j], shaOne, shaAry[j]);
            }
        }
        
        
    }
    public static void hashbuilder()
    {
        Dictionary dict = new Dictionary();
        sentAry = new String[dict.getSize()*10];
        shaAry = new String[dict.getSize()*10];
        String []ages = {"10","11","12","13","14","15","16","17","18","19"};
        int size = dict.getSize();
        int k =0;

        for(int j=0;j<ages.length;j++)
        {
            for(int i=0;i<size;i++)
            {
                if(size%dict.getSize()==0)
                sentAry[k] = dict.getWord(i) + " is " + ages[j] + " years old.";
                shaAry[k] = sha256(sentAry[k]);
                k++;
            }
        }

    }

}

class Dictionary{
     
    private String input[]; 

    public Dictionary(){
        input = load("./first-names.txt");  
    }
    
    public int getSize(){
        return input.length;
    }
    
    public String getWord(int n){
        return input[n];
    }
    
    private String[] load(String file) {
        File aFile = new File(file);     
        StringBuffer contents = new StringBuffer();
        BufferedReader input = null;
        try {
            input = new BufferedReader( new FileReader(aFile) );
            String line = null; 
            int i = 0;
            while (( line = input.readLine()) != null){
                contents.append(line);
                i++;
                contents.append(System.getProperty("line.separator"));
            }
        }catch (FileNotFoundException ex){
            System.out.println("Can't find the file - are you sure the file is in this location: "+file);
            ex.printStackTrace();
        }catch (IOException ex){
            System.out.println("Input output exception while processing file");
            ex.printStackTrace();
        }finally{
            try {
                if (input!= null) {
                    input.close();
                }
            }catch (IOException ex){
                System.out.println("Input output exception while processing file");
                ex.printStackTrace();
            }
        }
        String[] array = contents.toString().split("\n");
        /*for(String s: array){
            s.trim();
        }*/
        for (int i=0; i<array.length; i++)
        {
            array[i] = array[i].trim();
        }

        return array;
    }
}
