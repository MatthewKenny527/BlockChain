import java.util.*;
import java.security.*;
import java.io.*;



public class Cs210Project {
    public static String [] shaAry ;
    public static String [] sentAry;
    
    public static void main (String[] args) throws IOException{
      
           
        hashbuilder();  //Runs the hash builder method that builds a array with 1 million sha265 values.
        long startTime = System.currentTimeMillis(); //start time
        mainCode();
        long estimatedTime = System.currentTimeMillis() - startTime; //start time-end time
        System.out.println(estimatedTime/1000.00 + " seconds"); //prints time spent running
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
        /*This loop checks each position in each String and checks if they are the same. If they are the same count
        goes up by one.  I have placed a second if statement to try and speed up the loop. It will break out of the
        if count isnt high enough when it gets to an i value that can be set depending on what compare the user is
        going for.*/
        for(int i=0;i<x1.length();i++)
        {
            if(x1.charAt(i)==x2.charAt(i))
            {
                count++;
            }
            if(i>=54 && count < 6)
            {
                break;
            }
        }
        /*This if statement is uses to print the best compares to the screen and to a file so they dont't get lost.
        I have comment out the file writer*/ 
        if(count >= 19 && !s1.equals(s2))
        {
            //BufferedWriter writer = new BufferedWriter(new FileWriter("./hash-v1.txt", true));
            System.out.println(s1 + "\n" + s2 +"\n" + x1 + "\n" + x2 +"\n"+ count +"\n\n" );
            //writer.write(s1 + "\n" + s2 +"\n" + x1 + "\n" + x2 +"\n"+ count +"\n\n" );
            //writer.close();
        }

    }


    public static void mainCode() throws IOException
    {
        Dictionary dict = new Dictionary();
        Random rand = new Random();
        String sentence1="";
        String sentence2="";
        String shaOne="";

        int i=0;

        /* This double for loop compares a randomly created string to the whole array that is created in the hashbuilder
        method. The compare is done in a different method called comapare() */

        while(true)
        {
            if(i%100==0)
            {
                if(i==0)
                {
                    System.out.println("Start of code...");
                }
                else if(i<1000)
                {
                    System.out.println(i  + " million compares ran.");
                }
                else if (i>=1000)
                {
                    double num = i/1000.0;
                    System.out.println(num + " billion compares ran.");
                }
                
            }
            sentence1 = dict.getWord(rand.nextInt(dict.getSize())) + " and " + dict.getWord(rand.nextInt(dict.getSize())) + " are friends.";
            shaOne = sha256(sentence1);
            for(int j =0;j<dict.getSize()*10;j++) {
                
                compare(sentence1, sentAry[j], shaOne, shaAry[j]);
            }
            i++;
        }
        
        
        
    }

    public static void hashbuilder()
    {
        Dictionary dict = new Dictionary();
        sentAry = new String[dict.getSize()*10];
        shaAry = new String[dict.getSize()*10];
        String []ages = {"one","two","three","four","five","six","seven","eight","nine","ten"};
        int size = dict.getSize();
        int k =0;
        /*This method creates an array of just over 1 million sha265 values. This is done with the double for loop 
        below.*/

        for(int j=0;j<ages.length;j++)
        {
            for(int i=0;i<size;i++)
            {
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
