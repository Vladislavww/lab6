package task6;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
@SuppressWarnings("serial")

public class MainFrame extends JFrame{
	// ���������, �������� ������ ���� ����������, ���� ���
	// �� ���������� �� ���� �����
	private static final int WIDTH = 800;
	private static final int HEIGHT = 860;
	private JMenuItem addBallMenuItem;
	private JMenuItem pauseMenuItem;
	private JMenuItem resumeMenuItem;
	private JMenuItem brickMenuItem;
	private final int MAX_BALLS = 100; //�������� �� �������, ��� ����������� ����� ������������� �����
	private int balls_number = 0;
	// ����, �� �������� ������� ����
	private Field field = new Field();

	public MainFrame(){
		super("���������������� � ������������� �������");
		setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		// �������������� ���� ���������� �� ������
		setLocation((kit.getScreenSize().width - WIDTH)/2,
		(kit.getScreenSize().height - HEIGHT)/2);
		// ���������� ��������� ��������� ���� ����?������ �� ���� �����
		//setExtendedState(MAXIMIZED_BOTH);
		// ������� ����
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu ballMenu = new JMenu("����");
		Action addBallAction = new AbstractAction("�������� ���"){
			public void actionPerformed(ActionEvent event){
				if(balls_number < MAX_BALLS){
					field.addBall();
					balls_number += 1;
					if (!pauseMenuItem.isEnabled() &&!resumeMenuItem.isEnabled()){
						// �� ���� �� ������� ���� �� ��������
						// ���������� - ������� ��������� "�����"
						pauseMenuItem.setEnabled(true);
						brickMenuItem.setEnabled(false); //������ ��� ��������� - ������ ������� �����������
					}
				}
				if(balls_number >= MAX_BALLS){
					addBallMenuItem.setEnabled(false);
				}
			}
		};
		addBallMenuItem = ballMenu.add(addBallAction);
		menuBar.add(ballMenu);
		
		JMenu controlMenu = new JMenu("����������");
		menuBar.add(controlMenu);
		Action pauseAction = new AbstractAction("������������� ��������"){
			public void actionPerformed(ActionEvent event){
				field.pause();
				pauseMenuItem.setEnabled(false);
				resumeMenuItem.setEnabled(true);
			}
		};
		pauseMenuItem = controlMenu.add(pauseAction);
		pauseMenuItem.setEnabled(false);
		Action resumeAction = new AbstractAction("����������� ��������"){
			public void actionPerformed(ActionEvent event){
				field.resume();
				pauseMenuItem.setEnabled(true);
				resumeMenuItem.setEnabled(false);
			}
		};
		resumeMenuItem = controlMenu.add(resumeAction);
		resumeMenuItem.setEnabled(false);
		
		JMenu brickMenu = new JMenu("�������");
		menuBar.add(brickMenu);
		Action addBricksAction = new AbstractAction("�������� �������(�������� �����)"){
			public void actionPerformed(ActionEvent event){
				field.addBrick();
				brickMenuItem.setEnabled(false); //������ ���� ��� ������ ��������
			}
		};
		brickMenuItem = brickMenu.add(addBricksAction);
		
		// �������� � ����� ��������� ���������� ���� Field
		getContentPane().add(field, BorderLayout.CENTER);
		
	}

	public static void main(String[] args){
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}