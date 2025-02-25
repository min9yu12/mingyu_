package TeamProject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PetModifyScreen extends JFrame {
	
	private BufferedImage image;
	private JLabel backLabel, petProfileLabel, deleteLabel, calLabel;
	private JLabel petNameLabel, petSpecLabel, petBirthLabel, petGenderLabel, petMaleLabel, petFemaleLabel,
			warningLabel, petDeleteLabel, logoLabel;
	private JTextField petNameTField, petSpecTField, petBirthTField;
	private JButton petAddProButton, petSpSearchButton, petModifyButton, completionButton;
	private JRadioButton petMaleRdButton, petFemaleRdBotton;
	private String name, spec, birth;
	TPMgr mgr;
	PetBean bean, pb;
	private PetPhotoModifyDialog ppm;
	private byte[] imageBytes; // 이미지 데이터를 저장할 멤버 변수
	boolean flag = true;
	private RoundedImageLabel imageLabel;
	
	public PetModifyScreen(JFrame preFrame) {
		setTitle("프레임 설정");
		setSize(402, 874);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mgr = new TPMgr();
		bean = mgr.showOnePet(StaticData.pet_id);
		pb = new PetBean();
		imageBytes = bean.getPet_image();

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

				if (source == backLabel) {
					System.out.println("뒤로가기 클릭됨");
					dispose(); // 창 닫기
					preFrame.setEnabled(true);
					dispose();
					preFrame.setVisible(true);
				} else if (source == petAddProButton && petAddProButton.isEnabled()) {
					System.out.println("반려동물 프로필 사진 추가 클릭됨!");
					if (ppm == null) {
						ppm = new PetPhotoModifyDialog(PetModifyScreen.this); // 'this'를 넘겨줍니다.
						ppm.setLocation(getX() + 22, getY() + 630);
						ppm.setVisible(true); // 최초로 보여줄 때만 setVisible 호출
					} else {
						ppm.setLocation(getX() + 22, getY() + 630);
						if (!ppm.isVisible()) { // 이미 보이지 않는 상태일 때만 보이게 설정
							ppm.setVisible(true);
						}
					}
					setEnabled(false);
				} else if (source == petSpSearchButton && petSpSearchButton.isEnabled()) {
					System.out.println("반려동물 종 검색 버튼 클릭됨!");
					setEnabled(false);
					new PetSpeciesSearchDialogByModify(PetModifyScreen.this);
				} else if (source == completionButton && completionButton.isEnabled()) {
					System.out.println("반려동물 정보수정 완료 버튼 클릭됨!");
					name = petNameTField.getText().trim();
					spec = petSpecTField.getText().trim();
					birth = petBirthTField.getText().trim();
					if (name.isEmpty()) { // 이름이 비어있다면 경고
						warningLabel.setVisible(true);
					} else {
						warningLabel.setVisible(false);
						pb.setPet_name(name);
						pb.setPet_species(spec);
						pb.setPet_age(birth);
						if (petMaleRdButton.isSelected()) {
							pb.setPet_gender("수컷");
						} else if (petFemaleRdBotton.isSelected()) {
							pb.setPet_gender("암컷");
						} else
							pb.setPet_gender("");
						pb.setPet_image(imageBytes);
						mgr.updPet(StaticData.pet_id, pb);
						dispose();
						new PetHomeScreen(StaticData.pet_id);
					}
				} else if (source == petDeleteLabel && petDeleteLabel.isEnabled()) {
					System.out.println("반려동물 정보 삭제 버튼 클릭됨!");
					new PetDeleteDialog(PetModifyScreen.this);
				} else if (source == calLabel && calLabel.isEnabled()) {
					setEnabled(false);
					new CalendarDialog(PetModifyScreen.this, petBirthTField);
				} else if (source == petModifyButton) {
					petDeleteLabel.setEnabled(true);
					petAddProButton.setEnabled(true);
					petSpSearchButton.setEnabled(true);
					petNameTField.setEnabled(true);
					petMaleRdButton.setEnabled(true);
					petFemaleRdBotton.setEnabled(true);
					completionButton.setEnabled(true);
					calLabel.setEnabled(true);
				}
			}
		};
		// 🔹 상단 뒤로가기 아이콘
		backLabel = createScaledImageLabel("TeamProject/back_button.png", 40, 40);
		backLabel.setBounds(25, 120, 40, 40);
		backLabel.addMouseListener(commonMouseListener);
		add(backLabel);
		
		// 로고 아이콘
		logoLabel = createScaledImageLabel("TeamProject/logo2.png", 180, 165);
		logoLabel.setBounds(105, 54, 180, 165);
		add(logoLabel);
		
		// 🔹 캘린더 아이콘
		calLabel = createScaledImageLabel("TeamProject/calendar.png", 30, 30);
		calLabel.setBounds(155, 700, 30, 30);
		calLabel.addMouseListener(commonMouseListener);
		add(calLabel);

		// 반려동물 프로필 사진 추가 버튼
		petAddProButton = new JButton("추가");
		petAddProButton.setBounds(277, 450, 80, 35);
		petAddProButton.setBackground(new Color(91, 91, 91));
		petAddProButton.setForeground(Color.WHITE);
		petAddProButton.addMouseListener(commonMouseListener);
		add(petAddProButton);

		// 메인 프로필 이미지
		byte[] imgBytes = bean.getPet_image();
		if (imgBytes == null || imgBytes.length == 0) {
			// 기본 프로필 이미지 사용
			ImageIcon icon = new ImageIcon("TeamProject/dog.png");
			Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
			// RoundedImageLabel 사용
			imageLabel = new RoundedImageLabel(img, 200, 200, 3); // 270은 크기, 3은 둥근 정도
			imageLabel.setBounds(101, 230, 200, 200);
			imageLabel.addMouseListener(commonMouseListener);
			add(imageLabel);
		} else {
			// 사용자 이미지가 있을 경우
			ImageIcon icon = new ImageIcon(imgBytes);
			Image img = icon.getImage();
			
			// 원본 이미지 크기
			int imgWidth = icon.getIconWidth();
			int imgHeight = icon.getIconHeight();

			// 타겟 크기 (200x200)
			int targetWidth = 200;
			int targetHeight = 200;

			// 비율 유지하면서 자르기 위해 더 많이 필요한 쪽 기준으로 크기 조정
			double targetRatio = (double) targetWidth / targetHeight;
			double imgRatio = (double) imgWidth / imgHeight;

			int cropWidth = imgWidth;
			int cropHeight = imgHeight;

			if (imgRatio > targetRatio) {
				// 원본이 더 넓은 경우 → 가로를 자름
				cropWidth = (int) (imgHeight * targetRatio);
			} else {
				// 원본이 더 높은 경우 → 세로를 자름
				cropHeight = (int) (imgWidth / targetRatio);
			}

			// 중심을 기준으로 자를 영역 계산
			int x = (imgWidth - cropWidth) / 2;
			int y = (imgHeight - cropHeight) / 2;

			// BufferedImage로 자르기
			BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bufferedImage.getGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();

			BufferedImage croppedImage = bufferedImage.getSubimage(x, y, cropWidth, cropHeight);
	        
	        // 이미지 크기 조정 (200x200)
	        Image resizedImg = croppedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

			// RoundedImageLabel 사용
			imageLabel = new RoundedImageLabel(resizedImg, 200, 200, 3); // 270은 크기, 3은 둥근 정도
			imageLabel.setBounds(101, 230, 200, 200);
			imageLabel.addMouseListener(commonMouseListener);
			add(imageLabel);
		}

		// 경고문
		warningLabel = new JLabel("이름을 입력하시오");
		warningLabel.setForeground(Color.RED);
		warningLabel.setBounds(43, 545, 250, 60);
		add(warningLabel);
		warningLabel.setVisible(false);

		// 반려동물 이름 라벨
		petNameLabel = new JLabel("이름");
		petNameLabel.setBounds(43, 479, 32, 60);
		petNameLabel.setForeground(Color.black);
		add(petNameLabel);

		// 반려동물 이름 텍스트 필드 추가
		petNameTField = new JTextField(bean.getPet_name());
		petNameTField.setBounds(43, 520, 318, 40);
		add(petNameTField);

		// 반려동물 종 라벨
		petSpecLabel = new JLabel("종");
		petSpecLabel.setBounds(43, 567, 16, 60);
		petSpecLabel.setForeground(Color.black);
		add(petSpecLabel);

		// 반려동물 종 텍스트 필드 추가
		petSpecTField = new JTextField(bean.getPet_species());
		petSpecTField.setBounds(43, 608, 225, 40);
		add(petSpecTField);

		// 반려동물 종 검색 버튼
		petSpSearchButton = new JButton("검색");
		petSpSearchButton.setBounds(270, 608, 90, 40);
		petSpSearchButton.setBackground(new Color(91, 91, 91));
		petSpSearchButton.setForeground(Color.WHITE);
		petSpSearchButton.addMouseListener(commonMouseListener);
		add(petSpSearchButton);

		// 반려동물 생년월일 라벨
		petBirthLabel = new JLabel("생년월일");
		petBirthLabel.setBounds(43, 655, 66, 60);
		petBirthLabel.setForeground(Color.black);
		add(petBirthLabel);

		// 반려동물 종 생년월일 필드 추가
		petBirthTField = new JTextField(bean.getPet_age());
		petBirthTField.setForeground(Color.GRAY);
		petBirthTField.setBounds(43, 696, 100, 40);
		add(petBirthTField);
		
		// 반려동물 성별 라벨
		petGenderLabel = new JLabel("성별");
		petGenderLabel.setBounds(220, 655, 32, 60);
		petGenderLabel.setForeground(Color.black);
		add(petGenderLabel);

		// 반려동물 남 라벨
		petMaleLabel = new JLabel("남");
		petMaleLabel.setBounds(220, 683, 17, 60);
		petMaleLabel.setForeground(Color.black);
		add(petMaleLabel);

		// 반려동물 남 라디오 버튼
		petMaleRdButton = new JRadioButton();
		petMaleRdButton.setBounds(250, 705, 20, 20); // 위치와 크기 설정
		petMaleRdButton.setOpaque(false); // 배경 투명 처리
		petMaleRdButton.setContentAreaFilled(false); // 내용 영역 투명
		petMaleRdButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 테두리 설정
		add(petMaleRdButton);

		// 반려동물 여 라벨
		petFemaleLabel = new JLabel("여");
		petFemaleLabel.setBounds(290, 683, 17, 60);
		petFemaleLabel.setForeground(Color.black);
		add(petFemaleLabel);

		// 반려동물 여 라디오 버튼
		petFemaleRdBotton = new JRadioButton();
		petFemaleRdBotton.setBounds(320, 705, 20, 20); // 위치와 크기 설정
		petFemaleRdBotton.setOpaque(false); // 배경 투명 처리
		petFemaleRdBotton.setContentAreaFilled(false); // 내용 영역 투명
		petFemaleRdBotton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 테두리 설정
		add(petFemaleRdBotton);

		// 라디오 버튼 그룹으로 묶기 (선택은 하나만)
		ButtonGroup group = new ButtonGroup();
		group.add(petMaleRdButton);
		group.add(petFemaleRdBotton);

		if (bean.getPet_gender().equals("수컷"))
			petMaleRdButton.setSelected(true);
		else if (bean.getPet_gender().equals("암컷"))
			petFemaleRdBotton.setSelected(true);
		else if (bean.getPet_gender().isEmpty()) {
			petMaleRdButton.setSelected(false);
			petFemaleRdBotton.setSelected(false);
		}

		// 반려동물 정보 수정 버튼
		petModifyButton = new JButton("수정");
		petModifyButton.setBounds(98, 760, 91, 43);
		petModifyButton.setBackground(new Color(91, 91, 91));
		petModifyButton.setForeground(Color.WHITE);
		petModifyButton.addMouseListener(commonMouseListener);
		add(petModifyButton);

		// 반려동물 수정 완료 버튼
		completionButton = new JButton("완료");
		completionButton.setBounds(215, 760, 91, 43);
		completionButton.setBackground(new Color(91, 91, 91));
		completionButton.setForeground(Color.WHITE);
		completionButton.addMouseListener(commonMouseListener);
		add(completionButton);

		// 반려동물 정보 삭제 버튼
		petDeleteLabel = createScaledImageLabel("TeamProject/pet_delete.png", 40, 40);
		petDeleteLabel.setBounds(320, 123, 40, 40);
		petDeleteLabel.setBackground(new Color(255, 102, 102));
		petDeleteLabel.setForeground(Color.WHITE);
		petDeleteLabel.addMouseListener(commonMouseListener);
		add(petDeleteLabel);

		petDeleteLabel.setEnabled(false);
		petAddProButton.setEnabled(false);
		petSpSearchButton.setEnabled(false);
		petNameTField.setEnabled(false);
		petSpecTField.setEnabled(false);
		petBirthTField.setEnabled(false);
		petMaleRdButton.setEnabled(false);
		petFemaleRdBotton.setEnabled(false);
		completionButton.setEnabled(false);
		calLabel.setEnabled(false);

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
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mgr.userOut(StaticData.user_id);
				System.exit(0);
			}
		});
		panel.add(closeButton);

		setVisible(true);
	}

	public void updateSpecies(String species) {
		petSpecTField.setText(species);
	}

	/**
	 * 이미지 크기를 조정하여 JLabel을 생성하는 메서드
	 */
	private JLabel createScaledImageLabel(String imagePath, int width, int height) {
		ImageIcon icon = new ImageIcon(imagePath);
		Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new JLabel(new ImageIcon(scaledImage));
	}

	// 이미지 레이블 반환 타입을 RoundedImageLabel로 수정
		public RoundedImageLabel getImageLabel() {
		    return imageLabel;
	}

	// 이미지 바이트 배열을 설정하는 setter
	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	// imageBytes를 얻는 메서드
	public byte[] getImageBytes() {
		return imageBytes;
	}

	public static void main(String[] args) {
		new LoginScreen();
	}
}