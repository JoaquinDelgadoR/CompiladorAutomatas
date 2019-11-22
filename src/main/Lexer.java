package main;

import java.util.HashSet;
import java.util.Set;

public class Lexer
{
	private StringBuilder entrada = new StringBuilder();
	private Gramatica token;
	private String lexema;
	private boolean concluido = false;
	private String mensaje_error = "";
	private Set<Character> caracteres_vacios = new HashSet<Character>();
	private String codigo[];
	public Lexer()
	{
	}

	public void analizar(String codigo[])
	{
		this.codigo = codigo;
		for (String x : codigo)
		{
			entrada.append(x);
		}
		
		// Por algún motivo necesito eliminar estas cosas que no existen
		caracteres_vacios.add('\r');
		caracteres_vacios.add('\n');
		caracteres_vacios.add((char) 8);
		caracteres_vacios.add((char) 9);
		caracteres_vacios.add((char) 11);
		caracteres_vacios.add((char) 12);
		caracteres_vacios.add((char) 32);

		continuar();
	}

	public void continuar()
	{
		if (concluido)
		{
			return;
		}

		if (entrada.length() == 0)
		{
			concluido = true;
			return;
		}

		ignorar_espacios();

		if (siguiente_token())
		{
			return;
		}

		concluido = true;

		if (entrada.length() > 0)
		{
			mensaje_error = "Simbolo no esperado: '" + entrada.charAt(0) + "'" + " en la línea " + obtenerIndice(entrada.charAt(0));
			mensaje_error = "Simbolo no esperado: '" + entrada.charAt(0) + "'" + " en la línea " + obtenerIndice(entrada.charAt(0));
		}
	}
	
	private int obtenerIndice(char c)
	{
		String x;
		for(int i = 0; i < codigo.length ; i++)
		{
			x = codigo[i];
			if(x.contains("" + c))
			{
				return(i + 1);
			}
		}
		return 0;
	}
	private void ignorar_espacios()
	{
		int caracteres_a_borrar = 0;

		while (caracteres_vacios.contains(entrada.charAt(caracteres_a_borrar)))
		{
			caracteres_a_borrar++;
		}

		if (caracteres_a_borrar > 0)
		{
			entrada.delete(0, caracteres_a_borrar);
		}
	}

	private boolean siguiente_token()
	{
		for (Gramatica t : Gramatica.values())
		{
			int fin = t.final_coincidencias(entrada.toString());

			if (fin != -1)
			{
				token = t;
				lexema = entrada.substring(0, fin);
				entrada.delete(0, fin);
				return true;
			}
		}

		return false;
	}

	public Gramatica token_actual()
	{
		return token;
	}

	public String lexema_actual()
	{
		return lexema;
	}

	public boolean analisis_exitoso()
	{
		return mensaje_error.isEmpty();
	}

	public String mensaje_error()
	{
		return mensaje_error;
	}

	public boolean concluido()
	{
		return concluido;
	}
}