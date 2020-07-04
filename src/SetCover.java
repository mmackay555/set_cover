import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class SetCover{
    private int minimumSets;
    private boolean[] minimumSetsArr;
    private int universalTrueCounter;
    private boolean justCleared;
    public int globalMinK;
    public int minK;
    private int trueCounter;
    private boolean minKCondition = true;
    public SetCover(){
        minimumSetsArr = null;
    }
    public void setMinimumSets(int minimumSets){
        this.minimumSets = minimumSets;
    }
    public boolean[] getMinimumSetsArr() {
        return minimumSetsArr;
    }
    public void setMinK(int minK) {
        this.minK = minK;
    }
    public void backtrack(boolean[] a, int k, Integer[][] subsets, boolean[] universal, boolean[] c){
        if(universalTrueCounter == universal.length){
            process_solution(a);
            universal = clearUniversalArr(universal);
            justCleared = true;
            backtrack(a,k, subsets, universal, c);
        }
        else if(k == subsets.length-1){
            return;
        }
        else{
            k = k+1;
            int ncandidates = construct_candidates(k);
            for(int i = 0; i < ncandidates; i++) {
                a[k] = c[i];
                if (i == 1) {
                    trueCounter++;
                } else {
                    if (trueCounter > 0) {
                        trueCounter--;
                    }
                }
                if (i == 1 && k < minK) {
                    minK = k;
                }
                if (k == a.length-1) {
                    update_universal(a, subsets, universal, minK);
                }
                backtrack(a, k, subsets, universal, c);
                justCleared = false;
            }
        }
    }
    public int construct_candidates(int k){
        if(trueCounter >= minimumSets){
            return 1;
        }
        else{
            return 2;
        }
    }
    public void update_universal(boolean[] a, Integer[][] subsets, boolean[] universal, int m){
            universal = clearUniversalArr(universal);
                for (int j = m; j < a.length; j++) {
                    if (a[j]) {
                        int k = 0;
                        while (universalTrueCounter != universal.length && k < subsets[j].length) {
                            Integer val = subsets[j][k];
                            if (!(universal[val - 1])) {
                                universalTrueCounter++;
                                universal[val - 1] = true;
                            }
                            k++;
                        }
                        if(universalTrueCounter == universal.length){
                            j = a.length;
                        }
                    }
                }
    }
    public void process_solution(boolean[] a){
        if(trueCounter < minimumSets) {
            minimumSets = trueCounter;
            minimumSetsArr = a.clone();
        }
    }
    public boolean[] clearUniversalArr(boolean[] universal){
        universalTrueCounter = 0;
        return new boolean[universal.length];
    }
    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        try{
            SetCover tempObj = new SetCover();
            URL path = SetCover.class.getResource("s-k-20-35.txt");
            File testFile = new File(path.getFile());
            BufferedReader reader = new BufferedReader(new FileReader(testFile));
            String universalStr = reader.readLine();
            int universal = Integer.parseInt(universalStr);
            String subsetsStr = reader.readLine();
            int subsets = Integer.parseInt(subsetsStr);
            String str;
            Integer[][] listOfSubsets = new Integer[subsets][universal];
            int counter = 0;
            while((str = reader.readLine()) != null) {
                if (str.equals("")){
                    listOfSubsets[counter] = new Integer[0];
                    counter++;
                }
                else{
                    String[] splitStr = str.split(" ");
                    Integer[] intArray = new Integer[splitStr.length];
                    for (int i = 0; i < splitStr.length; i++) {
                        intArray[i] = Integer.parseInt(splitStr[i]);
                    }
                    listOfSubsets[counter] = intArray;
                    counter++;
                }
            }
            boolean[] universalArr = new boolean[universal];
            boolean[] solutionArr = new boolean[subsets];
            boolean[] c = {false, true};
            tempObj.setMinK(subsets);
            tempObj.setMinimumSets(subsets);
            tempObj.backtrack(solutionArr, -1, listOfSubsets, universalArr, c);
            boolean[] tempArr = tempObj.getMinimumSetsArr();
            for(int m = 0; m < tempArr.length; m++){
                if(tempArr[m]){
                    System.out.println(m+1);
                }
            }
        }
        catch(Exception e){
            System.out.println("Invalid file.");
        }
        long finishedTime = System.currentTimeMillis();
        long elapsedTime = finishedTime - startTime;
        elapsedTime = elapsedTime / (long) (1000.0);
        System.out.println(elapsedTime);
    }
}