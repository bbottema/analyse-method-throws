import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ThrowStmt;
import javadocdemo.ThrowsAnalyser;
import javadocdemo.ThrowsAnalysesResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static com.github.javaparser.ParserConfiguration.LanguageLevel.JAVA_8;
import static javadocdemo.ParserHelper.parseJava;

class Main {
	public static void main(String[] args) throws IOException {
		final CompilationUnit compilationUnit = parseJava("src/main/resources/test.java", JAVA_8);
		
		compilationUnit
				.findAll(ClassOrInterfaceDeclaration.class).parallelStream()
				.filter(c -> !c.isInterface())
				.flatMap(c -> c.getMethods().parallelStream())
				.filter(Main::methodContainsThrownExceptions)
				.map(ThrowsAnalyser::analyseThrows)
				.filter(ThrowsAnalysesResult::documentationMismatch)
				.forEach(ThrowsAnalysesResult::printMismatch);
		
//		BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/test.java"));
//		writer.write(compilationUnit.toString());
//		writer.close();
	}
	
	private static Boolean methodContainsThrownExceptions(MethodDeclaration m) {
		return m.getBody().map(s -> s.findFirst(ThrowStmt.class).isPresent()).orElse(false);
	}
}