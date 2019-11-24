package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Gramatica
{
	Declaracion_clase("(class)"), 
	Simbolos_especiales("(\\(|\\)|\\{|\\}|\\[|\\]|;)"),
	Simbolos_de_evaluacion("(<=|>=|<|>|==|!=)"),
	Asignacion("(=)"),
	While("(while)"),
	If("(if)"),
	Especificador("(boolean|int)"),
	Modificador("(public|private)"),
	Identificador("[a-z]+[1-9]*"),
	Entero_literal("[1-9]?[0-9]"), 
	Booleano_literal("(true|false)"),
	Operadores_aritmeticos("(\\+|-|/|\\*)");

	private final Pattern patron;

	Gramatica(String regex)
	{
		patron = Pattern.compile("^" + regex);
	}

	int final_coincidencias(String s)
	{
		Matcher m = patron.matcher(s);

		if (m.find())
		{
			return m.end();
		}
		return -1;
	}
}