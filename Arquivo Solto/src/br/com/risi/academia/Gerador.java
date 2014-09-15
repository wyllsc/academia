package br.com.risi.academia;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Wylliam.Novais - P_991250
 */	
public class Gerador extends Thread {

	private static final String ENCODING = "UTF-8";
	private JFrame Gerador;
	private JTextField txtDiretorio;
	private JProgressBar barraProgreso;
	private JButton btnGerar;
	private JComboBox cbLegislatura;
	private JComboBox cbEscolhaParlamentar;

	private JCheckBox chckbxBiografia;
	private JCheckBox chckbxPreposicaoAutoria;
	private JCheckBox chckbxPreposicaoRelatada;
	private JCheckBox chckbxVideosWebCamara;
	private JCheckBox chckbxDiscurso;
	private JCheckBox chckbxNoticiasTvCamara;
	private JCheckBox chckbxNoticiasRadioCamara;
	private JCheckBox chckbxNoticiasJornalCamara;
	private JCheckBox chckbxNoticiasAgenciaCamara;
	private JCheckBox chckbxArquivoSonoro;
	private int countTodosParlamentares;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gerador window = new Gerador();
					window.Gerador.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Gerador() throws RemoteException {
		carregaComponentesTela();
	}

	/**
	 * Método responsável por Criar e Carregar os Componentes da Tela.
	 * 
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	private void carregaComponentesTela() throws RemoteException {

		Gerador = new JFrame();
		Gerador.setTitle("Gerador Deputado em Foco");
		Gerador.setBounds(100, 100, 496, 402);
		Gerador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Gerador.getContentPane().setLayout(null);

		txtDiretorio = new JTextField();
		txtDiretorio.setEditable(false);
		txtDiretorio.setBounds(92, 266, 233, 20);
		txtDiretorio.setColumns(10);
		Gerador.getContentPane().add(txtDiretorio);

		JLabel lblDiretorio = new JLabel("Diretório");
		lblDiretorio.setHorizontalAlignment(SwingConstants.LEFT);
		lblDiretorio.setBounds(39, 269, 97, 14);
		Gerador.getContentPane().add(lblDiretorio);

		JButton btnSelecionarArquivo = new JButton("Escolher");
		btnSelecionarArquivo.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnSelecionarArquivo.setIcon(new ImageIcon("/src/main/resources/img/pasta.png"));
		btnSelecionarArquivo.setToolTipText("Escolher");
		btnSelecionarArquivo.setBounds(335, 266, 114, 20);
		btnSelecionarArquivo.addActionListener(selecionaDiretorio());
		Gerador.getContentPane().add(btnSelecionarArquivo);

		txtDiretorio = new JTextField();
		txtDiretorio.setEditable(false);
		txtDiretorio.setBounds(92, 266, 233, 20);
		txtDiretorio.setColumns(10);
		Gerador.getContentPane().add(txtDiretorio);

		btnGerar = new JButton("Gerar");
		btnGerar.setToolTipText("Escolher");
		btnGerar.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnGerar.setBounds(370, 312, 79, 20);
		Gerador.getContentPane().add(btnGerar);
	}


	/**
	 * Método responsável por selecionar o diretório onde será guardado o
	 * arquivo a ser gerado.
	 */
	private ActionListener selecionaDiretorio() {

		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser pasta = new JFileChooser();
				pasta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int res = pasta.showOpenDialog(null);

				if (res == JFileChooser.APPROVE_OPTION) {
					File diretorio = pasta.getSelectedFile();
					txtDiretorio.setText(diretorio.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(null, "Voce não selecionou nenhum diretorio.");
				}
			}
		};
	}

	private void exportarHTML(Boolean isTodosParlamentares, VelocityEngine ve, VelocityContext context, String caminhoDiretorio, Template template, String nomeArquivo) {
		
		ve.init();
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		try {
			FileWriter saida = new FileWriter(caminhoDiretorio + nomeArquivo);
			saida.write(writer.toString());
			saida.flush();
			saida.close();
			System.out.println("Arquivo '" + nomeArquivo + "' criado!");
		} catch (Exception e) {
			e.printStackTrace();
			barraProgreso.setString("Erro");
			JOptionPane.showMessageDialog(null, "Erro ao Gerar o Arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	@SuppressWarnings("unused")
	private String criaDiretorios(String caminho, String nomeArquivoHtml) throws IOException {

		String separador = java.io.File.separator;

		// CRIA DIRETORIO BASE
		String diretorioBase = caminho + separador + nomeArquivoHtml;
		if (!new File(diretorioBase).exists()) {
			(new File(diretorioBase)).mkdir();
		}

		// CRIA DIRETORIO DE VIDEO
		String diretorioVideo = diretorioBase + separador + "video";
		if (!new File(diretorioVideo).exists()) {
			(new File(diretorioVideo)).mkdir();
		}

		// CRIA DIRETORIO DE AUDIO
		String diretorioAudio = diretorioBase + separador + "audio";
		if (!new File(diretorioAudio).exists()) {
			(new File(diretorioAudio)).mkdir();
		}

		// CRIA DIRETORIO DO CSS
		String dirCss = diretorioBase + separador + "css";
		if (!new File(dirCss).exists()) {
			(new File(dirCss)).mkdir();
		}
		exportaArquivos(dirCss,"/templateDeputadoEmFoco/css/bootstrap.min.css", "bootstrap.min.css");
		exportaArquivos(dirCss,"/templateDeputadoEmFoco/css/jqvmap.css", "jqvmap.css");
		exportaArquivos(dirCss,"/templateDeputadoEmFoco/css/style.css", "style.css");

		// CRIA DIRETORIO DE FONTS
		String dirFonts = diretorioBase + separador + "fonts";
		if (!new File(dirFonts).exists()) {
			(new File(dirFonts)).mkdir();
		}
		exportaArquivos(dirFonts,"/templateDeputadoEmFoco/fonts/glyphicons-halflings-regular.eot", "glyphicons-halflings-regular.eot");
		exportaArquivos(dirFonts,"/templateDeputadoEmFoco/fonts/glyphicons-halflings-regular.svg", "glyphicons-halflings-regular.svg");
		exportaArquivos(dirFonts,"/templateDeputadoEmFoco/fonts/glyphicons-halflings-regular.ttf", "glyphicons-halflings-regular.ttf");
		exportaArquivos(dirFonts,"/templateDeputadoEmFoco/fonts/glyphicons-halflings-regular.woff", "glyphicons-halflings-regular.woff");

		// CRIA DIRETORIO DE IMAGENS
		String dirImg = diretorioBase + separador + "images";
		if (!new File(dirImg).exists()) {
			(new File(dirImg)).mkdir();
		}
		exportaArquivos(dirImg,"/templateDeputadoEmFoco/img/logo.jpg", "logo.jpg");
		exportaArquivos(dirImg,"/templateDeputadoEmFoco/img/texture-bg-header.jpg", "texture-bg-header.jpg");

		// CRIA DIRETORIO DOS JS
		String dirJs = diretorioBase + separador + "js";
		if (!new File(dirJs).exists()) {
			(new File(dirJs)).mkdir();
		}
		exportaArquivos(dirJs,"/templateDeputadoEmFoco/js/bootstrap.min.js", "bootstrap.min.js");
		exportaArquivos(dirJs,"/templateDeputadoEmFoco/js/jquery-1.11.0.min.js", "jquery-1.11.0.min.js");
		exportaArquivos(dirJs,"/templateDeputadoEmFoco/js/jquery.vmap.brazil.js", "jquery.vmap.brazil.js");
		exportaArquivos(dirJs,"/templateDeputadoEmFoco/js/jquery.vmap.min.js", "jquery.vmap.min.js");
		exportaArquivos(dirJs,"/templateDeputadoEmFoco/js/jquery.vmap.world.js", "jquery.vmap.world.js");
		return diretorioBase;
	}

	/**
	 * Método responsável por esportar os arquivos de css e imagens do template para o caminho excolhido
	 * @param caminhoDestino
	 * @param caminhoOrigem
	 * @param nomeArq
	 * @throws IOException
	 */
	private void exportaArquivos(String caminhoDestino, String caminhoOrigem, String nomeArq) throws IOException {
		String separador = java.io.File.separator;
		File destinho = new File(caminhoDestino + separador + nomeArq);

		InputStream in = Gerador.class.getResourceAsStream(caminhoOrigem);
		OutputStream out = new FileOutputStream(destinho);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}
}
