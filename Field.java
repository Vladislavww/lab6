package task6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.Timer;
@SuppressWarnings("serial")

public class Field extends JPanel{
	// ���� ������������������ ��������
	private boolean paused;
	// ������������ ������ �������� �����
	private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
	private LinkedList<BrickClass> bricks = new LinkedList<BrickClass>();
	// ����� ������ �������� �� ���������� ��������� ������� ActionEvent
	// ��� �������� ��� ���������� ������������ ��������� �����,
	// ����������� ��������� ActionListener
	private int num = 0;
	private Timer repaintTimer = new Timer(10, new ActionListener(){
	public void actionPerformed(ActionEvent ev){
		// ������ ����������� ������� ActionEvent - ����������� ����
		repaint();
		}
	});
	private static final int BRICKS_IN_WIDTH = 3;
	private static final int BRICKS_IN_HEIGHT = 1; //����������� �������� �� ����� � ������
	// ����������� ������ BouncingBall
	public Field() {
		// ���������� ���� ������� ���� �����
		setBackground(Color.WHITE);
		// ��������� ������
		repaintTimer.start();
	}
	// �������������� �� JPanel ����� ����������� ����������
	public void paintComponent(Graphics g){
		// ������� ������ ������, �������������� �� ������
		super.paintComponent(g);
		Graphics2D canvas = (Graphics2D) g;
		// ��������������� ��������� ���������� �� ���� ����� �� ������
		
		for (BrickClass brick: bricks){
			brick.paint(canvas);
		}
		for (BouncingBall ball: balls){
			ball.paint(canvas);
		}
	}
	// ����� ���������� ������ ���� � ������
	public void addBall(){
		//����������� � ���������� � ������ ������ ���������� BouncingBall
		// ��� ������������� ���������, ��������, �������, �����
		// BouncingBall ��������� ��� � ������������
		balls.add(new BouncingBall(this, bricks));
	}
	public void addBrick(){
		for(int i=0; i<BRICKS_IN_WIDTH; i++){
			for(int j=0; j<BRICKS_IN_HEIGHT; j++){
				//System.out.println('+');
				bricks.add(new BrickClass(this, i, j));
				
			}
		}
		
	}
	// ����� ������������������, �.�. ������ ���� ����� �����
	// ������������ ���� ������
	public synchronized void pause(){
		// �������� ����� �����
		paused = true;
		System.out.println(num);
	}
	// ����� ������������������, �.�. ������ ���� ����� �����
	// ������������ ���� ������
	public synchronized void resume(){
		// ��������� ����� �����
		paused = false;
		// ����� ��� ��������� ����������� ������
		notifyAll();
	}
	// ������������������ ����� ��������, ����� �� ��� ���������
	// (�� ������� �� ����� �����?)
	public synchronized void canMove(BouncingBall ball) throws InterruptedException{
		if (paused){
			// ���� ����� ����� �������, �� �����, ��������
			// ������ ������� ������, ��������
			wait();
		}
	}
}
