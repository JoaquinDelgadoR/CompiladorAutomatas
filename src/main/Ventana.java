package main;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

public class Ventana extends JFrame
{
	private static final long serialVersionUID = -3760618021421768380L;

	private JTextArea jta_texto;
	private JTextArea jta_consola;
	private JToolBar barra_tareas;
	private JButton btn_iniciar;
	private Lexer lexer;

	public Ventana()
	{
		setTitle("Compilador chafa");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		inicializar();
	}

	private void inicializar()
	{

		jta_texto = new JTextArea();
		this.add(jta_texto, BorderLayout.CENTER);

		jta_consola = new JTextArea();
		jta_consola.setEditable(false);
		this.add(jta_consola, BorderLayout.EAST);

		barra_tareas = new JToolBar();
		btn_iniciar = new JButton("Iniciar análisis");
		btn_iniciar.addActionListener(e -> resultado_analisis()); // Esto de acá es una función lambda, la uso para
																	// directamente llamar al método del Lexer
		barra_tareas.add(btn_iniciar);
		this.add(barra_tareas, BorderLayout.NORTH);

	}

	private void resultado_analisis()
	{
		lexer = new Lexer();
		jta_consola.setText("");
		
		lexer.analizar(jta_texto.getText().split("\n"));
		while (!lexer.concluido())
		{
			jta_consola.append(lexer.lexema_actual() + "\t" + lexer.token_actual() + "\n");
			lexer.continuar();
		}

		if (lexer.analisis_exitoso())
		{
			jta_consola.append("Análisis finalizado con éxito.");
		} else
		{
			jta_consola.append(lexer.mensaje_error());
		}

	}
}
