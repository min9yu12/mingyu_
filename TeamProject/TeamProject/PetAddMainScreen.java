package TeamProject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PetAddMainScreen extends JFrame {

	private BufferedImage image;
	private JLabel alarmLabel, profileLabel, mainProfileLabel, petProfileLabel, addButtonLabel;
	private JButton logoutButton;
	private JLabel welcomeLabel, petNameLabel, petSpeciesLabel, petAgeLabel, petGenderLabel;
	TPMgr mgr = new TPMgr();
	Vector<PetBean> vlist;
	PetBean bean[] = new PetBean[2];
	private PetChooseDialog pc;
	

	public PetAddMainScreen() {
		setTitle("프레임 설정");
		setSize(402, 874);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vlist = mgr.showPet(StaticData.user_id);
		try {
			bean[0] = (PetBean)vlist.elementAt(0);
			bean[1] = (PetBean)vlist.elementAt(1);
		} catch (Exception e) {}
		

		try {
			image = ImageIO.read(new File("TeamProject/phone_frame.png")); // 투명 PNG 불러오기
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 🔹 공통 마우스 클릭 이벤트 리스너
		MouseAdapter commonMouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Object source = e.getSource(); // 클릭된 컴포넌트 확인

				if (source == alarmLabel) {
					System.out.println("🔔 알람 클릭됨!");
					dispose();
					new AlarmMainScreen(PetAddMainScreen.this);
				} else if (source == profileLabel) {
					System.out.println("👤 프로필 클릭됨!");
					dispose();
					new UpdateUserScreen(PetAddMainScreen.this);
				} else if (source == mainProfileLabel) {
					System.out.println("🖼️ 메인 프로필 클릭됨!");
					dispose();
					new UpdateUserScreen(PetAddMainScreen.this);
				} else if (source == addButtonLabel) {
					System.out.println("➕ 추가 버튼 클릭됨!");
					if(pc==null) {
						pc = new PetChooseDialog(PetAddMainScreen.this);
						//ZipcodeFrame의 창의 위치를 MemberAWT 옆에 지정
						pc.setLocation(getX()+25, getY()+300);
					}else {
						pc.setLocation(getX()+25, getY()+300);
						pc.setVisible(true);
					}
					setEnabled(false);
					
				}
			}
		};

		// 🔹 알람 아이콘
		alarmLabel = createScaledImageLabel("TeamProject/alarm.png", 40, 40);
		alarmLabel.setBounds(280, 120, 40, 40);
		alarmLabel.addMouseListener(commonMouseListener);
		add(alarmLabel);

		// 🔹 상단 프로필 아이콘
		profileLabel = createScaledImageLabel("TeamProject/profile.png", 40, 40);
		profileLabel.setBounds(330, 120, 40, 40);
		profileLabel.addMouseListener(commonMouseListener);
		add(profileLabel);

		// 🔹 메인 프로필 이미지
		mainProfileLabel = createScaledImageLabel("TeamProject/profile.png", 200, 200);
		mainProfileLabel.setBounds(101, 178, 200, 200);
		mainProfileLabel.addMouseListener(commonMouseListener);
		add(mainProfileLabel);
		
		// 환영 문구
		welcomeLabel = new JLabel("어서오세요, " + mgr.userName(StaticData.user_id) + "님");
		welcomeLabel.setBounds(135, 401, 134, 20);
		welcomeLabel.setForeground(Color.BLACK);
		add(welcomeLabel);
		
		//한 사용자당 반려동물은 2마리로 제한
			
		//반려견 프로필
		petProfileLabel = createScaledImageLabel("TeamProject/dog.png", 150, 150);
		petProfileLabel.setBounds(37, 461, 150, 150);
		petProfileLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				new PetHomeScreen(bean[0].getPet_id());
			}
		});
		add(petProfileLabel);
				
		//반려견 이름
		petNameLabel = new JLabel("이름 : " + bean[0].getPet_name());
		petNameLabel.setBounds(222, 482, 146, 26);
		petNameLabel.setForeground(Color.BLACK);
		add(petNameLabel);
				
		//반려견 종
		petSpeciesLabel = new JLabel("종 : " +bean[0].getPet_species());
		petSpeciesLabel.setBounds(222, 512, 146, 26);
		petSpeciesLabel.setForeground(Color.BLACK);
		add(petSpeciesLabel);
				
		//반려견 생년월일
		petAgeLabel = new JLabel("나이 : " + bean[0].getPet_age());
		petAgeLabel.setBounds(222, 542, 146, 26);
		petAgeLabel.setForeground(Color.BLACK);
		add(petAgeLabel);
				
		//반려견 성별
		petGenderLabel = new JLabel("성별 : " + bean[0].getPet_gender());
		petGenderLabel.setBounds(222, 572, 146, 26);
		petGenderLabel.setForeground(Color.BLACK);
		add(petGenderLabel);
		
		if(bean[1] != null) {	//두 마리일 경우
			
			//반려견 프로필
			petProfileLabel = createScaledImageLabel("TeamProject/dog.png", 150, 150);
			petProfileLabel.setBounds(37, 667, 150, 150);
			petProfileLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					dispose();
					new PetHomeScreen(bean[1].getPet_id());
				}
			});
			add(petProfileLabel);
			
			//반려견 이름
			petNameLabel = new JLabel("이름 : " + bean[1].getPet_name());
			petNameLabel.setBounds(222, 688, 146, 26);
			petNameLabel.setForeground(Color.BLACK);
			add(petNameLabel);
			
			//반려견 종
			petSpeciesLabel = new JLabel("종 : " +bean[1].getPet_species());
			petSpeciesLabel.setBounds(222, 718, 146, 26);
			petSpeciesLabel.setForeground(Color.BLACK);
			add(petSpeciesLabel);
			
			//반려견 생년월일
			petAgeLabel = new JLabel("나이 : " + bean[1].getPet_age());
			petAgeLabel.setBounds(222, 748, 146, 26);
			petAgeLabel.setForeground(Color.BLACK);
			add(petAgeLabel);
			
			//반려견 성별
			petGenderLabel = new JLabel("성별 : " + bean[1].getPet_gender());
			petGenderLabel.setBounds(222, 778, 146, 26);
			petGenderLabel.setForeground(Color.BLACK);
			add(petGenderLabel);
		}
		
		//반려견 추가 버튼
		addButtonLabel = createScaledImageLabel("TeamProject/add_button.png", 70, 70);
		addButtonLabel.setBounds(280, 730, 70, 70);
		addButtonLabel.addMouseListener(commonMouseListener);
		add(addButtonLabel);
		
		// 로그아웃 버튼
		logoutButton = new RoundedButton("로그아웃");
		logoutButton.setBounds(30, 122, 85, 36);
		logoutButton.setBackground(new Color(91, 91, 91));
		logoutButton.setForeground(Color.WHITE);
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginScreen();
			}
		});
		add(logoutButton);

		// JPanel 추가
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (image != null) {
					// 이미지 크기 조정 후 그리기
					Image scaledImage = image.getScaledInstance(402, 874, Image.SCALE_SMOOTH);
					g.drawImage(scaledImage, 0, 0, this);
				}

				// y=158 위치에 가로로 회색 선 그리기
				g.setColor(Color.LIGHT_GRAY); // 선 색을 회색으로 설정
				g.drawLine(22, 165, 379, 165);
				g.drawLine(22, 443, 379, 443);
				g.drawLine(22, 639, 379, 639);
			}
		};

		panel.setOpaque(false);
		panel.setLayout(null);
		add(panel);

		// 닫기 버튼
		JButton closeButton = new JButton("X");
		closeButton.setBounds(370, 10, 20, 20);
		closeButton.setBackground(Color.RED);
		closeButton.setForeground(Color.WHITE);
		closeButton.setBorder(BorderFactory.createEmptyBorder());
		closeButton.setFocusPainted(false);
		closeButton.addActionListener(e -> System.exit(0));
		panel.add(closeButton);

		setVisible(true);

	}

	private JLabel createScaledImageLabel(String imagePath, int width, int height) {
		ImageIcon icon = new ImageIcon(imagePath);
		Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new JLabel(new ImageIcon(scaledImage));
	}

	public static void main(String[] args) {
		new LoginScreen();
	}
}
