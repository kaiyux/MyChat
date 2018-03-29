package objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;

import common.Connector;
import common.Get_IPv4_Address;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.control.Label;

public class Login extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Login");// 设置窗体的标题
		// primaryStage.initStyle(StageStyle.UNIFIED);//隐藏默认标题栏

		GridPane grid = new GridPane();// 创建一个GridPane面板，并给其命名为grid。
		grid.setAlignment(Pos.CENTER);// 改变grid的默认位置，默认情况下，grid的位置是在其父容器的左上方，此处父容器是Scene，现在将grid移至Scene的中间。
		grid.setHgap(10);// 设置该网格每行和每列之间的水平间距和垂直间距。
		grid.setVgap(10);
		// 设置了环绕在该网格面板上的填充距离，这里网格默认被设为在场景容器中居中，这里的填充距离是表示网格边缘距离里面内容的距离。
		// 传入的是一个Insets对象，该insets对象的参数是：上，左，下，右。
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label userID = new Label("User ID:");
		grid.add(userID, 0, 1);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 2);

		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);

		Button btn = new Button("Sign in");
		Button btn2 = new Button("Sign up");
		HBox hbBtn = new HBox(10);// 创建了一个HBox布局面板，并初始放置在其中的每个子组件的水平间距为10像素
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);// 设置该面板的相对于父容器的位置为右下
		hbBtn.getChildren().addAll(btn, btn2);// 将按钮作为子元素添加至其中
		grid.add(hbBtn, 1, 4);

		Scene scene = new Scene(grid, 400, 350);// 为该舞台创建一个场景
		grid.setId("pane");
		// scene.getStylesheets().addAll(this.getClass().getResource("../Login.css").toExternalForm());
		// scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
		primaryStage.setScene(scene);// 将场景加入舞台中

		primaryStage.show();// 让窗体显示

		btn.setOnAction(new EventHandler<ActionEvent>() {
			// 登陆按钮

			@Override
			public void handle(ActionEvent event) {
				// String id = userTextField.getText();
				// String pw = pwBox.getText();
				Connector cn = new Connector();
				User user = new User();
				user.setEmail(userTextField.getText());
				user.setPassword(pwBox.getText());
				user.setLoginIP(Get_IPv4_Address.getIPv4().getHostAddress());
				if (cn.checkLogin(user)) {
					cn.logIn(user); // 在数据库中插入此登陆者信息
					primaryStage.close(); // 准备页面跳转
					ChatController newchat = new ChatController();
					try {
						newchat.start(primaryStage);

						primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
							@Override
							public void handle(WindowEvent event) {

								// 保存聊天记录
								Iterator iterator = newchat.set.iterator();
								String historyPath = "D:\\MyChatDownload\\chat_history.txt";
								File historyFile = new File(historyPath);
								if (!historyFile.exists()) {
									try {
										historyFile.createNewFile();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								FileWriter fw = null;
								BufferedWriter writer = null;
								try {
									fw = new FileWriter(historyFile);
									writer = new BufferedWriter(fw);
									while (iterator.hasNext()) {
										writer.write(iterator.next().toString());
										writer.newLine();// 换行
									}
									writer.flush();
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								} finally {
									try {
										writer.close();
										fw.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}

								// 删除数据库online表中此用户
								try {
									byte b[] = ("/sendToServer" + "$" + "logOut").getBytes();
									InetAddress serverAddress = InetAddress.getByName("172.20.10.8"); // 输入服务器IP地址
									DatagramPacket data = new DatagramPacket(b, b.length, serverAddress, 8000);
									DatagramSocket mail = new DatagramSocket();
									mail.send(data); // 发出消息
									mail.close();
								} catch (Exception e) {
								}
								cn.logOut(user);
								System.out.println("Client closed");
								// 结束所有线程
								System.exit(0);
							}
						});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
					System.out.println("用户名或密码错误！");
			}
		});

		btn2.setOnAction(new EventHandler<ActionEvent>() {
			// 注册按钮

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				primaryStage.close(); // 准备页面跳转
				Signup signup = new Signup();
				try {
					signup.start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
