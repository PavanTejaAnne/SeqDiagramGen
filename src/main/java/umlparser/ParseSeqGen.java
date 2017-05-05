package umlparser;

import java.io.*;
import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

import net.sourceforge.plantuml.SourceStringReader;

public class ParseSeqGen {
    String parser;
    final String inPath;
    final String outPath;
    final String inFuncName;
    final String inClassName;

    HashMap<String, String> classMap;
    ArrayList<CompilationUnit> array;
    HashMap<String, ArrayList<MethodCallExpr>> callsMap;

    ParseSeqGen(String inPath, String inClassName, String inFuncName,
            String outFile) {
        this.inPath = inPath;
        this.outPath = inPath + "\\" + outFile + ".png";
        this.inClassName = inClassName;
        this.inFuncName = inFuncName;
        classMap = new HashMap<String, String>();
        callsMap = new HashMap<String, ArrayList<MethodCallExpr>>();
        parser = "@startuml\n";
    }

    public void start() throws Exception {
        array = getarray(inPath);
        buildMaps();
        parser += "Actor\n";
        parser += "User" + " -> " + inClassName + " : " + inFuncName + "\n";
        parser += "activate " + classMap.get(inFuncName) + "\n";
        parse(inFuncName);
        parser += "@enduml";
        generateDiagram(parser);
        System.out.println("Plant UML Code:\n" + parser);
    }

    private void parse(String callerFunc) {

        for (MethodCallExpr mce : callsMap.get(callerFunc)) {
            String callerClass = classMap.get(callerFunc);
            String recFunc = mce.getName();
            String recClass = classMap.get(recFunc);
            if (classMap.containsKey(recFunc)) {
                parser += callerClass + " -> " + recClass + " : "
                        + mce.toStringWithoutComments() + "\n";
                parser += "activate " + recClass + "\n";
                parse(recFunc);
                parser += recClass + " -->> " + callerClass + "\n";
                parser += "deactivate " + recClass + "\n";
            }
        }
    }

    private void buildMaps() {
        for (CompilationUnit cu : array) {
            String className = "";
            List<TypeDeclaration> td = cu.getTypes();
            for (Node n : td) {
                ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) n;
                className = coi.getName();
                for (BodyDeclaration bd : ((TypeDeclaration) coi)
                        .getMembers()) {
                    if (bd instanceof MethodDeclaration) {
                        MethodDeclaration md = (MethodDeclaration) bd;
                        ArrayList<MethodCallExpr> amc = new ArrayList<MethodCallExpr>();
                        for (Object bs : md.getChildrenNodes()) {
                            if (bs instanceof BlockStmt) {
                                for (Object es : ((Node) bs)
                                        .getChildrenNodes()) {
                                    if (es instanceof ExpressionStmt) {
                                        if (((ExpressionStmt) (es))
                                                .getExpression() instanceof MethodCallExpr) {
                                            amc.add(
                                                    (MethodCallExpr) (((ExpressionStmt) (es))
                                                            .getExpression()));
                                        }
                                    }
                                }
                            }
                        }
                        callsMap.put(md.getName(), amc);
                        classMap.put(md.getName(), className);
                    }
                }
            }
        }
    }

    private ArrayList<CompilationUnit> getarray(String inPath)
            throws Exception {
        File folder = new File(inPath);
        ArrayList<CompilationUnit> array = new ArrayList<CompilationUnit>();
        for (final File f : folder.listFiles()) {
            if (f.isFile() && f.getName().endsWith(".java")) {
                FileInputStream in = new FileInputStream(f);
                CompilationUnit cu;
                try {
                    cu = JavaParser.parse(in);
                    array.add(cu);
                } finally {
                    in.close();
                }
            }
        }
        return array;
    }

    private String generateDiagram(String source) throws IOException {

        OutputStream png = new FileOutputStream(outPath);
        SourceStringReader reader = new SourceStringReader(source);
        String desc = reader.generateImage(png);
        return desc;

    }

}
