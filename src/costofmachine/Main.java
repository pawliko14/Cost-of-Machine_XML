package costofmachine;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.itextpdf.text.DocumentException;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class Main extends JFrame {

	protected static final String DEST =  "C://Users/el08/Desktop/programiki/Cost_of_machine/Analiza_maszyn.pdf";
	private JPanel contentPane;
	private static JTextField numer_projektu;
	private static JTextField txtPodajNrProjektu;
	protected static String text = "";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					

					text = "190521";
					
					CountMaterial2test.run();

					
				//	Main frame = new Main();
				//	frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 453, 226);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JButton otworz_pdf = new JButton("Otworz pdf");
		otworz_pdf.setVisible(false);
		
		
		
		JButton btnNewButton = new JButton("Start analizy");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text = numer_projektu.getText();
				System.out.println("\ngenerowanie PDf22\n");

				otworz_pdf.setVisible(true);
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {});
		
		JLabel lblPlikTekstowyZ = new JLabel("<html> Plik tekstowy z wynikiem analizy zostanie zapisany na dysku sieciowym //dataserver/Logistyka/Raporty/Analiza maszyn </html>");
		lblPlikTekstowyZ.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlikTekstowyZ.setToolTipText("");
		
		
		
		
		otworz_pdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new java.io.File(DEST));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
			}
		});
		
		numer_projektu = new JTextField();
		numer_projektu.setColumns(10);
		numer_projektu.setText("190521");
		
		txtPodajNrProjektu = new JTextField();
		txtPodajNrProjektu.setText("Podaj nr projektu");
		txtPodajNrProjektu.setEditable(false);
		txtPodajNrProjektu.setColumns(10);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnNewButton)
						.addComponent(txtPodajNrProjektu, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addComponent(numer_projektu, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(44)
							.addComponent(otworz_pdf)))
					.addGap(74))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(20)
					.addComponent(lblPlikTekstowyZ, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(numer_projektu, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtPodajNrProjektu, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(otworz_pdf))
					.addGap(18)
					.addComponent(lblPlikTekstowyZ, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
					.addGap(92))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
