/**
 * @author Anurag Kumar
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class ReadFile {
    /**
     * This class helps to read a file (inout.dat) and parse it
     */
    private ArrayList<String> inputValues;
    private int numberOfProcesses;
    private int[] UIDs;
    private int root;

    public ArrayList<String> getInputValues() {
        return inputValues;
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public int[] getUIDs() {
        return UIDs;
    }

    public int getRoot() {
        return root;
    }

    public ArrayList<Integer>[] getConnectionMatrix() {
        return connectionMatrix;
    }

    private ArrayList<Integer>[] connectionMatrix;

    /**
     * public function to read a file
     * @param arg the relative filename to be opened
     */
    public ReadFile(String arg) {
        inputValues = new ArrayList<>();
        readFile(arg);
    }

    /**
     * private function to read a file
     * @param arg the relative filename to be opened
     */
    private void readFile(String arg)  {
        File file = new File(arg);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()){
            String str = sc.nextLine();
            inputValues.add(str);
        }
        init(inputValues);

    }

    /**
     * function to initialize the and populate the array variables
     * @param values the input string read from the file
     */
    public void init(ArrayList<String> values) {
        ArrayList<String> inputFile = values;
        Iterator itr = inputFile.iterator();
        numberOfProcesses = new Integer(Integer.parseInt((String) itr.next()));
        String [] IDs = ((String) itr.next()).split(", *");
        UIDs = Stream.of(IDs)
                .mapToInt(Integer::parseInt)
                .toArray();
        root = new Integer(Integer.parseInt((String) itr.next()));
        String[] connections = ((String) itr.next()).split(";");
        connectionMatrix = new ArrayList[connections.length];
        int i = 0;
        for (String s : connections) {
            String[] arr = s.split(" ");
            connectionMatrix[i] = new ArrayList<Integer>();
            for (String digit : arr) {
                if (!digit.isEmpty()) {
                    connectionMatrix[i].add(Integer.parseInt(digit));
                }
            }
            i++;
        }
    }
}
