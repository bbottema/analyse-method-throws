package javadocdemo;

import com.github.javaparser.Position;
import com.github.javaparser.ast.body.MethodDeclaration;
import lombok.Value;

import java.util.Set;

import static java.lang.String.format;

@Value
public class ThrowsAnalysesResult {
	private final MethodDeclaration method;
	private final Set<String> exceptionsThrown;
	private final Set<String> exceptionsDocumented;
	
	public boolean documentationMismatch() {
		return !this.exceptionsThrown.equals(this.exceptionsDocumented);
	}
	
	public void printMismatch() {
		final String methodLineInfo = method.getBegin().map(Position::toString).orElse("line unknown");
		System.out.println(format("@throws mismatch on Method [%s] @ %s:", method.getName(), methodLineInfo));
		System.out.println("\tDocumented: " + this.exceptionsDocumented);
		System.out.println("\tActually thrown: " + this.exceptionsThrown);
		System.out.println();
	}
}