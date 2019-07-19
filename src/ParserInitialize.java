import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

enum WordType {T, V, EOF, semanticRule}

class Word {
    WordType type;
    String value;

    Word(WordType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Word)) {
            return false;
        }
        Word word = (Word) obj;
        return word.value.equals(this.value) && word.type == this.type;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

class LR {
    String lhs;
    int rhsl;
    String semantic;

    LR(String lhs, int rhsl, String semantic) {
        this.lhs = lhs;
        this.rhsl = rhsl;
        this.semantic = semantic;
    }

}

/**
 * the main class that construct and initialize the parse table at the end depends on the given grammar
 *
 * @author Mohamadreza Fereydooni, Pouya Mohammadkhani
 */
public class ParserInitialize {


    private static LinkedList<LinkedList<Word>> RHST = new LinkedList<>();
    static LinkedList<LinkedList<Word>> semanticTable = new LinkedList<>();
    private static HashMap<Integer, LinkedList<Word>> firsts = new HashMap<>();
    private static LinkedHashMap<Word, LinkedList<Word>> firstsByVariable = new LinkedHashMap<>();
    private static LinkedHashMap<Word, LinkedHashSet<Word>> follows = new LinkedHashMap<>();
    static HashMap<Word, Map<Word, LinkedList<Integer>>> pt = new LinkedHashMap<>();
    private static LinkedHashSet<Word> terminals = new LinkedHashSet<>();
    private static LinkedHashSet<Word> variables = new LinkedHashSet<>();
    static ArrayList<LR> LRtable = new ArrayList<>();
    static HashMap<Integer, HashMap<Word, String>> bpt = new HashMap<>();

