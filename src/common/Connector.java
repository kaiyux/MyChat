package common;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import objects.User;

public class Connector {

	public static String serverAddress = "172.20.10.8";

	public boolean checkLogin(User account) {
		// 声明Connection对象
		Connection con;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名MyChat
		String url = "jdbc:mysql://" + serverAddress + ":3306/MyChat";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "xie";
		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// 2.创建statement类对象，用来执行SQL语句
			Statement statement = con.createStatement();
			// 要执行的SQL语句
			String sql = "select email,password from user";
			// 3.ResultSet类，用来存放获取的结果集
			ResultSet rs = statement.executeQuery(sql);
			// System.out.println("-----------------");
			// System.out.println("执行结果如下所示:");
			// System.out.println("-----------------");
			// System.out.println("姓名" + "\t" + "职称");
			// System.out.println("-----------------");

			// String username = null;
			// String introduce = null;
			// String gender = null;
			// String location = null;
			// String lastloginIP = null;
			// String lastloginTime = null;
			// String registrationTime = null;
			// String status = null;
			boolean check = false;
			while (rs.next()) {
				// 检查用户ID
				if (account.getEmail().compareTo(rs.getString("email")) == 0) {
					// 检查用户密码
					if (account.getPassword().compareTo(rs.getString("password")) == 0) {
						System.out.println("Succeeded logging in");
						check = true;
						return check;
					}
				}
			}
			// introduce = rs.getString("introduce");
			// gender = rs.getString("gender");
			// location = rs.getString("location");
			// lastloginIP = rs.getString("lastloginIP");
			// lastloginTime = rs.getString("lastloginTime");
			// registrationTime = rs.getString("registrationTime");
			// status = rs.getString("status");
			// //输出结果
			// System.out.println(
			// email + "\t" + username + "\t" + password + "\t" + introduce + "\t" + gender
			// + "\t" + location
			// + "\t" + lastloginIP + "\t" + lastloginTime + "\t" + registrationTime + "\t"
			// + status);

			rs.close();
			con.close();
			return check;
		} catch (ClassNotFoundException e) {
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库数据成功获取！！");
		}
		return false;
	}

	public void logIn(User account) {
		// 声明Connection对象
		Connection con;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名MyChat
		String url = "jdbc:mysql://" + serverAddress + ":3306/MyChat";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "xie";
		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// 要执行的SQL语句
			String sql = "INSERT INTO Online (loginIP,email) VALUES(?,?);";
			try {
				PreparedStatement preStmt = con.prepareStatement(sql);
				preStmt.setString(1, account.getLoginIP());
				preStmt.setString(2, account.getEmail());
				preStmt.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con.close();
		} catch (ClassNotFoundException e) {
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库数据成功获取！！");
		}
	}

	public void logOut(User account) {
		// 声明Connection对象
		Connection con;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名MyChat
		String url = "jdbc:mysql://" + serverAddress + ":3306/MyChat";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "xie";
		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// 要执行的SQL语句
			account.getLoginIP();
			// 登出后将信息从在线表中删除
			String sql = "delete from online where loginIP = '" + account.getLoginIP() + "';";
			Statement statement = con.createStatement();
			statement.executeUpdate(sql);
			con.close();
		} catch (ClassNotFoundException e) {
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库数据成功获取！！");
		}
	}

	public void signUp(User account) {
		// 声明Connection对象
		Connection con;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名MyChat
		String url = "jdbc:mysql://" + serverAddress + ":3306/MyChat";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "xie";
		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库
			con = DriverManager.getConnection(url, user, password);
			if (!con.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// 要执行的SQL语句
			String sql = "INSERT INTO User (email,password,username,introduce,gender) VALUES(?,?,?,?,?);";
			try {
				PreparedStatement preStmt = con.prepareStatement(sql);
				preStmt.setString(1, account.getEmail());
				preStmt.setString(2, account.getPassword());
				preStmt.setString(3, account.getUsername());
				preStmt.setString(4, account.getIntroduce());
				preStmt.setString(5, account.getGender());
				preStmt.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con.close();
		} catch (ClassNotFoundException e) {
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库数据成功获取！！");
		}
	}
}