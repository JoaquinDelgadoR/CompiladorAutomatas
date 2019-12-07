package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class Ventana extends JFrame
{
	private static final long serialVersionUID = -3760618021421768380L;

	private JPanel panel_sur;
	private JTextArea jta_texto;
	private JTextArea jta_consola;
	private JToolBar barra_tareas;
	private JButton btn_iniciar;
	private Lexer lexer;
	private JTabbedPane jtb_panel_consola;
	private JScrollPane scroll_consola;
	private JScrollPane scroll_editor;
	private JScrollPane scroll_identificadores;
	private JTable tabla_identificadores;
	private DefaultTableModel modelo_tabla;
	private String encabezado[];
	private LinkedList<String> identificadores;
	private ArrayList<String> listaLexemas;

	private boolean banderaIconos = true;

	public Ventana()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
		}
		setTitle("Compilador");
		setSize(800, 600);
		try
		{
			setIconImage(cargarIcono("/recursos/icono_codigo.png"));
		} catch (NullPointerException e)
		{
			JOptionPane.showMessageDialog(this, "El programa se ejecutará sin íconos", "Error al cargar íconos", JOptionPane.DEFAULT_OPTION);
			banderaIconos = false;
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		inicializar();
	}

	private void inicializar()
	{
		jta_texto = new JTextArea();
		jta_texto.setTabSize(2);
		scroll_editor = new JScrollPane(jta_texto);
		this.add(scroll_editor, BorderLayout.CENTER);

		jtb_panel_consola = new JTabbedPane();
		panel_sur = new JPanel(new BorderLayout());
		panel_sur.add(jtb_panel_consola, BorderLayout.CENTER);
		panel_sur.setPreferredSize(new Dimension(10, 200));
		this.add(panel_sur, BorderLayout.SOUTH);

		jta_consola = new JTextArea();
		jta_consola.setEditable(false);
		scroll_consola = new JScrollPane(jta_consola);
		jtb_panel_consola.add(scroll_consola, "Consola");

		encabezado = new String[] { "Nombre", "Tipo de dato", "Dato que contiene" };
		modelo_tabla = new DefaultTableModel();
		modelo_tabla.setColumnIdentifiers(encabezado);
		tabla_identificadores = new JTable(modelo_tabla);

		scroll_identificadores = new JScrollPane(tabla_identificadores);

		jtb_panel_consola.add(scroll_identificadores, "Tabla de identificadores");

		barra_tareas = new JToolBar();
		barra_tareas.setFloatable(false);

		btn_iniciar = new JButton("Iniciar proceso");
		btn_iniciar.addActionListener(e -> resultado_analisis());

		if (banderaIconos)
		{
			jtb_panel_consola.setIconAt(0, new ImageIcon(this.getClass().getResource("/recursos/icono_consola.png")));
			jtb_panel_consola.setIconAt(1, new ImageIcon(this.getClass().getResource("/recursos/icono_lista.png")));
			btn_iniciar.setIcon(new ImageIcon(this.getClass().getResource("/recursos/icono_compilar.png")));
		}

		barra_tareas.add(btn_iniciar);
		this.add(barra_tareas, BorderLayout.NORTH);

	}

	private void resultado_analisis()
	{
		lexer = new Lexer();
		jta_consola.setText("");
		identificadores = new LinkedList<String>();
		listaLexemas = new ArrayList<String>();
		lexer.analizar(jta_texto.getText().split("\n"));

		while (!lexer.concluido())
		{
			Gramatica token = lexer.token_actual();
			String lexema = lexer.lexema_actual();

			if (token == Gramatica.Identificador && !identificadores.contains(lexema))
			{
				identificadores.add(lexema);
			}
			listaLexemas.add(lexema);
			jta_consola.append(lexema + "\t" + token + "\n");
			lexer.continuar();
		}

		Parser p = new Parser();
		if (lexer.analisis_exitoso())
		{
			jta_consola.append("\n----::RESULTADO DE ANÁLISIS LÉXICO::----\n");
			jta_consola.append("Análisis léxico finalizado con éxito.");
		} else
		{
			jta_consola.append(lexer.mensaje_error());
		}
		jta_consola.append("\n--::RESULTADO DE ANÁLISIS SINTÁCTICO::--\n");
		p.motorSintactico(listaLexemas);
		jta_consola.append(p.salida + "\n");
		llenarTabla(identificadores);
	}

	private void llenarTabla(LinkedList<String> nombres)
	{
		borrarTabla(tabla_identificadores.getRowCount() - 1);
		for (int i = 0; i < nombres.size(); i++)
		{
			String renglon[] = new String[] { nombres.get(i), "", "" };
			modelo_tabla.addRow(renglon);
		}
	}

	private void borrarTabla(int i)
	{
		if (i >= 0)
		{
			modelo_tabla.removeRow(i);
			borrarTabla(i - 1);
		}
	}

	private BufferedImage cargarIcono(String ruta)
	{
		try
		{
			InputStream imageInputStream = this.getClass().getResourceAsStream(ruta);
			BufferedImage bufferedImage = ImageIO.read(imageInputStream);
			return bufferedImage;

		} catch (IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}
}