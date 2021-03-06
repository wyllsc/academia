package br.com.risi.academia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.risi.academia.model.Aluno;
import br.com.risi.academia.model.Serie;

public class LerExcel {

	public static void main(String[] args) throws BiffException, IOException,
			ClassNotFoundException, SQLException {
		// PERCORRE O DIRETORIO
		String dir = "C:/Users/P_991250/Desktop/perfect/teste/";
		File diretorio = new File(dir);
		File fList[] = diretorio.listFiles();

		System.out.println("Numero de arquivos no diretorio : " + fList.length);

		List<String> listaArquivos = new ArrayList<String>();

		for (File arq : fList) {
			if (arq.isDirectory()) {

				System.out.println("Pasta: " + arq.getName());

				File listaArq[] = arq.listFiles();

				for (File arquivoExcel : listaArq) {
					System.out.println("Arquivo: " + arquivoExcel.getPath());
					listaArquivos.add(arquivoExcel.getPath());
				}
			} else {
				listaArquivos.add(arq.getPath());
			}
		}

		List<Aluno> listaAlunos = new ArrayList<Aluno>();
		for (String nomeArquivo : listaArquivos) {
			Aluno aluno = new Aluno();
			aluno = pegaInformacoesExcel(nomeArquivo);
			listaAlunos.add(aluno);
		}
		ajustaAlunoComSuaSerie(listaAlunos);
	}

	public static void ajustaAlunoComSuaSerie(List<Aluno> listaAlunos) {
		for (Aluno aluno : listaAlunos) {
			
			Map<String, List<Serie>> mapa = new HashMap<String, List<Serie>>();
			String tipoSerie = "";

			for (Serie serie : aluno.getListaSerie()) {
				
				if (StringUtils.isEmpty(serie.getNumero()) && !StringUtils.isEmpty(serie.getExercicio())) {
					tipoSerie = serie.getExercicio();
				}
				serie.setTipoSerie(tipoSerie);
			}
			
			// Ajusta a Série de acordo com o Tipo da Série
			for (Serie serie : aluno.getListaSerie()) {
				if (!mapa.containsKey(serie.getTipoSerie())) {
					mapa.put(serie.getTipoSerie(), new ArrayList<Serie>());
				}
				mapa.get(serie.getTipoSerie()).add(serie);
			}

			List<Serie> listaFinal = new ArrayList<Serie>();
			List<Serie> listaRemocao = new ArrayList<Serie>();
			
			// Ajusta o HashMap e Retira os dados em Branco
			for (List<Serie> listaMapa : mapa.values()) {
				Serie obj = new Serie();
				obj.setTipoSerie(listaMapa.get(0).getTipoSerie());
				obj.setListaHash(listaMapa);
				
				for (Serie serie : listaMapa) 
				{
					if(StringUtils.isEmpty(serie.getNumero()))
					{
						listaRemocao.add(serie);
					}
				}
				listaFinal.add(obj);
				
				for (Serie serie : listaFinal) {
					serie.getListaHash().removeAll(listaRemocao);
				}
			}
			aluno.setListaSerie(listaFinal);
		}
	}

