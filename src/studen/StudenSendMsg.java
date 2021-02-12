package studen;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import dbConnection.Acess_memDAO;
import dbConnection.ConsDAO;
import dbConnection.ConsVO;

public class StudenSendMsg extends JPanel implements ActionListener, MouseListener{
   JPanel mainPane = new JPanel();
      JTable table;
         JScrollPane sp;
         DefaultTableModel model;
         
   // 테이블 필드명
   Object headList[] = {"선택","글번호","메세지내용","받는사람","보낸시간"};
   
   DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
   
   Color col6 = new Color(204,222,233);
   Font fntPlain15 = new Font("맑은 고딕", Font.PLAIN, 15);
   Font fntBold15 = new Font("맑은 고딕", Font.BOLD, 15);
   
   JButton delBtn = new JButton("삭제하기");
   
   String idStr;
   
   public StudenSendMsg() { }
   
   public StudenSendMsg(String id) {
	  this.idStr = id;
	  
      mainPane.setLayout(null);
      mainPane.setBackground(Color.white);

      //JTable
      // 내용 수정 불가
      model = new DefaultTableModel(headList, 0) {
          public boolean isCellEditable(int i, int c){ return false; }
      };
      table = new JTable(model);
      sp = new JScrollPane(table);
      
      // 컬럼 너비 조절
      table.getColumn("선택").setPreferredWidth(30);
      table.getColumn("글번호").setPreferredWidth(30);
      table.getColumn("메세지내용").setPreferredWidth(300);
      table.getColumn("받는사람").setPreferredWidth(100);
      table.getColumn("보낸시간").setPreferredWidth(100);
      table.setAlignmentX(JLabel.CENTER);
      
      // 테이블 필드명 높이 조절
      table.setTableHeader(new JTableHeader(table.getColumnModel()) {
         public Dimension getPreferredSize() {
          Dimension d = super.getPreferredSize();
          d.height = 40;
          return d;
         }
      });
      
      // 셀 이동 불가
      table.getTableHeader().setReorderingAllowed(false);
      
      // 테이블 레코드 높이 조절
      table.setRowHeight(30);
      
      // 폰트 설정
      table.getTableHeader().setFont(fntBold15);
      table.setFont(fntPlain15);
      delBtn.setFont(fntPlain15);
      
      // 배경색 설정
      table.getTableHeader().setBackground(Color.lightGray);
      table.getParent().setBackground(Color.white);
      
      // 가운데 정렬
      tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
      TableColumnModel tcmSchedule = table.getColumnModel();
      tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer);
      tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer);
      tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer);
      tcmSchedule.getColumn(4).setCellRenderer(tScheduleCellRenderer);
      
      //mainPane에 add
      mainPane.add(sp); mainPane.add(delBtn);
      
      sendMsgLst(idStr);
      
      // setBounds
      sp.setBounds(0,0,745,750); delBtn.setBounds(640,750,100,50);
      table.addMouseListener(this);
      delBtn.addMouseListener(this);
   }
   @Override
   public void mouseClicked(MouseEvent e) {
      int clickBtn = e.getButton();
      if(clickBtn==1) {
         //선택한 컬럼의 데이터 가져오기
         int row = table.getSelectedRow();
         int col = table.getSelectedColumn();
         Object value = table.getValueAt(row, col);
         if(value.equals("○")) {
            table.setValueAt("●", row, col);
         }else if(value.equals("●")) {
            table.setValueAt("○", row, col);
         }else if(col==2) {
            //학생 메세지쓰기 호출하세욘//
        	 int sendMsgNum = (int)table.getValueAt(row, 1);
        	 //다이얼로그 불러오기
        	 new StudenSendMsgDialog(idStr, sendMsgNum);
        	 System.out.println("쪽지쓰기에 아이디 받아오는지.... "+idStr+", 쪽지번호 읽히는지.... "+sendMsgNum);
         }
      }
   }
   
   public void actionPerformed(ActionEvent ae) {
      Object delObj = ae.getSource();
      if(delObj == delBtn) {
         // 데이터 삭제 구현
         int result = 0;
         for(int i=0; i<table.getRowCount(); i++) {
        	 if(table.getValueAt(i, 0).equals("●")) {
        		 int sendMsgNum = (int)table.getValueAt(i, 1);
        		 ConsDAO dao = new ConsDAO();
        		 result = dao.msgDelete(sendMsgNum);
        	 }
         }
         if(result>0) {
        	 JOptionPane.showMessageDialog(this, "선택한 메시지가 삭제되었습니다.");
        	 model.setRowCount(0);
        	 sendMsgLst(idStr); //다시 표시
         }else {
        	JOptionPane.showMessageDialog(this, "선택한 메시지가 없습니다.");
         }
      }
      
   }
   //보낸 메시지 받아오기 
   public void sendMsgLst(String idStr) {
	   ConsDAO dao = new ConsDAO();
	   List<ConsVO> lst = dao.studenMsgRec(idStr);
	   
	   for(int i=0; i<lst.size(); i++) {
		   ConsVO vo = lst.get(i);
		   Object data[] = {"○", vo.getMsg_num(),"<HTML> <U>"+vo.getMsg_title()+"</U></HTML>",vo.getGet(),vo.getSend_time()};
		   model.addRow(data);
	   }
   }
   
   public void mousePressed(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mouseReleased(MouseEvent e) {}
   //프레임 X 눌렀을때의 이벤트
   class AdapterInner extends WindowAdapter{
		//다시 오버라이딩
		public void windowClosing(WindowEvent we) {
			Acess_memDAO dao = new Acess_memDAO();
			int result = dao.LogOut(idStr);
			System.exit(0);
		}
	}
   
}