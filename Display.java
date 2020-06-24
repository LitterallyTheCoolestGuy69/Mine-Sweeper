import javax.swing.JFrame;

public class Display {
	public static void main(String[] args) {
		JFrame screen = new JFrame();
		screen.setSize(510,600);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setResizable(false);
		screen.setVisible(true);
		Field field = new Field();
		screen.addKeyListener(field);
		screen.addMouseListener(field);
		screen.add(field);
	}
}
