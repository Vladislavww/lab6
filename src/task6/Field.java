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
	private boolean paused;
	private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
	
	// ����� ������ �������� �� ���������� ��������� ������� ActionEvent
	// ��� �������� ��� ���������� ������������ ��������� �����,
	// ����������� ��������� ActionListener
	private int num = 0;
	private Timer repaintTimer = new Timer(1000, new ActionListener(){
	public void actionPerformed(ActionEvent ev){
		// ������ ����������� ������� ActionEvent - ����������� ����
		repaint();
		}
	});
	private static final int BRICKS_IN_WIDTH = 20;
	private static final int BRICKS_IN_HEIGHT = 10; //����������� �������� �� ����� � ������
	private BrickClass[][] bricks = new BrickClass[BRICKS_IN_WIDTH][BRICKS_IN_HEIGHT];
	private boolean added_bricks = false;//���� ������� ��������

	public Field() {
		setBackground(Color.WHITE);
		repaintTimer.start();
	}
	// �������������� �� JPanel ����� ����������� ����������
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D canvas = (Graphics2D) g;
		//����������� ��� �������
		if(added_bricks){
			for(int i=0;i<BRICKS_IN_WIDTH; i++){
				for(int j=0; j<BRICKS_IN_HEIGHT; j++){
					bricks[i][j].paint(canvas);
				}
			}
		}
		// ��������������� ��������� ���������� �� ���� ����� �� ������
		for (BouncingBall ball: balls){
			ball.paint(canvas);
		}
	}
	// ����� ���������� ������ ���� � ������
	public void addBall(){
		//����������� � ���������� � ������ ������ ���������� BouncingBall
		// ��� ������������� ���������, ��������, �������, �����
		// BouncingBall ��������� ��� � ������������
		if(added_bricks){
			balls.add(new BouncingBall(this, bricks, BRICKS_IN_WIDTH, BRICKS_IN_HEIGHT));
		}
		else{
			balls.add(new BouncingBall(this, bricks, -1, -1));
		}
	}
	
	//����� �������� ��������-������� � �������
	public void addBrick(){
		for(int i=0; i<BRICKS_IN_WIDTH; i++){
			for(int j=0; j<BRICKS_IN_HEIGHT; j++){
				BrickClass brick = new BrickClass(this, i, j);
				bricks[i][j] = brick;
				
			}
		}
		added_bricks = true;
		
	}
	// ����� ������������������, �.�. ������ ���� ����� �����
	// ������������ ���� ������
	public synchronized void pause(){
		// �������� ����� �����
		paused = true;
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
