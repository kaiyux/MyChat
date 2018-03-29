package common;

import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Server extends JFrame implements Runnable, ActionListener {
	public static String newHisIP = null;
	JTextField outMessage = new JTextField(12);
	JTextArea inMessage = new JTextArea(12, 20);
	JButton b = new JButton("发送数据");

	Server() {
		super("Server");
		setSize(320, 200);
		setVisible(true);
		JPanel p = new JPanel();
		b.addActionListener(this);
		p.add(outMessage);
		p.add(b);
		Container con = getContentPane();
		con.add(new JScrollPane(inMessage), BorderLayout.CENTER);
		con.add(p, BorderLayout.NORTH);
		Thread thread = new Thread(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		validate();
		thread.start();
		ReceiveFile receive = new ReceiveFile();
		Thread receiveThread = new Thread(receive);
		receiveThread.start();
	}

	public void actionPerformed(ActionEvent event) {
		byte b[] = outMessage.getText().trim().getBytes();
		try {
			InetAddress address = InetAddress.getByName("172.20.10.6");
			DatagramPacket data = new DatagramPacket(b, b.length, address, 8888);
			DatagramSocket mail = new DatagramSocket();
			mail.send(data);
			mail.close();
		} catch (Exception e) {
		}
	}

	public void run() {
		DatagramPacket pack = null;
		DatagramSocket mail = null;
		byte b[] = new byte[8192];
		try {
			pack = new DatagramPacket(b, b.length);
			mail = new DatagramSocket(8000);
		} catch (Exception e) {
		}
		while (true) {
			try {
				mail.receive(pack);
				System.out.println("Server received pack!");
				String message = new String(pack.getData(), 0, pack.getLength());
				// 将message中消息接收者ip取出
				int temp = 0;
				char getIp[] = message.toCharArray();
				for (int i = 1; i < getIp.length; i++) {
					if (getIp[i] != '$')
						continue;
					else {
						temp = i;
						break;
					}
				}
				String hisIp = new String(pack.getData(), 1, temp - 1); // 消息接收者ip
				// 将message中消息内容取出
				String messageReceived = new String(getIp, temp + 1, getIp.length - temp - 1);
				System.out.println("hisIp:" + hisIp);
				System.out.println("messageReceived:" + messageReceived);
				if (hisIp.compareTo("sendToServer") == 0) {
					// 如果此消息发给服务器
					System.out.println("此消息发给服务器");
					if (messageReceived.compareTo("logOut") == 0)
						inMessage.append(pack.getAddress() + " has logged out.\n\n");
					else {
						inMessage.append(pack.getAddress() + " has logged in.\n\n");
						showOnLine(); // 给所有在线用户发消息 告诉他们有用户登陆
					}
					System.out.println("Server received message!");
				} else if (hisIp.compareTo("sendFile") == 0) {
					// 如果此消息包含文件
					System.out.println("此消息包含文件");
					// byte a[] = ("/file").getBytes();
					char getIP[] = messageReceived.toCharArray();
					newHisIP = new String(getIP, 1, getIP.length - 1); // 消息接收者ip
					System.out.println("newHisIP:" + newHisIP);

					byte[] buf = new byte[100];
					Socket s = new Socket();
					try {
						// 建立连接
						s.connect(new InetSocketAddress("127.0.0.1", 12345), 54321);
						InputStream is = s.getInputStream();
						// 接收传输来的文件名
						int len = is.read(buf);
						String fileName = new String(buf, 0, len);
						System.out.println("fileName:" + fileName);
						// 接收传输来的文件
						String filePath = "D:\\MyChatDownload\\temp\\" + fileName;
						System.out.println("filePath:" + filePath);
						FileOutputStream fos = new FileOutputStream(filePath);
						int data;
						while (-1 != (data = is.read())) {
							fos.write(data);
						}
						is.close();
						s.close();

						byte fileMessage[] = ("/file$").getBytes();
						try {
							InetAddress address = InetAddress.getByName("172.20.10.8");
							DatagramPacket fileData = new DatagramPacket(fileMessage, fileMessage.length, address,
									8888);
							DatagramSocket fileMail = new DatagramSocket();
							fileMail.send(fileData);
							fileMail.close();
						} catch (Exception e) {
						}

						OutputStream os = null;
						FileInputStream fins = null;
						try {
							ServerSocket ss = new ServerSocket(6666);
							Socket send = ss.accept();
							os = send.getOutputStream();
							// 文件路径
							// String filePath = "D:\\files\\课表.jpg";
							// 文件名
							System.out.println("将文件名:" + fileName + "传输过去");
							// 先将文件名传输过去
							os.write(fileName.getBytes());
							System.out.println("开始传输文件");
							// 将文件传输过去
							// 获取文件
							fins = new FileInputStream(filePath);
							int dataForward;
							// 通过fins读取文件，并通过os将文件传输
							while ((data = fins.read()) != -1) {
								os.write(data);
							}
							System.out.println("文件传输结束");
							
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					// 如果此消息发给其他用户
					System.out.println("此消息为文字消息");
					inMessage.append("收到的数据来自" + pack.getAddress());
					inMessage.append("\n收到的数据是" + messageReceived + "\n\n");
					inMessage.setCaretPosition(inMessage.getText().length());
					b = (" message $" + messageReceived).getBytes();
					DatagramPacket data = new DatagramPacket(b, b.length, InetAddress.getByName(hisIp), 8888);
					mail.send(data);
					System.out.println("Server forward mail!");
				}
			} catch (Exception e) {
			}
		}
	}

	public void showOnLine() { // 将数据库online表内容逐一发给所有用户
		// 声明Connection对象
		Connection con;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名MyChat
		String url = "jdbc:mysql://localhost:3306/MyChat";
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
				byte b[] = ("/login" + "$" + rs.getString("loginIP")).getBytes();
				try {
					String loginIP = new String(rs.getString("loginIP"));
					System.out.println(loginIP);
					InetAddress address = InetAddress.getByName(loginIP);
					DatagramPacket data = new DatagramPacket(b, b.length, address, 8888);
					DatagramSocket mail = new DatagramSocket();
					mail.send(data);
					mail.close();
				} catch (Exception e) {
				}
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

	public static void main(String[] args) {
		new Server();
	}

	class ReceiveFile implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("ReceiveFile线程开始");
			DatagramSocket file = null;
			DatagramSocket forwardFile = null;
			try {
				file = new DatagramSocket(6000);
				forwardFile = new DatagramSocket();
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int i = 0;
			while (i == 0) {// 无数据，则循环
				byte a[] = new byte[10240];
				DatagramPacket data = new DatagramPacket(a, a.length);
				try {
					System.out.println("开始监听6000端口");
					file.receive(data);
					System.out.println("6000端口收到！");
					InetAddress sendTo = InetAddress.getByName(newHisIP);
					DatagramPacket forwardData = new DatagramPacket(a, a.length, sendTo, 6666);
					forwardFile.send(forwardData);
					System.out.println("6666端口转发！");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i = data.getLength();
				// 接收数据
				if (i > 0) {
					// 指定接收到数据的长度，可使接收数据正常显示，开始时很容易忽略这一点
					i = 0;// 循环接收
				}
			}
			// file.close();
		}
	}

}