	public static Aluno pegaInformacoesExcel(String nomeArquivo) throws FileNotFoundException, IOException {

		Aluno aluno = new Aluno();

		// USADO PARA PEGAR OS VALORES DA TABELA DO EXCEL
		FileInputStream arquivo = new FileInputStream(nomeArquivo);

		if (arquivo.toString().contains(".xlsx")) {
			XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
			XSSFSheet worksheet = workbook.getSheetAt(0);

			XSSFRow linha1 = worksheet.getRow(0);
			XSSFRow linha2 = worksheet.getRow(1);
			XSSFRow linha4 = worksheet.getRow(3);
			XSSFRow linha5 = worksheet.getRow(4);
			XSSFRow linha6 = worksheet.getRow(5);

			XSSFCell nome = linha1.getCell(2);
			XSSFCell inicio = linha2.getCell(6);
			XSSFCell termino = linha2.getCell(10);
			XSSFCell tipoPrograma = linha4.getCell(0);
			XSSFCell professor = linha5.getCell(2);
			XSSFCell cref = linha6.getCell(2);
			XSSFCell obs = linha4.getCell(3);

			aluno.setNome(nome.getStringCellValue());
			aluno.setDataInicio(inicio.getStringCellValue());
			aluno.setDataFinal(termino.getStringCellValue());
			aluno.setTipoPrograma(tipoPrograma.getStringCellValue());
			aluno.setProfessor(professor.getStringCellValue());
			aluno.setCref(cref.getStringCellValue());
			aluno.setObservacao(obs.getStringCellValue());

			// System.out.println("nome: " + nome);
			// System.out.println("inicio: " + inicio);
			// System.out.println("termino: " + termino);
			// System.out.println("tipoPrograma: " + tipoPrograma);
			// System.out.println("professor: " + professor);
			// System.out.println("cref: " + cref);
			// System.out.println("obs: " + obs);

			int linhaInicial = 7;
			int linhaFinal = 49;

			int colunaInicial = 0;
			int colunaFinal = 10;

			// Colunas da Tabela contendo o exercicio
			int numero = 0;
			int exercicio = 1;
			int observacao = 5;
			int serie = 7;
			int repeticoes = 8;
			int carga = 9;

			for (int i = linhaInicial; i < linhaFinal; i++) {
				Serie s = new Serie();
				for (int j = colunaInicial; j < colunaFinal; j++) {
					XSSFRow linha = worksheet.getRow(i);
					XSSFCell celula = linha.getCell(j);

					try {
						if (j == numero) {
							s.setNumero(celula.toString());
							System.out.println("NUMERO: ");
							System.out.println(celula.toString());
						}
						if (j == exercicio) {
							s.setExercicio(celula.toString());
							// System.out.println("EXERCICIO: ");
							// System.out.println(celula.toString());
						}
						if (j == observacao) {
							s.setObservacao(celula.toString());
							// System.out.println("OBSERVACAO: ");
							// System.out.println(celula.toString());
						}
						if (j == serie) {
							s.setSerie(celula.toString());
							// System.out.println("SERIE: ");
							// System.out.println(celula.toString());
						}
						if (j == repeticoes) {
							s.setRepeticao(celula.toString());
						}
						if (j == carga) {
							s.setCarga(celula.toString());
							// System.out.println("CARGA: ");
							// System.out.println(celula.toString());
						}
					} catch (Exception e) {
						// System.out.println("Não faz Nada !!!");
						System.out.println(e.getMessage());
					}
				}
				aluno.getListaSerie().add(s);
			}
		} else {
			HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
			HSSFSheet worksheet = workbook.getSheetAt(0);

			HSSFRow linha1 = worksheet.getRow(0);
			HSSFRow linha2 = worksheet.getRow(1);
			HSSFRow linha4 = worksheet.getRow(3);
			HSSFRow linha5 = worksheet.getRow(4);
			HSSFRow linha6 = worksheet.getRow(5);

			HSSFCell nome = linha1.getCell(2);
			HSSFCell inicio = linha2.getCell(6);
			HSSFCell termino = linha2.getCell(10);
			HSSFCell tipoPrograma = linha4.getCell(0);
			HSSFCell professor = linha5.getCell(2);
			HSSFCell cref = linha6.getCell(2);
			HSSFCell obs = linha4.getCell(3);

			aluno.setNome(nome.getStringCellValue().toString());
			aluno.setDataInicio(inicio.toString());
			aluno.setDataFinal(termino.toString());
			aluno.setTipoPrograma(tipoPrograma.getStringCellValue().toString());
			aluno.setProfessor(professor.getStringCellValue().toString());
			aluno.setCref(cref.getStringCellValue().toString());
			aluno.setObservacao(obs.getStringCellValue().toString());

			// System.out.println("---///------///------///---");
			// System.out.println("nome: " + nome);
			// System.out.println("inicio: " + inicio);
			// System.out.println("termino: " + termino);
			// System.out.println("tipoPrograma: " + tipoPrograma);
			// System.out.println("professor: " + professor);
			// System.out.println("cref: " + cref);
			// System.out.println("obs: " + obs);
			// System.out.println("---///------///------///---");

			int linhaInicial = 7;
			int linhaFinal = 49;

			int colunaInicial = 0;
			int colunaFinal = 10;

			// Colunas da Tabela contendo o exercicio
			int numero = 0;
			int exercicio = 1;
			int observacao = 5;
			int serie = 7;
			int repeticoes = 8;
			int carga = 9;

			for (int i = linhaInicial; i < linhaFinal; i++) {
				Serie s = new Serie();
				for (int j = colunaInicial; j < colunaFinal; j++) {
					HSSFRow linha = worksheet.getRow(i);

					// System.out.println("LINHA:" + linha.getRowNum());

					if (linha.getCell(j) != null) {
						HSSFCell celula = linha.getCell(j);

						if (celula == null) {
							// System.out.println("Faz nada é null...!");
						} else {
							if (j == numero) {
								s.setNumero(celula.toString());
								// System.out.println("NUMERO: ");
								// System.out.println(celula.toString());
							}
							if (j == exercicio) {
								s.setExercicio(celula.toString());
								// System.out.println("EXERCICIO: ");
								// System.out.println(celula.toString());
							}
							if (j == observacao) {
								s.setObservacao(celula.toString());
								// System.out.println("OBSERVACAO: ");
								// System.out.println(celula.toString());
							}
							if (j == serie) {
								s.setSerie(celula.toString());
								// System.out.println("SERIE: ");
								// System.out.println(celula.toString());
							}
							if (j == repeticoes) {
								s.setRepeticao(celula.toString());
								// System.out.println("REPETIÇÕES: ");
								// System.out.println(celula.toString());
							}
							if (j == carga) {
								s.setCarga(celula.toString());
								// System.out.println("CARGA: ");
								// System.out.println(celula.toString());
							}
						}
					}
				}
				aluno.getListaSerie().add(s);
			}
		}
		return aluno;
	}

	public static void banco() throws ClassNotFoundException, SQLException {

		String stringa1 = null;
		String stringb2 = null;
		String stringc2 = null;

		String sql = "INSERT INTO MinhaTabela(col1,col2,col3) VALUES('"+ stringa1 + "','" + stringb2 + "'," + stringc2 + "')";

		/* Executa o insert para inserir os dados no banco de testes MySQL */
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jar", "user", "senha");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO Teste(string1,string2,titulo) VALUES('"+ stringa1 + "','" + stringb2 + "','" + stringc2 + "')");
		ps.execute();
		ps.close();
	}
}
