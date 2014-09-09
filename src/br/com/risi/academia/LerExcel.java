package br.com.risi.academia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class LerExcel {
	
	public static void main(String[] args) throws BiffException, IOException, ClassNotFoundException, SQLException 
	{
		
		// PERCORRE O DIRETORIO
		String dir = "C:/Users/P_991250/Desktop/perfect/teste/"; 
		File diretorio = new File(dir); 
		File fList[] = diretorio.listFiles(); 
		
		System.out.println("Numero de arquivos no diretorio : " + fList.length ); 

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

		 for (String nomeArquivo : listaArquivos) {
		 pegaInformacoesExcel(nomeArquivo);
		 }
		
	      
	}

	public static void pegaInformacoesExcel(String nomeArquivo) throws FileNotFoundException, IOException {
		
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
			
			System.out.println("nome: " + nome);
			System.out.println("inicio: " + inicio);
			System.out.println("termino: " + termino);
			System.out.println("tipoPrograma: " + tipoPrograma);
			System.out.println("professor: " + professor);
			System.out.println("cref: " + cref);
			System.out.println("obs: " + obs);

//			String b1Val = cellB1.getStringCellValue();

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
			
			System.out.println("nome: " + nome);
			System.out.println("inicio: " + inicio);
			System.out.println("termino: " + termino);
			System.out.println("tipoPrograma: " + tipoPrograma);
			System.out.println("professor: " + professor);
			System.out.println("cref: " + cref);
			System.out.println("obs: " + obs);

		}
	}

	public static void banco() throws ClassNotFoundException {
		/*
		 * String sql =
		 * "INSERT INTO MinhaTabela(col1,col2,col3) VALUES('"+stringa1
		 * +"','"+stringb2+"',"+stringc2+"')";
		 */
		/* Executa o insert para inserir os dados no banco de testes MySQL */
		// Class.forName("com.mysql.jdbc.Driver");
		// Connection conn =
		// DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jar",
		// "user", "senha");
		// PreparedStatement ps =
		// conn.prepareStatement("INSERT INTO Teste(string1,string2,titulo) VALUES('"+stringa1+"','"+stringb2+"','"+stringc2+"')");
		// ps.executeUpdate();
		// ps.close();
	}
}