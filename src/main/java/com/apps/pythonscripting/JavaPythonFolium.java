package com.apps.pythonscripting;

import org.python.core.Options;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

// jython has only pure python stuff no libs numpy, matplotlib, folium.

public class JavaPythonFolium {

    public static void main(String[] args) throws Exception {
        Options.importSite = false;
        ScriptEngineManager manager = new ScriptEngineManager();
        for(ScriptEngineFactory factory : manager.getEngineFactories()){
            for(String name : factory.getNames()){
                System.out.println(name);
            }
        }
        ScriptEngine engine = manager.getEngineByName("python");

        engine.eval("def factorial(num):\n" +
                "    if num == 0:\n" +
                "        return 1\n" +
                "\n" +
                "    return num * factorial(num-1)\n" +
                "\n" +
                "print('Enter int:')\n" +
                "\n" +
                "num = int(input())\n" +
                "print(factorial(num))");
    }
}

