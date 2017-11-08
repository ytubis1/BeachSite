
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.InputStream;


public class GetData {
	
	private static final String url = "jdbc:mysql://127.0.0.1/beach_app";

    private static final String user = "root";

    private static final String password = "hck17ba";

    public static void executeUpdate(String sqlQuery) throws SQLException{
                    Connection con = null;
            try {
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection(url, user, password);
                    System.out.println("Success");

                    // Declare the JDBC objects.
                    Statement stmt = null;
                    ResultSet rs = null;

                    //String SQL = "INSERT INTO district (district_id, district_name) VALUES (6, \"Eilat\")";
                    stmt = con.createStatement();
                    stmt.executeUpdate(sqlQuery);

            }
            catch (Exception ex) {
                    ex.printStackTrace();
            }
            finally {
                    if (con != null) {
                            con.close();
                    }
            }
    }
    
	
public static String loadFile(File file) throws IOException{
		
		System.out.println("file to read: " + file.getPath());

		StringBuilder data = new StringBuilder();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
			String line;
		    try {
				while ((line = in.readLine()) != null)
				{
					System.out.println("line: " + line);
					data.append("\n" + line);
				}
			in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("IOException");
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("FileNotFoundException");
		}
		
	    //System.out.println("\n\ndata: " + data.toString());
	    
	    return data.toString();
	}
	
	public static String getHeatLoad(String heat){
		Integer heat_id = 1;
		if (heat.equalsIgnoreCase("אין")){
			heat_id = 1; 
		}
		if (heat.equalsIgnoreCase("קל")){
			heat_id = 2; 
		}
		if (heat.equalsIgnoreCase("בינוני")){
			heat_id = 3; 
		}
		if (heat.equalsIgnoreCase("כבד")){
			heat_id = 4; 
		}
		
		return heat_id.toString();
	}
	
	public static String getWindDirection(String direction){
		
		Integer direction_id = 1;
		if (direction.equalsIgnoreCase("צפונית")){
			direction_id = 1; 
		}
		if (direction.equalsIgnoreCase("צפון מזרחית")){
			direction_id = 2; 
		}
		if (direction.equalsIgnoreCase("מזרחית")){
			direction_id = 3; 
		}
		if (direction.equalsIgnoreCase("דרום מזרחית")){
			direction_id = 4; 
		}
		if (direction.equalsIgnoreCase("דרומית")){
			direction_id = 5; 
		}
		if (direction.equalsIgnoreCase("דרום מערבית")){
			direction_id = 6; 
		}
		if (direction.equalsIgnoreCase("מערבית")){
			direction_id = 7; 
		}
		if (direction.equalsIgnoreCase("צפון מערבית")){
			direction_id = 8; 
		}				
		
		return direction_id.toString();
	}
	