    private static void LRInitialize() throws FileNotFoundException {

        File file = new File("/Users/apple/IdeaProjects/compilerProject/src/beGrammar.txt");
//        File file = new File("C:/Users/ASUS/IdeaProjects/CompilerG/src/beGrammar.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split("( : )|( :)|(: )|( )|(:)");
            int tmp = 0;
            for (int i = 1; i < words.length; i++) {
                if (words[i].startsWith("@") || words[i].startsWith("#")) {
                    continue;
                }
                tmp++;
            }
            String semantic = "";
            if (words[words.length - 1].startsWith("@")) {
                semantic = words[words.length - 1];
            }
            LRtable.add(new LR(words[0], tmp, semantic));
        }
        scanner.close();
    }

    private static void bptInitialize() {
        //TODO
//        static HashMap<Integer, HashMap<Word, String>> bpt = new HashMap<>();
        List<Word> words = Arrays.asList(
                new Word(WordType.T, ")"), new Word(WordType.T, "("),
                new Word(WordType.T, "id"), new Word(WordType.T, "/"),
                new Word(WordType.T, "*"), new Word(WordType.T, "-"),
                new Word(WordType.T, "+"), new Word(WordType.T, "!="),
                new Word(WordType.T, "=="), new Word(WordType.T, ">="),
                new Word(WordType.T, ">"), new Word(WordType.T, "<="),
                new Word(WordType.T, "<"), new Word(WordType.T, "&&"),
                new Word(WordType.T, "||"), new Word(WordType.V, "BE"),
                new Word(WordType.V, "BE'"), new Word(WordType.V, "ORR"),
                new Word(WordType.V, "BT"), new Word(WordType.V, "BT'"),
                new Word(WordType.V, "ANDD"), new Word(WordType.V, "BF"),
                new Word(WordType.V, "E"));


        for (int i = 0; i < 42; i++) {
            LinkedHashMap<Word, String> tmp = new LinkedHashMap<>();
            for (Word word : words) {
                tmp.put(word, "");
            }
            bpt.put(i, tmp);
        }
//        //state 0
        bpt.get(0).put(new Word(WordType.T, "(" ), "s8");
        bpt.get(0).put(new Word(WordType.T, "num"), "s7");
        bpt.get(0).put(new Word(WordType.T, "id"), "s6");
        bpt.get(0).put(new Word(WordType.V, "BE"), "g5");
        bpt.get(0).put(new Word(WordType.V, "BT"), "g4");
        bpt.get(0).put(new Word(WordType.V, "BF"), "g3");
        bpt.get(0).put(new Word(WordType.V, "E"), "g2");
        bpt.get(0).put(new Word(WordType.V, "ION"), "g1");
//        //state 1
        bpt.get(1).put(new Word(WordType.T, "/"), "r23");
        bpt.get(1).put(new Word(WordType.T, "*"), "r23");
        bpt.get(1).put(new Word(WordType.T, "-"), "r23");
        bpt.get(1).put(new Word(WordType.T, "+"), "r23");
        bpt.get(1).put(new Word(WordType.T, "!="), "s25");
        bpt.get(1).put(new Word(WordType.T, "=="), "s24");
        bpt.get(1).put(new Word(WordType.T, ">="), "s23");
        bpt.get(1).put(new Word(WordType.T, ">"), "s22");
        bpt.get(1).put(new Word(WordType.T, "<="), "s21");
        bpt.get(1).put(new Word(WordType.T, "<"), "s20");
        bpt.get(1).put(new Word(WordType.T, ")"), "r23");
        bpt.get(1).put(new Word(WordType.T, "&&"), "r23");
        bpt.get(1).put(new Word(WordType.T, "||"), "r23");
//        //state 2
        bpt.get(2).put(new Word(WordType.T, ")"), "r11");
        bpt.get(2).put(new Word(WordType.T, "/"), "s19");
        bpt.get(2).put(new Word(WordType.T, "*"), "s18");
        bpt.get(2).put(new Word(WordType.T, "-"), "s17");
        bpt.get(2).put(new Word(WordType.T, "+"), "s16");
        bpt.get(2).put(new Word(WordType.T, "&&"), "r11");
        bpt.get(2).put(new Word(WordType.T, "||"), "r11");
//        //state 3
        bpt.get(3).put(new Word(WordType.T, ")"), "r7");
        bpt.get(3).put(new Word(WordType.T, "&&"), "s15");
        bpt.get(3).put(new Word(WordType.T, "||"), "r7");
        bpt.get(3).put(new Word(WordType.V, "BT'"), "g14");
        bpt.get(3).put(new Word(WordType.V, "ANDD"), "g13");
//        //state 4
        bpt.get(4).put(new Word(WordType.T, ")"), "r3");
        bpt.get(4).put(new Word(WordType.T, "||"), "s12");
        bpt.get(4).put(new Word(WordType.V, "BE'"), "g11");
        bpt.get(4).put(new Word(WordType.V, "ORR"), "g10");
//        //state 5
        bpt.get(5).put(new Word(WordType.T, ")"), "acc");
//        //state 6
        bpt.get(6).put(new Word(WordType.T, ")"), "r8");
        bpt.get(6).put(new Word(WordType.T, "/"), "r8");
        bpt.get(6).put(new Word(WordType.T, "*"), "r8");
        bpt.get(6).put(new Word(WordType.T, "-"), "r8");
        bpt.get(6).put(new Word(WordType.T, "+"), "r8");
        bpt.get(6).put(new Word(WordType.T, "!="), "r8");
        bpt.get(6).put(new Word(WordType.T, "=="), "r8");
        bpt.get(6).put(new Word(WordType.T, ">="), "r8");
        bpt.get(6).put(new Word(WordType.T, ">"), "r8");
        bpt.get(6).put(new Word(WordType.T, "<="), "r8");
        bpt.get(6).put(new Word(WordType.T, "<"), "r8");
        bpt.get(6).put(new Word(WordType.T, "&&"), "r8");
        bpt.get(6).put(new Word(WordType.T, "||"), "r8");
//        //state 7
        bpt.get(7).put(new Word(WordType.T, "("), "r9");
        bpt.get(7).put(new Word(WordType.T, "/"), "r9");
        bpt.get(7).put(new Word(WordType.T, "*"), "r9");
        bpt.get(7).put(new Word(WordType.T, "-"), "r9");
        bpt.get(7).put(new Word(WordType.T, "+"), "r9");
        bpt.get(7).put(new Word(WordType.T, "!="), "r9");
        bpt.get(7).put(new Word(WordType.T, "=="), "r9");
        bpt.get(7).put(new Word(WordType.T, ">="), "r9");
        bpt.get(7).put(new Word(WordType.T, ">"), "r9");
        bpt.get(7).put(new Word(WordType.T, "<="), "r9");
        bpt.get(7).put(new Word(WordType.T, "<"), "r9");
        bpt.get(7).put(new Word(WordType.T, "&&"), "r9");
        bpt.get(7).put(new Word(WordType.T, "||"), "r9");
//        //state 8
        bpt.get(8).put(new Word(WordType.T, "("), "s8");
        bpt.get(8).put(new Word(WordType.T, "num"), "s7");
        bpt.get(8).put(new Word(WordType.T, "id"), "s6");
        bpt.get(8).put(new Word(WordType.V, "BE"), "g9");
        bpt.get(8).put(new Word(WordType.V, "BT"), "g4");
        bpt.get(8).put(new Word(WordType.V, "BF"), "g3");
        bpt.get(8).put(new Word(WordType.V, "E"), "g2");
        bpt.get(8).put(new Word(WordType.V, "ION"), "g1");
//        //state 9
        bpt.get(9).put(new Word(WordType.T, ")"), "s41");
//        //state 10
        bpt.get(10).put(new Word(WordType.T, ")"), "r3");
        bpt.get(10).put(new Word(WordType.T, "||"), "s12");
        bpt.get(10).put(new Word(WordType.V, "BE'"), "g40");
        bpt.get(10).put(new Word(WordType.V, "ORR"), "g10");
//        //state 11
        bpt.get(11).put(new Word(WordType.T, ")"), "r0");
//        //state 12
        bpt.get(12).put(new Word(WordType.T, "("), "s8");
        bpt.get(12).put(new Word(WordType.T, "num"), "s7");
        bpt.get(12).put(new Word(WordType.T, "id"), "s6");
        bpt.get(12).put(new Word(WordType.V, "BT"), "g39");
        bpt.get(12).put(new Word(WordType.V, "BF"), "g3");
        bpt.get(12).put(new Word(WordType.V, "E"), "g2");
        bpt.get(12).put(new Word(WordType.V, "ION"), "g1");
        //state 13
        bpt.get(13).put(new Word(WordType.T, ")"), "r7");
        bpt.get(13).put(new Word(WordType.T, "&&"), "s15");
        bpt.get(13).put(new Word(WordType.T, "||"), "r7");
        bpt.get(13).put(new Word(WordType.V, "BT'"), "g38");
        bpt.get(13).put(new Word(WordType.V, "ANDD"), "g13");
        //state 14
        bpt.get(14).put(new Word(WordType.T, ")"), "r4");
        bpt.get(14).put(new Word(WordType.T, "||"), "r4");
        //state 15
        bpt.get(15).put(new Word(WordType.T, "("), "s8");
        bpt.get(15).put(new Word(WordType.T, "num"), "s7");
        bpt.get(15).put(new Word(WordType.T, "id"), "s6");
        bpt.get(15).put(new Word(WordType.V, "BF"), "g37");
        bpt.get(15).put(new Word(WordType.V, "E"), "g2");
        bpt.get(15).put(new Word(WordType.V, "ION"), "g1");
        //state 16
        bpt.get(16).put(new Word(WordType.T, "num"), "s7");
        bpt.get(16).put(new Word(WordType.T, "id"), "s6");
        bpt.get(16).put(new Word(WordType.V, "E"), "g36");
        bpt.get(16).put(new Word(WordType.V, "ION"), "g35");
        //state 17
        bpt.get(17).put(new Word(WordType.T, "num"), "s7");
        bpt.get(17).put(new Word(WordType.T, "id"), "s6");
        bpt.get(17).put(new Word(WordType.V, "E"), "g34");
        bpt.get(17).put(new Word(WordType.V, "ION"), "g35");
        //state 18
        bpt.get(18).put(new Word(WordType.T, "num"), "s7");
        bpt.get(18).put(new Word(WordType.T, "id"), "s6");
        bpt.get(18).put(new Word(WordType.V, "E"), "g33");
        bpt.get(18).put(new Word(WordType.V, "ION"), "g35");
        //state 19
        bpt.get(19).put(new Word(WordType.T, "num"), "s7");
        bpt.get(19).put(new Word(WordType.T, "id"), "s6");
        bpt.get(19).put(new Word(WordType.V, "E"), "g32");
        bpt.get(19).put(new Word(WordType.V, "ION"), "g35");
        //state 20
        bpt.get(20).put(new Word(WordType.T, "num"), "s7");
        bpt.get(20).put(new Word(WordType.T, "id"), "s6");
        bpt.get(20).put(new Word(WordType.V, "ION"), "g31");
        //state 21
        bpt.get(21).put(new Word(WordType.T, "num"), "s7");
        bpt.get(21).put(new Word(WordType.T, "id"), "s6");
        bpt.get(21).put(new Word(WordType.V, "ION"), "g30");
        //state 22
        bpt.get(22).put(new Word(WordType.T, "num"), "s7");
        bpt.get(22).put(new Word(WordType.T, "id"), "s6");
        bpt.get(22).put(new Word(WordType.V, "ION"), "g29");
        //state 23
        bpt.get(23).put(new Word(WordType.T, "num"), "s7");
        bpt.get(23).put(new Word(WordType.T, "id"), "s6");
        bpt.get(23).put(new Word(WordType.V, "ION"), "g28");
        //state 24
        bpt.get(24).put(new Word(WordType.T, "num"), "s7");
        bpt.get(24).put(new Word(WordType.T, "id"), "s6");
        bpt.get(24).put(new Word(WordType.V, "ION"), "g27");
        //state 25
        bpt.get(25).put(new Word(WordType.T, "num"), "s7");
        bpt.get(25).put(new Word(WordType.T, "id"), "s6");
        bpt.get(25).put(new Word(WordType.V, "ION"), "g26");
        //state 26
        bpt.get(26).put(new Word(WordType.T, ")"), "r18");
        bpt.get(26).put(new Word(WordType.T, "&&"), "r18");
        bpt.get(26).put(new Word(WordType.T, "||"), "r18");
        //state 27
        bpt.get(27).put(new Word(WordType.T, ")"), "r17");
        bpt.get(27).put(new Word(WordType.T, "&&"), "r17");
        bpt.get(27).put(new Word(WordType.T, "||"), "r17");
        //state 28
        bpt.get(28).put(new Word(WordType.T, ")"), "r16");
        bpt.get(28).put(new Word(WordType.T, "&&"), "r16");
        bpt.get(28).put(new Word(WordType.T, "||"), "r16");
        //state 29
        bpt.get(29).put(new Word(WordType.T, ")"), "r15");
        bpt.get(29).put(new Word(WordType.T, "&&"), "r15");
        bpt.get(29).put(new Word(WordType.T, "||"), "r15");
        //state 30
        bpt.get(30).put(new Word(WordType.T, ")"), "r14");
        bpt.get(30).put(new Word(WordType.T, "&&"), "r14");
        bpt.get(30).put(new Word(WordType.T, "||"), "r14");
        //state 31
        bpt.get(31).put(new Word(WordType.T, ")"), "r13");
        bpt.get(31).put(new Word(WordType.T, "&&"), "r13");
        bpt.get(31).put(new Word(WordType.T, "||"), "r13");
        //state 32
        bpt.get(32).put(new Word(WordType.T, ")"), "r22");
        bpt.get(32).put(new Word(WordType.T, "/"), "r22"); //TODO: conflict
        bpt.get(32).put(new Word(WordType.T, "*"), "r22"); //TODO: conflict
        bpt.get(32).put(new Word(WordType.T, "-"), "r22"); //TODO: conflict
        bpt.get(32).put(new Word(WordType.T, "+"), "r22"); //TODO: conflict
        bpt.get(32).put(new Word(WordType.T, "&&"), "r22");
        bpt.get(32).put(new Word(WordType.T, "||"), "r22");
        //state 33
        bpt.get(33).put(new Word(WordType.T, ")"), "r21");
        bpt.get(33).put(new Word(WordType.T, "/"), "r21"); //TODO: conflict
        bpt.get(33).put(new Word(WordType.T, "*"), "r21"); //TODO: conflict
        bpt.get(33).put(new Word(WordType.T, "-"), "r21"); //TODO: conflict
        bpt.get(33).put(new Word(WordType.T, "+"), "r21"); //TODO: conflict
        bpt.get(33).put(new Word(WordType.T, "&&"), "r21");
        bpt.get(33).put(new Word(WordType.T, "||"), "r21");
        //state 34
        bpt.get(34).put(new Word(WordType.T, ")"), "r20");
        bpt.get(34).put(new Word(WordType.T, "/"), "s19"); //TODO: conflict
        bpt.get(34).put(new Word(WordType.T, "*"), "s18"); //TODO: conflict
        bpt.get(34).put(new Word(WordType.T, "-"), "r20"); //TODO: conflict
        bpt.get(34).put(new Word(WordType.T, "+"), "r20"); //TODO: conflict
        bpt.get(34).put(new Word(WordType.T, "&&"), "r20");
        bpt.get(34).put(new Word(WordType.T, "||"), "r20");
        //state 35
        bpt.get(35).put(new Word(WordType.T, "/") , "r23");
        bpt.get(35).put(new Word(WordType.T, "*") , "r23");
        bpt.get(35).put(new Word(WordType.T, "-") , "r23");
        bpt.get(35).put(new Word(WordType.T, "+") , "r23");
        //state 36
        bpt.get(36).put(new Word(WordType.T, ")") , "r19");
        bpt.get(36).put(new Word(WordType.T, "/") , "s19"); //TODO: conflict
        bpt.get(36).put(new Word(WordType.T, "*") , "s18"); //TODO: conflict
        bpt.get(36).put(new Word(WordType.T, "-") , "r19"); //TODO: conflict
        bpt.get(36).put(new Word(WordType.T, "+") , "r19"); //TODO: conflict
        bpt.get(36).put(new Word(WordType.T, "&&"), "r19");
        bpt.get(36).put(new Word(WordType.T, "||"), "r19");
        //state 37
        bpt.get(37).put(new Word(WordType.T, ")"), "r6");
        bpt.get(37).put(new Word(WordType.T, "&&"), "r6");
        bpt.get(37).put(new Word(WordType.T, "||"), "r6");
        //state 38
        bpt.get(38).put(new Word(WordType.T, ")"), "r5");
        bpt.get(38).put(new Word(WordType.T, "||"), "r5");
        //state 39
        bpt.get(39).put(new Word(WordType.T, ")"), "r2");
        bpt.get(39).put(new Word(WordType.T, "||"), "r2");
        //state 40
        bpt.get(40).put(new Word(WordType.T, ")"), "r1");
        //state 41
        bpt.get(41).put(new Word(WordType.T, ")") , "r12");
        bpt.get(41).put(new Word(WordType.T, "&&"), "r12");
        bpt.get(41).put(new Word(WordType.T, "||"), "r12");
    }

    private static void showBPT() {
        System.out.println("bottomUp parse table\n");
        System.out.printf("          ");
        List<Word> words = Arrays.asList(
                new Word(WordType.T, ")"), new Word(WordType.T, "("),
                new Word(WordType.T, "id"), new Word(WordType.T, "num"),
                new Word(WordType.T, "/"),
                new Word(WordType.T, "*"), new Word(WordType.T, "-"),
                new Word(WordType.T, "+"), new Word(WordType.T, "!="),
                new Word(WordType.T, "=="), new Word(WordType.T, ">="),
                new Word(WordType.T, ">"), new Word(WordType.T, "<="),
                new Word(WordType.T, "<"), new Word(WordType.T, "&&"),
                new Word(WordType.T, "||"), new Word(WordType.V, "BE"),
                new Word(WordType.V, "BE'"), new Word(WordType.V, "ORR"),
                new Word(WordType.V, "BT"), new Word(WordType.V, "BT'"),
                new Word(WordType.V, "ANDD"), new Word(WordType.V, "BF"),
                new Word(WordType.V, "E"), new Word(WordType.V, "ION"));
        for (Word terminal : words) {
            System.out.printf("%10s", terminal);
        }
        System.out.println();
        for (Integer word : bpt.keySet()) {
            System.out.printf("%10s", word);
            for (Word word1 : bpt.get(word).keySet()) {
                System.out.printf("%10s", bpt.get(word).get(word1));
            }
            System.out.println();
        }
    }

    private static void PTGenerator() {

        for (Word variable : variables) {

            HashMap<Word, LinkedList<Integer>> tmp = new LinkedHashMap<>();
            for (Word terminal : terminals) {
                if (terminal.value.equals("#")) {
                    continue;
                }
                tmp.put(terminal, new LinkedList<>());
            }
            pt.put(variable, tmp);
        }
    }

    private static void setPredicts() {
        for (int i = 0; i < RHST.size(); i++) {
            Word var = RHST.get(i).getFirst();
            for (int j = 1; j < RHST.get(i).size(); j++) {
                if (RHST.get(i).get(j).type == WordType.T && !RHST.get(i).get(j).value.equals("#")) {
                    pt.get(var).get(RHST.get(i).get(j)).addLast(i);
                    break;
                } else if (!isNullable(RHST.get(i).get(j))) {
                    for (Word word : firstsByVariable.get(RHST.get(i).get(j))) {
                        pt.get(var).get(word).addLast(i);
                    }
                    break;
                } else if (RHST.get(i).get(j).value.equals("#")) {
                    for (Word word : follows.get(var)) {
                        pt.get(var).get(word).addLast(i);
                    }
                } else {
                    for (Word word : firstsByVariable.get(RHST.get(i).get(j))) {
                        pt.get(var).get(word).addLast(i);
                    }
                    if (j == RHST.get(i).size() - 1) {
                        for (Word word : follows.get(var)) {
                            pt.get(var).get(word).addLast(i);
                        }
                        continue;
                    }
                    continue;
                }

            }
        }
    }

    // initialize RHST semantic table
    private static void RHSTGenerator() throws FileNotFoundException {
        //grammer file
        File file = new File("/Users/apple/IdeaProjects/compilerProject/src/Grammar.txt");
//        File file = new File("C:/Users/ASUS/IdeaProjects/CompilerG/src/Grammar.txt");

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            RHST.add(new LinkedList<Word>());
            semanticTable.add(new LinkedList<Word>());
            String line = scanner.nextLine();
            String[] words = line.split("( : )|( :)|(: )|( )|(:)");
            for (int i = 0; i < words.length; i++) {
                //if a word is a semantic rule
                if (words[i].charAt(0) == '@') {
                    Word tmp = new Word(WordType.semanticRule, words[i]);
                    semanticTable.getLast().add(tmp);
                    continue;
                }
                //if a word is a Variable
                if (words[i].charAt(0) > 64 && words[i].charAt(0) < 91) {
                    Word tmp = new Word(WordType.V, words[i]);
                    RHST.getLast().add(tmp);
                    semanticTable.getLast().add(tmp);
                    if (i == 0) {
                        variables.add(tmp);
                    }
                } else {
                    Word tmp = new Word(WordType.T, words[i]);
                    RHST.getLast().add(tmp);
                    semanticTable.getLast().add(tmp);
                    terminals.add(tmp);
                }
            }
        }
        terminals.add(new Word(WordType.T, "$"));
        scanner.close();
    }

    private static boolean isNullable(Word word) {
        if (word.type == WordType.T && word.value.equals("#")) {
            return true;
        } else if (word.type == WordType.T) {
            return false;
        }
        //if the word is a Variable
        for (LinkedList<Word> aRHST : RHST) {
            if (aRHST.getFirst().equals(word)) {
                for (int j = 1; j < aRHST.size(); j++) {
                    if (!isNullable(aRHST.get(j))) {
                        break;
                    }
                    if (j == aRHST.size() - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // the last parameter is the variable word of a rule that we want to find it's first set
    //take just one line of grammer as a rule and compute first of it
    //در بار اول فراخوانی اولین Word در سمت راست یک قاعده را به این تابع می دهیم که می تواند ترمینال یا ناپایانه باشد
    private static void findFirsts(HashMap<Integer, LinkedList<Word>> firsts, Integer ruleNumber, Word word) {
        //اگر word یک ترمینال غیر ε باشد
        if (word.type == WordType.T && !word.value.equals("#")) {
            firsts.computeIfAbsent(ruleNumber, k -> new LinkedList<Word>());
            if (!firsts.get(ruleNumber).contains(word)) {
                firsts.get(ruleNumber).add(word);
            }
        }
        //اگر word پایانه ی ε باشد
        //در صورت ε بودن هیچ اتفاقی نمی افتد و هیچ سطری به firsts اضافه نمی شود
        if (isNullable(word)) {
            int index = RHST.get(ruleNumber).indexOf(word);
            if (word.value.equals("#")) {
                return;
            }
        }
        //اگر word یک ناپایانه (چه میرا شونده و چه غیر میرا شونده) باشد

        if (word.type == WordType.V) {
            for (LinkedList<Word> aRHST : RHST) {
                if (aRHST.getFirst().equals(word)) {
                    int k = 1;
                    do {
                        findFirsts(firsts, ruleNumber, aRHST.get(k));
                        k++;
                    } while (k < aRHST.size() && isNullable(aRHST.get(k - 1)));
                }
            }
        }
    }

    private static void first() {
        for (int i = 0; i < RHST.size(); i++) {
            findFirsts(firsts, i, RHST.get(i).get(1));
        }
        //print first sets for each line
//        for (int i = 0; i < RHST.size(); i++) {
//            System.out.print((i + 1) + " : " + "{");
//            if (firsts.get(i) != null) {
//                for (int j = 0; j < firsts.get(i).size(); j++) {
//                    System.out.print(firsts.get(i).get(j).value);
//                    if (j != firsts.get(i).size() - 1)
//                        System.out.print(",");
//                }
//            }
//            System.out.println("}");
//        }
        //end of print code
    }

    //this method organize a mapping from all variables to each first set in firstsByVariable Map
    private static void findFirstsByVariables(LinkedHashMap<Word, LinkedList<Word>> firstsByVariable) {

        //فرض میکنیم که مجموعه firsts آماده شده است و قبلا تابع first صدا زده شده است
        for (int i = 0; i < RHST.size(); i++) {
            Word leftVar = RHST.get(i).getFirst();//متغیر سمت چپ هر خط
            if (firstsByVariable.containsKey(leftVar)) {
                continue;
            }
//            firstsByVariable.computeIfAbsent(leftVar, k -> new LinkedList<Word>());
            firstsByVariable.put(leftVar, new LinkedList<Word>());
            if (firsts.get(i) != null) {
                firstsByVariable.get(leftVar).addAll(firsts.get(i));
            }
            for (int j = i + 1; j < RHST.size(); j++) {
                if (RHST.get(j).getFirst().equals(leftVar)) {
                    if (firsts.get(j) != null) {
                        for (int i1 = 0; i1 < firsts.get(j).size(); i1++) {
                            if (!firstsByVariable.get(leftVar).contains(firsts.get(j).get(i1))) {
                                firstsByVariable.get(leftVar).add(firsts.get(j).get(i1));
                            }
                        }
                    }
                }
            }

        }


//        print
        for (Word word : firstsByVariable.keySet()) {
            System.out.print((word.value) + " : " + "{");
            if (!firstsByVariable.get(word).isEmpty()) {
                for (int i = 0; i < firstsByVariable.get(word).size(); i++) {
                    System.out.print(firstsByVariable.get(word).get(i).value);
                    if (i != firstsByVariable.get(word).size() - 1)
                        System.out.print(",");
                }
            }
            System.out.println("}");
        }


    }

    private static void follow() {
        for (Word word : firstsByVariable.keySet()) {
            follows.put(word, new LinkedHashSet<Word>());
        }
        for (Word variable : variables) {
            findFollow(variable);
        }
    }

    private static void findFollow(Word key) {
        follows.computeIfAbsent(key, k -> new LinkedHashSet<Word>());
        for (LinkedList<Word> aRHST : RHST) {
            for (int j = 1; j < aRHST.size(); j++) {
                if (aRHST.get(j).equals(key)) {
                    //متغیر key پیدا شده آخرین word در یک قاعده نباشد و word بعد از آن میرا نباشد
                    if (j < aRHST.size() - 1) {
                        if (!isNullable(aRHST.get(j + 1))) {
                            if (aRHST.get(j + 1).type == WordType.T) {
                                follows.get(key).add(aRHST.get(j + 1));
                            } else if (!aRHST.get(j + 1).equals(key)) {
                                follows.get(key).addAll(firstsByVariable.get(aRHST.get(j + 1)));
                            }
                            break;
                        }
                    }

                    Word after = null;
                    if (j < aRHST.size() - 1) {
                        after = aRHST.get(j + 1);
                    }
                    //متغیر key پیدا شده آخرین word در یک قاعده نباشد ولی متغیر بعد از آن میرا باشد
                    if (after != null && isNullable(after)) {
                        do {
                            if (after.type == WordType.T) {
                                follows.get(key).add(after);
                            } else {
                                follows.get(key).addAll(firstsByVariable.get(after));
                            }
                            if (j < aRHST.size() - 1) {
                                j++;
                                after = aRHST.get(j);
                            } else {
                                if (follows.get(aRHST.getFirst()).isEmpty()) {
                                    if (aRHST.getFirst().equals(key)) {
                                        break;
                                    }
                                    findFollow(aRHST.getFirst());
                                }
                                follows.get(key).addAll(follows.get(aRHST.getFirst()));
                                break;
                            }
                        } while (isNullable(after));
                        if (after.type == WordType.T) {
                            follows.get(key).add(after);
                        } else {
                            follows.get(key).addAll(firstsByVariable.get(after));
                        }
                        break;
                    }

                    //متغیر key آخرین Word پیدا شده در یک قاعده باشد
                    if (after == null) {
                        if (follows.get(aRHST.getFirst()).isEmpty()) {
                            if (!aRHST.getFirst().equals(key)) {
                                findFollow(aRHST.getFirst());
                            }
                        }
                        follows.get(key).addAll(follows.get(aRHST.getFirst()));
                    }
                }
            }
        }
        //add $ to follow set of the first variable
        if (key.value.equals("S")) {
            follows.get(key).add(new Word(WordType.T, "$"));
        }
    }

    private static void showFollowSets() {
        for (Word word : follows.keySet()) {
            System.out.print((word.value) + " : " + " ");
            for (Word word1 : follows.get(word)) {
                System.out.print(word1.value + ",");
            }
            System.out.println();
        }
    }

    private static void showPT() {
        System.out.println("parse table\n");
        System.out.printf("          ");
        for (Word terminal : terminals) {
            if (terminal.value.equals("#")) {
                continue;
            }
            System.out.printf("%10s", terminal);
        }
        System.out.println();
        for (Word word : pt.keySet()) {
            System.out.printf("%10s", word);
            for (Word word1 : pt.get(word).keySet()) {
                System.out.printf("%10s", pt.get(word).get(word1));
            }
            System.out.println();
        }
    }

    static void init() throws FileNotFoundException {
        RHSTGenerator();
        first();
        findFirstsByVariables(firstsByVariable);
        follow();
        PTGenerator();
        setPredicts();
        LRInitialize();
        bptInitialize();
    }

    public static void main(String[] args) throws FileNotFoundException {
        RHSTGenerator();
        System.out.printf("");
        first();
        findFirstsByVariables(firstsByVariable);
        follow();
        System.out.println("follows");
        showFollowSets();
        PTGenerator();
        setPredicts();
        showPT();
        LRInitialize();
        bptInitialize();
        System.out.println();
        showBPT();
    }
}
