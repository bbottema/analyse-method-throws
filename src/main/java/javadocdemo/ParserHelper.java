package javadocdemo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class ParserHelper {
	
	public static CompilationUnit parseJava(String path, ParserConfiguration.LanguageLevel languageLevel) throws IOException {
		ParseResult<CompilationUnit> parseResult = getJavaParser(languageLevel).parse(Paths.get(path));
		if (!parseResult.isSuccessful()) {
			throw new RuntimeException("Parsing java code fail with the following problems:\n\t" + parseResult.getProblems());
		}
		return parseResult.getResult().orElseThrow(AssertionError::new);
	}
	
	public static JavaParser getJavaParser(ParserConfiguration.LanguageLevel languageLevel) {
		ParserConfiguration parseConfig = new ParserConfiguration();
		parseConfig.setCharacterEncoding(UTF_8);
		parseConfig.setTabSize(4);
		parseConfig.setLanguageLevel(languageLevel);
		return new JavaParser(parseConfig);
	}
}
