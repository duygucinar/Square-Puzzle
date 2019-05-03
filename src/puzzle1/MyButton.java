/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle1;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author uset
 */
class MyButton extends JButton {

    protected Image image;
    public MyButton() {

        super();
        initUI();
    }

    public MyButton(Image image) {
            
        super(new ImageIcon(image));
        this.image=image;
        initUI();
    }

    protected void initUI() {

        BorderFactory.createLineBorder(Color.gray);
        
        addMouseListener(new MouseAdapter() {

           /* @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.yellow));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.gray));
            }*/
            
            @Override
            public void mouseClicked(MouseEvent e){
                setBorder(BorderFactory.createLineBorder(Color.yellow,2));
            }
                
        });
    }
    
}
