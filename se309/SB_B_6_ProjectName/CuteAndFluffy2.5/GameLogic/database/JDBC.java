package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
	
		static Connection conn1;
		static String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309sbb6";
		static String user = "dbu309sbb6";
		static String password = "AB04xqvf";
		
		public static void openDB()
		{   try 
			{
	        // Load the driver (registers itself)
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
			}
			catch (Exception E) 
			{
	    	
	        System.err.println("Unable to load driver.");
	        E.printStackTrace();
			}

			try 
			{
	        // Connect to the database
	        conn1 = DriverManager.getConnection(dbUrl, user, password);
	        System.out.println("*** Connected to the database ***");

			} 
			catch (SQLException e) 
			{
	        System.out.println("SQLException: " + e.getMessage());
	        System.out.println("SQLState: " + e.getSQLState());
	        System.out.println("VendorError: " + e.getErrorCode());
			}
		}
		public static void retrive() throws SQLException
		{
			System.out.println("retrive ID");
			String SQL = "select ID, userName from userInfo";
			Statement st = conn1.createStatement(); 
			ResultSet rs = st.executeQuery(SQL);
			while(rs.next())
			{
				String retriveData = rs.getInt("ID")+ "  "+rs.getString("userName");
				System.out.println(retriveData);
			}
			
			
		}
		public static void addWin(int win, String IGN) throws SQLException// win:1/ lose:0 
		{
 
			if(win==1)
			{
	
				String SQL = "UPDATE userInfo SET Win = Win + "+win+
						", gamePlayed = gamePlayed + 1, Shell= Shell + 100 WHERE userName ='"+ IGN +"'";
				
				Statement st = conn1.createStatement();
				int rs = st.executeUpdate(SQL);
			}
			if(win ==0)
			{
				String SQL = "UPDATE userInfo SET  gamePlayed = gamePlayed + 1, Shell= Shell + 50 WHERE userName ='"+ IGN +"'";
				
				Statement st = conn1.createStatement();
				int rs = st.executeUpdate(SQL);
			}
		}
		public static void addTotalDMG(int dmg, String IGN) throws SQLException
		{
			
			String SQL = "UPDATE userInfo SET DMGTotal = DMGTotal + "+dmg+" WHERE userName ='"+ IGN +"'";
			
			Statement st = conn1.createStatement();
			int rs = st.executeUpdate(SQL);
		}
		
	
		
		public static void getUserInfo(String userName) throws SQLException
		{
			System.out.println("retrive ID");
			String SQL = "select *, userName from userInfo where userName='"+ userName+"'";
			Statement st = conn1.createStatement(); 
			ResultSet rs = st.executeQuery(SQL);
			while(rs.next())
			{
				String retriveData = "ID: "+rs.getInt("ID")+ 
						" userName: "+rs.getString("userName")+
						" Gameplayed: "+ rs.getInt("gamePlayed")+
						" win: "+rs.getInt("Win")+
						" shell: "+rs.getInt("Shell")+
						" totalDMG:"+rs.getInt("DMGTotal");
				
				System.out.println(retriveData);
			}
			
			
		}
		  public static void loginUser(String userName, String pw)
		  {
			  
		        try 
		        {

		            PreparedStatement stat;

		            ResultSet rs; 
		            String result; 
		          

		            stat = conn1.prepareStatement("select * from userInfo where userName=?");

		            stat.setString(1,userName);

		            rs = stat.executeQuery();

		            if(true == rs.next())
		            {


		                String temp = rs.getString(4);

		                if(temp.equals(pw))
		                {

		                    result = userName + " login successful! welcome!";
		                    System.out.println(result);

		                }

		                else
		                {

		                    result = userName + " login failed invalid password";
		                    System.out.println(result);
		                }
		            }
		            else
		            {
		            	result = userName+" invalid userName";
		            	System.out.println(result);
		            }
		         
		        stat.close();
		        }
		        catch(Exception ex)
		        {
		        	System.out.println("somethingwrong");

		        }

		 

		    
		       }

		  public static boolean loginUserBool(String userName, String pw)
		  {
			  
		        try 
		        {

		            PreparedStatement stat;

		            ResultSet rs; 
		            String result; 
		          

		            stat = conn1.prepareStatement("select * from userInfo where userName=?");

		            stat.setString(1,userName);

		            rs = stat.executeQuery();

		            if(true == rs.next())
		            {


		                String temp = rs.getString(4);

		                if(temp.equals(pw))
		                {

		                    result = userName + " login successful! welcome!";
		                    System.out.println(result);
		                    return true;

		                }

		                else
		                {

		                    result = userName + " login failed invalid password";
		                    System.out.println(result);
		                    return false;
		                }
		            }
		            else
		            {
		            	result = userName+" invalid userName";
		            	System.out.println(result);
		            	return false; 
		            }
		        }
		        catch(Exception ex)
		        {
		        	System.out.println("somethingwrong");

		        }
				return false;

		 

		    
		       }





		public static void addAccount(String userName, String IGN, String pw) throws SQLException
		{
//			Statement st = conn1.createStatement(); 
//			String SQL = "Select * from userInfo";
//			ResultSet rs = st.executeQuery(SQL);
//			while(rs.next())
//			{	
//				String checkUserName = rs.getString("userName");
//				System.out.println(checkUserName);
//				String checkIGN = rs.getString("IGN");
//				System.out.println(checkIGN);
//				
//				if(checkUserName.equals(userName) && checkIGN.equals(IGN))
//				{
//					System.out.println("Username or IGN already exist in DataBase");
//					
//				}
//				
//				else
//				{
//					
//				
//				}  
//			}
				
				
			System.out.println("Account created!");
			String query = " insert into userInfo (userName, IGN, pw, Shell, Win, gamePlayed, DMGTotal)"
			        + " values (?, ?, ?, ?, ? ,? ,?)";

			      // create the mysql insert preparedstatement
			      PreparedStatement preparedStmt = conn1.prepareStatement(query);
			      preparedStmt.setString (1, userName);
			      preparedStmt.setString (2, IGN);
			      preparedStmt.setString (3, pw);
			      preparedStmt.setInt(4, 0);
			      preparedStmt.setInt(5, 0);
			      preparedStmt.setInt(6, 0);
			      preparedStmt.setInt(7, 0);
			      
			      
			      preparedStmt.execute();
		
		}
			
		

				      // execute the preparedstatement
				     
		 public static void main(String[] args) throws Exception 
		 {
			 openDB();
			//addAccount("calakin", "cody", "1111");
			// addAccount("cksdnd004","moon", "1234");
			// retrive();
			// loginUser("cksdnd004", "1234");
			 //loginUser("calakin", "1111");//wrong pasword
			// loginUser("calakim", "1234");//wrong user name
			// getUserInfo("cksdnd004");
			 //getUserInfo("admin");
			
			//addTotalDMG(100, "cody");
			// addWin(1, "cody");
			//addWin(0, "cody");
			
			 
			 getUserInfo("calakin");
		 
		 }
		

}

		







