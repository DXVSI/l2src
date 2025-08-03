package org.mmocore.commons.compiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Class for compiling external Java files<br>
 *
 * @author G1ta0
 */
public class Compiler {
    private static final Logger _log = LoggerFactory.getLogger(Compiler.class);
    private static final JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    private final DiagnosticListener<JavaFileObject> listener = new DefaultDiagnosticListener();
    private final StandardJavaFileManager fileManager;
    private final MemoryClassLoader memClassLoader = new MemoryClassLoader();
    private final MemoryJavaFileManager memFileManager;
    
    public Compiler() {
        fileManager = javac.getStandardFileManager(listener, Locale.getDefault(), Charset.defaultCharset());
        memFileManager = new MemoryJavaFileManager(fileManager, memClassLoader);
    }

    public boolean compile(File... files) {
        // javac options - simplified configuration for Java 21
        List<String> options = new ArrayList<>();
        options.add("-cp");
        options.add(System.getProperty("java.class.path"));
        options.add("-source");
        options.add("21");
        options.add("-target");
        options.add("21");
        options.add("-g");
        options.add("-Xlint:-options"); // Disable warnings about deprecated options
        
        //_log.info("Компилируем {} файлов с classpath: {}", files.length, System.getProperty("java.class.path"));

        _log.info("Compiling " + files.length + " files");

        Writer writer = new StringWriter();
        JavaCompiler.CompilationTask compile = javac.getTask(writer, memFileManager, listener, options, null, fileManager.getJavaFileObjects(files));

        boolean result = compile.call();
        if (!result) {
            _log.error("Compilation error. Compiler output: {}", writer.toString());
        }
        return result;
    }

    public boolean compile(Collection<File> files) {
        return compile(files.toArray(new File[files.size()]));
    }

    public MemoryClassLoader getClassLoader() {
        return memClassLoader;
    }

    private class DefaultDiagnosticListener implements DiagnosticListener<JavaFileObject> {
        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            Diagnostic.Kind kind = diagnostic.getKind();
            if (kind != Diagnostic.Kind.ERROR)
                _log.debug(diagnostic.getSource().getName() + (diagnostic.getPosition() == Diagnostic.NOPOS ? "" : ":" + diagnostic.getLineNumber() + "," + diagnostic.getColumnNumber()) + ": "
                        + diagnostic.getMessage(Locale.getDefault()));
            else
                _log.error(diagnostic.getSource().getName() + (diagnostic.getPosition() == Diagnostic.NOPOS ? "" : ":" + diagnostic.getLineNumber() + "," + diagnostic.getColumnNumber()) + ": "
                        + diagnostic.getMessage(Locale.getDefault()));
        }
    }
}