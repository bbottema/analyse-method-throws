package javadocdemo;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.javadoc.JavadocBlockTag;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@UtilityClass
public class ThrowsAnalyser {
	
	public static ThrowsAnalysesResult analyseThrows(MethodDeclaration method) {
		final Set<String> exceptionsThrown = method.getBody().get().findAll(ThrowStmt.class).parallelStream()
				.map(stmt -> stmt.getExpression().asObjectCreationExpr().getType().toString())
				.collect(toSet());
		
		final Set<String> exceptionsDocumented = method.getJavadoc()
				.map(value -> value.getBlockTags().stream()
						.filter(t -> t.getType() == JavadocBlockTag.Type.THROWS)
						.map(tag -> tag.getContent().toText())
						.map(s -> s.replaceFirst("(?s)^(\\w*).*", "$1"))
						.collect(Collectors.toSet()))
				.orElseGet(HashSet::new);
		
		return new ThrowsAnalysesResult(method, exceptionsThrown, exceptionsDocumented);
	}
}
