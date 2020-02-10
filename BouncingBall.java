package task6;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;

public class BouncingBall implements Runnable{
	private static final int MAX_RADIUS = 40;
	private static final int MIN_RADIUS = 3;
	private static final int MAX_SPEED = 10;
	private int BRICKS_IN_WIDTH;//����������
	private int BRICKS_IN_HEIGHT;//����������
	private Field field;
	private int radius;
	private Color color;
	// ������� ���������� ����
	private double x;
	private double y;
	// ������������ � �������������� ���������� ��������
	private int speed;
	private double speedX;
	private double speedY;
	private BrickClass[][] bricks; //������� ��������
	// ����������� ������ BouncingBall
	public BouncingBall(Field field, BrickClass[][] bricks, final int BRICKS_WIDTH, final int BRICKS_HEIGHT){
		// ���������� ����� ������ �� ����, �� �������� ������� ���,
		// ����� ����������� ����� �� ��� ������� 
		// ����� getWidth(), getHeight()
		//����� ���������� ����� ������ �� ������� ����������� ��� ������������ ������������
		this.field = field;
		this.bricks = bricks;
		BRICKS_IN_WIDTH = BRICKS_WIDTH;
		BRICKS_IN_HEIGHT = BRICKS_HEIGHT;
		// ������ ���� ���������� �������
		radius = new Double(Math.random()*(MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
		// ���������� �������� �������� ������� �� �������� ����,
		// ��� �� ������, ��� ���������
		speed = new Double(Math.round(5*MAX_SPEED / radius)).intValue();
		if (speed>MAX_SPEED){
			speed = MAX_SPEED;
		}
		// ��������� ����������� �������� ���� ��������,
		// ���� � �������� �� 0 �� 2PI
		double angle = Math.random()*2*Math.PI;
		// ����������� �������������� � ������������ ���������� ��������
		speedX = 3*Math.cos(angle);
		speedY = 3*Math.sin(angle);
		// ���� ���� ���������� ��������
		color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
		// ��������� ��������� ���� ��������
		do{
			x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
			y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
		}while(brick_collide(true)==true); //���� �����, ����� ���� �� ��������� � ��������
		// �����?� ����� ��������� ������, ��������� ����������
		// ������ �� �����, ����������� Runnable (�.�. �� ����)
		Thread thisThread = new Thread(this);
		// ��������� �����
		thisThread.start();
	}
	
	// ����� run() ����������� ������ ������. ����� �� ��������� ������,
	// �� ����������� � �����
	public void run(){
		try {
			while(true){
				// ������������� ������� �� ����� ������� ����
				// ���� �������� ��������� - ���������� �����
				// ���������� � �����
				// � ��������� ������ - �������� ����� ����?�
				field.canMove(this);
				if(window_collide()==false && brick_collide(false)==false){
					// ������ ���������
					x += speedX;
					y += speedY;
				}
				// �������� �� X �����������, ��� X ������������
				// ������ �� ��������
				// �������� = 1 (��������), �������� �� 15 ��.
				// �������� = 15 (������), �������� �� 1 ��.
				Thread.sleep(16-speed);
			}
		} 
		catch (InterruptedException ex){
			// ���� ��� ��������, �� ������ �� ������
			// � ������ ������� (�����������)
		}
	}
	
	private boolean window_collide(){
		if (x + speedX <= radius){
			// �������� ����� ������, ����������� �����
			speedX = -speedX;
			x = radius;
		}
		else if (x + speedX >= field.getWidth() - radius){
			// �������� ������ ������, ������ �����
			speedX = -speedX;
			x=new Double(field.getWidth()-radius).intValue();
		} 
		else if (y + speedY <= radius){
			// �������� ������� ������
			speedY = -speedY;
			y = radius;
		} 
		else if (y + speedY >= field.getHeight() - radius){
			// �������� ������ ������
			speedY = -speedY;
			y=new Double(field.getHeight()-radius).intValue();
		}
		else{
			return false;
		}
		return true;
	}
	
	private boolean brick_collide(boolean ball_creating){
		boolean collided = false;
		for(int i=0; i<BRICKS_IN_WIDTH; i++){ //�������� �� ���� ��������
			for(int j=0; j<BRICKS_IN_HEIGHT; j++){
				if(bricks[i][j].check_strikable()){
					//���������� ����� 
					double win_x0 = bricks[i][j].get_coordX(0);
					double win_x1 = bricks[i][j].get_coordX(1);
					double win_y0 = bricks[i][j].get_coordY(0);
					double win_y1 = bricks[i][j].get_coordY(1);
					//���������� ��������
					double win_xc = (win_x0 + win_x1)/2;
					double win_yc = (win_y0 + win_y1)/2;
					//��������� ����� ���� � ������ ��������
					double y0 = y + speedY + radius;
					double y1 = y + speedY - radius;
					double x0 = x + speedX + radius;
					double x1 = x + speedX - radius;
					//��������� �� ������������ � �������/������ ��������
					if((x>=win_x0 && x<= win_x1)||(x>=win_x0 && x<= win_x1)){
						if(y0 >= win_y0 && y0 <= win_yc){
							speedY = -speedY;
							y=new Double(win_y0-radius).intValue();
							collided = true;
						}
						else if(y1 <= win_y1 && y1 >= win_yc){
							speedY = -speedY;
							y=new Double(win_y1+radius).intValue();
							collided = true;
						}
					}
					//��������� �� ������������ � �����/������ ��������
					else if((y>=win_y0 && y<= win_y1)||(y>=win_y0 && y<= win_y1)){
						if(x0 >= win_x0 && x0 <= win_xc){
							speedX = -speedX;
							x=new Double(win_x0-radius).intValue();
							collided = true;
						}
						else if(x1 <= win_x1 && x1 >= win_xc){
							speedX = -speedX;
							x=new Double(win_x1+radius).intValue();
							collided = true;
						}
					}
					//��������� �� ������������ � ������
					/*����� ������������� ���������� �� ������ ���� �� ������ �������
					������ �������� ��������� � �������. ����������� 1.4 ������ ��� ���������� ������ ����� �����������
					��� ���������� ������.
					*/
					else if(Math.sqrt(Math.pow(win_yc-y, 2)+Math.pow(win_xc-x, 2))<=(Math.sqrt(Math.pow(win_yc-win_y0, 2)+Math.pow(win_xc-win_x0, 2))+radius)/1.4){
						speedX = -speedX;
						speedY = -speedY;
						x += speedX;
						y += speedY;
						collided = true;
						
					}
					else if(ball_creating == true){
						if(Math.sqrt(Math.pow(win_yc-y, 2)+Math.pow(win_xc-x, 2))<=(Math.sqrt(Math.pow(win_yc-win_y0, 2)+Math.pow(win_xc-win_x0, 2))+radius)*2){
							//����������� ����, ����� ��� ���������� � �����
							return true;
						}

					}
					if(collided){
						if(ball_creating == false){
							bricks[i][j].strike(); //��������� ��������� �������, ���� ��� �� ���� �������� ����
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	// ����� ���������� ������ ����
	public void paint(Graphics2D canvas){
		canvas.setColor(color);
		canvas.setPaint(color);
		Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);
		canvas.draw(ball);
		canvas.fill(ball);
	}
}
					
