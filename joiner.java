/*17/09/2017
 * Etude 7 - Joined up Writing
 * Jessy Ruiter and Rebecca Wilson
 */

import java.util.*;

public class joiner{
    //////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> dict = new ArrayList<String>(); //dictionary scanned in here
    //store the shortest double and single chains
    public static List<String> doub = new ArrayList<String>();
    public static boolean isD = false;
    public static List<String> sing = new ArrayList<String>();
    public static boolean isS = false;
    public static Queue<String> q = new LinkedList<String>();//q for building the tree
    public static Tree<String> tree; //for doing wfs guarnteed shortest path
    public static Tree<String> dtree;
    public static int[] charPos = {-1,-1,-1,-1,-1,
                                   -1,-1,-1,-1,-1,
                                   -1,-1,-1,-1,-1,
                                   -1,-1,-1,-1,-1,
                                   -1,-1,-1,-1,-1,-1}; //first instance of each char in dict
    public static String letters = "abcdefghijklmnopqrstuvwxyz";
    public static boolean doublefound = false; //used to persist search until both types are found
    public static boolean singlefound = true;
    public static String loopstopper = "";
    //////////////////////////////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args){
        //System.out.println("Scanning...");
        Scanner scan = new Scanner(System.in); //scans file not command line
        while(scan.hasNext()){ //scan in the entire dictionary
            dict.add(scan.next());
        }
        //System.out.println("Sorting...");
        Collections.sort(dict); //sort the dictionary
        //System.out.println("Finding Letters...");
        int posCh = 0, posDict = 0;
        for(char cur = 'a'; cur <='z'; cur++){ //find the first occurance of each letter of the alphabet
            for(int i = posDict; i < dict.size(); i++){                
                if(dict.get(i).charAt(0) == cur){
                    charPos[posCh] = i;
                    posCh++;
                    break;
                }
                posDict++;
            }
        }
        //System.out.println("Checking...");
        String target = args[0];
        if(args[0].equals(args[1])){ //if args[1] and args[0] are the same no need to search
            System.out.println("1" + args[0] + " " + args[1]  + "\n1 " + args[0] + " " + args[1]);
        }else{
            //check that the dictionary actually contains args[0] and args[1]
            boolean a = false, b = false;
            for(int i = 0; i < dict.size(); i++){
                if(dict.get(i).equals(args[0])){
                    a = true; //located args[0]
                } else if((dict.get(i).equals(args[1]))){
                    b = true; //located args[1]
                }
                if(a && b){
                    break; //no need to go through every word once they're found
                }
            }
            /////////////
            //System.out.println("Generating trees...");
            if(a && b){
                tree = new Tree<String>(args[0]);
                dtree = new Tree<String>(args[0]);
                q.add(args[0]);
                builder(q.peek(),args[1]);
                if(!q.isEmpty()){
                    submain(args[0],args[1]); //SUBMAIN IS CALLED HERE
                    // //***************DISPLAYING********************
                    if(sing.size() > 0){
                        System.out.print(sing.size() + " ");
                        for(String aa : sing){
                            System.out.print(aa + " ");
                        }
                        System.out.println();
                    } else{
                        System.out.println("0");
                    }
                    if(doub.size() > 0){
                        System.out.print(doub.size() + " ");
                        for(String aa : doub){
                            System.out.print(aa + " ");
                        }
                        System.out.println();
                    } else{
                        System.out.println("0");
                    }
                    // //***************DISPLAYING ENDS********************
                }else{
                    System.out.println("0\n0");
                }
            }else{
                System.out.println("0\n0");
            }
        }
    }

    public static void submain(String s, String f){
        //System.out.println("Processing...");
        a: while(!isD && !q.isEmpty()){
            String result = "", e;
            e = tree.findParents(f);
            int num = 0;
            for(String aa : e.split(" ")){
                if(!result.contains(aa)){
                    result += aa + " ";
                    num++;
                }
            }
            result += s; //might be easier to just adjust the double check
            num++;
            String[] resarr= new String[num];
            num--;
            for(String aa : result.split(" ")){
                resarr[num] = aa;
                num --;
            }
            for(int i = 0; i < resarr.length-1; i++){
                isD = checkD(resarr[i], resarr[i+1]);
                if(!isD){
                    break;
                }
            }
            // if( dtree.find(f) != null){ if the doubly joined tree contains args[1]
            //     isD = true;
            // }
            if(isD){
                for(String aa : resarr){
                    doub.add(aa);
                    if(!isS){
                        sing.add(aa);
                    }
                }
                break a;
                //if something is doubly linked it is also singly
            } else{
                if(!isS){
                    isS = true;
                    for(String aa : resarr){
                        sing.add(aa);
                    }
                }
                if(!q.isEmpty() && !loopstopper.equals(q.peek())){
                    tree.remove(f);
                    loopstopper = q.peek(); //probably not necessary
                    builder(q.peek(),f);
                } else{
                    break a;
                }
            }
        }
        return;
    }

    /*Determines if the start of one word is the end of another
     * @param one the starting word
     * @param two the word we're looking for
     * @return true only if the start of two matches the end of one
     */
    public static void builder(String s, String f){
        //System.out.println("Building...");
        do{
            //System.out.println("Children of " + s + ": ");
            q.remove();
            //System.out.println("do while");
            ArrayList<String> l  = subFinder(s, f);
            //System.out.println(l);
            if(l != null){
                String kids = "";
                String dkids = ""; //for children who are doubly joined
                for(String str : l){
                    q.add(str);
                    if(str.equals(f)){
                        tree.addChildren(s, f, tree);
                        if(checkD(s,str)){
                            dtree.addChildren(s, f, tree);
                        }
                        return; //stop building to the tree once you find the string
                    }
                    kids = kids + str + " ";
                    if(checkD(s,str)){
                        dkids = kids + str + " ";
                    }
                }
                kids.trim();
                //System.out.println(kids);
                tree.addChildren(s, kids, tree); //adds all valid joined words to the tree
                dtree.addChildren(s, dkids, tree);
            }
           
            s = q.peek();
        } while(!q.isEmpty());
        return;
        
    }

    /*Finds children too add to the tree
     * @param one the word conataining the suffix
     * @param two the word we're looking for
     * @return an arraylist of all valid children
     */
    public static ArrayList<String> subFinder(String S, String f){
        //System.out.println("Finding results: ");
        ArrayList<String> found = new ArrayList<String>();
        for(int i = 0; i < S.length(); i++){
            int index = charPos[letters.indexOf(S.charAt(i))];
            int k = 0;
            
            if(letters.indexOf(S.charAt(i))+1 == 26){
                k = dict.size()-1;
            } else if(index == -1){
                index = 0;
                 k = dict.size()-1;
            } else {
                k = charPos[letters.indexOf(S.charAt(i))+1];
            }
            
            //System.out.println(k);
            //if(index != -1){
                for(int j = index; j < k; j++ ){
                    String seeker = dict.get(j);
                    if(connect(S, seeker)){
                        if(!isS){
                            found.add(seeker);
                            //System.out.print(seeker + " ");
                            if(!seeker.equals(f)){
                                dict.set(j,"0"); //words need to be replaced to preserve indices
                            }
                        } else if(checkD(S,seeker)){
                            found.add(seeker);
                            //System.out.print(seeker + " ");
                            if(!seeker.equals(f)){
                                dict.set(j,"0"); //words need to be replaced to preserve indices
                            }
                        }
                    }
                }
                //}
        }
        //System.out.println();
        return found;
    }

    /* Finds overlap between two words
     * @param one the word conataining the suffix
     * @param two the word containing the prefix
     * @return true only if the start of two matches the end of one
     */
    public static boolean connect(String one, String two){
        //System.out.println("Connecting...");
        if(one.equals(two)) return false;
        for(int i = 0; i < one.length(); i++){
            if(two.contains(one.substring(i,one.length()))
               && one.substring(i,one.length()).length() > 1
               && one.charAt(i) == two.charAt(0)
               && one.charAt(one.length()-1) == two.charAt(one.length()-i-1)){  //charAt(0) shouldn't be found each time
                //System.out.println("Success");
                return true;
            }
        }
        //System.out.println("Failed");
        return false;
    }

    /*Determines if a pair of words is doubly joined
     * @param one the word conataining the suffix
     * @param two the word containing the prefix
     * @return true only if the two words are doubly joined
     */
    public static boolean checkD(String one, String two){
        //System.out.println("Checking double...");
        String s = "";
        for(int i = 0; i < one.length(); i++){
            if(two.contains(one.substring(i,one.length()))
               && one.charAt(i) == two.charAt(0)
               && one.charAt(one.length()-1) == two.charAt(one.length()-i-1)){  //charAt(0) shouldn't be found each time
                s = one.substring(i,one.length());
                break;
            }
        }
        //System.out.println(s + ": " + s.length() + " " + one  + " [" + one.length()/2 + "] "
        //                   + two  + " [" + two.length()/2 + "] ");
        if(s.length() >= one.length()/2
           && s.length() >= two.length()/2){
            //System.out.println("Success");
            return true;
        }
        //System.out.println("Failed");
        return false;
    }
}
