/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author uset
 */
public class frame extends javax.swing.JFrame {

    private BufferedImage resim;
    private BufferedImage b_resim;
    private Image k_resim;
    private Image resimler[];
    private int width, height;
    private final int DESIRED_WIDTH = 1200;
    private final int DESIRED_HEIGHT = 800;
    private int[] click_index;
    private int index=0;
    private boolean kontrol[];
    private boolean kontrol2;
    private int kontrol3=0;
    private boolean kontrol4=false;
    private int score;
    private ArrayList<MyButton> butonlar = new ArrayList<>();
    private ArrayList<Integer> skorlar = new ArrayList<>();
    
    public frame() throws IOException {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Puzzle");
        eny_score();
    }
    
    private void eny_score() throws FileNotFoundException, IOException{
        FileReader fileReader = new FileReader("C:\\Users\\uset\\Desktop\\Proje\\Yazlab-II\\proje1\\puzzle1\\enyuksekskor.txt");
        String line;

        BufferedReader br = new BufferedReader(fileReader);

        while ((line = br.readLine()) != null) {
            skorlar.add(Integer.parseInt(line));
        }
        eny_skor.setVerticalAlignment(JLabel.CENTER);
        eny_skor.setHorizontalAlignment(JLabel.CENTER);
           
        if(!skorlar.isEmpty()){
           Collections.sort(skorlar);
           eny_skor.setText(String.valueOf(skorlar.get(skorlar.size()-1)));
        }
        else
            eny_skor.setText("Puan Tablosu Boş");

        br.close();
    }
    
    private void score_kaydet() throws IOException{
        FileWriter fileWriter = new FileWriter("C:\\Users\\uset\\Desktop\\Proje\\Yazlab-II\\proje1\\puzzle1\\enyuksekskor.txt",true);
        BufferedWriter bWriter = new BufferedWriter(fileWriter);
        bWriter.write(skor.getText());
        bWriter.newLine();
        bWriter.close();
    }
    
