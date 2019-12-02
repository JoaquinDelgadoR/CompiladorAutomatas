package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser
{
	// Declaración de clase ::= <Modificador> "class" identificador "{" <declaración
	// de campo> <Enunciado> "}"
	// Delcaración de campo ::= declaración de variable ";"
	// Declaración de variable ::= <Modificador> (tipo (declarador|identificador))
	// Declarador ::= identificador "=" entero literal | booleano literal
	// Expresión ::= expresión de prueba
	// Expresión de prueba ::= (entero literal | identificador (operador) entero
	// literal | identificador
	// Enunciado ::= Declaración de variable | if | while
	// While ::= "while" "(" expresión ")" "{" <Enunciado> "}"
	// If ::= "if" "(" expresión ")" "{" expresión aritmetica <Enunciado> "}"
	// Tipo ::= especificador
	// Especificador ::= boolean | int
	// Modificador ::= public | private
	// Entero Literal ::= (("1..9" < "0..9" > )
	// Booleano literal ::= true | false
	// Identificador ::= "a..z""1..9"
	// Expresión Aritmética ::= Identificador "=" (entero literal (operador) entero
	// literal ";")

	ArrayList<String> componentes;

	String componente, auxDer, auxIzq;

	String salida = "";
	public static String salida2 = "";
	private int idx = 0, fila = 0;

	public Stack<Integer> pila;
	boolean error;

	public Parser()
	{
		componentes = new ArrayList<String>();

	}

	private void Acomodar(Gramatica token, String s)
	{
		if (getTipo(componente) == token && componente.equals(s))
		{
			Avanza();
		} else
		{
			error(getTipo(componente), s);
		}
	}

	private void Avanza()
	{
		salida2 += "Token obtenido:" + componente + "\n" + "Token Esperado: " + componente + "\n-------------------------------------------\n";
		if (idx < componentes.size() - 1)
		{
			idx++;
		}
		try
		{

			componente = componentes.get(idx);

		} catch (IndexOutOfBoundsException e)
		{
			idx--;

			//Componente caux = componentes.get(idx);
			//componente = new Componente(19, "", caux.getColumna(), caux.getFila());
			// error(tipo,s);
		}
	}

	private void aritmetic_expression()
	{

		String c;
		c = componente;

		identificador();

		Acomodar(Gramatica.Simbolos_especiales, "=");
		if (getTipo(componente) == Gramatica.Entero_literal)
		{
			integer_literal();
		} else if (getTipo(componente) == Gramatica.Identificador)
		{
			identificador();
		}

		c = componente;
		while (!c.equals(";"))
		{
			if (getTipo(c) == Gramatica.Operadores_aritmeticos)
			{
				Avanza();
			} else
			{
				error(Gramatica.Operadores_aritmeticos, "arit");
				System.out.println("Se esperaba un operador o un operando");
				break;
			}

			if (getTipo(componente) == Gramatica.Entero_literal)
			{
				integer_literal();
			} else if (getTipo(componente) ==Gramatica.Identificador)
			{
				identificador();
			}
			c = componente;
		}

		Acomodar(Gramatica.Simbolos_especiales, ";");
	}

	private void boolean_literal()
	{
		String c;
		Avanza();
	}

	private void declaracion_Clase()
	{
		String c = componente;
		if (!c.equals("class"))
		{
			modificador();
		}
		c = componente;

		Acomodar(Gramatica.Simbolos_especiales, "class");

		c = componente;

		identificador();
		c = componente;
		Acomodar(Gramatica.Simbolos_especiales, "{");

		// -----------------field_Declaration
		c = componente;
		// if(c.getTipo() == Componente.MOD || c.getTipo() == Componente.TIPO )

		field_Declaration();
		// -----------------statement
		statement();
		Acomodar(Gramatica.Simbolos_especiales, "}");
	}

	private void expression()
	{
		testing_expression();
	}

	private void error(Gramatica t, String to)
	{
		switch (t)
		{
			case Simbolos_especiales:
				switch (to)
				{
					case "{":
						salida += "Error Sintactico, Fila: " + componente + " se esperaba un \"" + to + "\"\t" + componente + "\n";
						break;
					case "}":
						salida += "Error Sintactico, Fila: " + componente + " se esperaba un \"" + to + "\"\t" + componente + "\n";
						break;
					case "(":
						salida += "Error Sintactico, Fila: " + componente + " se esperaba un \"" + to + "\"\t" + componente + "\n";
						break;
					case ")":
						salida += "Error Sintactico, Fila: " + componente + " se esperaba un \"" + to + "\"\t" + componente + "\n";
						break;
					case ";":
						salida += "Error Sintactico, Fila: " + componente + " se esperaba un \"" + to + "\"\t" + componente + "\n";
						break;
					case "blanck":
						salida += "";
						break;
					default:
						salida += "Error Sintactico, Fila: " + componente + " se esperaba un Simbolo especial\t" + componente + "\n";
						break;
				}
				break;
			case Operadores_aritmeticos:
				if (to.equals("arit"))
				{
					salida += "Error Sintactico, Fila: " + componente + " se esperaba un operador aritmetico\t" + componente + "\n";
				} else
				{
					break;
				}
			case Asignacion:
				salida += "Error Sintactico, Fila: " + componente + " se esperaba un \"int\" o \"boolean\"\t" + componente + "\n";
				break;
			case Modificador:
				salida += "Error Sintactico, Fila: " + componente + " se esperaba un \"public\" o \"private\"\t" + componente + "\n";
				break;
			case Entero_literal:
				salida += "Error Sintactico, Fila: " + componente + " se espeba un digito\t" + componente + "\n";
				break;
			case Booleano_literal:
				salida += "Error Sintactico, Fila: " + componente + " se esperaba \"true\" o \"false\"\t" + componente + "\n";
				break;
			case Identificador:
				salida += "Error Sintactico, Fila: " + componente + " se esperaba un identificador\t" + componente + "\n";
				break;
		}
		salida2 += "Token obtenido:" + componente + "\n" + "Token Esperado: " + to + "\n-------------------------------------------\n";

	}

	private void field_Declaration()
	{
		String c = componente;
		if (getTipo(c) == Gramatica.Modificador || getTipo(c) == Gramatica.Asignacion)
		{
			variable_declaration();
			c = componente;
			Acomodar(Gramatica.Simbolos_especiales, ";");
		}
	}

	public String getSalida()
	{
		return salida;
	}

	private void if_Statement()
	{
		String c;
		c = componente;
		Acomodar(Gramatica.Simbolos_especiales, "(");
		expression();
		Acomodar(Gramatica.Simbolos_especiales, ")");
		Acomodar(Gramatica.Simbolos_especiales, "{");
		aritmetic_expression();

		Acomodar(Gramatica.Simbolos_especiales, "}");

		statement();
	}

	private void integer_literal()
	{
		Acomodar(Gramatica.Entero_literal, componente);
	}

	private void identificador()
	{
		String c, caux, cauxa;
		String men = "";
		c = componente;

		men = componente;
		Acomodar(Gramatica.Identificador, c);
		men = componente;

	}

	private void modificador()
	{
		String c = null, caux = null;
		c = componente;
		if (getTipo(c) == Gramatica.Identificador)
		{
			Avanza();
		} else
		{
			error(Gramatica.Identificador, "");
		}
	}

	public String motorSintactico(ArrayList<String> listaTokens)
	{

		salida = "";
		idx = 0;
		componentes = listaTokens;
		try
		{
			componente = componentes.get(0);
		} catch (IndexOutOfBoundsException e)
		{

		}
		declaracion_Clase();
		if (salida.equals(""))
		{
			salida = "No hay errores sintacticos";
			return salida;
		} else
		{
			return salida;
		}

	}

	private void statement()
	{
		String c = null, caux = null;
		c = componente;
		if (getTipo(c) == Gramatica.If)
		{
			Avanza();
			if_Statement();
		} else if (getTipo(c) == Gramatica.While)
		{
			Avanza();
			while_Statement();
		} else if (getTipo(c) == Gramatica.Asignacion)
		{
			variable_declaration();
		}
	}

	private void string_literal()
	{
		Acomodar(Gramatica.Identificador, componente);
	}

	private void testing_expression()
	{
		String c = null, caux = null;
		c = componente;
		if (getTipo(c) == Gramatica.Entero_literal)
		{
			integer_literal();
		} else
		{
			identificador();
		}
		c = componente;
		if (getTipo(c) == Gramatica.Operadores_aritmeticos)
		{
			Avanza();
		}
		c = componente;
		if (getTipo(c) == Gramatica.Entero_literal)
		{
			integer_literal();
		} else
		{
			identificador();
		}
	}

	private void type()
	{
		type_specifier();
	}

	private void type_specifier()
	{
		String c = null, caux = null;
		c = componente;
		// if(c.getToken().matches("(int|boolean)"))
		if (getTipo(c) == Gramatica.Identificador)
		{
			Avanza();
		} else
		{
			error(getTipo(c), "");
		}
	}

	private void variable_declaration()
	{

		String c = null, caux = null;
		c = componente;

		if (getTipo(c) != Gramatica.Asignacion)
		{
			modificador();
		}
		type();
		if (getTipo(c) == Gramatica.Booleano_literal)
		{

		}

		// fila = componente;
		identificador();

		c = componente;
		if (c.equals("="))
		{
			Avanza();
			variable_declarator();
		}

	}

	private void variable_declarator()
	{
		String c, cauxa;
		c = componente;

		if (getTipo(c) == Gramatica.Entero_literal)
		{
			integer_literal();
		} else if (getTipo(c) == Gramatica.Booleano_literal)
		{
			boolean_literal();
		} else
		{
			error(getTipo(componente), "");
		}
	}

	private void while_Statement()
	{

		// Componente c = null, caux = null, cauxa = null;
		String c = componente;
		Acomodar(Gramatica.Simbolos_especiales, "(");
		expression();
		Acomodar(Gramatica.Simbolos_especiales, ")");
		Acomodar(Gramatica.Simbolos_especiales, "{");
		statement();
		Acomodar(Gramatica.Simbolos_especiales, "}");
	}

	private Gramatica getTipo(String s)
	{
		for (Gramatica t : Gramatica.values())
		{
			if (t.final_coincidencias(s) != -1)
				return t;
		}
		return null;
	}
}
