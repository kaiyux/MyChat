package objects;

import javafx.scene.control.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author novo
 */
public class Window extends Application {

	public Button btn1 = new Button();
	public Button btn2 = new Button();
	public Button btn3 = new Button();
	public Button btn4 = new Button();
	public Button btn5 = new Button();

	public TextArea textOutput = new TextArea();
	public TextArea textInput = new TextArea();
	public TextArea onLine = new TextArea();
	public TextField userIP = new TextField();
	public Button send = new Button("Send");
	public Button file = new Button("File");

	/**
	 * 设置根节点布局
	 * 
	 * @return 返回布局
	 */
	public BorderPane setBorder() {
		BorderPane layout = new BorderPane();
		// layout.setTop(new Rectangle(200, 50, Color.DARKCYAN));
		// layout.setBottom(new Rectangle(200, 50, Color.DARKCYAN));
		// layout.setCenter(new Rectangle(100, 100, Color.MEDIUMAQUAMARINE));
		// layout.setLeft(new Rectangle(50, 100, Color.DARKTURQUOISE));
		// layout.setRight(new Rectangle(50, 100, Color.DARKTURQUOISE));
		return layout;
	}

	/**
	 * 设置顶部
	 * 
	 * @param layout
	 *            传入布局
	 */
	public void settop(BorderPane layout) {
		HBox hbox = new HBox();
		// hbox.setPadding(new Insets(15, 12, 15, 12));
		// hbox.setSpacing(10);
		// hbox.setStyle("-fx-background-color: #336699");
		// Label toplabel = new Label("XXXX科技有限公司");
		// toplabel.setFont(new Font(35));
		// toplabel.setTextFill(Color.BLACK);
		// hbox.getChildren().add(toplabel);

		VBox vbox = new VBox();
		Separator sper = new Separator();
		sper.setOrientation(Orientation.HORIZONTAL);
		// sper.setStyle(" -fx-background-color: #e79423;-fx-background-radius: 2;");
		vbox.getChildren().addAll(hbox, sper);

		layout.setTop(vbox);
	}

	/**
	 * 设置左边部分
	 * 
	 * @param layout
	 *            传入的布局
	 */
	public void setLeft(BorderPane layout) {
		// 第一个根节点
		VBox vb1 = new VBox(5);
		vb1.setPadding(new Insets(15, 12, 15, 12));
		btn1.setText("Setting");
		btn1.setPrefSize(150, 20);

		btn2.setText("B");
		btn2.setPrefSize(150, 20);

		btn3.setText("C");
		btn3.setPrefSize(150, 20);

		btn4.setText("D");
		btn4.setPrefSize(150, 20);

		btn5.setText("E");
		btn5.setPrefSize(150, 20);
		onLine.setPrefColumnCount(5);
		onLine.setPrefHeight(450);

		HBox btngroup = new HBox(5);
		btngroup.getChildren().addAll(btn1);
		btngroup.setPrefWidth(5);
		Label tip1 = new Label("在此输入接收方IP：");
		Label tip2 = new Label("当前在线：");

		vb1.getChildren().addAll(tip1, userIP, tip2, onLine, btngroup);

		HBox hb = new HBox();
		Separator sper = new Separator();
		sper.setOrientation(Orientation.VERTICAL);
		// sper.setStyle(" -fx-background-color: #e79423;-fx-background-radius: 2;");
		hb.getChildren().addAll(vb1, sper);

		// 将树形节点加如布局的左边
		layout.setLeft(hb);

	}

	/**
	 * 设置右边部分
	 * 
	 * @param layout
	 *            传入的布局
	 */
	public void setRight(BorderPane layout) {

		BorderPane rightPart = setBorder();
		// 将右边部分分为Chat主窗口和输入两个界面
		setChat(rightPart);
		setInput(rightPart);
		layout.setCenter(rightPart);

		btn1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				textOutput.setText("btn1 pressed");
			}

		});

		btn2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				textOutput.setText("btn2 pressed");
			}

		});
	}

	private void setChat(BorderPane rightPart) {
		// TODO Auto-generated method stub
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.getChildren().add(textOutput);
		rightPart.setCenter(hbox);
	}

	private void setInput(BorderPane rightPart) {
		// TODO Auto-generated method stub
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);

		VBox vbox = new VBox(10);// 创建了一个VBox布局面板，并初始放置在其中的每个子组件的水平间距为10像素
		// vbox.setAlignment(Pos.BOTTOM_RIGHT);// 设置该面板的相对于父容器的位置为右下
		send.setPrefSize(50, 20);
		file.setPrefSize(50, 20);
		vbox.getChildren().addAll(send, file);// 将按钮作为子元素添加至其中

		hbox.getChildren().addAll(textInput, vbox);
		rightPart.setBottom(hbox);
	}

	/**
	 * 树节点1的第一个按钮的页面设置
	 * 
	 * @return
	 * 
	 * @return 返回一个竖直方向的布局
	 */
	public static class Message {

	}

	@Override
	public void start(Stage primaryStage) {
		BorderPane root = setBorder();
		settop(root);
		setLeft(root);
		setRight(root);
		Scene scene = new Scene(root, 800, 600);
//		root.setId("pane");
//		scene.getStylesheets().addAll(this.getClass().getResource("../Login.css").toExternalForm());
		primaryStage.setTitle("MyChat");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}