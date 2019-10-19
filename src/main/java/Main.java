import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ThrowStmt;
import javadocdemo.ThrowsAnalyser;
import javadocdemo.ThrowsAnalysesResult;

import java.io.IOException;

import static com.github.javaparser.ParserConfiguration.LanguageLevel.JAVA_8;
import static java.lang.String.format;
import static javadocdemo.ParserHelper.parseJava;

class Main {
	public static void main(String[] args) throws IOException {
		parseJava("src/main/resources/test.java", JAVA_8)
				.findAll(ClassOrInterfaceDeclaration.class).parallelStream()
				.filter(c -> !c.isInterface())
				.flatMap(c -> c.getMethods().parallelStream())
				.filter(Main::methodContainsThrownExceptions)
				.map(ThrowsAnalyser::analyseThrows)
				.filter(ThrowsAnalysesResult::documentationMismatch)
				.forEach(ThrowsAnalysesResult::printMismatch);
	}
	
	private static Boolean methodContainsThrownExceptions(MethodDeclaration m) {
		return m.getBody().map(s -> s.findFirst(ThrowStmt.class).isPresent()).orElse(false);
	}
}