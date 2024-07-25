package com.jspiders.hms;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
public class HMS 
{
	private static final String dburl="jdbc:mysql://localhost:3306/hotel_db";
	private static final String user="root";
	private static final String password="21_Jun_02";
	public static void main(String[] args) throws ClassNotFoundException , SQLException{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		try
		{
			Connection con=DriverManager.getConnection(dburl,user,password);
			while(true)
			{
				System.out.println();//new line consume
				System.out.println("     WELCOME TO     ");
				System.out.println("---TAJ GROUP OF HOTELS----");
				System.out.println("---- HOSTEL MANAGEMENT SYSTEM ----");
				Scanner scanner=new Scanner(System.in);
				System.out.println("1.RESERVE A ROOM");
				System.out.println("2.VIEW ROOM RESERVATION");
				System.out.println("3.GET A ROOM NUMBER");
				System.out.println("4.UPDATE RESERVATION");
				System.out.println("5.DELETE RESERVATION");
				System.out.println("0.EXIT");
				System.out.println("CHOOSE AN OPTION");
				int choice =scanner.nextInt();
				switch(choice)
				{
				case 1 : reserveRoom(con,scanner) ;
				         break;
				case 2: viewReservations(con);
				        break;
				case 3: getRoomNumber(con,scanner);
				        break;
				case 4:updateReservation(con,scanner);
				       break;
				case 5: deleteReservation(con,scanner);
				       break;
				case 0: exit();
				       scanner.close();
				       return;
				default:System.out.println("Invalid choice.........try again......");       
				
				
				}
			}
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		} 
		catch (InterruptedException e) 
		{
			
			throw new RuntimeException(e);
		}
		
	}
//method for reservation of an hotel 
	private static void reserveRoom(Connection con,Scanner scanner)
	{
		try
		{
			System.out.println("Enter Guest Name:");
			String guest=scanner.next();
			scanner.nextLine();
			System.out.println("Enter Room Number");
			int room=scanner.nextInt();
			System.out.println("Enter Contact Number");
			String contact=scanner.next();
			String query="INSERT INTO RESERVATIONS(GUESTNAME,ROOMNO,CONTACT)VALUES('"+guest+"',"+room+",'"+contact+"')";
			Statement stmt=con.createStatement();
			int count=stmt.executeUpdate(query);
			if(count>0)
			{
				System.out.println("----------------RESERVATION DONE SUCCESSFULLY-------------");
				
			}
			else
			{
				System.out.println("--------------RESERVATION FAIL----- TRY AGAIN---------------");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//Method for view reservation
	private static void viewReservations(Connection con)
	{
		
			String query="SELECT ID,GUESTNAME,ROOMNO,CONTACT,DATE FROM RESERVATIONS";
			try
			{
				Statement stmt=con.createStatement();
				ResultSet rs=stmt.executeQuery(query);
				System.out.println("================Current Resverations============");
				System.out.println("+-----------------------------------------------------------+");
				System.out.println("|   R_ID  |   GUEST NAME  |  ROOM NO  |  CONTACT   |  DATE  |");
				System.out.println("+-----------------------------------------------------------+");
				while(rs.next())
				{
					int id=rs.getInt("ID");
					String guest=rs.getString("GUESTNAME");
					int room=rs.getInt("ROOMNO");
					String contact=rs.getString("CONTACT");
				    String date=rs.getTimestamp("DATE").toString();
				    
				    System.out.printf("| %-14d | %-15s | %-13d | %20s | %-19s |\n",id,guest,room,contact,date);
				}
				System.out.println("+----------+------------+-------------+-----------+---------------+------------+");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}	
	}
	//Method for getting room numbers
	private static void getRoomNumber(Connection con,Scanner scanner)
	{
		try
		{
			System.out.println("Enter Reservation id: ");
			int id=scanner.nextInt();
			System.out.println("Enter the Guest Name:");
			String guest=scanner.next();
			//scanner.nextLine();
			String query="SELECT ROOMNO FROM RESERVATIONS"+" WHERE ID="+id+" AND GUESTNAME='"+guest+"'";
			Statement stmt =con.createStatement();
			ResultSet rs=stmt.executeQuery(query);
			if(rs.next())
			{
				int room=rs.getInt("ROOMNO");
				System.out.println("ROOM NO FOR GIVEN RESERVATIONS ID "+id+"And Guest Name:"+" "+guest+"ROOM NO is"+room);
			}
			else
			{
				System.out.println("----RESERVATIONS NOT FOUND FOR GIVEN ID:"+id+"AND GUEST NAME:"+ guest);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	// method for update the reservation
	private static void updateReservation(Connection con, Scanner scanner)
	{
		try
		{
			System.out.println("Enter Reservation ID to Update:");
			int id=scanner.nextInt();
			scanner.nextLine();//consume the newline character
			if(!reservationExists(con,id))
			{
				System.out.println("------RESERVATION NOT FOUND FOR GIVEN ID-----");
				return ;
			}
			System.out.println("Enter new Guest Name:");
			String newGuest=scanner.nextLine();
			System.out.println("Enter new room number:");
			int newRoom=scanner.nextInt();
			System.out.println("Enter new Contact number:");
			String newContact=scanner.next();
			
			String query="UPDATE RESERVATIONS SET GUESTNAME='"+newGuest+"',ROOMNO ="+newRoom+","+ "CONTACT ='"+newContact+"'WHERE ID="+id;
			Statement stmt=con.createStatement();
			int count=stmt.executeUpdate(query);
			if(count>0)
			{
				System.out.println("----RESERVATION UPDATE SUCCESSFULLY--------");
			}
			else
			{
				System.out.println("--------RESERVATION UPDATE FAILED-----");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//Method for delete reservation
	private static void deleteReservation(Connection con,Scanner scanner)
	{
		try
		{
			System.out.println("Enter Reservation ID to Delete Reservation :");
			int id=scanner.nextInt();
			if(!reservationExists(con,id))
			{
				System.out.println("---- RESERVATION NOT FOUND FOR GIVEN ID -----");
				return;
			}
			String query ="DELETE FROM RESERVATIONS WHERE ID="+id;
			Statement stmt=con.createStatement();
			int count =stmt.executeUpdate(query);
			if (count >0)
			{
				System.out.println("---- RESERVATION DELETED SUCCESSFULLY ----");
			}
			else
			{
				System.out.println("--- RESERVATION DELETION FAILED ----");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Method for checking weather reservation exist
	private static boolean reservationExists(Connection con ,int id)
	{
		try
		{
			String query="SELECT ID FROM RESERVATIONS WHERE ID="+id;
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(query);
			return rs.next(); // if there's a result the reservation exits
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false; // handle the database errors as needed
		}
	}

	// method for existing the reservation
	public static void exit() throws InterruptedException
	{
		System.out.println("------EXITING THE SYSTEM-------");
		int i=5;
		while(i!=0)
		{
			System.out.println(".");
			Thread.sleep(2000);
			i--;
		}
		System.out.println();
		System.out.println("THANK YOU FOR USING HOTEL RESERVATION SYSTEM!!!!---");
	}




}

