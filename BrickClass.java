package task6;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class BrickClass{
	//������������ � ����������� �������(�� ����)
	private int x;
	private int y;
	//������������ ����� � ����������� ����
	private double win_x0;
	private double win_y0; 
	private double win_x1;
	private double win_y1;
	//���������� ������
	private double win_xc;
	private double win_yc;
	private Field field;
	private Color color;
	private int health = 5;//��������� �������
	private static final int BRICKS_IN_WIDTH = 20;//��� ��������� ������ �� ������� �������(��� ������ ��������
	private static final int BRICKS_IN_HEIGHT = 20; //��� ������ ������)
	public BrickClass(Field field, int x, int y){
		this.field = field;
		this.x = x;
		this.y = y;
	}
	
	//���������� ��������� ������� � ����(��� ������� �� ������� ����) 
	private void get_win_coords(){
		win_x0 = x*field.getWidth()/BRICKS_IN_WIDTH;
		win_x1 = x*field.getWidth()/BRICKS_IN_WIDTH + field.getWidth()/BRICKS_IN_WIDTH;
		win_y0 = y*field.getHeight()/BRICKS_IN_HEIGHT;
		win_y1 = y*field.getHeight()/BRICKS_IN_HEIGHT + field.getHeight()/BRICKS_IN_HEIGHT;
		win_xc = (win_x0+win_x1)/2;
		win_yc = (win_y0+win_y1)/2;
	}
	
	//����� ���������� ������ ����
	public void paint(Graphics2D canvas){
		if(health >0){ //���� ������ �������(health=0), �� �� ����� ���
			color = new Color(140, 50*health, 0);
			canvas.setColor(color);
			canvas.setPaint(color);
			get_win_coords();
			Rectangle2D.Double brick = new Rectangle2D.Double(win_x0, win_y0, win_x1-win_x0, win_y1-win_y0);
			canvas.draw(brick);
			canvas.fill(brick);
			paint_label(canvas);
		}
	}
	private void paint_label(Graphics2D canvas){
		Integer to_string = health;
		String str = to_string.toString();
		// ������� ������� � ����� � ������������ ������������ 
		canvas.setColor(Color.BLACK);
		canvas.drawString(str, (float)win_xc, (float)win_yc);

	}
	//��������� ��������� ������� �� X
	public double get_coordX(int num){
		if(num==0){
			return win_x0;
		}
		if(num==1){
			return win_x1;
		}
		return -1;
	}
	//��������� ��������� ������� �� Y
	public double get_coordY(int num){
		if(num==0){
			return win_y0;
		}
		if(num==1){
			return win_y1;
		}
		return -1;
	}
	//��������� ��������� ��� ������������
	public void strike(){
		if(health > 0){
			health -= 1;
		}
	}
	//�������� �� ������� ���������
	public boolean check_strikable(){
		if(health>0){
			return true;
		}
		return false;
	}

}