	public static void main(String []args) throws IOException {
		
		String url = "https://www.israelweather.co.il/forecast/sea.html";
		
		ProcessBuilder getSource = new ProcessBuilder("sh", "-c" , "wget --secure-protocol=SSLv3 " + url);
		//ProcessBuilder getSource = new ProcessBuilder("sh", "-c" , "curl " + url + " | iconv -f iso-8859-8 -t utf-8 > out1.tmp");
		ProcessBuilder getTable = new ProcessBuilder("sh", "-c", "cat out1.tmp | grep '<tr><td bgcolor=\"#7899d7\">' | sed 's/<tr><td bgcolor=\"[^>]*\">/\\n/g' | sed 's|</td><td align=\"center\" bgcolor=\"[^>]*\">|;|g' | sed 's|</td></tr>$||g' | grep -v '^[[:space:]]*$' > out2.tmp");

		try {
			getSource.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			InputStream input = new BufferedInputStream(new FileInputStream("out1.tmp"));
			byte[] buffer = new byte[500000];

			try {
    			for (int length = 0; (length = input.read(buffer)) != -1;) {
        			System.out.write(buffer, 0, length);
   			 }
			} finally {
    			input.close();
			}

			ProcessBuilder parseSource = new ProcessBuilder("sh" , "-c", "cat sea.html | iconv -f iso-8859-8 -t utf-8 > out1.tmp");
			parseSource.start();

			try {
                                Thread.sleep(5000);
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }

			getTable.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File data = new File("out2.tmp");
		
		String info = loadFile(data);
		System.out.println("data: " + info);
		
		String[][] allData = new String[14][3];
		String[] lineInWork = new String[5];
		String[] lines = new String[14];
		lines = info.split("\n");
		
		int i = 1;
		lineInWork = lines[2].split(";", 4);
		allData[i][0] = "wind_direction_id";
		allData[i][1] = getWindDirection(lineInWork[1]);
		allData[i][2] = getWindDirection(lineInWork[2]);
		System.out.println("wind: " + allData[1][0] + "/" + allData[1][1] + "/" + allData[1][2]);
		/*********************************************************************************************/
		lineInWork = lines[11].split(";", 4);
		i = 2;
		allData[i][0] = "heat_index_id";
		allData[i][1] = getHeatLoad(lineInWork[1]);
		allData[i][2] = getHeatLoad(lineInWork[2]);
		System.out.println("heat: " + allData[2][0] + "/" + allData[2][1] + "/" + allData[2][2]);
		/*********************************************************************************************/
		lineInWork = lines[5].split(";", 4);
		i = 3;
		allData[i][0] = "waves_height_min";
		allData[i][1] = lineInWork[1].substring(0, lineInWork[1].indexOf("-"));
		allData[i][2] = lineInWork[2].substring(0, lineInWork[2].indexOf("-"));
		i = 4;
		allData[i][0] = "waves_height_max";
		allData[i][1] = lineInWork[1].substring(lineInWork[1].indexOf("-") + 1, lineInWork[1].indexOf("c"));
		allData[i][2] = lineInWork[2].substring(lineInWork[2].indexOf("-") + 1, lineInWork[2].indexOf("c"));

		System.out.println(allData[3][0] + "/" + allData[3][1] + "/" + allData[3][2]);
		System.out.println(allData[4][0] + "/" + allData[4][1] + "/" + allData[4][2]);
		/*********************************************************************************************/
		lineInWork = lines[3].split(";", 4);
		i = 5;
		allData[i][0] = "wind_speed_min";
		int makaf1 = lineInWork[1].indexOf("-");
		int makaf2 = lineInWork[1].indexOf("-", makaf1 + 1);
		System.out.println(lineInWork[1].substring(makaf1 + 1, makaf2));
		System.out.println(lineInWork[1].substring(makaf2 + 1, lineInWork[1].indexOf(" ")));

		allData[i][1] = lineInWork[1].substring(makaf1 + 1, makaf2);
		allData[i+1][1] = lineInWork[1].substring(makaf2 + 1, lineInWork[1].indexOf(" "));

		makaf1 = lineInWork[2].indexOf("-");
		makaf2 = lineInWork[2].indexOf("-", makaf1 + 1);
		allData[i][2] = lineInWork[2].substring(makaf1 + 1, makaf2);
		allData[i+1][2] = lineInWork[2].substring(makaf2 + 1, lineInWork[1].indexOf(" "));
		System.out.println(allData[i][0] + "/" + allData[i][1] + "/" + allData[i][2]);
		
		i = 6;
		allData[i][0] = "wind_speed_max";
		System.out.println(allData[i][0] + "/" + allData[i][1] + "/" + allData[i][2]);
		/*********************************************************************************************/
		
		int sea_id = 1;
		int area = 1;
		String query1 = "UPDATE sea_information SET wind_direction_id = " + allData[1][area] + 
				", heat_index_id = " + allData[2][area] + 
				", waves_height_min = " + allData[3][area] + 
				", waves_height_max = " + allData[4][area] + 
				", wind_speed_min = " + allData[5][area] + 
				", wind_speed_max = " + allData[6][area] + " WHERE sea_id = " + sea_id;

		sea_id = 7;
		area = 2;
		String query2 = "UPDATE sea_information SET wind_direction_id = " + allData[1][area] + 
				", heat_index_id = " + allData[2][area] + 
				", waves_height_min = " + allData[3][area] + 
				", waves_height_max = " + allData[4][area] + 
				", wind_speed_min = " + allData[5][area] + 
				", wind_speed_max = " + allData[6][area] + " WHERE sea_id = " + sea_id;		

		System.out.println(query1);
		System.out.println(query2);
		
		//JDBC myquery = new JDBC();
		try {
			executeUpdate(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Connection dbConnection = DriverManager.getConnection("doc5.xicod.com/phpmyadmin", user, password)
	}

}