    public void myinit() throws IOException{
        this.setLocationRelativeTo(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setLayout(new GridLayout(4,4,0,0));
        eny_score();
        
        try {
            b_resim = resizeImage(resim, DESIRED_WIDTH, DESIRED_HEIGHT,BufferedImage.TYPE_INT_ARGB);

        } catch (IOException ex) {
            
        }        
                
        width = b_resim.getWidth(null);
        height = b_resim.getHeight(null);
        resimler = new Image[16];
        
        int k = 0;
        
        try{
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    k_resim = createImage(new FilteredImageSource(b_resim.getSource(),new CropImageFilter(j * width / 4, i * height / 4,(width / 4), height / 4)));
                    resimler[k]=k_resim;
                    MyButton buton = new MyButton(k_resim);
                    butonlar.add(buton);
                    k++;
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Resim Seçiniz !");
            System.exit(0);
        }
        
        for(int i=0;i<16;i++){
            butonlar.get(i).setName(String.valueOf(i));
        }
        
        Collections.shuffle(butonlar);
        
        for(int i = 0;i<16;i++){
            butonlar.get(i).addActionListener(new ClickAction());
            panel.add(butonlar.get(i));
            butonlar.get(i).setBorder(BorderFactory.createLineBorder(Color.gray));
        }
        
        click_index = new int[2];
        click_index[0]=-1;
        click_index[1]=-1;
        kontrol = new boolean[16];
        kontrol2=true;
        score=100;
        skor.setText("100");
        
        for(int i=0;i<16;i++){
            kontrol[i]=false;
        }
        check2();
        
        
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, int width,int height, int type) throws IOException {

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }
    
    private class ClickAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
        
        if(kontrol3<16){
            JButton buton = (JButton) e.getSource();
            if(index<2){
                click_index[index]=butonlar.indexOf(buton);
                System.out.println("Tıklanan butonun indexi :"+click_index[index]+"  Tıklanan butonun adı :"+buton.getName());
                index++;
            }
            System.out.println("--------------------------");
            if(index==2){
                Collections.swap(butonlar, click_index[1],click_index[0]);
                updateButtons();
                
                if(click_index[0]!=click_index[1]){
                    score=score-3;
                    skor.setText(String.valueOf(score));
                }
                
                check();
                for(int i =0 ;i<16;i++){
                   if(kontrol[i]==false){
                        kontrol2=false;
                        break;
                    }
                    kontrol2=true;
                }
                
                if(kontrol2==true){
                    JOptionPane.showMessageDialog(null, "Tebrikler !"+" "+score+" ile tamamladınız");
                    for(int i = 0;i<16;i++){
                        butonlar.get(i).setBorder(BorderFactory.createEmptyBorder());
                    }
                    try {
                        score_kaydet();
                        eny_score();
                    } catch (IOException ex) {
                        Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                index=0;
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "En az bir parça doğru yerinde olana kadar karıştırınız !");
        }

        }
    }
    
    private void updateButtons() {

            panel.removeAll();

            for (JComponent btn : butonlar) {

                panel.add(btn);
            }

            panel.validate();
    }
    
    
    public static BufferedImage cevir(Image img){
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }
    
    
    private void check(){
        double percentage = 0;
        for(int i = 0 ;i < 2; i++){
            BufferedImage imgA = cevir(resimler[click_index[i]]); 
            BufferedImage imgB = cevir(butonlar.get(click_index[i]).image);
            
            int width1 = imgA.getWidth(); 
            int width2 = imgB.getWidth(); 
            int height1 = imgA.getHeight(); 
            int height2 = imgB.getHeight(); 

            if ((width1 != width2) || (height1 != height2)) 
                System.out.println("Error: Images dimensions mismatch"); 
            else
            { 
                long difference = 0; 
                for (int y = 0; y < height1; y++) 
                { 
                    for (int x = 0; x < width1; x++) 
                    { 
                        int rgbA = imgA.getRGB(x, y); 
                        int rgbB = imgB.getRGB(x, y); 
                        int redA = (rgbA >> 16) & 0xff; 
                        int greenA = (rgbA >> 8) & 0xff; 
                        int blueA = (rgbA) & 0xff; 
                        int redB = (rgbB >> 16) & 0xff; 
                        int greenB = (rgbB >> 8) & 0xff; 
                        int blueB = (rgbB) & 0xff; 
                        difference += Math.abs(redA - redB); 
                        difference += Math.abs(greenA - greenB); 
                        difference += Math.abs(blueA - blueB); 
                    } 
                } 

                // Total number of red pixels = width * height 
                // Total number of blue pixels = width * height 
                // Total number of green pixels = width * height 
                // So total number of pixels = width * height * 3 
                double total_pixels = width1 * height1 * 3; 

                // Normalizing the value of different pixels 
                // for accuracy(average pixels per color 
                // component) 
                double avg_different_pixels = difference / total_pixels; 

                // There are 255 values of pixels in total 
                percentage = (avg_different_pixels / 255) * 100; 

                System.out.println("Difference Percentage-->" + percentage); 
                
                if(percentage==0.0){
                    kontrol[Integer.parseInt(butonlar.get(click_index[i]).getName())]=true;
                    butonlar.get(click_index[i]).setBorder(BorderFactory.createLineBorder(Color.green,3));
                }
                
                if(percentage!=0.0){
                    kontrol[Integer.parseInt(butonlar.get(click_index[i]).getName())]=false;
                    butonlar.get(click_index[i]).setBorder(BorderFactory.createLineBorder(Color.gray,3));
                }
            } 
        }
    }
    
    private void check2(){
        double percentage = 0;
        for(int i = 0 ;i < 16; i++){
            BufferedImage imgA = cevir(resimler[i]); 
            BufferedImage imgB = cevir(butonlar.get(i).image);
            
            
            int width1 = imgA.getWidth(); 
            int width2 = imgB.getWidth(); 
            int height1 = imgA.getHeight(); 
            int height2 = imgB.getHeight(); 

            if ((width1 != width2) || (height1 != height2)) 
                System.out.println("Error: Images dimensions mismatch"); 
            else
            { 
                long difference = 0; 
                for (int y = 0; y < height1; y++) 
                { 
                    for (int x = 0; x < width1; x++) 
                    { 
                        int rgbA = imgA.getRGB(x, y); 
                        int rgbB = imgB.getRGB(x, y); 
                        int redA = (rgbA >> 16) & 0xff; 
                        int greenA = (rgbA >> 8) & 0xff; 
                        int blueA = (rgbA) & 0xff; 
                        int redB = (rgbB >> 16) & 0xff; 
                        int greenB = (rgbB >> 8) & 0xff; 
                        int blueB = (rgbB) & 0xff; 
                        difference += Math.abs(redA - redB); 
                        difference += Math.abs(greenA - greenB); 
                        difference += Math.abs(blueA - blueB); 
                    } 
                } 

                // Total number of red pixels = width * height 
                // Total number of blue pixels = width * height 
                // Total number of green pixels = width * height 
                // So total number of pixels = width * height * 3 
                double total_pixels = width1 * height1 * 3; 

                // Normalizing the value of different pixels 
                // for accuracy(average pixels per color 
                // component) 
                double avg_different_pixels = difference / total_pixels; 

                // There are 255 values of pixels in total 
                percentage = (avg_different_pixels / 255) * 100; 

                System.out.println("Difference Percentage-->" + percentage); 
                
                if(percentage==0.0){
                    kontrol[i]=true;
                    butonlar.get(i).setBorder(BorderFactory.createLineBorder(Color.green,3));
                }
            } 
        }
        
        kontrol3=0;
        
        for(int i = 0;i<16;i++){
            if(kontrol[i]==false){
                kontrol3++;
            }
        }
        
        if(kontrol3==0){
            JOptionPane.showMessageDialog(null, "Tebrikler !"+" "+score+" ile tamamladınız");
            for(int i = 0;i<16;i++){
                butonlar.get(i).setBorder(BorderFactory.createEmptyBorder());
            }
            try {
                score_kaydet();
            } catch (IOException ex) {
                Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        else if(kontrol3==16){
            JOptionPane.showMessageDialog(null, "Parçaları karıştırınız !");
        }
        else{
            for(int i = 0;i<16;i++){
                if(kontrol[i]==true){
                    JOptionPane.showMessageDialog(null, "Oyuna Başlayabilirsiniz !");
                    break;
                }
            }
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        panel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        buton1 = new javax.swing.JButton();
        karistir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        skor = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        eny_skor = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(174, 168, 211));

        jPanel2.setBackground(new java.awt.Color(190, 144, 212));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(241, 231, 254));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 153)));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        buton1.setText("Resim Seç");
        buton1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 0, 153), null));
        buton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buton1ActionPerformed(evt);
            }
        });

        karistir.setText("Karıştır");
        karistir.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 0, 153), null));
        karistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                karistirActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 0, 102));
        jLabel2.setText("SKORUNUZ");

        skor.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        skor.setForeground(new java.awt.Color(102, 0, 102));

        jLabel3.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 0, 102));
        jLabel3.setText("EN YÜKSEK SKOR");

        eny_skor.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        eny_skor.setForeground(new java.awt.Color(102, 0, 102));
        eny_skor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(eny_skor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(karistir, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(skor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(buton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(karistir, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(skor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eny_skor, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buton1ActionPerformed
        
        String[] filtre = {"jpg","png","jpeg","bmp"};
        JFileChooser f = new JFileChooser();
        f.setCurrentDirectory(new java.io.File("C:/Users/uset/desktop"));
        f.setDialogTitle("Puzzle");
        FileNameExtensionFilter filtreler = new FileNameExtensionFilter("Resim Dosyası",filtre);
        f.setFileFilter(filtreler);
        f.setAcceptAllFileFilterUsed(false);
        int result = f.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = f.getSelectedFile();
                    try {
                        resim = ImageIO.read(file);
                    } catch (IOException ex) {
                        Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    kontrol4=true;
                }
                else
                    JOptionPane.showMessageDialog(null, "Resim Dosyası Seçilmedi !");
        if(kontrol4==true){            
            try {
                butonlar.removeAll(butonlar);
                panel.removeAll();
                myinit();
            } catch (IOException ex) {
                Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_buton1ActionPerformed

    private void karistirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_karistirActionPerformed
       if(kontrol4==true){
           try {
            butonlar.removeAll(butonlar);
            panel.removeAll();
            myinit();
        } catch (IOException ex) {
            Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
       else
           JOptionPane.showMessageDialog(null, "Lütfen resim seçiniz !");
    }//GEN-LAST:event_karistirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new frame().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton buton1;
    private javax.swing.JLabel eny_skor;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton karistir;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel skor;
    // End of variables declaration//GEN-END:variables
}
