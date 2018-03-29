package objects;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import common.CreateFileUtil;
import common.Time;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChatController extends Application implements Runnable {
	static Window window = new Window();
	public static String sa = "172.20.10.8";
	public static Set set = new HashSet<String>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DatagramPacket pack = null;
		DatagramSocket mail = null;
		byte b[] = new byte[8192];
		while (true) {
			try {
				pack = new DatagramPacket(b, b.length);
				mail = new DatagramSocket(8888);
				System.out.println("开始监听8888端口");
				mail.receive(pack);
				System.out.println("8888端口收到！");
				String message = new String(pack.getData(), 0, pack.getLength());

				// 将message中消息类型code取出
				int temp = 0;
				char getContent[] = message.toCharArray();
				for (int i = 1; i < getContent.length; i++) {
					if (getContent[i] != '$')
						continue;
					else {
						temp = i;
						break;
					}
				}
				String code = new String(pack.getData(), 1, temp - 1); // 消息类型code
				// 将message中消息内容取出
				String messageReceived = new String(getContent, temp + 1, getContent.length - temp - 1);
				System.out.println(code);
				System.out.println(messageReceived);
				if (code.compareTo("login") == 0) {
					// 此消息为提示有人登陆
					Platform.runLater(() -> {
						getonLine(); // 更新onLine窗口为数据库online表内容
					});
				} else if (code.compareTo("file") == 0) {
					// 此消息为文件消息
					System.out.println("收到文件");
					ClientReceiveFile receiveFile = new ClientReceiveFile();
					Thread receiveThread = new Thread(receiveFile);
					receiveThread.start();
				} else {
					// 此消息为文字消息
					String time = Time.showTime_HMS();
					InetAddress getAddress = pack.getAddress();
					String history = time + getAddress + "：" + messageReceived;
					Platform.runLater(() -> {
						window.textOutput.appendText(time + "\n");
						window.textOutput.appendText("收到的数据来自" + getAddress);
						window.textOutput.appendText("\n收到数据：" + messageReceived + "\n\n");
					});
					set.add(history); // 将此消息加入历史记录
				}
				mail.close();
				// window.textOutput.setCaretPosition(window.textOutput.getText().length());
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		String dirName = "D:/MyChatDownload";
		String dirName2 = "D:/MyChatDownload/temp";
		CreateFileUtil.createDir(dirName);
		CreateFileUtil.createDir(dirName2);

		// 启动主窗口
		window.start(primaryStage);
		// 启动接收消息线程
		ChatController chat = new ChatController();
		Thread thread = new Thread(chat);
		thread.start();

		// 发送第一条消息给服务器，告诉服务器此用户已登陆
		sendFstMsg();

		window.send.setOnAction(new EventHandler<ActionEvent>() { // 发送消息
			@Override
			public void handle(ActionEvent event) {
				try {
					String userIP = window.userIP.getText().trim();
					InetAddress hisAddress = InetAddress.getByName(userIP); // 获取对方IPv4地址
					byte b[] = (hisAddress + "$" + window.textInput.getText().trim()).getBytes();

					// System.out.println(address.getHostAddress());
					InetAddress serverAddress = InetAddress.getByName(sa); // 输入服务器IP地址
					DatagramPacket data = new DatagramPacket(b, b.length, serverAddress, 8000);
					DatagramSocket mail = new DatagramSocket();
					mail.send(data); // 发出消息
					System.out.println("Client sent message!");
					String time = Time.showTime_HMS();
					Platform.runLater(() -> {
						window.textOutput.appendText(time + "\n");
						window.textOutput.appendText("发出数据：" + window.textInput.getText().trim() + "\n\n");
						window.textInput.setText(""); // 清空输入框
					});
					String history = time + " " + window.textInput.getText().trim();
					mail.close();

					set.add(history); // 将此消息加入历史记录
				} catch (Exception e) {
				}
			}
		});

		window.file.setOnAction(new EventHandler<ActionEvent>() { // 发送文件

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				FileChooser fileChooser = new FileChooser();
				Stage stage = null;
				fileChooser.setTitle("Open Resource File");
				File file = fileChooser.showOpenDialog(stage);
				String filePath = null;
				filePath = file.getPath();
				System.out.println("已选择文件：" + filePath);
				DataInputStream dis = null;
				try {
					dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				byte b[] = new byte[10240];
				InetAddress serverAddress = null;
				try {
					serverAddress = InetAddress.getByName(sa); // 输入服务器IP地址
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DatagramSocket mail = null;
				try {
					mail = new DatagramSocket();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					String userIP = window.userIP.getText().trim();
					InetAddress hisAddress = InetAddress.getByName(userIP); // 获取对方IPv4地址
					byte a[] = ("/sendFile" + "$" + hisAddress).getBytes();
					// InetAddress serverAddress = InetAddress.getByName(sa); // 输入服务器IP地址
					DatagramPacket data = new DatagramPacket(a, a.length, serverAddress, 8000);
					// DatagramSocket mail = new DatagramSocket();
					mail.send(data); // 发出消息
					System.out.println("Client sent fileMessage!");
				} catch (Exception e) {
				}
				byte[] buf = new byte[100];
				OutputStream os = null;
				FileInputStream fins = null;
				try {
					ServerSocket ss = new ServerSocket(12345);
					Socket s = ss.accept();
					os = s.getOutputStream();
					String fileName = file.getName();
					System.out.println("将文件:" + fileName + "传输过去");
					// 先将文件名传输过去
					os.write(fileName.getBytes());
					String time = Time.showTime_HMS();
					window.textOutput.appendText(time + "\n");
					window.textOutput.appendText("开始传输文件" + filePath + "\n");
					// 将文件传输过去
					// 获取文件
					fins = new FileInputStream(filePath);
					int data;
					// 通过fins读取文件，并通过os将文件传输
					while ((data = fins.read()) != -1) {
						os.write(data);
					}
					System.out.println("文件传输结束");
					window.textOutput.appendText("已发送文件：" + filePath + "\n\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (fins != null)
							fins.close();
						if (os != null)
							os.close();
						// s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		window.btn1.setOnAction(new EventHandler<ActionEvent>() { // 设置按钮

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Settings setting = new Settings();
				Stage settingStage=new Stage();
				try {
					setting.start(settingStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void sendFstMsg() {
		// 登陆后自动发送第一条消息给服务器，以广播此用户上线并更新数据库内容
		InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getByName(sa); // 输入服务器IP地址
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte logIn[] = ("/sendToServer" + "$" + " log in").getBytes();
		DatagramPacket data = new DatagramPacket(logIn, logIn.length, serverAddress, 8000);
		DatagramSocket firstMail = null;
		try {
			firstMail = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			firstMail.send(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Client sent first mail!");
		String time = Time.showTime_YMD();
		Platform.runLater(() -> {
			window.textOutput.appendText(time + "\n");
		});
		firstMail.close();
	}

	private void getonLine() {
		// TODO Auto-generated method stub
		// 声明Connection对象
		Connection con;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名MyChat
		String url = "jdbc:mysql://" + sa + ":3306/MyChat";
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
			String sql = "select email,loginIP from online";
			// 3.ResultSet类，用来存放获取的结果集
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String email = new String(rs.getString("email"));
				window.onLine.appendText(email + "\n");
			}
			rs.close();
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

	class ClientReceiveFile implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			byte[] buf = new byte[100];
			Socket s = new Socket();
			try {
				// 建立连接
				s.connect(new InetSocketAddress("127.0.0.1", 6666), 6000);
				InputStream is = s.getInputStream();
				// 接收传输来的文件名
				int len = is.read(buf);
				String fileName = new String(buf, 0, len);
				System.out.println(fileName);
				// 接收传输来的文件
				String filePath = "D:\\MyChatDownload\\" + fileName;
				FileOutputStream fos = new FileOutputStream(filePath);
				int data;
				while (-1 != (data = is.read())) {
					fos.write(data);
				}
				is.close();
				s.close();
				String time = Time.showTime_HMS();
				window.textOutput.appendText(time + "\n");
				window.textOutput.appendText("收到文件：" + filePath + "\n\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
