package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Gramatica
{
	//guardar identificadores
	Declaracion_clase("(class)"), 
	Simbolos_especiales("(\\(|\\)|\\{|\\}|\\[|\\]|;)"),
	Simbolos_de_evaluacion("(<=|>=|<|>|==|!=)"),
	Operadores_aritmeticos("(\\+|-|/|\\*)"),
	Asignacion("(=)"),
	While("(while)"),
	If("(if)"),
	Especificador("(boolean|int)"),
	Modificador("(public|private)"),
	Entero_literal("([1-9][1-9]?)|^99 "), 
	Booleano_literal("(true|false)"),
	//Identificador("\\w+"),
	Identificador("([a-z]+([1-9])*)");

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