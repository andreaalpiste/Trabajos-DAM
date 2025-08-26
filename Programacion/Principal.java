package terceraev;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Principal {
		
		private static String url = "jdbc:sqlite:./src/terceraev/BDiccionario.db";
		
		public static void main(String[] args) {
		String ficheroDeseado = pedirFichero();
		ArrayList<TraduccionPalabra> palabrasLeidas = leerFichero(ficheroDeseado);
		ArrayList<TraduccionPalabra> listaTraduccion = traducirLista(palabrasLeidas);
		generarArchivo(ficheroDeseado, listaTraduccion);
		}
		
		private static String pedirFichero() {
			Scanner sc = new Scanner(System.in);
			String ficheroDeseado ="";
			boolean existeFichero = false;
			
			while(!existeFichero) {
				System.out.println("Introduce nombre fichero de texto");
			
				ficheroDeseado = sc.next();
				try(BufferedReader br = new BufferedReader(new FileReader(ficheroDeseado))){
			
					existeFichero = true;
				
				}catch(IOException ex) {
					System.out.println("No existe archivo. Introduzca de nuevo");
				}
			}
			return ficheroDeseado;	
		}
		
		private static ArrayList<TraduccionPalabra> leerFichero(String ficheroDeseado) {
			ArrayList<TraduccionPalabra> palabrasLeidas = new ArrayList<TraduccionPalabra>();
			System.out.println("Fichero encontrado de nombre " + ficheroDeseado);
			
			try (BufferedReader br = new BufferedReader(new FileReader(ficheroDeseado))){
				System.out.println("Contenido fichero");
				String miRead;	
				while((miRead = br.readLine()) != null) {
					String[] lista = miRead.split(" ");
					for(String p : lista) {
						if(!p.isEmpty()) {
							palabrasLeidas.add(new TraduccionPalabra(p));
							System.out.println(p);
						}
					}
				}
			} catch (FileNotFoundException e) {
				System.err.println("No se encontro fichero" + e.getMessage());
				e.printStackTrace();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return palabrasLeidas;
		}
		
		private static ArrayList<TraduccionPalabra> traducirLista(ArrayList<TraduccionPalabra> palabrasLeidas) {
			ArrayList<TraduccionPalabra> listaTraduccion = new ArrayList<>();
			String sqlConsult = "SELECT PalEspanyol, PalIngles FROM TPalabras WHERE PalAleman = ?";
			
				try (Connection connect = DriverManager.getConnection(url)){
					
					System.out.println("Conectado a la base de datos");
					PreparedStatement ps = connect.prepareStatement(sqlConsult);
					
					for(TraduccionPalabra palabra : palabrasLeidas) {
						
						ps.setString(1, palabra.getPalAleman());
						try (ResultSet rs = ps.executeQuery()){
							if(rs.next()) {
								String espanyol = rs.getString("PalEspanyol");
								String ingles = rs.getString("PalIngles");
								if(espanyol != null && !espanyol.isEmpty()) {
									listaTraduccion.add(new TraduccionPalabra(palabra.getPalAleman(), espanyol));
								}
								else if(ingles != null && !ingles.isEmpty()) {
									
									listaTraduccion.add(new TraduccionPalabra(palabra.getPalAleman(), ingles));
								}
								else listaTraduccion.add(new TraduccionPalabra(palabra.getPalAleman(), "Sin traduccion"));
							}
							else listaTraduccion.add(new TraduccionPalabra(palabra.getPalAleman(), "No existe en la base de datos"));
						}
					}
					
				} catch (SQLException e) {
					System.err.println("Error en la conexion a la base de datos");
					e.printStackTrace();
				}
			
			System.out.println("Conexion cerrada a la base de datos");
			
			return listaTraduccion;
			
		}

		private static void generarArchivo(String ficheroDeseado, ArrayList<TraduccionPalabra> listaTraduccion) {
			
			String anyo, mes, dia;
			GregorianCalendar gc = new GregorianCalendar();
			int anyoFile = (gc.get(Calendar.YEAR));
			int mesFile = (gc.get(Calendar.MONTH) + 1);
			int diaFile = (gc.get(Calendar.DAY_OF_MONTH));
			if (mesFile >=10) mes  = ""+mesFile;  else  mes  = "0"+mesFile; 
			if (diaFile >=10) dia  = ""+diaFile;  else  dia  = "0"+diaFile; 
			String fechaFile = anyoFile + "-" + mes + "-" + dia;
			
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroDeseado + " " + fechaFile + ".txt"))){
				
				for(TraduccionPalabra t : listaTraduccion) {
					
					bw.write(t.toString());
					bw.newLine();
				}
				
			} catch (FileNotFoundException e) {
				System.out.println("Error en la creacion del archivo");
				e.printStackTrace();
				
			} catch (IOException e) {
				System.out.println("ERROR " + e.getMessage());
				e.printStackTrace();
			}
			
		}
}
