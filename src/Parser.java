import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser {
    private static int column = 0;
    private static int row = 1;
    private static int chcode;
    private static char ch;
    private static FileReader in;

    static {
        try {
            in = new FileReader("test.txt");
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] keywords = {
            "main",
            "void",
            "int",
            "float",
            "char",
            "string",
            "bool",
            "return",
            "break",
            "if",
            "do",
            "while",
            "for",
            "switch",
            "case",
            "default",
            "continue",
            "define",
            "include",
            "enum",
            "struct",
            "union",
            "false",
            "true",
            "typedef",
            "unsigned",
            "repeat",
            "until",
            "override",
            "else",
            "in",
            "sizeof",
            "null",
            "range",
            "double"
    };


    private static boolean isKeyWord(String s) {
        for (String keyword : keywords) {
            if (s.equals(keyword)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDigitOrLetter(Character ch) {
        return Character.isDigit(ch) || Character.isLetter(ch);
    }

    private static void read() throws IOException {
        chcode = in.read();
        ch = (char) chcode;
        if (chcode == 10) { //if you are on windows put 13 instead of 10
            row += 1;
            column = 1;
//            in.read(); //if you are on windows uncomment this line
            read();
        } else {
            column += 1;
        }
    }

    private static Token scanner() throws Exception {
        Token token = new Token();
        switch (ch) {
            case ' ':
                read();
                return scanner();
            case '+': {
                token.type = TokenType.SpecialToken;
                read();
                if (ch == '=') {
                    token.value = "+=";
                    read();
                    return token;
                } else if (ch == '+') {
                    token.value = "++";
                    read();
                    return token;
                }
                token.value = "+";
                return token;
            }

            case '-': {
                token.type = TokenType.SpecialToken;
                read();
                if (ch == '=') {
                    token.value = "-=";
                    read();
                    return token;
                } else if (ch == '-') {
                    token.value = "--";
                    read();
                    return token;
                } else if (ch == '>') {
                    token.value = "->";
                    read();
                    return token;
                }

                token.value = "-";
                return token;
            }

            case '*': {
                token.type = TokenType.SpecialToken;
                read();
                if (ch == '=') {
                    token.value = "*=";
                    read();
                    return token;
                } else if (ch == '*') {
                    token.value = "**";
                    read();
                    return token;
                }
                token.value = "*";
                return token;
            }

            case '/': {
                token.type = TokenType.SpecialToken;
                read();
                if (ch == '*') {
                    do {
                        do {
                            read();
                            if (chcode == -1) {
                                throw new Exception("incomplete comment, end of file occurs" + " row = " + row + " column = " + column);
                            }

                        } while (ch != '*');
                        while (ch == '*') {
                            read();
                            if (chcode == -1) {
                                throw new Exception("incomplete comment, end of file occurs" + " row = " + row + " column = " + column);
                            }
                        }
                    } while (ch != '/');
                    read();
                    return scanner();
                }
                if (ch == '/') {
                    token.value = "//";
                    read();
                    return token;
                }
                if (ch == '=') {
                    token.value = "/=";
                    read();
                    return token;
                }

                token.value = "/";
                return token;
            }

            case '%': {
                token.type = TokenType.SpecialToken;
                read();
                if (ch == '=') {
                    token.value = "%=";
                    read();
                    return token;
                }
                token.value = "%";
                return token;
            }

            case '<': {
                token.type = TokenType.SpecialToken;
                read();

                if (ch == '<') {
                    token.value = "<<";
                    read();
                    if (chcode == -1) {
                        throw new Exception("end of file occurs" + " row = " + row + " column = " + column);
                    }

                    if (ch == '=') {
                        token.value = "<<=";
                        read();
                    }
                    return token;
                } else if (ch == '=') {
                    token.value = "<=";
                    read();
                    return token;
                }
                token.value = "<";
                return token;
            }
            case '>': {
                token.type = TokenType.SpecialToken;
                read();
                if (ch == '>') {
                    token.value = ">>";
                    read();
                    if (chcode == -1) {
                        throw new Exception("end of file occurs" + " row = " + row + " column = " + column);
                    }

                    if (ch == '=') {
                        token.value = ">>=";
                        read();
                    }
                    return token;
                } else if (ch == '=') {
                    token.value = ">=";
                    read();
                    return token;
                }
                token.value = ">";
                return token;

            }

            case '=': {
                token.type = TokenType.SpecialToken;
                read();

                if (ch == '=') {
                    token.value = "==";
                    read();
                    return token;
                }
                token.value = "=";
                return token;
            }

            case '!': {
                token.type = TokenType.SpecialToken;
                read();

                if (ch == '=') {
                    token.value = "!=";
                    read();
                    return token;
                }
                token.value = "!";
                return token;
            }

            case '&': {
                token.type = TokenType.SpecialToken;
                read();

                if (ch == '&') {
                    token.value = "&&";
                    read();
                    return token;
                } else if (ch == '=') {
                    token.value = "&=";
                    read();
                    return token;
                }
                token.value = "&";
                return token;
            }

            case '|': {
                token.type = TokenType.SpecialToken;
                read();

                if (ch == '|') {
                    token.value = "||";
                    read();
                    return token;
                }
                if (ch == '=') {
                    token.value = "|=";
                    read();
                    return token;
                }
                token.value = "|";
                return token;
            }

            case '^': {
                token.type = TokenType.SpecialToken;
                read();

                if (ch == '=') {
                    token.value = "^=";
                    read();
                    return token;
                }
                token.value = "^";
                return token;
            }

            case '~': {
                token.type = TokenType.SpecialToken;
                token.value = "~";
                read();
                return token;
            }

            case ':': {
                token.type = TokenType.SpecialToken;
                read();
                if (chcode == -1) {
                    throw new Exception("end of file occurs" + " row = " + row + " column = " + column);
                }

                if (ch == '=') {
                    token.value = ":=";
                    read();
                    return token;
                }
                token.value = ":";
                return token;
            }

            case ',':
                token.type = TokenType.SpecialToken;
                token.value = ",";
                read();
                return token;

            case '.': {
                token.type = TokenType.SpecialToken;
                token.value = ".";
                read();
                return token;
            }

            case '[': {
                token.type = TokenType.SpecialToken;
                token.value = "[";
                read();
                return token;
            }

            case ']': {
                token.type = TokenType.SpecialToken;
                token.value = "]";
                read();
                return token;
            }

            case '?': {
                token.type = TokenType.SpecialToken;
                token.value = "?";
                read();
                return token;
            }

            case '"': {
                read();
                if (chcode == -1) {
                    throw new Exception("end of file occurs" + " row = " + row + " column = " + column);
                }

                if (ch == '"') {
                    read();
                    if (ch == '"') {
                        int nowRow = row;
                        do {
                            read();
                            if (chcode == -1) {
                                throw new Exception("end of file occures" + " row = " + row + " column = " + column);
                            }
                            if (row - nowRow != 0) {
                                throw new Exception("illegal next line in single line comment" + " row = " + row + " column = " + column);
                            }
                        } while (ch != '"');
                        read();
                        if (ch != '"') {
                            throw new Exception("unfinished comment" + " row = " + row + " column = " + column);
                        }
                        read();
                        if (ch != '"') {
                            throw new Exception("unfinished comment" + " row = " + row + " column = " + column);
                        }
                        scanner();

                    } else {
                        token.type = TokenType.StringLiteral;
                        token.value = "";
                        return token;
                    }
                } else {
                    token.type = TokenType.StringLiteral;
                    token.value = "" + ch;
                    while (ch != '"') {
                        read();
                        if (chcode == -1) {
                            throw new Exception("end of file occurs" + " row = " + row + " column = " + column);
                        }

                        token.value += ch;
                        if (ch == '\\') {
                            read();
                            if (chcode == -1) {
                                throw new Exception("end of file occurs" + " row = " + row + " column = " + column);
                            }

                            token.value += ch;
                            ch = 7;
                        }
                    }
                    read();
                    token.value = token.value.substring(0, token.value.length() - 1);
                    return token;

                }
            }

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                token.value = "" + ch;
                do {
                    read();
                    if (chcode == -1) {
                        throw new Exception("end of file occurs" + " row = " + row + " column = " + column);
                    }

                    token.value += ch;
                }
                while (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9');
                if (ch == '.') {
                    token.type = TokenType.RealNum;
                    do {
                        read();
                        if (chcode == -1) {
                            throw new Exception("end of file occurs" + " row = " + row + " column = " + column);
                        }

                        token.value += ch;
                    }
                    while (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9');
                    token.value = token.value.substring(0, token.value.length() - 1);
                    return token;
                } else {
                    token.type = TokenType.IntegerNum;
                    token.value = token.value.substring(0, token.value.length() - 1);
                    return token;
                }
            }

            case 'a':
            case 'A':

            case 'b':
            case 'B':

            case 'c':
            case 'C':

            case 'd':
            case 'D':

            case 'e':
            case 'E':

            case 'f':
            case 'F':

            case 'g':
            case 'G':

            case 'h':
            case 'H':

            case 'i':
            case 'I':

            case 'j':
            case 'J':

            case 'k':
            case 'K':

            case 'l':
            case 'L':

            case 'm':
            case 'M':

            case 'n':
            case 'N':

            case 'o':
            case 'O':

            case 'p':
            case 'P':

            case 'q':
            case 'Q':

            case 'r':
            case 'R':

            case 's':
            case 'S':

            case 't':
            case 'T':

            case 'u':
            case 'U':

            case 'v':
            case 'V':

            case 'w':
            case 'W':

            case 'x':
            case 'X':

            case 'y':
            case 'Y':

            case 'z':
            case 'Z': {
                token.value = "" + ch;
                int nowRow = row;
                do {
                    read();

                    token.value += ch;
                } while (isDigitOrLetter(ch));
                token.value = token.value.substring(0, token.value.length() - 1);
                if (isKeyWord(token.value)) {
                    token.type = TokenType.Keyword;
                }
//                else if (token.value.length() == 1) {
//                    token.type = TokenType.Character;
//                }
                else {
                    token.type = TokenType.Identifier;
                }
                return token;
            }

            case ';': {
                token.type = TokenType.SpecialToken;
                token.value = ";";
                read();
                return token;
            }

            case '(': {
                token.type = TokenType.SpecialToken;
                token.value = "(";
                read();
                return token;
            }

            case ')': {
                token.type = TokenType.SpecialToken;
                token.value = ")";
                read();
                return token;
            }

            case '{': {
                token.type = TokenType.SpecialToken;
                token.value = "{";
                read();
                return token;
            }

            case '}': {
                token.type = TokenType.SpecialToken;
                token.value = "}";
                read();
                return token;
            }

            default: {
                if (chcode == -1) {
                    token.type = TokenType.eof;
                    token.value = "$";
                    return token;
                } else {
                    throw new Exception("\n\n(Error: " + token.value + " Expected a token here)\n");
                }

            }
        }

    }

    static {
        try {
            ParserInitialize.init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static Descriptor findSymbol(String name) {
        for (STEntry stEntry : symbolTable) {
            if (stEntry.name.equals(name)) {
                return stEntry.desc;
            }
        }
        return null;
    }

    private static Stack<Word> ps = new Stack<>();
    private static Token token;

    private static void parse() throws Exception {
        ps.push(new Word(WordType.T, "$"));
        ps.push(new Word(WordType.V, "S"));
        token = scanner();
        while (!ps.empty()) {
            switch (ps.peek().type) {
                case V: {
                    int prod = ParserInitialize.pt.get(ps.peek()).get(setType(token)).getFirst();
                    ps.pop();
                    for (int i = ParserInitialize.semanticTable.get(prod).size() - 1; i > 0; i--) {
                        if (ParserInitialize.semanticTable.get(prod).get(i).value.equals("#")) {
                            continue;
                        }
                        ps.push(ParserInitialize.semanticTable.get(prod).get(i));
                    }
                    break;
                }
                case T: {
                    if (setType(token).value.equals(ps.peek().value)) {
                        ps.pop();
                        token = scanner();
                    } else {
                        throw new Exception("\n     Error in parser method" + "\n     row = " + row + " column = " + column + "\n     token = (" + token + ")");
                    }
                    break;
                }
                case semanticRule: {
                    codeGenerator(ps.pop().value);
                    break;
                }
            }
        }
        System.out.println(ColoredPrint.ANSI_BG_GREEN+ColoredPrint.ANSI_BRIGHT_BG_BLACK+"parse done successfully");
        System.out.print(ColoredPrint.ANSI_RESET);
    }

    private static Stack<String> ss = new Stack<>();

    private static void codeGenerator(String action) throws Exception {
        switch (action) {
            case "@AND": //+
                Code code = new Code();
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                if (isNumber(code.op1) || isNumber(code.op2)){
                } else if (findSymbol(code.op1) == null || findSymbol(code.op2) == null) {
                    throw new Exception("variable not declared");
                } else if (!findSymbol(code.op2).type.equals(findSymbol(code.op1).type)) {
//                    throw new Exception("uncompatible types");
                }
                code.opCode = OpCode.and;
                code.res = getTemp(findSymbol(code.op1).type).name;
                ss.push(code.res);
                codes.add(code);
                break;
            case "@OR": //*
                code = new Code();
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                if (isNumber(code.op1) || isNumber(code.op2)){
                } else if (findSymbol(code.op1) == null || findSymbol(code.op2) == null) {
                    throw new Exception("variable not declared");
                } else if (!findSymbol(code.op2).type.equals(findSymbol(code.op1).type)) {
//                    throw new Exception("uncompatible types");
                }
                code.opCode = OpCode.or;
                code.res = getTemp(findSymbol(code.op1).type).name;
                ss.push(code.res);
                codes.add(code);
                break;
            case "@ADD":
                code = new Code();
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                if (isNumber(code.op1) || isNumber(code.op2)){
                } else if (findSymbol(code.op1) == null || findSymbol(code.op2) == null) {
                    throw new Exception("variable not declared");
                } else if (!findSymbol(code.op2).type.equals(findSymbol(code.op1).type)) {
                    throw new Exception("uncompatible types");
                }
                code.opCode = OpCode.add;
                code.res = getTemp(findSymbol(code.op1).type).name;
                ss.push(code.res);
                codes.add(code);
                break;
            case "@MULT":
                code = new Code();
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                if (isNumber(code.op1) || isNumber(code.op2)){
                } else if (findSymbol(code.op1) == null || findSymbol(code.op2) == null) {
                    throw new Exception("variable not declared");
                } else if (!findSymbol(code.op2).type.equals(findSymbol(code.op1).type)) {
                    throw new Exception("uncompatible types");
                }
                code.opCode = OpCode.mult;
                code.res = getTemp(findSymbol(code.op1).type).name;
                ss.push(code.res);
                codes.add(code);
                break;
            case "@SUB":
                code = new Code();
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                if (isNumber(code.op1) || isNumber(code.op2)){
                } else if (findSymbol(code.op1) == null || findSymbol(code.op2) == null) {
                    throw new Exception("variable not declared");
                } else if (!findSymbol(code.op2).type.equals(findSymbol(code.op1).type)) {
                    throw new Exception("uncompatible types");
                }
                code.opCode = OpCode.sub;
                code.res = getTemp(findSymbol(code.op1).type).name;
                ss.push(code.res);
                codes.add(code);
                break;
            case "@DIV":
                code = new Code();
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                if (isNumber(code.op1) || isNumber(code.op2)){
                } else if (findSymbol(code.op1) == null || findSymbol(code.op2) == null) {
                    throw new Exception("variable not declared");
                } else if (!findSymbol(code.op2).type.equals(findSymbol(code.op1).type)) {
                    throw new Exception("uncompatible types");
                }
                code.opCode = OpCode.div;
                code.res = getTemp(findSymbol(code.op1).type).name;
                ss.push(code.res);
                codes.add(code);
                break;
            case "@PP": //TODO : a++
                System.out.println("@PP " + ss.peek());
                code = new Code();
                code.opCode = OpCode.add;
                code.op1 = ss.peek();
                code.op2 = "#1";
                String tmp = getTemp("int").name;
                code.res = tmp;
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = tmp;
                code.op2 = ss.peek();
                codes.add(code);
                break;
            case "@MM":
                System.out.println("@MM " + ss.peek());
                code = new Code();
                code.opCode = OpCode.sub;
                code.op1 = ss.peek();
                code.op2 = "#1";
                tmp = getTemp("int").name;
                code.res = tmp;
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = tmp;
                code.op2 = ss.peek();
                codes.add(code);
                break;
            case "@PUSH":
                System.out.println("push " + token.value);
                ss.push(token.value);
                break;
            case "@SWITCHPARSER":
                bottomUp();
                break;
            case "@ASSIGN":
                code = new Code();
                System.out.println("@ASSIGN " + ss.peek());
                code.opCode = OpCode.mov;
                code.op1 = "#" + ss.pop();
                try {
                    code.res = ss.pop();
                } catch (EmptyStackException e) {
                    code.res = symbolTable.get(symbolTable.size() - 1).name;
                }
                codes.add(code);
                break;
            //case "@JL":
            case "@LT":
                tmp = getTemp("boolean").name;
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#1";
                code.res = tmp;
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.jl;
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                code.res = "#" + String.valueOf(codes.size() + 2);
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#0";
                code.res = tmp;
                codes.add(code);
                ss.push(tmp);
                break;
            //case "@JLE":
            case "@LE":
                tmp = getTemp("boolean").name;
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#0";
                code.res = tmp;
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.jle;
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                code.res = "#" + String.valueOf(codes.size() + 2);
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#1";
                code.res = tmp;
                codes.add(code);
                ss.push(tmp);
                break;
            //case "@JG":
            case "@GT":
                tmp = getTemp("boolean").name;
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#1";
                code.res = tmp;
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.jg;
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                code.res = "#" + String.valueOf(codes.size() + 2);
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#0";
                code.res = tmp;
                codes.add(code);
                ss.push(tmp);
                break;
            //case "@GE":
            case "@GE":
                tmp = getTemp("boolean").name;
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#1";
                code.res = tmp;
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.jge;
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                code.res = "#" + String.valueOf(codes.size() + 2);
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#0";
                code.res = tmp;
                codes.add(code);
                ss.push(tmp);
                break;
            //case "@JEQ":
            case "@EQ":
                tmp = getTemp("boolean").name;
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#1";
                code.res = tmp;
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.je;
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                code.res = "#" + String.valueOf(codes.size() + 2);
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#0";
                code.res = tmp;
                codes.add(code);
                ss.push(tmp);
                break;
            //case "@NEQ":
            case "@NE":
                tmp = getTemp("boolean").name;
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#1";
                code.res = tmp;
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.jne;
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                code.res = "#" + String.valueOf(codes.size() + 2);
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#0";
                code.res = tmp;
                codes.add(code);
                ss.push(tmp);
                break;
            case "@JZ":
                code = new Code();
                code.opCode = OpCode.jz;
                code.op1 = ss.pop();
                ss.push(String.valueOf(codes.size()));
                codes.add(code);
                break;
            case "@COMPLJZ":
                codes.get(Integer.valueOf(ss.peek())).op1 = codes.get(Integer.parseInt(ss.peek()) - 1).res;//TODO: sometimes have bug for 'if(a)'
                codes.get(Integer.valueOf(ss.pop())).res = "#" + String.valueOf(codes.size());
                break;
            case "@JMPCOMPLJZ":
                code = new Code();
                code.opCode = OpCode.jmp;
                codes.get(Integer.valueOf(ss.pop())).op2 = String.valueOf(codes.size() + 2);
                ss.push(String.valueOf(codes.size()));
                codes.add(code);
                break;
            case "@COMPLJMP":
                codes.get(Integer.valueOf(ss.pop())).op1 = String.valueOf(codes.size());
                break;
            case "@PUSHPC":
                System.out.println(String.valueOf("@PC = " + codes.size()));
                ss.push(String.valueOf(codes.size()));
                break;
            case "@JMPWCOMPLJZ":
                code = new Code();
                code.opCode = OpCode.jmp;
                codes.get(Integer.valueOf(ss.pop())).op2 = String.valueOf(codes.size() + 1);//TODO: problem
                code.op1 = ss.pop();
                codes.add(code);
                break;
            case "@MOV":
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = token.value;
                code.res = ss.peek();
                codes.add(code);
                break;
            case "@FOR": //TODO: check
                code = new Code();
                code.opCode = OpCode.sub;
                code.op1 = ss.pop();
                code.op2 = ss.peek();
                tmp = getTemp("int").name;
                code.res = tmp;
                ss.push(String.valueOf(codes.size()));
                codes.add(code);
                code = new Code();
                code.opCode = OpCode.jl;
                //TODO:  مشکوکه
                code.res = tmp;
                code.op1 = "#0";
                codes.add(code);
                break;
            case "@COMPLFOR":
                Code codeNxt = new Code();
                codeNxt.opCode = OpCode.jmp;
                codeNxt.op1 = ss.peek();
                codes.get(Integer.parseInt(ss.pop()) + 1).res = String.valueOf(codes.size() + 2); //TODO: good to go?
                code = new Code();
                code.opCode = OpCode.inc;
                code.op1 = ss.pop();
                codes.add(code);
                codes.add(codeNxt);
                break;
            case "@DW":
                code = new Code();
                code.opCode = OpCode.jnz;
                System.out.println("ss.peek() " + ss.peek());
                code.op1 = ss.pop(); //TODO:
                code.res = ss.pop();
                codes.add(code);
                break;
            case "@JMPL0":
                code = new Code();
                code.opCode = OpCode.jmp;
                codes.add(code);
                ss.push("#");
                break;
            case "@JMPOUT":
                code = new Code();
                code.opCode = OpCode.jmp;
                code.op1 = "LOUT";
                codes.add(code);
                break;
            case "@SWITCH":
                LinkedList<ArrayList<String>> first = null;
                LinkedList<ArrayList<String>> p = null;
                int count = 0, out;
                String label, value;
                String default_pc = ss.pop();
                while (!Objects.equals(ss.peek(), "#")) {
                    label = ss.pop();
                    value = ss.pop();
                    p = new LinkedList<>();
                    if (first != null) {
                        for (ArrayList<String> f : first) {
                            p.add(f); //TODO: is it ok?
                        }
                        first = p;
                    } else {
                        first = p;
                    }
                    ArrayList<String> arrayListTmp = new ArrayList<>();
                    arrayListTmp.add(label);
                    arrayListTmp.add(value);
                    p.add(arrayListTmp);
                    System.out.println(p);
                    count++;
                }
                ss.pop(); //should be '#', remove it to make sure we checked all cases
                out = codes.size() + count;
                String te = ss.pop();
                ss.push(String.valueOf(codes.size() - 1));
                if (p != null) {
                    for (ArrayList<String> arrList : p) {
                        code = new Code();
                        code.opCode = OpCode.je;
                        code.op1 = te;
                        code.res = arrList.get(0); //label
                        code.op2 = arrList.get(1); //value
                        if (Objects.equals(codes.get(Integer.parseInt(arrList.get(0)) - 1).op1, "LOUT")) {
                            codes.get(Integer.parseInt(arrList.get(0)) - 1).op1 = String.valueOf(out);
                        }
                        codes.add(code);
                    }
                    codes.get(Integer.parseInt(ss.peek())).op1 = String.valueOf(codes.size() + 1); //break after default
                    code = new Code();
                    code.opCode = OpCode.jmp;
                    code.op1 = default_pc; //TODO
                    codes.add(code);
                }
                if (first != null) {
                    codes.get(Integer.parseInt(first.getLast().get(0)) - 1).op1 = String.valueOf(codes.size() - count - 1);
                }
                break;
            case "@POP":
                ss.pop();
                break;
            case "CFDESC":
                String tm;
                FunctionDescriptor descr = new FunctionDescriptor(codes.size());
                while (!ss.peek().equals("#")) {
                    String s1;
                    s1 = ss.pop();
                    if (descr.parameters.containsKey(s1)) {
                        throw new Exception("vaiable name is already defined in this scope");
                    }
                    descr.parameters.put(s1, ss.pop());
                }
                ss.pop();//pop #
                symbolTable.add(new STEntry(ss.pop(), descr));
                descr.type = ss.pop();
                break;
            case "@CDESC":
                String name = ss.pop();
                String type;
                try {
                    type = ss.pop();
                } catch (EmptyStackException e) {
                    type = symbolTable.get(symbolTable.size() - 1).desc.type;
                    if (symbolTable.get(symbolTable.size() - 1).desc.type.equals("array")) {
                        type = ((ArrayDescriptor) symbolTable.get(symbolTable.size() - 1).desc).elementType;
                    }
                }
                if (findSymbol(name) != null) {
                    throw new Exception("\n\n   * redefine a variable *\n");
                }
                symbolTable.add(new STEntry(name, new Descriptor(type)));
                break;
            case "@PUSH#":
                ss.push("#");
                break;
            case "@ARRAYSETSIZEB"://for implicit declaration
                count = 0;
                while (!ss.pop().equals("#")) {count++;}
                ArrayDescriptor desc = (ArrayDescriptor) symbolTable.get(symbolTable.size() - 1).desc;
                desc.size = count;
                ADRS += getSize(desc.elementType) * count;
                break;
            case "@CADESC": {
                String array = ss.pop();
                try {
                    type = ss.pop();
                } catch (EmptyStackException e) {
                    type = symbolTable.get(symbolTable.size() - 1).desc.type;
                    if (symbolTable.get(symbolTable.size() - 1).desc.type.equals("array")) {
                        type = ((ArrayDescriptor) symbolTable.get(symbolTable.size() - 1).desc).elementType;
                    }
                }
                if (findSymbol(array) != null) {
                    throw new Exception("\n\n   * redefine a variable *\n");
                }
                if (token.value.equals("]")) {//for implicit declaration
                    ArrayDescriptor arrayDescriptor = new ArrayDescriptor();
                    arrayDescriptor.type = "array";
                    arrayDescriptor.elementType = type;
                    arrayDescriptor.adrs = ADRS;
                    STEntry entry = new STEntry(array, arrayDescriptor);
                    symbolTable.add(entry);
                } else {//for explicit declaration
                    int size = Integer.parseInt(token.value);
                    ArrayDescriptor arrayDescriptor = new ArrayDescriptor("array", type, ADRS, size);
                    ADRS += getSize(type) * size;
                    STEntry entry = new STEntry(array, arrayDescriptor);
                    symbolTable.add(entry);
                }
                break;
            }
//            case "@JMP": //TODO: declared in other @ cases
//                code = new Code();
//                code.opCode = OpCode.jmp;
//                code.op1 = ss.pop();
//                codes.add(code);
//                break;
            case "@NEG":
                code = new Code();
                code.opCode = OpCode.neg;
                code.op1 = ss.pop();
                code.res = getTemp(code.op1).name; //TODO : CHECK not r1 r2
                codes.add(code);
                break;
            case "@NOT":
                code = new Code();
                code.opCode = OpCode.neg;
                code.op1 = ss.pop();
                code.res = getTemp(code.op1).name; //TODO : CHECK not r1 r2
                codes.add(code);
                break;
            case "@XOR":
                code = new Code();
                code.opCode = OpCode.xor;
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                code.res = getTemp("int").name; //TODO : CHECK not r1 r2
                codes.add(code);
                break;
            case "@CMP": //TODO: which flag should get filled?
                code = new Code();
                code.opCode = OpCode.sub;
                code.op2 = ss.pop();
                code.op1 = ss.pop();
                tmp = getTemp("boolean").name;
                code = new Code();
                code.opCode = OpCode.mov;
                code.op1 = "#" + tmp;
                code.res = tmp;
                codes.add(code);
                break;
            case "@CALL":
                break;
            case "@RET":
                break;
        }
    }

    private static boolean isNumber(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private static void bottomUp() throws Exception {
        Stack<Integer> bps = new Stack<>();//bottomUp ps
        bps.push(0);
        while (true) {
            System.out.println(":: "+ParserInitialize.bpt.get(bps.peek()).get(setType(token)));
            char action = ParserInitialize.bpt.get(bps.peek()).get(setType(token)).charAt(0);
            int num;
            switch (action) {
                case 'a':
                    //accept
                    bps.clear();
                    return;
                case 's':
                    if (setType(token).value.equals("id") || setType(token).value.equals("num")) {
                        ss.push(token.value);
                    }
                    num = Integer.valueOf(ParserInitialize.bpt.get(bps.peek()).get(setType(token)).substring(1));
                    bps.push(num);
                    token = scanner();
                    break;
                case 'r':
                    num = Integer.valueOf(ParserInitialize.bpt.get(bps.peek()).get(setType(token)).substring(1));
                    for (int i = 0; i < ParserInitialize.LRtable.get(num).rhsl; i++) {
                        bps.pop();
                    }
                    bps.push(Integer.valueOf(ParserInitialize.bpt.get(bps.peek()).get(new Word(WordType.V, ParserInitialize.LRtable.get(num).lhs)).substring(1)));
                    if (!ParserInitialize.LRtable.get(num).semantic.isEmpty()) {
                        codeGenerator(ParserInitialize.LRtable.get(num).semantic);
                    }
                    break;
                default:
                    System.out.println("bottomUp(): PROBLEM !!!");
            }
        }
    }

    private static Word setType(Token token) {
        switch (token.type) {
            case Identifier:
                return new Word(WordType.T, "id");
            case SpecialToken:
                return new Word(WordType.T, token.value);
            case eof:
                return new Word(WordType.T, "$");
            case Keyword:
                return new Word(WordType.T, token.value);
            case RealNum:
                return new Word(WordType.T, "num");
            case IntegerNum:
                return new Word(WordType.T, "num");
        }
        return null;
    }

    private static ArrayList<Code> codes = new ArrayList<>();
    static int ADRS = 0;

    static int getSize(String type) {
        switch (type) {
            case "int":
            case "float":
                return 4;
            case "double":
                return 8;
        }
        return 0;
    }

    private static ArrayList<STEntry> symbolTable = new ArrayList<>();
    private static int lastIntTemp = 0;
    private static int lastDoubleTemp = 50;
    private static int lastBoolTemp = 100;

    static {
        for (int i = 0; i < 50; i++) {
            symbolTable.add(new STEntry("intTemp" + i, new Descriptor("int")));
        }
        for (int i = 0; i < 50; i++) {
            symbolTable.add(new STEntry("doubleTemp" + String.valueOf(i + 50), new Descriptor("double")));
        }
        for (int i = 0; i < 50; i++) {
            symbolTable.add(new STEntry("boolTemp" + String.valueOf(i + 100), new Descriptor("boolean")));
        }
    }

    private static STEntry getTemp(String type) {
        switch (type) {
            case "int":
                return symbolTable.get(lastIntTemp++);
            case "double":
                return symbolTable.get(lastDoubleTemp++);
            case "boolean":
                return symbolTable.get(lastBoolTemp++);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        parse();
        printST();
        printSymbolTable();
    }

    private static void printSymbolTable() {
        System.out.println(ColoredPrint.ANSI_BG_RED + ColoredPrint.ANSI_BLACK +"\nSymbol Table:");
        for (STEntry x: symbolTable) {
            if (x.desc instanceof ArrayDescriptor) {
                System.out.println(
                        ((ArrayDescriptor) x.desc).adrs + "  " +
                                ((ArrayDescriptor) x.desc).elementType + "  " +
                                x.name + "  " +
                                ((ArrayDescriptor) x.desc).type + "  " +
                                "(size:" + ((ArrayDescriptor) x.desc).size + ")"
                );
            } else if (!x.name.contains("Temp")){
                System.out.println(x.desc.adrs + "  " + x.desc.type + "  " + x.name);
            }
        }
    }

    private static void printST() {
        System.out.print(ColoredPrint.ANSI_RESET);
        System.out.println(ColoredPrint.ANSI_BG_WHITE+ColoredPrint.ANSI_BLACK+"Code Gen:");
        int i = 0;
        for (Code c : codes) {
            System.out.printf("%1s", i++);
            System.out.printf("%7s", c.opCode);
            if (c.op1 != null) System.out.printf("%12s", c.op1);
            else System.out.printf("%1s", "  ");
            if (c.op2 != null) System.out.printf("%12s", c.op2);
            else System.out.printf("%1s", "  ");
            if (c.res != null) System.out.printf("%12s", c.res);
            else System.out.printf("%1s", "  ");
            System.out.println();
        }
    }

    class ColoredPrint {
        public static final String ANSI_RESET  = "\u001B[0m";

        public static final String ANSI_BLACK  = "\u001B[30m";
        public static final String ANSI_RED    = "\u001B[31m";
        public static final String ANSI_GREEN  = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE   = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN   = "\u001B[36m";
        public static final String ANSI_WHITE  = "\u001B[37m";
        public static final String ANSI_BRIGHT_BLACK  = "\u001B[90m";
        public static final String ANSI_BRIGHT_RED    = "\u001B[91m";
        public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
        public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
        public static final String ANSI_BRIGHT_BLUE   = "\u001B[94m";
        public static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
        public static final String ANSI_BRIGHT_CYAN   = "\u001B[96m";
        public static final String ANSI_BRIGHT_WHITE  = "\u001B[97m";
        public static final String ANSI_BG_BLACK  = "\u001B[40m";
        public static final String ANSI_BG_RED    = "\u001B[41m";
        public static final String ANSI_BG_GREEN  = "\u001B[42m";
        public static final String ANSI_BG_YELLOW = "\u001B[43m";
        public static final String ANSI_BG_BLUE   = "\u001B[44m";
        public static final String ANSI_BG_PURPLE = "\u001B[45m";
        public static final String ANSI_BG_CYAN   = "\u001B[46m";
        public static final String ANSI_BG_WHITE  = "\u001B[47m";
        public static final String ANSI_BRIGHT_BG_BLACK  = "\u001B[100m";
        public static final String ANSI_BRIGHT_BG_RED    = "\u001B[101m";
        public static final String ANSI_BRIGHT_BG_GREEN  = "\u001B[102m";
        public static final String ANSI_BRIGHT_BG_YELLOW = "\u001B[103m";
        public static final String ANSI_BRIGHT_BG_BLUE   = "\u001B[104m";
        public static final String ANSI_BRIGHT_BG_PURPLE = "\u001B[105m";
        public static final String ANSI_BRIGHT_BG_CYAN   = "\u001B[106m";
        public static final String ANSI_BRIGHT_BG_WHITE  = "\u001B[107m";
    }
}
